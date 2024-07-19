-- H2 database initialization script for role table

-- Creating role table
CREATE TABLE role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    alias VARCHAR(20) UNIQUE,
    creation_date TIMESTAMP,
    description VARCHAR(255),
    update_date TIMESTAMP
);

-- Inserting data into role table
INSERT INTO role (id, alias, creation_date, description, update_date) VALUES
(1, 'USER', '2024-07-15 14:41:21', 'Normal api user', '2024-07-15 14:41:26');


-- src/test/resources/data.sql
-- H2 database initialization script for app_user and user_role_assoc tables

-- Creating app_user table
CREATE TABLE app_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    address VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP,
    non_expired BOOLEAN,
    non_locked BOOLEAN,
    enabled BOOLEAN,
    password VARCHAR(255) NOT NULL,
    update_date TIMESTAMP,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    f_action_count INT
);

-- Creating user_role_assoc table
CREATE TABLE user_role_assoc (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

-- Inserting data into app_user table
INSERT INTO app_user (id, address, creation_date, non_expired, non_locked, enabled, password, update_date, user_name, f_action_count) VALUES
(45, '22 rue de france 2489', '2024-07-16 17:23:10', TRUE, TRUE, TRUE, '$2a$10$ga.wqVYJLgXNCDtzQtC.aOOTbZi2JVdmWwzrEcEl3iduNlQUuY4Ei', '2024-07-16 17:23:10', 'paul', 0),
(455, '22 rue prince henri L-4563', '2024-07-18 21:50:08', TRUE, TRUE, TRUE, '$2a$10$WlN3uEip4d6VsSxGyg7Lf.1D8FmNVsRKNEwdEt9XBZTjxwv1i5kVq', '2024-07-18 21:50:08', 'joe', 0),
(452, 'new address', '2024-07-18 22:07:19', TRUE, FALSE, TRUE, '$2a$10$zeLm9mxqq5xe.OfI/aq9N.fkJGU7Sb04/m9fWvh2/J6qRZzdvwYsS', '2024-07-19 16:11:55', 'jack', 6);

-- Inserting data into user_role_assoc table
INSERT INTO user_role_assoc (user_id, role_id) VALUES
(45, 1),
(455, 1),
(452, 1);
