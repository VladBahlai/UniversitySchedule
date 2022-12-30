INSERT INTO privileges(name) VALUES ('READ_TEACHER_DEPARTMENT_SCHEDULE');
INSERT INTO privileges(name) VALUES ('READ_STUDY_PLAN');
INSERT INTO privileges(name) VALUES ('READ_TEACHER_SCHEDULE');
INSERT INTO privileges(name) VALUES ('READ_USER_SCHEDULE');
INSERT INTO privileges(name) VALUES ('READ_AUDIENCE_SCHEDULE');

INSERT INTO roles_privileges(role_id,privilege_id) VALUES (1,29);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (1,30);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (1,31);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (1,32);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (1,33);

INSERT INTO roles_privileges(role_id,privilege_id) VALUES (2,29);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (2,30);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (2,31);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (2,32);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (2,33);

INSERT INTO roles_privileges(role_id,privilege_id) VALUES (3,29);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (3,32);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (3,33);

INSERT INTO roles_privileges(role_id,privilege_id) VALUES (4,30);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (4,31);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (4,32);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (4,33);
INSERT INTO roles_privileges(role_id,privilege_id) VALUES (4,4);


DELETE FROM roles_privileges WHERE role_id = 3 AND privilege_id = 13;
DELETE FROM roles_privileges WHERE role_id = 3 AND privilege_id = 19;
DELETE FROM roles_privileges WHERE role_id = 3 AND privilege_id = 22;

DELETE FROM roles_privileges WHERE role_id = 4 AND privilege_id = 4;
DELETE FROM roles_privileges WHERE role_id = 4 AND privilege_id = 7;
DELETE FROM roles_privileges WHERE role_id = 4 AND privilege_id = 10;
DELETE FROM roles_privileges WHERE role_id = 4 AND privilege_id = 13;
DELETE FROM roles_privileges WHERE role_id = 4 AND privilege_id = 22;

