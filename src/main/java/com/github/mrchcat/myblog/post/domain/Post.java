package com.github.mrchcat.myblog.post.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Post {
    private long id;
    private String name;
    private String text;
    private String base64Jpeg;
    private long likes;
    private long commentsNumber;
}
