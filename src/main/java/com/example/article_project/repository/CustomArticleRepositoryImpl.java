package com.example.article_project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.example.article_project.domain.Article;
import com.example.article_project.domain.QArticle;
import com.example.article_project.dto.ArticleSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class CustomArticleRepositoryImpl implements CustomArticleRepository {

    // JPA에서 EntityManager를 주입할 때 사용하는 애너테이션
    @PersistenceContext
    private EntityManager em;

    private QArticle qArticle = QArticle.article;

    private JPAQueryFactory jpaQueryFactory;

    public CustomArticleRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Article> search(ArticleSearchCondition condition, Pageable pageable) {
        // 검색을 하지 않는 경우
        if (condition.getTitle() == "" && condition.getContents() == "" && condition.getWriter() == "") {
            List<Article> articles = jpaQueryFactory
                .selectFrom(qArticle)
                .orderBy(qArticle.id.desc())
                .offset(pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .fetch();

            Long totalCount = jpaQueryFactory
                .select(qArticle.count())
                .from(qArticle)
                .fetchOne();
                
            return PageableExecutionUtils.getPage(articles, pageable, () -> totalCount);

        } else {
            // 페이지 번호와 검색 조건에 해당하는 게시글 목록 조회
            List<Article> articles = jpaQueryFactory
                .selectFrom(qArticle)
                .where(
                    writerLike(condition.getWriter())   // 조건이 null이 아니면 where 쿼리에 포함
                    .and(titleLike(condition.getTitle()))
                    .and(contentsLike(condition.getContents()))
                )
                .orderBy(qArticle.id.desc())
                .offset(pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .fetch();

            // 게시물 수 계산
            Long totalCount = jpaQueryFactory
                .select(qArticle.count())
                .from(qArticle)
                .where(
                    writerLike(condition.getWriter())   // 조건이 null이 아니면 where 쿼리에 포함
                    .and(titleLike(condition.getTitle()))
                    .and(contentsLike(condition.getContents()))
                )
                .fetchOne();

            return PageableExecutionUtils.getPage(articles, pageable, () -> totalCount);

        }
        

    }

    // 동적 쿼리
    private BooleanExpression writerLike(String writer) {
        return writer == null ? null : qArticle.writer.contains(writer);
    }

    private BooleanExpression titleLike(String title) {
        return title == null ? null : qArticle.title.contains(title);
    }

    private BooleanExpression contentsLike(String contents) {
        return contents == null ? null : qArticle.contents.contains(contents);
    }
}
