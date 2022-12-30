create table audiences
(
    id   bigserial primary key,
    name text not null
);
create table time_spans
(
    id               bigserial primary key,
    number_of_lesson int  not null,
    start_time       time not null,
    end_time         time not null
);
create table departments
(
    id   bigserial primary key,
    name text not null
);
create table specialties
(
    id            bigserial primary key,
    name          text   not null,
    department_id bigint references departments (id) on delete set null
);

create table groups
(
    id           bigserial primary key,
    name         text   not null,
    course       text   not null,
    specialty_id bigint references specialties (id) on delete set null
);
create table users
(
    id            bigserial primary key,
    user_type     text,
    name          text   not null,
    password_hash text   not null,
    group_id      bigint references groups (id) on delete set null,
    department_id bigint references departments (id) on delete set null
);

create table disciplines
(
    id           bigserial primary key,
    name         text not null,
    ects         float,
    total_clock  integer,
    course       text not null,
    specialty_id bigint references specialties (id) on delete cascade
);
create table teachers_disciplines
(
    teacher_id    bigint references users (id) on delete cascade,
    discipline_id bigint references disciplines (id) on delete cascade,
    primary key (discipline_id, teacher_id)
);

create table lessons
(
    id            bigserial primary key,
    discipline_id bigint references disciplines (id) on delete cascade,
    group_id      bigint references groups (id) on delete cascade,
    teacher_id    bigint references users (id) on delete set null,
    date_lesson   date   not null,
    time_span_id  bigint references time_spans (id) on delete cascade,
    audience_id   bigint references audiences (id) on delete cascade,
    type_lesson   text
);