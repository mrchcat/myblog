package com.github.mrchcat.myblog.post.repository;

import com.github.mrchcat.myblog.post.domain.Post;
import com.github.mrchcat.myblog.post.dto.ShortPostDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> getPost(long postId);

    Collection<Post> getFeed();

    Collection<Post> getFeedByTag(long tagId);

    long addNewPost(Post post);

    long updatePost(Post post);

    void deletePost(long postId);

    void addLike(long postId);


}
