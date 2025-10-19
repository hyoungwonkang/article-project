package com.example.article_project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.example.article_project.domain.Article;
import com.example.article_project.domain.Attachment;
import com.example.article_project.dto.ArticleDto;
import com.example.article_project.dto.ArticleFileDto;
import com.example.article_project.dto.ArticleSearchCondition;
import com.example.article_project.dto.PageRequestDto;
import com.example.article_project.dto.PageResponseDto;

public interface ArticleService {
    // 검색
    PageResponseDto<ArticleDto> search(ArticleSearchCondition condition, PageRequestDto pageRequestDto);

    // 페이징
    PageResponseDto<ArticleDto> paging(PageRequestDto pageRequestDto);

    // 게시글 등록 - 파일은 선택적으로 받음 (null 가능)
    Long registerArticle(ArticleDto articleDto, List<MultipartFile> files);

    // 게시글 상세
    ArticleDto retrieveArticle(Long id);

    // 게시글 수정 (modifyArticle)
    void modifyArticle(ArticleDto articleDto);

    // 게시글 삭제
    void removeArticle(Long id);

    // default method
    default Article dtoToEntity(ArticleDto articleDto) {
        List<Attachment> attachments = new ArrayList<>();
        if (articleDto.getFiles() != null) {
            attachments = articleDto.getFiles().stream()
                .map(fileDto -> Attachment.builder()
                    .fileName(fileDto.getFileName())
                    .filePath(fileDto.getFilePath())
                    .fileSize(fileDto.getFileSize())
                    .build())
                .collect(Collectors.toList());
        }

        Article article = Article.builder()
                .title(articleDto.getTitle())
                .contents(articleDto.getContents())
                .writer(articleDto.getWriter())
                .regDate(articleDto.getRegDate())
                .build();

        // 양방향 관계 설정
        attachments.forEach(attachment -> attachment.setArticle(article));
        article.setFiles(attachments);

        return article;
    }

    default ArticleDto entityToDto(Article article) {
        List<ArticleFileDto> fileDtos = new ArrayList<>();
        if (article.getFiles() != null) {
            fileDtos = article.getFiles().stream()
                .map(attachment -> ArticleFileDto.builder()
                    .id(attachment.getId().intValue()) // Long -> int 변환
                    .fileName(attachment.getFileName())
                    .filePath(attachment.getFilePath())
                    .fileSize(attachment.getFileSize())
                    .articleId(article.getId())
                    .build())
                .collect(Collectors.toList());
        }

        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .contents(article.getContents())
                .writer(article.getWriter())
                .regDate(article.getRegDate())
                .files(fileDtos)
                .build();
    }

}
