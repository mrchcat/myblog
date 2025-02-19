package com.github.mrchcat.myblog.tag.repository;

import com.github.mrchcat.myblog.tag.domain.Tag;
import com.github.mrchcat.myblog.tag.dto.TagDto;

import java.util.List;

public interface TagRepository {
    List<Tag> getAllTagsByPost(long postId);
    List<Tag> saveTags(List<String> tags);
}
