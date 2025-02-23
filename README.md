# My little facebook

Сервис представляет собой фронтэнд и бэкэнд ленты сообщений. Реализовано создание постов, их редактирование, добавление и удаление комментариев, 
фильтрация по тегам, пагинация. 

Версия: Java 21
Зависимости: Spring MVC, Spring Data, Thymeleaf, Postgres, Maven, JUnit, Mockito, Hamcrest, Lombok, Logback.
В качестве контейнера сервлетов используется Tomcat.

Для запуска программы необходим Docker и Maven. 
1) Для сборки war архива выполните команду "mvn package" 
2) Перейдите в папку /myblog, где расположен файл docker-compose.yaml и выполните команду "docker-compose up". После запуска контейнеров,
блог будет доступен по адресу http://localhost:8080/myblog. 
В случае конфликта портов внесите исправления в docker-compose.yaml. 
