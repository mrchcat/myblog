package com.github.mrchcat.myblog.post.repository;

import com.github.mrchcat.myblog.post.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> getPost(long postId);

    void deletePost(long postId);

    long savePost(Post post);

    void addLike(long postId);

}
