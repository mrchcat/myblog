# My little facebook

Сервис представляет собой фронтэнд и бэкэнд ленты сообщений. Реализовано создание постов, их редактирование, добавление и удаление комментариев, 
фильтрация по тегам, пагинация. 

Версия: Java 21
Зависимости: Spring Boot, Spring MVC, Spring Data, Thymeleaf, Postgres, Gradle, Testcontainers, JUnit, Lombok, Logback.

Для запуска программы необходим Docker. 
1) Перейдите в папку /myblog и соберите проект командой "gradlew clean bootJar"
2) Выполните команду "docker-compose up". После запуска контейнеров блог будет доступен по адресу http://localhost:8080/myblog. 
В случае конфликта портов внесите исправления в docker-compose.yaml. Длина превью комментариев задается параметром POST_PREVIEW_LENGTH 
