package com.example.article_project.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArticleDto {
    private Long id;
    private String title;
    private String contents;
    private String writer;
    private LocalDateTime regDate;

    private List<ArticleFileDto> files = new ArrayList<>();
}
