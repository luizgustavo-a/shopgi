INSERT INTO users (id, username, password, created_at, updated_at)
VALUES ('6fc3f800-1790-48eb-aa99-61055b94fd39', 'admin', '$2a$10$wmZe/EGU.5de3qrd3/IQN.tVM3LN.if7wOAvpLnx1KQehjkib9ye2', NOW(), NOW());

INSERT INTO user_roles(user_id, role)
VALUES ('6fc3f800-1790-48eb-aa99-61055b94fd39', 'ROLE_ADMIN')

