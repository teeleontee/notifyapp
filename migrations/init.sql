create table if not exists tg_chat
(
    id bigint generated alw
)

create table if not exists link
(
    id bigint primary key not null,
    url text not null,
    checked_time timestamp with time zone not null,
)

create table if not exists task
(
    chat_id bigint references tg_chat (id),
    link_id bigint references link (id),
    primary key (chat_id, link_id)
)


