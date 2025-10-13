package com.example.article_project.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

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
}
