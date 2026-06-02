-- USERS
CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    username   VARCHAR(100) NOT NULL,
    password   VARCHAR(50)  NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    role       VARCHAR(50)  NOT NULL DEFAULT 'user',
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP
);

-- CATEGORY
CREATE TABLE category
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- TAGS
CREATE TABLE tags
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- POSTS
CREATE TABLE posts
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    content     TEXT,
    author_id   INT NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP,
    category_id INT
);

-- COMMENTS
CREATE TABLE comments
(
    id         BIGSERIAL PRIMARY KEY,
    content    TEXT NOT NULL,
    post_id    BIGINT NOT NULL,
    author_id  INT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- POST_TAGS (many-to-many)
CREATE TABLE post_tags
(
    post_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL
);

-- =========================
-- FOREIGN KEYS
-- =========================

ALTER TABLE posts
    ADD CONSTRAINT fk_posts_author
    FOREIGN KEY (author_id) REFERENCES users (id);

ALTER TABLE posts
    ADD CONSTRAINT fk_posts_category
    FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_author
    FOREIGN KEY (author_id) REFERENCES users (id);

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_post
    FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE post_tags
    ADD CONSTRAINT fk_post_tags_post
    FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE post_tags
    ADD CONSTRAINT fk_post_tags_tag
    FOREIGN KEY (tag_id) REFERENCES tags (id);