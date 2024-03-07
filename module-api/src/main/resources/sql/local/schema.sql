-- define local database schema

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
