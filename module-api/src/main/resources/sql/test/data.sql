-- define test database data

-- Member (id, username, password, role, createdAt, removedAt)
INSERT INTO `Member` VALUES (0, 'test00', 'test00', 'ROLE_NORMAL', '2024-01-01 00:00:00.000', null);
INSERT INTO `Member` VALUES (1, 'test01', 'test01', 'ROLE_NORMAL', '2024-01-01 00:00:00.000', null);
INSERT INTO `Member` VALUES (2, 'test02', 'test02', 'ROLE_NORMAL', '2024-01-01 00:00:00.000', null);
INSERT INTO `Member` VALUES (3, 'test03', 'test03', 'ROLE_NORMAL', '2024-01-01 00:00:00.000', null);
INSERT INTO `Member` VALUES (4, 'test04', 'test04', 'ROLE_NORMAL', '2024-01-01 00:00:00.000', null);
INSERT INTO `Member` VALUES (5, 'test05', 'test05', 'ROLE_NORMAL', '2024-01-01 00:00:00.000', now());

-- Room(id, title, password, capacity, attending, saturation, createdAt, removedAt)
INSERT INTO `Room` VALUES (0, 'testTitle00', 'test', 3, 3, (3 / 3), '2024-01-02 00:00:00.000', null);
INSERT INTO `Room` VALUES (1, 'testTitle01', null, 3, 1, (1 / 3), '2024-01-02 00:00:00.001', null);
INSERT INTO `Room` VALUES (2, 'testTitle02', 'test', 3, 0, 0, '2024-01-02 00:00:00.003', now());
INSERT INTO `Room` VALUES (3, 'testTitle03', null, 4, 3, (3 / 4), '2024-01-02 00:00:00.004', null);
INSERT INTO `Room` VALUES (4, 'testTitle04', null, 13, 2, (2 / 13), '2024-01-02 00:00:00.005', null);
INSERT INTO `Room` VALUES (5, 'testTitle05', null, 5, 4, (4 / 5), '2024-01-02 00:00:00.006', null);
INSERT INTO `Room` VALUES (6, 'testTitle06', 'test', 2, 2, (2 / 2), '2024-01-02 00:00:00.007', null);
INSERT INTO `Room` VALUES (7, 'testTitle07', null, 12, 11, (11 / 12), '2024-01-02 00:00:00.008', null);
INSERT INTO `Room` VALUES (8, 'testTitle08', null, 19, 19, (19 / 19), '2024-01-02 00:00:00.009', null);
INSERT INTO `Room` VALUES (9, 'testTitle09', null, 100, 71, (71 / 100), '2024-01-02 00:00:00.010', null);
INSERT INTO `Room` VALUES (10, 'testTitle10', 'test', 30, 14, (14 / 30), '2024-01-02 00:00:00.011', null);
INSERT INTO `Room` VALUES (11, 'testTitle11', null, 50, 0, 0, '2024-01-02 00:00:00.012', now());
INSERT INTO `Room` VALUES (12, 'testTitle12', null, 50, 1, (1 / 50), '2024-01-02 00:00:00.013', null);
INSERT INTO `Room` VALUES (13, 'testTitle13', null, 50, 1, (1 / 50), '2024-01-02 00:00:00.014', null);
INSERT INTO `Room` VALUES (14, 'testTitle14', null, 50, 1, (1 / 50), '2024-01-02 00:00:00.015', null);
INSERT INTO `Room` VALUES (15, 'testTitle15', null, 50, 1, (1 / 50), '2024-01-02 00:00:00.016', null);
INSERT INTO `Room` VALUES (16, 'testTitle16', null, 50, 1, (1 / 50), '2024-01-02 00:00:00.017', null);

-- Participant(id, member, room, isHost, nickname, createdAt, removedAt)
INSERT INTO `Participant` VALUES (0, 0, 0, 1, 'iam00', now(), null);
INSERT INTO `Participant` VALUES (1, 1, 0, 0, 'iam01', now(), null);
INSERT INTO `Participant` VALUES (2, 2, 0, 0, 'iam02', now(), null);
INSERT INTO `Participant` VALUES (3, 1, 1, 1, 'iam01', now(), null);
INSERT INTO `Participant` VALUES (4, 2, 2, 1, 'iam02', now(), now());
