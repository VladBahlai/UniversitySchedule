CREATE TABLE roles
(
    id   bigserial primary key,
    name text not null
);
CREATE TABLE privileges
(
    id   bigserial primary key,
    name text not null
);

CREATE TABLE roles_privileges
(
    role_id      bigint references roles (id) on delete cascade,
    privilege_id bigint references privileges (id) on delete cascade,
    primary key (role_id, privilege_id)
);

CREATE TABLE users_roles
(
    user_id bigint references users (id) on delete cascade,
    role_id bigint references roles (id) on delete cascade,
    primary key (user_id, role_id)
);