drop table if exists phrase_group;
create table phrase_group
(
    id             int          not null primary key identity(1,1),
    name           varchar(100) not null,
    description    varchar(1000),
    image_url      varchar(255),
    next_date      datetime
)