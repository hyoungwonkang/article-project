package com.example.article_project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.article_project.domain.Article;
import com.example.article_project.domain.QArticle;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@Slf4j
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // MySQL 사용, H2 안씀
public class ArticleTest {
    @Autowired
    private EntityManager em;

    @Test
    void test() {
        //given
        //Entity
        Article article = Article.builder()
                            .title("Querydsl")
                            .contents("Querydsl 테스트 예제")
                            .writer("hwkang")
                            .build();
        
        em.persist(article);

        // Q 클래스
        QArticle qArticle = QArticle.article;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        
        //when
        // List<Article> articles = queryFactory
        //             .selectFrom(qArticle)
        //             .where(qArticle.title.contains("title"))
        //             // .where(qArticle.title.like("%title"))
        //             .fetch();
        // log.info("articles={}", articles.size());
        
        List<Article> articles = queryFactory
                    .selectFrom(qArticle)
                    .where(qArticle.title.contains("title1").and(qArticle.contents.contains("contents")))
                    // .where(qArticle.title.like("%title"))
                    .orderBy(qArticle.id.desc())
                    .fetch();

        log.info("article.first id: {}", articles.get(0).getId());
        log.info("article.last id: {}", articles.get(articles.size()-1).getId());
        log.info("articles={}", articles.size());


        //then
        assertThat(articles).isNotNull();
    }


    @Test
    void test1() {
        //given
        Long articleId = 2L;

        //when
        QArticle qArticle = QArticle.article;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        //when
        Article article = queryFactory
                    .selectFrom(qArticle)
                    .where(qArticle.id.eq(articleId))
                    .fetchOne();

        //then
        assertThat(article).isNotNull();
    }

}
