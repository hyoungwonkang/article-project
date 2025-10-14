package com.example.article_project.service;

import com.example.article_project.domain.Article;
import com.example.article_project.dto.ArticleDto;

public interface ArticleService {
    
    // 게시글 등록
    Long registerArticle(ArticleDto articleDto);

    // 게시글 상세
    ArticleDto retrieveArticle(Long id);

    // 게시글 수정 (modifyArticle)
    void modifyArticle(ArticleDto articleDto);

    // 게시글 삭제
    void removeArticle(Long id);

    // default method
    default Article dtoToEntity(ArticleDto articleDto) {
        return Article.builder()
                .title(articleDto.getTitle())
                .contents(articleDto.getContents())
                .writer(articleDto.getWriter())
                .regDate(articleDto.getRegDate())
                .build();
    }

    default ArticleDto entityToDto(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .contents(article.getContents())
                .writer(article.getWriter())
                .regDate(article.getRegDate())
                .build();
    }

}
