package com.example.article_project.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String contents;
    private String writer;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    private void changeTitle(String title) {
        this.title = title;
    }

    private void changeContents(String contents) {
        this.contents = contents;
    }

    private void changeWriter(String writer) {
        this.writer = writer;
    }
}
