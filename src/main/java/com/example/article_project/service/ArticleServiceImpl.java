package com.example.article_project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.article_project.domain.Article;
import com.example.article_project.dto.ArticleDto;
import com.example.article_project.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = false)
    @Override
    public Long registerArticle(ArticleDto articleDto) {
        return articleRepository.save(dtoToEntity(articleDto)).getId();
    }

    @Override
    public ArticleDto retrieveArticle(Long id) {
        // Article article = articleRepository.findById(id)
        //     .orElseThrow(()->{
        //         return new IllegalArgumentException(id + "에 해당하는 게시글 정보가 없습니다.")
        //     });

        //     ArticleDto articleDto = entityToDto(article);

        //     return articleDto;


        // return articleRepository.findById(id)
        //     .map(entityToDto)
        //     .orElseThrow(() ->{
        //         return new IllegalArgumentException(id + "에 해당하는 게시글 정보가 없습니다.")
        //     });

        
        return articleRepository.findById(id)
            .map(article -> entityToDto(article)) // 람다
            .orElseThrow(() ->{
                return new IllegalArgumentException(id + "에 해당하는 게시글 정보가 없습니다.");
            });
    }

    @Transactional(readOnly = false)
    @Override
    public void modifyArticle(ArticleDto articleDto) {

        Article article = articleRepository.findById(articleDto.getId())
            .orElseThrow(() ->{
                return new IllegalArgumentException(articleDto.getId() + "에 해당하는 게시글 정보가 없습니다.");
            });
        
        article.setTitle(articleDto.getTitle());
        article.setContents(articleDto.getContents());
        article.setWriter(articleDto.getWriter());
    }

    @Transactional(readOnly = false)
    @Override
    public void removeArticle(Long id) {

        articleRepository.findById(id)
            .orElseThrow(() ->new IllegalArgumentException(id + "에 해당하는 게시글 정보가 없습니다."));

        articleRepository.deleteById(id);
    }
}
