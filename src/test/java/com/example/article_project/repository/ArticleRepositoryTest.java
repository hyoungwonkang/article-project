package com.example.article_project.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.article_project.domain.Article;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @Rollback(false)
    void testSave() {
        //given
        Article article = Article.builder()
                .title("아무거나")
                .contents("내용")
                .writer("아무개")
                .build();

        //when
        Long id =  articleRepository.save(article).getId();
        log.info("id: {}", id);

        //then
        assertThat(id).isNotNull();
        assertThat(article.getTitle()).isEqualTo("아무거나");
    }

}
