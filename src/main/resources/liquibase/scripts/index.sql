/**
*Adding indexes for working with the database
*/
-- liquibase formated sql
/**
*Adding an index to search by student name
*/
-- changeset norekhov:1
CREATE INDEX name_idx
ON student (name);
/**
*Adding an index Index for searching by faculty name and color
*/
-- changeset norekhov:2
CREATE INDEX name_and_color_idx
ON faculty (name, color);
