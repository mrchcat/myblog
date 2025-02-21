package com.github.mrchcat.myblog.post.mapper;

import com.github.mrchcat.myblog.comment.dto.CommentDto;
import com.github.mrchcat.myblog.post.domain.Post;
import com.github.mrchcat.myblog.post.dto.NewPostDto;
import com.github.mrchcat.myblog.post.dto.PostDto;
import com.github.mrchcat.myblog.post.dto.ShortPostDto;
import com.github.mrchcat.myblog.tag.mapper.TagMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

public class PostMapper {
    private static final int POST_PREVIEW_LENGTH = 150;


    public static PostDto toDto(Post post, List<CommentDto> commentDtos) {
        return PostDto.builder()
                .id(post.getId())
                .name(post.getName())
                .base64Jpeg(post.getBase64Jpeg())
                .text(post.getText())
                .likes(post.getLikes())
                .commentCounter(post.getCommentCounter())
                .tagsDto(TagMapper.toDto(post.getTags()))
                .commentsDto(commentDtos)
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
        System.out.println("[" + base64Jpeg + "]");

        return Post.builder()
                .name(newPostDto.getName())
                .text(newPostDto.getText())
                .base64Jpeg(base64Jpeg)
                .likes(0)
                .commentCounter(0)
                .build();
    }

    public static ShortPostDto toShortDto(Post post) {
        String text = post.getText();
        String preview = (text.length() <= POST_PREVIEW_LENGTH) ? text : text.substring(0, POST_PREVIEW_LENGTH) + "...";
        return ShortPostDto.builder()
                .id(post.getId())
                .name(post.getName())
                .base64Jpeg(post.getBase64Jpeg())
                .text(preview)
                .likes(post.getLikes())
                .commentCounter(post.getCommentCounter())
                .tagsDto(TagMapper.toDto(post.getTags()))
                .build();
    }

    public static List<ShortPostDto> toShortDto(Collection<Post> posts) {
        return posts.stream()
                .map(PostMapper::toShortDto)
                .toList();
    }
}
