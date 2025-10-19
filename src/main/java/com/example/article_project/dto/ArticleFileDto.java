package com.example.article_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArticleFileDto {
    private int id;
    private String fileName;
    private String filePath;
    private long fileSize;
    private Long articleId;
}
