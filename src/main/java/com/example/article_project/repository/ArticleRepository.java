package com.example.article_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.article_project.domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    @Query("SELECT a FROM Article a WHERE a.id = :articleId")
    Article findArticleById(@Param("articleId") Long id);

    @Query("SELECT SIZE(a.files) FROM Article a WHERE a.id = :articleId")
    int getFileCount(@Param("articleId") Long id);

    // SpringDataJPA가 아닌 하이버네이트 기준
    // @Query("select a from Article a join a.files f where a.id = :articleId and index(f) = 0")
    // Article findArticleWithFirstFile(@Param("articleId") Long id);
}
