CREATE TABLE IF NOT EXISTS tgchat
(
    id bigint PRIMARY KEY NOT NULL
);

CREATE TABLE IF NOT EXISTS link
(
    id bigint PRIMARY KEY NOT NULL,
    url text NOT NULL,
    checked_time TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS task
(
    chat_id bigint REFERENCES tgchat (id),
    link_id bigint REFERENCES link (id),
    PRIMARY KEY (chat_id, link_id)
);
