CREATE TABLE IF NOT EXISTS tgchat
(
    id bigint UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS link
(
    id SERIAL NOT NULL,
    url text UNIQUE NOT NULL,
    content text,
    checked_time TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS task
(
    chat_id bigint REFERENCES tgchat (id) ON DELETE CASCADE,
    link_id bigint REFERENCES link (id) ON DELETE CASCADE,
    PRIMARY KEY (chat_id, link_id)
);
