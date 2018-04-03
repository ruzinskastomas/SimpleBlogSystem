drop database if exists myBlog;
create database if not exists myBlog;

use myBlog;
create table if not exists users
(
    username varchar(20) not null,
    password varchar(10) not null,
    firstName varchar(20),
    lastName varchar(30),
    PRIMARY KEY (username)
);

INSERT INTO users(username, password, firstName, lastName) 
VALUES ("Michelle", "password", "Michelle", "Graham"), 
("Charles", "password", "Charles", "Dickens"), 
("Steph", "password", "Stephenie", "Meyer"), 
("Rick", "password", "Rick", "Riordan"),
("Heidi", "password", "Johanna", "Spyri");

create table if not exists blog_entries
(
    entryID int not null AUTO_INCREMENT,
    username varchar(10) not null,
    title varchar(150),
    content varchar(600),
    PRIMARY KEY (entryID),
    FOREIGN KEY (username) REFERENCES users(username) on delete cascade
);

INSERT INTO blog_entries (username, title, content)
VALUES ("Michelle", "My Test Entry", "This is the first entry I have written in my blog!"), 
("Michelle", "Second Entry", "I'm not quite sure why I set up this blog"), 
("Michelle", "Retirement entry", "I think I'll give up my blog."), 
("Rick", "Schedule of upcoming releases", "To be added"), 
("Heidi", "Hello world", "Hello world");

create table if not exists friends
(
    friend1 varchar(10) not null,
    friend2 varchar(10) not null,
    PRIMARY KEY (friend1, friend2),
    FOREIGN KEY (friend1) REFERENCES users(username) on delete cascade,
    FOREIGN KEY (friend2) REFERENCES users(username) on delete cascade
);

/*
    Make sure that no matter what order you put in the friend requests,
    you don't have duplication
        No chance of: 
            Row 1) Rick, Charles
            Row 2) Charles, Rick

    Done by making sure that whatever order someone attempts to insert them,
    the usernames will always go in in alphabetical order.
*/
DELIMITER |
CREATE TRIGGER enforce_friendship_order BEFORE INSERT ON friends
  FOR EACH ROW BEGIN
    SET @lowerName := IF(NEW.friend1 < NEW.friend2, NEW.friend1, NEW.friend2);
    SET @higherName := IF(NEW.friend1 > NEW.friend2, NEW.friend1, NEW.friend2);
    SET NEW.friend1 = @lowerName;
    SET NEW.friend2 = @higherName;
  END;
|
DELIMITER ;

INSERT INTO friends 
VALUES ("Rick", "Michelle"), 
("Charles", "Rick"), 
("Heidi", "Steph");
