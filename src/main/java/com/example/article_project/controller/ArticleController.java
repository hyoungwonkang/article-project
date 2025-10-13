package com.example.article_project.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.article_project.dto.ArticleDto;
import com.example.article_project.service.ArticleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    // 게시글 등록
    @PostMapping("/articles")
    public ResponseEntity<Map<String, Long>> postArticle(@RequestBody ArticleDto articleDto) {

        Long id = articleService.registerArticle(articleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id));

    }
}
