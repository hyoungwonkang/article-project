package com.example.article_project.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.article_project.dto.ArticleFileDto;

import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnailator;

@Component
public class FileUploadUtils {
    // Component Scan 대상(@Componenet)
    @Value("${file.upload.path}")
    private String uploadPath; // ./upload

    @PostConstruct
    public void init() {
        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdir();
        }

        System.out.println("-------------------------------------uploadPath: " + file.getAbsolutePath());
    }

    public List<ArticleFileDto> uploadFiles(List<MultipartFile> multipartFiles) {
    
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return null;
        }

        List<ArticleFileDto> files = new ArrayList<>();
        
        multipartFiles.forEach(multipartFile -> {
            // 32자리 16진수
            String fileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

            //업로드할 파일 경로
            Path savePath = Paths.get(uploadPath, fileName);

            try {
                // 파일 복사
                Files.copy(multipartFile.getInputStream(), savePath);

                String contentType = multipartFile.getContentType();

                // 업로드된 파일이 이미지인 경우
                if (contentType != null && contentType.startsWith("image")) {
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + fileName);
                    // 원본 이미지 파일을 읽어서 200X200 크기의 썸네일 이미지를 만들어 지정된 경로에 저장한다
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbnailPath.toFile(), 200, 200);
                }

                ArticleFileDto articleFileDto = ArticleFileDto.builder()
                    .fileName(fileName)
                    .filePath(uploadPath)
                    .fileSize(multipartFile.getSize())
                    .build();

                files.add(articleFileDto);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return files;
    }
}
