package com.github.mrchcat.myblog.tag.mapper;

import com.github.mrchcat.myblog.tag.domain.Tag;
import com.github.mrchcat.myblog.tag.dto.TagDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TagMapper {
    public static List<TagDto> toDto(List<Tag> tags) {
        return tags.stream()
                .map(t -> new TagDto(t.getName()))
                .toList();
    }

    public static List<String> toTagList(String tags) {
        if (tags.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(tags.split("\\s*;\\s*"));
    }

}
