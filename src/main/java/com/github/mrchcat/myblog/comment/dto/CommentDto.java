package com.github.mrchcat.myblog.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDto {
    long id;
    String text;
}
