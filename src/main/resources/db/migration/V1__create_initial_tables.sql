create table to_do_app.task
(
    id          bigint auto_increment
        primary key,
    description varchar(255) null,
    title       varchar(255) null
);

