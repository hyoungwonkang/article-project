package com.example.article_project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.article_project.domain.Article;
import com.example.article_project.dto.ArticleSearchCondition;

public interface CustomArticleRepository {
    Page<Article> search(ArticleSearchCondition condition, Pageable pageable);
}
