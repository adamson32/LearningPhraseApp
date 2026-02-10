alter table phrases add phrase_group_id int;
alter table phrases
add foreign key (phrase_group_id) references phrase_group(id);
