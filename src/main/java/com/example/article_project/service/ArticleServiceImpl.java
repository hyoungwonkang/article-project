package com.example.article_project.service;

import org.springframework.stereotype.Service;

import com.example.article_project.dto.ArticleDto;
import com.example.article_project.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    @Override
    public Long registerArticle(ArticleDto articleDto) {
        return articleRepository.save(dtoToEntity(articleDto)).getId();
    }
}
