package com.github.mrchcat.myblog.tag.repository;

import com.github.mrchcat.myblog.tag.domain.Tag;

import java.util.List;
import java.util.Set;

public interface TagRepository {

    public List<Tag> saveTagsForPost(Set<String> tagNames, long postId);

    void deleteSingleTagsOfPost(long postId);

    void unlinkTagsFromPost(long postId);

}
