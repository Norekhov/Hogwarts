-- liquibase formated sql

-- changeset norekhov:1
CREATE INDEX name_idx
ON student (name);

-- changeset norekhov:2
CREATE INDEX name_and_color_idx
ON faculty (name, color);