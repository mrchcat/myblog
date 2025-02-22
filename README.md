# My little facebook

Сервис представляет из себя фронтэнд и бэкэнд афиши. Реализовано создание постов, их редактирование, добавление и удаление комментариев, 
фильтрация по тегам, пагинация. 

Версия: Java 21

Зависимости: Spring MVC, Spring Data, Thymeleaf, Maven, Postgres, JUnit, Mockito, Hamcrest, Lombok, Logback.

Для запуска программы необходим Docker. Перейдите в папку myblog, где расположен файл docker-compose.yaml и выполните команду "docker-compose up".
Блог будет доступен по адресу http://localhost:8080/myblog. В случае конфликта портов внесите исправления в docker-compose.yaml. 
