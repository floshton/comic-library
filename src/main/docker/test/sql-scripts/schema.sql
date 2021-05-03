CREATE TABLE personaje ( pk_character INT AUTO_INCREMENT PRIMARY KEY, name varchar(250), alias varchar(50), dt_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, dt_last_sync TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, core BOOLEAN not null default 0 );

CREATE TABLE creator ( pk_creator INT AUTO_INCREMENT PRIMARY KEY, name varchar(250), creator_type varchar(25) );

CREATE TABLE character_creator ( pk_character INT, pk_creator INT, PRIMARY KEY (pk_character, pk_creator), FOREIGN KEY(pk_character) REFERENCES personaje(pk_character), FOREIGN KEY(pk_creator) REFERENCES creator(pk_creator) );

CREATE TABLE comic ( pk_comic INT AUTO_INCREMENT PRIMARY KEY, name varchar(250) );

CREATE TABLE character_comic ( pk_character INT, pk_comic INT, PRIMARY KEY (pk_character, pk_comic), FOREIGN KEY(pk_character) REFERENCES personaje(pk_character), FOREIGN KEY(pk_comic) REFERENCES comic(pk_comic) );

INSERT INTO personaje (name, alias, dt_created, dt_last_sync, core) values ('Iron Man', 'ironman', NOW(), NOW(), true);

INSERT INTO personaje (name, alias, dt_created, dt_last_sync, core) values ('Captain America', 'capamerica', NOW(), NOW(), true);