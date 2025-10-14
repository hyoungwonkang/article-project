package com.example.article_project.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// value object
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private String name;


}
