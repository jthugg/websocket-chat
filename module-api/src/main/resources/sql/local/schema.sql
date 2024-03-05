-- define schema

-- Member
DROP TABLE IF EXISTS `Member`;
CREATE TABLE `Member` (
    id BIGINT NOT NULL,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM ('ROLE_NORMAL','ROLE_PREMIUM') NOT NULL,
    createdAt TIMESTAMP(3) NOT NULL,
    removedAt TIMESTAMP(3),
    PRIMARY KEY (id)
);
