alter table users add constraint uq_user_name unique (name);
alter table audiences add constraint uq_audience_name unique (name);
alter table departments add constraint uq_department_name unique (name);
alter table specialties add constraint uq_specialty_name unique (name);
alter table groups add constraint uq_group_name unique (name);
alter table time_spans add constraint uq_time_span_name unique (number_of_lesson);
