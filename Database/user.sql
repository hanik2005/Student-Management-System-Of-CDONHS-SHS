
-- =====================
-- Table: student
-- =====================
CREATE TABLE student (
  lrn BIGINT PRIMARY KEY,  -- unique learner reference number
  first_name VARCHAR(50) NOT NULL,
  middle_name VARCHAR(50),
  last_name VARCHAR(50) NOT NULL,
  date_of_birth DATE NOT NULL,
  gender VARCHAR(20) NOT NULL,
  email VARCHAR(50) UNIQUE NOT NULL,
  phone_number VARCHAR(20),
  mother_name VARCHAR(50),
  father_name VARCHAR(50),
  address1 VARCHAR(50),
  address2 VARCHAR(50),
  birth_certificate VARCHAR(100),
  form_137 VARCHAR(100),
  image_path VARCHAR(100)
);

-- =====================
-- Table: strand
-- =====================
CREATE TABLE strand (
  strand_id INT AUTO_INCREMENT PRIMARY KEY,
  strand_name VARCHAR(30) NOT NULL,
  grade_level INT NOT NULL,
  section_name VARCHAR(30) NOT NULL
);

-- =====================
-- Table: subject
-- =====================
CREATE TABLE subject (
  subject_id INT AUTO_INCREMENT PRIMARY KEY,
  strand_id INT NOT NULL,
  subject_name VARCHAR(50) NOT NULL,
  FOREIGN KEY (strand_id) REFERENCES strand(strand_id)
);

-- =====================
-- Table: student_strand
-- =====================
CREATE TABLE student_strand (
  student_strand_id INT AUTO_INCREMENT PRIMARY KEY,
  lrn BIGINT NOT NULL,
  strand_id INT NOT NULL,
  FOREIGN KEY (lrn) REFERENCES student(lrn),
  FOREIGN KEY (strand_id) REFERENCES strand(strand_id)
);

-- =====================
-- Table: grade
-- =====================
CREATE TABLE grade (
  grade_id INT AUTO_INCREMENT PRIMARY KEY,
  lrn BIGINT NOT NULL,
  subject_id INT NOT NULL,
  quarter INT NOT NULL,
  grade DECIMAL(5,2) NOT NULL,
  FOREIGN KEY (lrn) REFERENCES student(lrn),
  FOREIGN KEY (subject_id) REFERENCES subject(subject_id)
);

-- =====================
-- Table: general_average
-- =====================
CREATE TABLE general_average (
  ga_id INT AUTO_INCREMENT PRIMARY KEY,
  lrn BIGINT NOT NULL,
  grade_level INT NOT NULL,
  strand_id INT NOT NULL,
  quarter INT NOT NULL,
  average DECIMAL(5,2),
  FOREIGN KEY (lrn) REFERENCES student(lrn),
  FOREIGN KEY (strand_id) REFERENCES strand(strand_id)
);

-- =====================
-- Table: user (authentication)
-- =====================
CREATE TABLE user (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  lrn BIGINT,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  type ENUM('Admin', 'Student', 'Teacher') NOT NULL,
  FOREIGN KEY (lrn) REFERENCES student(lrn)
);

-- =====================
-- SAMPLE DATA
-- =====================

-- Insert Students (LRNs must be unique, using sample numbers)
INSERT INTO student (lrn, first_name, middle_name, last_name, date_of_birth, gender, email, phone_number, mother_name, father_name, address1, address2, birth_certificate, form_137, image_path)
VALUES
(123456789000, 'Nick Charles', 'Durangparang', 'Claritoy', '2005-08-20', 'Male', 'nick@gmail.com', '09944719534', 'papa', 'mama', 'dsadsad', 'dasdasdsa', 'dasdsada', 'dsdsdzzzzss', 'D:\\PICTURES\\anime-moon-landscape.jpg'),
(123456789001, 'Andry', 'Dur', 'Clarito', '2020-08-08', 'Male', 'andry@gmail.com', '099483434', 'dadsad', 'dasdad', 'dadad', 'adad', 'adadafasf', 'dsgdsgg', 'D:\\PICTURES\\boy_pick_right_2.png');

-- Insert Strand
INSERT INTO strand (strand_name, grade_level, section_name)
VALUES
('STEM', 11, 'A');

-- Insert Subjects for STEM Strand
INSERT INTO subject (strand_id, subject_name)
VALUES
(1, 'Oral Communication'),
(1, 'Komunikasyon at Pananaliksik'),
(1, 'General Mathematics'),
(1, 'Earth and Life Science'),
(1, 'Understanding Culture, Society and Politics'),
(1, 'Pre-Calculus'),
(1, 'Basic Calculus'),
(1, 'Chemistry 1');

-- Assign Students to Strand
INSERT INTO student_strand (lrn, strand_id) VALUES
(123456789000, 1),
(123456789001, 1);

-- Insert Grades (Nick, LRN 123456789000)
INSERT INTO grade (lrn, subject_id, quarter, grade) VALUES
(123456789000, 1, 1, 70.00),
(123456789000, 2, 1, 100.00),
(123456789000, 3, 1, 80.00),
(123456789000, 4, 1, 70.00),
(123456789000, 5, 1, 90.00),
(123456789000, 6, 1, 97.00),
(123456789000, 7, 1, 98.00),
(123456789000, 8, 1, 99.00);

-- Insert Grades (Andry, LRN 123456789001)
INSERT INTO grade (lrn, subject_id, quarter, grade) VALUES
(123456789001, 1, 1, 100.00),
(123456789001, 2, 1, 0.00),
(123456789001, 3, 1, 0.00),
(123456789001, 4, 1, 0.00),
(123456789001, 5, 1, 0.00),
(123456789001, 6, 1, 0.00),
(123456789001, 7, 1, 0.00),
(123456789001, 8, 1, 0.00);

-- Insert Users
INSERT INTO user (lrn, username, password, type)
VALUES
(123456789000, 'nick_admin', '123', 'Admin'),
(123456789001, 'andry_student', 'mypass123', 'Student');
