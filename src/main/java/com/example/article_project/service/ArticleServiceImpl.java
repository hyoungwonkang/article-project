package com.example.article_project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.article_project.domain.Article;
import com.example.article_project.dto.ArticleDto;
import com.example.article_project.dto.PageRequestDto;
import com.example.article_project.dto.PageResponseDto;
import com.example.article_project.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    @Override
    public PageResponseDto<ArticleDto> paging(PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(pageRequestDto.getPage() -1,
                                           pageRequestDto.getSize(),
                                           Sort.by("id").descending());

        Page<Article> page = articleRepository.findAll(pageable); // PagingAndSortingRepository의 Page<T> findAll(Pageable pageable) 메서드 사용

        List<ArticleDto> posts = page.getContent().stream().map(this::entityToDto).collect(Collectors.toList());

        int totalCount = (int)page.getTotalElements(); // 전체 게시글 수

        return PageResponseDto.<ArticleDto>builder()
            .dtoList(posts)
            .pageRequestDto(pageRequestDto)
            .totalCount(totalCount)
            .build();
    }

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
