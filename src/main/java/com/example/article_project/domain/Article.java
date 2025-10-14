package com.example.article_project.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
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

    // 1:N 관계
    @ElementCollection
    @CollectionTable(name = "attachment", joinColumns = @JoinColumn(name ="id"))
    @OrderColumn(name="order_index") // 순서 보장
    @Builder.Default
    List<Attachment> files = new ArrayList<>();

    // 2. 카테고리 - 단순 문자열 컬렉션  
    @ElementCollection
    @CollectionTable(name = "article_categories", joinColumns = @JoinColumn(name = "article_id"))
    @OrderColumn(name="order_index") // 순서 보장
    // @Column(name = "category")
    @Builder.Default
    List<String> categories = new ArrayList<>();

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
