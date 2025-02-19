package com.github.mrchcat.myblog.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Comment {
    private long id;
    private String text;
}
