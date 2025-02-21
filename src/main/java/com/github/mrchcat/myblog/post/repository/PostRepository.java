package com.github.mrchcat.myblog.post.repository;

import com.github.mrchcat.myblog.post.domain.Post;

import java.util.Collection;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> getPost(long postId);

    Collection<Post> getFeed();

    long updatePost(Post post);

    void deletePost(long postId);

    void addLike(long postId);

    long addNewPost(Post post);

}
