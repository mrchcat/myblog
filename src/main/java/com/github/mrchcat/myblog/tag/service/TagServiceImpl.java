package com.github.mrchcat.myblog.tag.service;

import com.github.mrchcat.myblog.tag.domain.Tag;
import com.github.mrchcat.myblog.tag.dto.TagDto;
import com.github.mrchcat.myblog.tag.mapper.TagMapper;
import com.github.mrchcat.myblog.tag.repository.TagRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<TagDto> getAllTagsByPost(long postId) {
        List<Tag> tags = tagRepository.getAllTagsByPost(postId);
        return TagMapper.toDto(tags);
    }

    @Override
    public List<TagDto> saveTags(String tags, long postId) {
        List<String> tagStringList = TagMapper.toTagList(tags);
        List<Tag> tagList = tagRepository.saveTags(tagStringList);
        return TagMapper.toDto(tagList);
    }
}
