package com.example.article_project.exception;


// Unchecked Exception
// RuntimeException 상속
public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException() {
        super("해당 게시글을 찾을 수 없습니다.");
    }
    public ArticleNotFoundException(String msg) {
        super(msg);
    }
}
