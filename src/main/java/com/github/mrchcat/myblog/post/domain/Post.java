package com.github.mrchcat.myblog.post.domain;


import com.github.mrchcat.myblog.tag.domain.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
