package com.github.mrchcat.myblog.post.repository;

import com.github.mrchcat.myblog.post.domain.Post;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Repository
@Getter
@Setter
public class PostRepositoryImpl implements PostRepository {
    private HashMap<Long, Post> posts = initPosts();

    private HashMap<Long, Post> initPosts() {
        try {
            HashMap<Long, Post> posts = new HashMap<>();
            Path dir = Path.of("C:/Users/User/IdeaProjects/myblog/src/image/");
            Path file = Path.of("cat.jpg");
            byte[] imageArray = Files.readAllBytes(dir.resolve(file));
            String encodedString = Base64.getEncoder().encodeToString(imageArray);
            String text1 = """
                    На сайте Кабмина зарегистрирована петиция с призывом лишить брони получателей грантов от USAID.
                    Автор петиции считает, что сотрудники, чьи организации остались без финансирования, могут быть мобилизованы. А также предлагает провести аудит и тех, кто полученные средства использовал неэффективно или не по назначению, также лишить брони.
                    Петиция набрала уже 2,3 тыс. подписей из 25 тыс. необходимых.
                    """;
            Post post1 = Post.builder()
                    .id(1)
                    .name("FirstPost")
                    .text(text1)
                    .base64Jpeg(encodedString)
                    .likes(10)
                    .commentsNumber(12)
                    .build();
            String text2 = """
                    НВ Европе и на Украине не осталось запасов оружия на
                    складах, заявил глава Rheinmetall Армин Паппергер.
                    Он добавил, что спрос на оружие в регионе останется высоким даже после прекращения конфликта на Украине на фоне обещаний Трампа резко сократить военную поддержку Европы.
                    """;
            Post post2 = Post.builder()
                    .id(2)
                    .name("SecondPost")
                    .text(text2)
                    .base64Jpeg(encodedString)
                    .likes(5)
                    .commentsNumber(23)
                    .build();
            posts.put(post1.getId(), post1);
            posts.put(post2.getId(), post2);
            return posts;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Post getPost(long posId) {
        return posts.get(posId);
    }

    @Override
    public void addLike(long postId) {
        Post post = posts.get(postId);
        post.setLikes(post.getLikes() + 1);
    }

    @Override
    public void deletePost(long postId) {
        posts.remove(postId);
    }

    @Override
    public long savePost(Post post) {
        if (post.getId() != 0) {
            post.setId(posts.size() + 1);
        }
        posts.put(post.getId(), post);
        return post.getId();
    }
}
