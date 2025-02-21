package com.github.mrchcat.myblog.post.service;

import com.github.mrchcat.myblog.post.dto.NewPostDto;
import com.github.mrchcat.myblog.post.dto.PostDto;
import com.github.mrchcat.myblog.post.dto.ShortPostDto;

import java.util.List;

public interface PostService {
    PostDto getPostDto(long postId);

    List<ShortPostDto> getFeed();

    List<ShortPostDto> getFeedByTag(long tagId);

    PostDto editPost(long postId, NewPostDto newPostDto);

    void addLike(long postId);

    void deletePost(long postId);

    void addNewPost(NewPostDto newPostDto);
}
