package com.example.article_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.article_project.domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("select a from Article a where a.id = :articleId")
    Article findArticleById(@Param("articleId") Long id);

    @Query("select size(a.files) from Article a where a.id = :articleId")
    int getFileCount(@Param("articleId") Long id);

    @Query("select a from Article a join a.files f where a.id = :articleId and index(f) = 0")
    Article findArticleWithFirstFile(@Param("articleId") Long id);
    
    // 개인 연습
    @Query("select a from Article a join fetch a.categories c where a.id = :articleId and index(c) = 0")
    Article findArticleWithFirstCategory(@Param("articleId") Long id);
}
