package com.github.mrchcat.myblog.post.mapper;

import com.github.mrchcat.myblog.post.domain.Post;
import com.github.mrchcat.myblog.post.dto.NewPostDto;
import com.github.mrchcat.myblog.post.dto.PostDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

public class PostMapper {

    public static PostDto toDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .name(post.getName())
                .base64Jpeg(post.getBase64Jpeg())
                .text(post.getText())
                .likes(post.getLikes())
                .commentsNumber(post.getCommentsNumber())
                .build();
    }

    public static Post toPost(NewPostDto newPostDto) {
        String base64Jpeg = null;
        try {
            MultipartFile multipartImage = newPostDto.getImage();
            if (!multipartImage.isEmpty()) {
                base64Jpeg = Base64.getEncoder().encodeToString(newPostDto.getImage().getBytes());
            }
        } catch (IOException e) {
        }
        return Post.builder()
                .name(newPostDto.getName())
                .text(newPostDto.getText())
                .base64Jpeg(base64Jpeg)
                .likes(0)
                .commentsNumber(0)
                .build();
    }
}
