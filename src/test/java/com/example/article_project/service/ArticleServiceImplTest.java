package com.example.article_project.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.example.article_project.dto.ArticleDto;
import com.example.article_project.dto.ArticleFileDto;
import com.example.article_project.dto.ArticleSearchCondition;
import com.example.article_project.dto.PageRequestDto;
import com.example.article_project.dto.PageResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleServiceImplTest {

    @Autowired
    private ArticleService articleService;

    @Test
    void testSearch() {
        //given
        ArticleSearchCondition condition = new ArticleSearchCondition();
        condition.setWriter("writer");

        PageRequestDto pageRequestDto = PageRequestDto.builder()
                                            .page(1)
                                            .size(10)
                                            .build();

        //when
        PageResponseDto<ArticleDto> page = articleService.search(condition, pageRequestDto);

        //then
        log.info("size: {}", page.getDtoList().size());
    }

    @Test
    @Rollback(false)
    void testRegisterArticle() {
        //given
        List<ArticleFileDto> files = new ArrayList<>();

        ArticleDto articleDto = ArticleDto.builder()
                .title("테스트")
                .contents("테스트 내용")
                .writer("테스터")
                .regDate(LocalDateTime.now())
                .files(files)
                .build();

        files.add(ArticleFileDto.builder().fileName("a.txt").filePath("/upload").fileSize(100L).build());
        files.add(ArticleFileDto.builder().fileName("b.txt").filePath("/upload").fileSize(200L).build());
        files.add(ArticleFileDto.builder().fileName("c.txt").filePath("/upload").fileSize(300L).build());

        log.info("files: {}", files.toString());
        log.info("articleDto: {}", articleDto.toString());
        //when
        Long id = articleService.registerArticle(articleDto, null); // files 파라미터는 null
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

    // 페이징 처리
    @Test
    void testPaging() {
        //given
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                                            .page(2)
                                            .size(10)
                                            .build();

        //when
        PageResponseDto<ArticleDto> page = articleService.paging(pageRequestDto);

        log.info("page: {}, size: {}", page.getPageRequestDto().getPage(), page.getPageRequestDto().getSize());
        log.info("page.totalcount: {}", page.getTotalCount());

        page.getDtoList().forEach(article -> log.info("article: {}", article.toString())); // 오름차순으로 나옴. 확인 필요.
        
        //then
        assertThat(page.getDtoList()).hasSize(10);
        assertThat(page.getTotalCount()).isEqualTo(125);
    }

}
