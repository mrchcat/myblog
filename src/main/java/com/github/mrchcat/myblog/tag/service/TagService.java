package com.github.mrchcat.myblog.tag.service;

import com.github.mrchcat.myblog.tag.dto.TagDto;

import java.util.List;

public interface TagService {
    List<TagDto> getAllTagsByPost(long postId);

    List<TagDto> saveTags(String tags, long posId);

    void deleteSingleTagsOfPost(long posId);
}
