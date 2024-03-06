drop table if exists phrases;
create table phrases
(
    id             int          not null primary key identity(1,1),
    phrase         varchar(250) not null,
    description    varchar(1000),
    meaning        varchar(250) not null,
    progress       int          not null default 1,
    next_date      datetime     not null default CURRENT_TIMESTAMP
)
