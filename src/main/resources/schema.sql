DROP TABLE IF EXISTS "poststags";
DROP TABLE IF EXISTS "comments";
DROP TABLE IF EXISTS "tags";
DROP TABLE IF EXISTS "posts";

CREATE TABLE IF NOT EXISTS "posts" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar(100) NOT NULL,
  "text" varchar(10000) NOT NULL,
  "picture" bytea,
  "likes" bigint NOT NULL,
  "comment_nums" bigint NOT NULL
);

CREATE TABLE IF NOT EXISTS "comments" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "text" varchar(1000) NOT NULL,
  "post_id" bigint NOT NULL,
  CONSTRAINT FK_COMMENTS FOREIGN KEY ("post_id") REFERENCES "posts" ("id") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "tags" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar(20)
);

CREATE TABLE IF NOT EXISTS "poststags" (
  "post_id" bigint,
  "tag_id" bigint,
  PRIMARY KEY ("post_id", "tag_id"),
  CONSTRAINT FK_POSTS FOREIGN KEY ("post_id") REFERENCES "posts" ("id") ON DELETE CASCADE,
  CONSTRAINT FK_TAGS FOREIGN KEY ("tag_id") REFERENCES "tags" ("id") ON DELETE CASCADE
);