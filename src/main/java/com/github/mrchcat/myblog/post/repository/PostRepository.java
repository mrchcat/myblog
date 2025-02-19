package com.github.mrchcat.myblog.post.repository;

import com.github.mrchcat.myblog.post.domain.Post;

import java.util.List;

public interface PostRepository {
    Post getPost(long postId);

    void addLike(long postId);

    void deletePost(long postId);

    long savePost(Post post);
}
