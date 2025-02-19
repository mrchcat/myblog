package com.github.mrchcat.myblog.post.service;

import com.github.mrchcat.myblog.post.dto.NewPostDto;
import com.github.mrchcat.myblog.post.dto.PostDto;

public interface PostService {
    PostDto getPostDto(long postId);

    void addLike(long postId);

    void deletePost(long postId);

    PostDto editPost(long postId, NewPostDto newPostDto);

}
