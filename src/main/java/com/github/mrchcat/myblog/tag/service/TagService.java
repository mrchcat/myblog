package com.github.mrchcat.myblog.tag.service;

import com.github.mrchcat.myblog.tag.domain.Tag;

import java.util.List;

public interface TagService {

    List<Tag> saveTags(String tagsAsLine, long posId);

    void deleteSingleTagsOfPost(long posId);

    void unlinkTagsFromPost(long postId);

}
