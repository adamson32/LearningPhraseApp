drop table if exists group_phrases;
create table group_phrases
(
    id             int          not null primary key identity(1,1),
    name           varchar(100) not null,
    description    varchar(1000),
    image          varbinary(max),
    next_date      datetime
)