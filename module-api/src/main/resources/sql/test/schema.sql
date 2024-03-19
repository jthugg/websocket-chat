-- define test database schema

-- Member
DROP TABLE IF EXISTS `Member`;
CREATE TABLE `Member` (
    id BIGINT NOT NULL,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    createdAt TIMESTAMP(3) NOT NULL,
    removedAt TIMESTAMP(3),
    PRIMARY KEY (id)
);

-- Room
-- NOTE: No foreign key for "host" column
DROP TABLE IF EXISTS `Room`;
CREATE TABLE `Room` (
    id BIGINT NOT NULL,
    title VARCHAR(50) NOT NULL,
    password VARCHAR(255),
    capacity INTEGER NOT NULL,
    attending INTEGER NOT NULL,
    saturation FLOAT NOT NULL,
    createdAt TIMESTAMP(3) NOT NULL,
    removedAt TIMESTAMP(3),
    PRIMARY KEY (id)
);

-- Participant
-- NOTE: No foreign key for "member" and "room" column
DROP TABLE IF EXISTS `Participant`;
CREATE TABLE `Participant` (
    id BIGINT NOT NULL,
    member BIGINT NOT NULL,
    room BIGINT NOT NULL,
    isHost TINYINT NOT NULL,
    nickname VARCHAR(20) NOT NULL,
    createdAt TIMESTAMP(3) NOT NULL,
    removedAt TIMESTAMP(3),
    PRIMARY KEY (id)
);
