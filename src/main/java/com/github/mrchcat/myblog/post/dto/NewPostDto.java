package com.github.mrchcat.myblog.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class NewPostDto {
    private String name;
    private MultipartFile image;
    private String text;
    private String tags;
}
