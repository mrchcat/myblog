package com.github.mrchcat.myblog.tag.repository;

import com.github.mrchcat.myblog.tag.domain.Tag;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private final HashSet<Tag> tags = init();


    private HashSet<Tag> init() {
        HashSet<Tag> tags = new HashSet<>();
        Tag tag1 = new Tag(1, "кошки");
        Tag tag2 = new Tag(2, "мышки");
        Tag tag3 = new Tag(3, "роботы");
        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);
        return tags;
    }

    @Override
    public List<Tag> getAllTagsByPost(long postId) {
        return tags.stream().toList();
    }

    @Override
    public List<Tag> saveTags(List<String> list) {
        return tags.stream().toList();
    }
}
