package com.github.mrchcat.myblog.tag.repository;

import com.github.mrchcat.myblog.tag.domain.Tag;

import java.util.List;
import java.util.Set;

public interface TagRepository {
    List<Tag> getAllTagsByPost(long postId);

    public List<Tag> saveTagsForPost(Set<String> tagNames, long postId);

    void deleteUnusedTags(List<Long> tagIds);

}
