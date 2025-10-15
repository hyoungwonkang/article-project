package com.example.article_project.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PageResponseDto<T> {
    
    private List<T> dtoList;

    private PageRequestDto pageRequestDto;

    private int totalCount;

    private boolean prev = false, next = false;

    private int start = 0, end = 0;

    private int prevPage = 0, nextPage = 0, totalPage = 0, currentPage = 0, size = 0;

    private int pageSize = 10; // 페이지 블록 수

    private List<Integer> pageNumList = new ArrayList<>();

    @Builder
    private PageResponseDto(List<T> dtoList, PageRequestDto pageRequestDto, int totalCount) {
        this.dtoList = dtoList;
        this.pageRequestDto = pageRequestDto;
        this.totalCount = totalCount;

        this.currentPage = pageRequestDto.getPage();

        this.size = pageRequestDto.getSize();

        // 현재 페이지 번호가 속한 페이지 블록의 마지막(끝) 페이지 번호 계산

        end = (int) (Math.ceil((double) currentPage / pageSize) * pageSize);

        log.info("end: {}", end);

        start = end - (pageSize - 1);
    
        log.info("start: {}", end);
    
        // 총 페이지 수
        int lastPage = (int) Math.ceil((double) totalCount / size);

        log.info("lastPage: {}", lastPage);

        this.totalPage = lastPage;

        log.info("totalPage: {}", totalPage);

        // 끝 페이지 번호가 총 페이지수를 넘지 않도록 설정
        end = end > lastPage ? lastPage : end;

        log.info("end: {}", end);

        // 이전/다음 페이지 블록 존재 여부
        this.prev = start > 1;

        log.info("prev: {}", prev);

        this.next = totalCount > (end * size);

        log.info("next: {}", next);

        // 페이지 번호를 생성
        //IntStream -> Stream<Integer> -> List<Integer>
        this.pageNumList = IntStream.range(start, end + 1).boxed().collect(Collectors.toList());

        log.info("pageNumList: {}", pageNumList);

        // prev 활성화된 경우 prevPage 이전 페이지 블록의 마지막 페이지 번호 생성
        // prev 비활성화된 경우 prevPage =0 설정
        this.prevPage = prev ? start -1 : 0;

        log.info("prevPage: {}", prevPage);

        // next 활성화된 경우 nextPage 다음 페이지 블록의 첫 페이지 번호 생성
        // next 비활성화된 경우 nextPage =0 설정
        this.nextPage = next ? end + 1 : 0;

        log.info("nextPage: {}", nextPage);

    }
}
