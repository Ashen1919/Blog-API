CREATE TABLE users
(
    id         INT AUTO_INCREMENT NOT NULL,
    username   VARCHAR(100)       NOT NULL,
    password   VARCHAR(50)        NOT NULL,
    email      VARCHAR(255)       NOT NULL,
    `role`     VARCHAR(50)        NOT NULL,
    created_at datetime           NOT NULL,
    updated_at datetime           NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

CREATE TABLE category
(
    id   INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(100)       NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE post_tags
(
    post_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL
);

CREATE TABLE tags
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(50)           NOT NULL,
    CONSTRAINT pk_tags PRIMARY KEY (id)
);

ALTER TABLE tags
    ADD CONSTRAINT uc_tags_name UNIQUE (name);

CREATE TABLE posts
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    title       VARCHAR(100)          NOT NULL,
    content     TEXT                  NULL,
    author_id   INT                   NOT NULL,
    created_at  datetime              NOT NULL,
    updated_at  datetime              NULL,
    category_id INT                   NULL,
    CONSTRAINT pk_posts PRIMARY KEY (id)
);

ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id);

ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE post_tags
    ADD CONSTRAINT fk_post_tags_on_post FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE post_tags
    ADD CONSTRAINT fk_post_tags_on_tag FOREIGN KEY (tag_id) REFERENCES tags (id);

CREATE TABLE comments
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    content    TEXT                  NOT NULL,
    post_id    BIGINT                NOT NULL,
    author_id  INT                   NOT NULL,
    created_at datetime              NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);


