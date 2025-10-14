package com.example.article_project.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.article_project.domain.Article;
import com.example.article_project.domain.Attachment;

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
        List<Article> articles = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Article article = Article.builder()
                    .title("제목 " + i)
                    .contents("내용 " + i)
                    .writer("작성자 " + i)
                    // .regDate(LocalDateTime.now())
                    .build();
                articles.add(article);
        }
        
        //when
        articleRepository.saveAll(articles);

        //then
        assertThat(articles).hasSize(100);

        // //given
        // Article article = Article.builder()
        //         .title("아무거나")
        //         .contents("내용")
        //         .writer("아무개")
        //         .build();

        // //when
        // Long id =  articleRepository.save(article).getId();
        // log.info("id: {}", id);

        // //then
        // assertThat(id).isNotNull();
        // assertThat(article.getTitle()).isEqualTo("아무거나");
    }
    
    @Test
    void testFindById() {
        //given
        Long articleId = 200L;

        //when
        Optional<Article> found = articleRepository.findById(articleId);

        if (found.isPresent()) {
            Article article = found.get();
            log.info("id: {}", article.getId());
            log.info("title: {}", article.getTitle());
            log.info("writer: {}", article.getWriter());
            log.info("contents: {}", article.getContents());
        } else {
            log.info("{}에 해당하는 게시글이 존재하지 않습니다.", articleId);
        }

        //then
    }

    @Test
    // 익셉션이 나오는걸 확인하는 테스트
    void testFindById1() {
        //given
        Long articleId = 2L;

        //when
        assertThatThrownBy(()->{
            Article article = articleRepository.findById(articleId)
                .orElseThrow(()->new IllegalAccessException(articleId + "에 해당하는 게시글이 존재하지 않습니다."));
        
        log.info("id: {}", article.getId());
        log.info("title: {}", article.getTitle());
        log.info("writer: {}", article.getWriter());
        log.info("contents: {}", article.getContents());

        }).isInstanceOf(IllegalAccessException.class);

        //then
    }

    // 게시글 수정
    @Test
    @Rollback(false)
    void testUpdate() {
        //given
        Long articleId = 1L;
        Article article = articleRepository.findById(articleId).get();

        //when
        article.setTitle("title 수정");
        article.setContents("contents 수정");
        article.setWriter("writer 수정");
        
        //then
        assertThat(article.getTitle()).isEqualTo("title 수정");
        assertThat(article.getContents()).isEqualTo("contents 수정");
        assertThat(article.getWriter()).isEqualTo("writer 수정");
    }

    // 게시글 삭제
    @Test
    @Rollback(false)
    void testDelete() {
        //given
        Long articleId = 100L; // 없는 번호

        //when
        articleRepository.deleteById(articleId);

        //then
        assertThatThrownBy(()->{
            articleRepository.findById(articleId)
                .orElseThrow(()->new IllegalAccessException(articleId + "에 해당하는 게시글이 존재하지 않습니다."));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Rollback(false)
    void testSaveArticleAndFile() {
        //given
        List<Attachment> files = new ArrayList<>();
        files.add(new Attachment("a.txt", "/upload", 100L));
        files.add(new Attachment("b.txt", "/upload", 200L));
        files.add(new Attachment("c.txt", "/upload", 300L));
        
        Article article = Article.builder()
            .title("파일 테스트")
            .contents("파일 테스트 내용")
            .writer("파일 테스트 작성자")
            .files(files)
            .build();

        //when
        articleRepository.save(article);

        //then
        assertThat(article.getId()).isNotNull();
    }

    @Test
    @Rollback(false)
    void findArticleWithFirstCategory() {
        //given
        List<String> categories = new ArrayList<>();
        categories.add("파일");
        categories.add("테스트");
        categories.add("카테고리");

        Article article = Article.builder()
            .title("카테고리 테스트")
            .contents("카테고리 테스트 내용")
            .writer("카테고리 테스트 작성자")
            .categories(categories)
            .build();

        //when
        articleRepository.save(article);

        //then
        assertThat(article.getId()).isNotNull();
    }

    @Test
    void testFindArticleById() {
        //given
        Long articleId = 4L;

        //when
        Article article = articleRepository.findArticleById(articleId);
        log.info("id: {}", article.getId());
        log.info("title: {}", article.getTitle());
        log.info("writer: {}", article.getWriter());
        log.info("contents: {}", article.getContents());
        article.getFiles().forEach(file -> log.info("file: {}", file.toString()));



        //then
        assertThat(article.getId()).isNotNull();
        assertThat(article.getFiles()).hasSize(3);

    }

    @Test
    void testGetFileCount() {
        //given
        Long articleId = 4L;

        //when
        int fileCount = articleRepository.getFileCount(articleId);

        //then

        assertThat(fileCount).isEqualTo(3);
    }

    @Test
    void testFindArticleWithFirstFile() {
        //given
        Long articleId = 1L;

        //when
        Article article = articleRepository.findArticleWithFirstFile(articleId);

        //then
        assertThat(article.getId()).isNotNull();
        assertThat(article.getFiles()).hasSize(1);
    }

    
    @Test
    void testFindArticleWithFirstCategory() {
        //given
        Long articleId = 2L;

        //when
        Article article = articleRepository.findArticleWithFirstCategory(articleId);
        log.info("categories: {}", article.getCategories());

        //then
        assertThat(article.getId()).isNotNull();
        assertThat(article.getCategories()).hasSize(1);
    }
}