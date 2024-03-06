alter table phrases add group_phrases_id int;
alter table phrases
add foreign key (group_phrases_id) references group_phrases(id);
