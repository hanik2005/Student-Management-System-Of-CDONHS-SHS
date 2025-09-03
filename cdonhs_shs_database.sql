-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 03, 2025 at 05:26 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cdonhs_shs_database`
--

-- --------------------------------------------------------

--
-- Table structure for table `general_average`
--

CREATE TABLE `general_average` (
  `general_average_id` int(11) NOT NULL,
  `student_id` int(10) NOT NULL,
  `grade_level` int(2) NOT NULL,
  `strand_name` varchar(30) NOT NULL,
  `section_name` varchar(3) NOT NULL,
  `quarter_1_average` decimal(5,2) DEFAULT NULL,
  `quarter_2_average` decimal(5,2) DEFAULT NULL,
  `quarter_3_average` decimal(5,2) DEFAULT NULL,
  `quarter_4_average` decimal(5,2) DEFAULT NULL,
  `final_average` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `general_average`
--

INSERT INTO `general_average` (`general_average_id`, `student_id`, `grade_level`, `strand_name`, `section_name`, `quarter_1_average`, `quarter_2_average`, `quarter_3_average`, `quarter_4_average`, `final_average`) VALUES
(1, 1, 11, 'STEM', 'A', 88.00, 88.00, 88.00, 88.00, 88.00),
(2, 2, 11, 'STEM', 'A', 12.50, 0.00, 0.00, 0.00, 3.13),
(3, 4, 11, 'STEM', 'A', 61.88, 61.88, 61.88, 61.88, 61.88);

-- --------------------------------------------------------

--
-- Table structure for table `grade`
--

CREATE TABLE `grade` (
  `grade_id` int(11) NOT NULL,
  `student_id` int(10) NOT NULL,
  `grade_level` int(2) NOT NULL,
  `strand_name` varchar(30) NOT NULL,
  `section_name` varchar(5) NOT NULL,
  `subject_1` varchar(50) NOT NULL,
  `sub_grade1` decimal(5,2) NOT NULL,
  `subject_2` varchar(50) NOT NULL,
  `sub_grade2` decimal(5,2) NOT NULL,
  `subject_3` varchar(50) NOT NULL,
  `sub_grade3` decimal(5,2) NOT NULL,
  `subject_4` varchar(50) NOT NULL,
  `sub_grade4` decimal(5,2) NOT NULL,
  `subject_5` varchar(50) NOT NULL,
  `sub_grade5` decimal(5,2) NOT NULL,
  `subject_6` varchar(50) NOT NULL,
  `sub_grade6` decimal(5,2) NOT NULL,
  `subject_7` varchar(50) NOT NULL,
  `sub_grade7` decimal(5,2) NOT NULL,
  `subject_8` varchar(50) NOT NULL,
  `sub_grade8` decimal(5,2) NOT NULL,
  `quarter` int(1) NOT NULL,
  `quarter_average` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `grade`
--

INSERT INTO `grade` (`grade_id`, `student_id`, `grade_level`, `strand_name`, `section_name`, `subject_1`, `sub_grade1`, `subject_2`, `sub_grade2`, `subject_3`, `sub_grade3`, `subject_4`, `sub_grade4`, `subject_5`, `sub_grade5`, `subject_6`, `sub_grade6`, `subject_7`, `sub_grade7`, `subject_8`, `sub_grade8`, `quarter`, `quarter_average`) VALUES
(1, 1, 11, 'STEM', 'A', 'Oral Communication', 70.00, 'Komunikasyon at Pananaliksik', 100.00, 'General Mathematics', 80.00, 'Earth and Life Science', 70.00, 'Understanding Culture, Society and Politics', 90.00, 'Pre-Calculus', 97.00, 'Basic Calculus', 98.00, 'Chemistry 1', 99.00, 1, 88.00),
(2, 1, 11, 'STEM', 'A', 'Oral Communication', 70.00, 'Komunikasyon at Pananaliksik', 100.00, 'General Mathematics', 80.00, 'Earth and Life Science', 70.00, 'Understanding Culture, Society and Politics', 90.00, 'Pre-Calculus', 97.00, 'Basic Calculus', 98.00, 'Chemistry 1', 99.00, 2, 88.00),
(3, 1, 11, 'STEM', 'A', 'Oral Communication', 70.00, 'Komunikasyon at Pananaliksik', 100.00, 'General Mathematics', 80.00, 'Earth and Life Science', 70.00, 'Understanding Culture, Society and Politics', 90.00, 'Pre-Calculus', 97.00, 'Basic Calculus', 98.00, 'Chemistry 1', 99.00, 3, 88.00),
(4, 1, 11, 'STEM', 'A', 'Oral Communication', 70.00, 'Komunikasyon at Pananaliksik', 100.00, 'General Mathematics', 80.00, 'Earth and Life Science', 70.00, 'Understanding Culture, Society and Politics', 90.00, 'Pre-Calculus', 97.00, 'Basic Calculus', 98.00, 'Chemistry 1', 99.00, 4, 88.00),
(5, 2, 11, 'STEM', 'A', 'Oral Communication', 100.00, 'Komunikasyon at Pananaliksik', 0.00, 'General Mathematics', 0.00, 'Earth and Life Science', 0.00, 'Understanding Culture, Society and Politics', 0.00, 'Pre-Calculus', 0.00, 'Basic Calculus', 0.00, 'Chemistry 1', 0.00, 1, 12.50),
(6, 4, 11, 'STEM', 'A', 'Oral Communication', 99.00, 'Komunikasyon at Pananaliksik', 99.00, 'General Mathematics', 99.00, 'Earth and Life Science', 99.00, 'Understanding Culture, Society and Politics', 99.00, 'Pre-Calculus', 0.00, 'Basic Calculus', 0.00, 'Chemistry 1', 0.00, 1, 61.88),
(7, 4, 11, 'STEM', 'A', 'Oral Communication', 99.00, 'Komunikasyon at Pananaliksik', 99.00, 'General Mathematics', 99.00, 'Earth and Life Science', 99.00, 'Understanding Culture, Society and Politics', 99.00, 'Pre-Calculus', 0.00, 'Basic Calculus', 0.00, 'Chemistry 1', 0.00, 2, 61.88),
(8, 4, 11, 'STEM', 'A', 'Oral Communication', 99.00, 'Komunikasyon at Pananaliksik', 99.00, 'General Mathematics', 99.00, 'Earth and Life Science', 99.00, 'Understanding Culture, Society and Politics', 99.00, 'Pre-Calculus', 0.00, 'Basic Calculus', 0.00, 'Chemistry 1', 0.00, 3, 61.88),
(9, 4, 11, 'STEM', 'A', 'Oral Communication', 99.00, 'Komunikasyon at Pananaliksik', 99.00, 'General Mathematics', 99.00, 'Earth and Life Science', 99.00, 'Understanding Culture, Society and Politics', 99.00, 'Pre-Calculus', 0.00, 'Basic Calculus', 0.00, 'Chemistry 1', 0.00, 4, 61.88);

-- --------------------------------------------------------

--
-- Table structure for table `strand`
--

CREATE TABLE `strand` (
  `strand_id` int(11) NOT NULL,
  `student_id` int(10) NOT NULL,
  `grade_level` int(2) NOT NULL,
  `strand` varchar(30) NOT NULL,
  `section_name` varchar(30) NOT NULL,
  `subject_1` varchar(50) NOT NULL,
  `subject_2` varchar(50) NOT NULL,
  `subject_3` varchar(50) NOT NULL,
  `subject_4` varchar(50) NOT NULL,
  `subject_5` varchar(50) NOT NULL,
  `subject_6` varchar(50) NOT NULL,
  `subject_7` varchar(50) NOT NULL,
  `subject_8` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `strand`
--

INSERT INTO `strand` (`strand_id`, `student_id`, `grade_level`, `strand`, `section_name`, `subject_1`, `subject_2`, `subject_3`, `subject_4`, `subject_5`, `subject_6`, `subject_7`, `subject_8`) VALUES
(1, 1, 11, 'STEM', 'A', 'Oral Communication', 'Komunikasyon at Pananaliksik', 'General Mathematics', 'Earth and Life Science', 'Understanding Culture, Society and Politics', 'Pre-Calculus', 'Basic Calculus', 'Chemistry 1'),
(2, 2, 11, 'STEM', 'A', 'Oral Communication', 'Komunikasyon at Pananaliksik', 'General Mathematics', 'Earth and Life Science', 'Understanding Culture, Society and Politics', 'Pre-Calculus', 'Basic Calculus', 'Chemistry 1'),
(3, 3, 11, 'STEM', 'A', 'Oral Communication', 'Komunikasyon at Pananaliksik', 'General Mathematics', 'Earth and Life Science', 'Understanding Culture, Society and Politics', 'Pre-Calculus', 'Basic Calculus', 'Chemistry 1'),
(4, 4, 11, 'STEM', 'A', 'Oral Communication', 'Komunikasyon at Pananaliksik', 'General Mathematics', 'Earth and Life Science', 'Understanding Culture, Society and Politics', 'Pre-Calculus', 'Basic Calculus', 'Chemistry 1');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `student_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `middle_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `date_of_birth` date NOT NULL,
  `gender` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `mother_name` varchar(50) NOT NULL,
  `father_name` varchar(50) NOT NULL,
  `address1` varchar(50) NOT NULL,
  `address2` varchar(50) NOT NULL,
  `birth_certificate` varchar(100) NOT NULL,
  `form_137` varchar(100) NOT NULL,
  `image_path` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`student_id`, `first_name`, `middle_name`, `last_name`, `date_of_birth`, `gender`, `email`, `phone_number`, `mother_name`, `father_name`, `address1`, `address2`, `birth_certificate`, `form_137`, `image_path`) VALUES
(1, 'Nick Charles', 'Durangparang', 'Claritoy', '2005-08-20', 'Male', 'nick@gmail.com', '09944719534', 'papa', 'mama', 'dsadsad', 'dasdasdsa', 'dasdsada', 'dsdsdzzzzss', 'D:\\PICTURES\\anime-moon-landscape.jpg'),
(2, 'Andry', 'Dur', 'Clarito', '2020-08-08', 'Male', 'dadsad@gmail.com', '099483434', 'dadsad', 'dasdad', 'dadad', 'adad', 'adadafasf', 'dsgdsgg', 'D:\\PICTURES\\boy_pick_right_2.png'),
(3, 'dartSa', 'dasay', 'dilayse', '2005-08-20', 'Male', 'nick123@gmail.com', '09944732535', 'mama', 'koksdsds', 'tae', 'poop', 'dads', 'dadddddad', 'D:\\PICTURES\\boy_pick_right_1.png'),
(4, 'Nick', 'lols', 'Mamarandy', '2014-09-19', 'Male', 'nickTheGreat123@gmail.com', '09866554555', 'dsfsdfsf', 'ffsfsdf', 'fsdfsf', 'fsfsd', 'fsdfs', 'fsfsdf', 'D:\\PICTURES\\boy_pick_right_1.png');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `Id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `password` varchar(30) NOT NULL,
  `type` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`Id`, `user_id`, `password`, `type`) VALUES
(1, 1, '123', 'Admin'),
(4, 4, 'mamarandy20140919', 'Student');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `general_average`
--
ALTER TABLE `general_average`
  ADD PRIMARY KEY (`general_average_id`),
  ADD KEY `student_id` (`student_id`);

--
-- Indexes for table `grade`
--
ALTER TABLE `grade`
  ADD PRIMARY KEY (`grade_id`),
  ADD KEY `student_id` (`student_id`);

--
-- Indexes for table `strand`
--
ALTER TABLE `strand`
  ADD PRIMARY KEY (`strand_id`),
  ADD KEY `student_id` (`student_id`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`student_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `user_id` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `general_average`
--
ALTER TABLE `general_average`
  MODIFY `general_average_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `grade`
--
ALTER TABLE `grade`
  MODIFY `grade_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `strand`
--
ALTER TABLE `strand`
  MODIFY `strand_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `student`
--
ALTER TABLE `student`
  MODIFY `student_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `Id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `general_average`
--
ALTER TABLE `general_average`
  ADD CONSTRAINT `general_average_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`);

--
-- Constraints for table `grade`
--
ALTER TABLE `grade`
  ADD CONSTRAINT `grade_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`);

--
-- Constraints for table `strand`
--
ALTER TABLE `strand`
  ADD CONSTRAINT `strand_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `student` (`student_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
