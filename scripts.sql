SELECT * FROM student
SELECT * FROM student WHERE age BETWEEN 10 AND 20;
SELECT name FROM student;
SELECT * FROM student WHERE name LIKE '%о%';
SELECT * FROM student WHERE age < id;
SELECT * FROM student ORDER BY age;
SELECT * FROM faculty;
SELECT count(*) AS count FROM student;
SELECT AVG(age) AS avg FROM student;
SELECT * FROM student ORDER BY id DESC LIMIT 5;