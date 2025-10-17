package com.example.article_project.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.article_project.dto.ArticleDto;
import com.example.article_project.dto.ArticleSearchCondition;
import com.example.article_project.dto.PageRequestDto;
import com.example.article_project.dto.PageResponseDto;
import com.example.article_project.service.ArticleService;

import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    // 검색
    @GetMapping("/articles")
    public ResponseEntity<PageResponseDto<ArticleDto>> search(
            @RequestParam(required = false) String keyfield, // defaultValue = "" 을 제거하여 키필드와 키워드가 이상하면 익셉션 발생 유도. 키필드가 정상적이면 키워드가 비어있어도 전체조회 가능.
            @RequestParam(required = false) String keyword,
            PageRequestDto pageRequestDto) {

        if (keyfield == "" && keyword == "") {
            PageResponseDto<ArticleDto> pageResponseDto = articleService.paging(pageRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(pageResponseDto);
        } else {
            ArticleSearchCondition condition = new ArticleSearchCondition();

            switch (keyfield) {
                case "writer":
                    condition.setWriter(keyword);
                    break;
                case "title":
                    condition.setTitle(keyword);
                    break;
                case "contents":
                    condition.setContents(keyword);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid search field: '" + keyfield + "'. Only 'title', 'writer', 'contents' are allowed.");
            }

            PageResponseDto<ArticleDto> pageResponseDto = articleService.search(condition, pageRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(pageResponseDto);
        }

    }

    // 페이징 조회
    // @GetMapping("/articles")
    // public ResponseEntity<PageResponseDto<ArticleDto>> paging(PageRequestDto pageRequestDto) {
    //     PageResponseDto<ArticleDto> pageResponseDto = articleService.paging(pageRequestDto);
    //     return ResponseEntity.status(HttpStatus.OK).body(pageResponseDto);
    // }

    // 게시글 등록
    @PostMapping("/articles")
    public ResponseEntity<Map<String, Long>> postArticle(@RequestBody ArticleDto articleDto) {

        Long id = articleService.registerArticle(articleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id));

    }

    // 게시글 상세조회: getArticle
    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleDto> getArticle(@PathVariable(value = "id") Long articleId) {
        
        ArticleDto articleDto = articleService.retrieveArticle(articleId);
        return ResponseEntity.status(HttpStatus.OK).body(articleDto);
    }

    // 게시글 수정
    @PutMapping("/articles/{id}")
    public ResponseEntity<String> putArticle(
        @PathVariable(value = "id") Long artticleId,
        @RequestBody ArticleDto articleDto) {
        
        articleDto.setId(artticleId);

        articleService.modifyArticle(articleDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }

    // 게시글 삭제
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable(value = "id") Long artticleId) {
        articleService.removeArticle(artticleId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
