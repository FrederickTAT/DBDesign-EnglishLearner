create database english;
create table word
(
	word_id int not null auto_increment,
	word varchar(40) not null,
	levels varchar(10),
	primary key (word_id),
	unique (word)
);
create table translation
(
	  trans_id int not null auto_increment,
	  property varchar(10),
	  translation varchar(20) not null,
	  primary key(trans_id)
);
create table sentence
(
	sentence_id int not null auto_increment,
    sentence varchar(256) not null,
    translation varchar(128),
    primary key ( sentence_id),
    unique(sentence)
);
create table pronunciation
(
	pron_id int not null auto_increment,
	pronunciation varchar(30) not null,
    primary key (pron_id)
);

create table translate
(
	word_id int not null,
    trans_id int not null,
    foreign key(word_id) references word(word_id),
    foreign key(trans_id) references translation(trans_id)
);
create table instance
(
	word_id int not null,
    sentence_id int not null,
    foreign key(word_id) references word(word_id),
    foreign key(sentence_id) references sentence(sentence_id)
) ;
create table pronunce
(
	word_id int not null,
    en_pron_id int not null,
	us_pron_id int not null,
    foreign key(word_id) references word(word_id),
    foreign key(en_pron_id) references pronunciation(pron_id),
	foreign key(us_pron_id) references pronunciation(pron_id)
);
create table note
(
	word_id int not null,
    date timestamp default CURRENT_TIMESTAMP,
	foreign key(word_id) references word(word_id)
);

