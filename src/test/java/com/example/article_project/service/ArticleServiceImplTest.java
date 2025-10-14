package com.example.article_project.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.example.article_project.dto.ArticleDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleServiceImplTest {

    @Autowired
    private ArticleService articleService;

    @Test
    void testRegisterArticle() {
        //given
        ArticleDto articleDto = ArticleDto.builder()
                .title("테스트")
                .contents("테스트 내용")
                .writer("테스터")
                .build();

        //when
        Long id = articleService.registerArticle(articleDto);
        log.info("id: {}", id);

        //then
        assertThat(id).isNotNull();
    }

    @Test
    void testRetrieveArticle() {
        //given
        Long articleId = 10L;
        
        //when
        ArticleDto articleDto = articleService.retrieveArticle(articleId);
        log.info("articleDto: {}", articleDto.toString());
        
        //then
        assertThat(articleDto).isNotNull();
        assertThat(articleDto.getId()).isEqualTo(10L);
    }

    @Test
    @Rollback(false)
    void testMofifyArticle() {
        //given
        ArticleDto articleDto = ArticleDto.builder()
            .id(1L)
            .title("title 수정1")
            .contents("contents 수정1")
            .writer("writer 수정1")
            .build();

        //when
        articleService.modifyArticle(articleDto);

        //then
        ArticleDto found = articleService.retrieveArticle(articleDto.getId());
        
        assertThat(found.getTitle()).isEqualTo("title 수정1");
        assertThat(found.getContents()).isEqualTo("contents 수정1");
        assertThat(found.getWriter()).isEqualTo("writer 수정1");

    }
}
