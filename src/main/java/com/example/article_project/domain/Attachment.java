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
public class Attachment {
    
    private String fileName;
    private String filePath;
    private Long fileSize;


}
