-- define test database data

INSERT INTO `Member` VALUES (0, 'test00', 'test00', 'ROLE_NORMAL', now(), null);
INSERT INTO `Member` VALUES (1, 'test01', 'test01', 'ROLE_NORMAL', now(), null);
INSERT INTO `Member` VALUES (2, 'test02', 'test02', 'ROLE_NORMAL', now(), null);
INSERT INTO `Member` VALUES (3, 'test03', 'test03', 'ROLE_NORMAL', now(), null);
INSERT INTO `Member` VALUES (4, 'test04', 'test04', 'ROLE_NORMAL', now(), null);
INSERT INTO `Member` VALUES (5, 'test05', 'test05', 'ROLE_NORMAL', now(), now());

INSERT INTO `Room` VALUES (0, 'testTitle00', 'test', 3, 1, now(), null);
INSERT INTO `Room` VALUES (1, 'testTitle01', null, 3, 1, now(), null);
INSERT INTO `Room` VALUES (2, 'testTitle02', 'test', 3, 1, now(), now());

INSERT INTO `Participant` VALUES (0, 0, 0, 1, 'iam00', now(), null);
INSERT INTO `Participant` VALUES (1, 1, 1, 1, 'iam01', now(), null);
INSERT INTO `Participant` VALUES (2, 2, 2, 1, 'iam02', now(), now());
