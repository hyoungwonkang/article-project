package com.example.article_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.article_project.domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, CustomArticleRepository {
    // 파일이 있는 경우까지 포함한 조회
    @Query("select a from Article a left join fetch a.files f where a.id = :articleId")
    Article findArticleById(@Param("articleId") Long id);

    @Query("select size(a.files) from Article a where a.id = :articleId")
    int getFileCount(@Param("articleId") Long id);

    // elementCollection의 첫 번째 요소 가져오기였으나 이제 entity로 바뀌어 사용 안함
    // @Query("select a from Article a join a.files f where a.id = :articleId and index(f) = 0")
    // Article findArticleWithFirstFile(@Param("articleId") Long id);
    
    // 개인 연습
    @Query("select a from Article a join fetch a.categories c where a.id = :articleId and index(c) = 0")
    Article findArticleWithFirstCategory(@Param("articleId") Long id);
}
