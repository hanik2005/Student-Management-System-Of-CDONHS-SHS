-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 13, 2025 at 04:04 PM
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
-- Database: `cdonhs_shs_database_new`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `admin_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) NOT NULL,
  `gender` enum('Male','Female') NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `address1` varchar(100) NOT NULL,
  `address2` varchar(100) NOT NULL,
  `role` enum('Super Admin','Registrar','Guidance Admin','Finance Admin') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `grade`
--

CREATE TABLE `grade` (
  `grade_id` int(11) NOT NULL,
  `student_id` int(10) NOT NULL,
  `subject_id` int(11) NOT NULL,
  `quarter` int(1) NOT NULL,
  `grade` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `grade`
--

INSERT INTO `grade` (`grade_id`, `student_id`, `subject_id`, `quarter`, `grade`) VALUES
(177, 1, 1, 1, 93.00),
(178, 1, 2, 1, 94.00),
(179, 1, 3, 1, 92.00),
(180, 1, 4, 1, 95.00),
(181, 1, 5, 1, 95.00),
(182, 1, 6, 1, 93.00),
(183, 1, 7, 1, 94.00),
(184, 1, 8, 1, 98.00),
(185, 1, 1, 2, 77.00),
(186, 1, 2, 2, 75.00),
(187, 1, 3, 2, 76.00),
(188, 1, 4, 2, 95.00),
(189, 1, 5, 2, 95.00),
(190, 1, 6, 2, 93.00),
(191, 1, 7, 2, 94.00),
(192, 1, 8, 2, 98.00),
(193, 1, 1, 3, 66.00),
(194, 1, 2, 3, 55.00),
(195, 1, 3, 3, 77.00),
(196, 1, 4, 3, 77.00),
(197, 1, 5, 3, 76.00),
(198, 1, 6, 3, 93.00),
(199, 1, 7, 3, 94.00),
(200, 1, 8, 3, 98.00),
(201, 1, 1, 4, 93.00),
(202, 1, 2, 4, 94.00),
(203, 1, 3, 4, 92.00),
(204, 1, 4, 4, 95.00),
(205, 1, 5, 4, 95.00),
(206, 1, 6, 4, 93.00),
(207, 1, 7, 4, 94.00),
(208, 1, 8, 4, 98.00),
(209, 2, 33, 1, 98.00),
(210, 2, 34, 1, 92.00),
(211, 2, 35, 1, 93.00),
(212, 2, 36, 1, 94.00),
(213, 2, 37, 1, 95.00),
(214, 2, 38, 1, 96.00),
(215, 2, 39, 1, 98.00),
(216, 2, 40, 1, 99.00);

-- --------------------------------------------------------

--
-- Table structure for table `strands`
--

CREATE TABLE `strands` (
  `strand_id` int(11) NOT NULL,
  `strand_name` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `strands`
--

INSERT INTO `strands` (`strand_id`, `strand_name`) VALUES
(1, 'STEM'),
(2, 'ABM'),
(3, 'HUMSS'),
(4, 'GAS'),
(5, 'TVL-ICT'),
(6, 'TVL-EIM'),
(7, 'TVL-HE');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `student_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `middle_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `date_of_birth` date NOT NULL,
  `gender` enum('Male','Female') NOT NULL,
  `email` varchar(50) NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `mother_name` varchar(50) NOT NULL,
  `father_name` varchar(50) NOT NULL,
  `address1` varchar(50) NOT NULL,
  `address2` varchar(50) NOT NULL,
  `birth_certificate` varchar(100) NOT NULL,
  `form_137` varchar(100) NOT NULL,
  `image_path` varchar(100) NOT NULL,
  `LRN` varchar(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`student_id`, `user_id`, `first_name`, `middle_name`, `last_name`, `date_of_birth`, `gender`, `email`, `phone_number`, `mother_name`, `father_name`, `address1`, `address2`, `birth_certificate`, `form_137`, `image_path`, `LRN`) VALUES
(1, 1, 'Nick', 'hfhfdhd', 'hdfhhdfhh', '2025-09-04', 'Male', 'asdaf@gmail.ocm', '09976866868', 'fasfaf', 'fafasfa', 'fasfaf', 'fafafa', 'fafsaf', 'fasfaf', 'D:\\PICTURES\\boy_pick_left_1.png', NULL),
(2, 2, 'Harvey ', 'D.', 'Clarito', '2025-09-04', 'Male', 'dasdsda@gmail.com', '09329239232', 'mama', 'papa', 'dadasda', 'dada', 'dad', 'dasda', 'D:\\PICTURES\\boy_pick_left_2.png', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `student_strand`
--

CREATE TABLE `student_strand` (
  `strand_id` int(11) NOT NULL,
  `student_id` int(10) NOT NULL,
  `grade_level` int(2) NOT NULL,
  `section_name` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `student_strand`
--

INSERT INTO `student_strand` (`strand_id`, `student_id`, `grade_level`, `section_name`) VALUES
(1, 1, 11, 'A'),
(5, 2, 11, 'A');

-- --------------------------------------------------------

--
-- Table structure for table `subject`
--

CREATE TABLE `subject` (
  `subject_id` int(11) NOT NULL,
  `subject_name` varchar(100) NOT NULL,
  `grade_level` int(2) NOT NULL,
  `strand_id` int(11) NOT NULL,
  `subject_order` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `subject`
--

INSERT INTO `subject` (`subject_id`, `subject_name`, `grade_level`, `strand_id`, `subject_order`) VALUES
(1, 'Oral Communication', 11, 1, 1),
(2, 'Komunikasyon at Pananaliksik', 11, 1, 2),
(3, 'General Mathematics', 11, 1, 3),
(4, 'Earth and Life Science', 11, 1, 4),
(5, 'Understanding Culture, Society and Politics', 11, 1, 5),
(6, 'Pre-Calculus', 11, 1, 6),
(7, 'Basic Calculus', 11, 1, 7),
(8, 'Chemistry 1', 11, 1, 8),
(9, 'Oral Communication', 11, 2, 1),
(10, 'Komunikasyon at Pananaliksik', 11, 2, 2),
(11, 'General Mathematics', 11, 2, 3),
(12, 'Statistics and Probability', 11, 2, 4),
(13, 'Understanding Culture, Society and Politics', 11, 2, 5),
(14, 'Business Math', 11, 2, 6),
(15, 'Fundamentals of Accountancy 1', 11, 2, 7),
(16, 'Organization and Management', 11, 2, 8),
(17, 'Oral Communication', 11, 3, 1),
(18, 'Komunikasyon at Pananaliksik', 11, 3, 2),
(19, 'General Mathematics', 11, 3, 3),
(20, 'Statistics and Probability', 11, 3, 4),
(21, 'Understanding Culture, Society and Politics', 11, 3, 5),
(22, 'Creative Writing', 11, 3, 6),
(23, 'Disciplines and Ideas in Social Sciences', 11, 3, 7),
(24, 'Introduction to Philosophy', 11, 3, 8),
(25, 'Oral Communication', 11, 4, 1),
(26, 'Komunikasyon at Pananaliksik', 11, 4, 2),
(27, 'General Mathematics', 11, 4, 3),
(28, 'Statistics and Probability', 11, 4, 4),
(29, 'Understanding Culture, Society and Politics', 11, 4, 5),
(30, 'Humanities 1', 11, 4, 6),
(31, 'Applied Economics', 11, 4, 7),
(32, 'Organization and Management', 11, 4, 8),
(33, 'Oral Communication', 11, 5, 1),
(34, 'Komunikasyon at Pananaliksik', 11, 5, 2),
(35, 'General Mathematics', 11, 5, 3),
(36, 'Statistics and Probability', 11, 5, 4),
(37, 'Understanding Culture, Society and Politics', 11, 5, 5),
(38, 'Computer Systems Servicing 1', 11, 5, 6),
(39, 'Computer Systems Servicing 2', 11, 5, 7),
(40, 'Computer Systems Servicing 3', 11, 5, 8),
(41, 'Oral Communication', 11, 6, 1),
(42, 'Komunikasyon at Pananaliksik', 11, 6, 2),
(43, 'General Mathematics', 11, 6, 3),
(44, 'Statistics and Probability', 11, 6, 4),
(45, 'Understanding Culture, Society and Politics', 11, 6, 5),
(46, 'Electrical Installation 1', 11, 6, 6),
(47, 'Electrical Installation 2', 11, 6, 7),
(48, 'Electrical Installation 3', 11, 6, 8),
(49, 'Oral Communication', 11, 7, 1),
(50, 'Komunikasyon at Pananaliksik', 11, 7, 2),
(51, 'General Mathematics', 11, 7, 3),
(52, 'Statistics and Probability', 11, 7, 4),
(53, 'Understanding Culture, Society and Politics', 11, 7, 5),
(54, 'Cookery 1', 11, 7, 6),
(55, 'Cookery 2', 11, 7, 7),
(56, 'Bread and Pastry Production', 11, 7, 8),
(57, 'Reading and Writing', 12, 1, 1),
(58, '21st Century Literature', 12, 1, 2),
(59, 'Contemporary Philippine Arts', 12, 1, 3),
(60, 'Media and Information Literacy', 12, 1, 4),
(61, 'Physical Science', 12, 1, 5),
(62, 'Physics 2', 12, 1, 6),
(63, 'Biology 1', 12, 1, 7),
(64, 'Chemistry 2', 12, 1, 8),
(65, 'Reading and Writing', 12, 2, 1),
(66, '21st Century Literature', 12, 2, 2),
(67, 'Contemporary Philippine Arts', 12, 2, 3),
(68, 'Media and Information Literacy', 12, 2, 4),
(69, 'Business Ethics', 12, 2, 5),
(70, 'Fundamentals of Accountancy 2', 12, 2, 6),
(71, 'Applied Economics', 12, 2, 7),
(72, 'Business Finance', 12, 2, 8),
(73, 'Reading and Writing', 12, 3, 1),
(74, '21st Century Literature', 12, 3, 2),
(75, 'Contemporary Philippine Arts', 12, 3, 3),
(76, 'Media and Information Literacy', 12, 3, 4),
(77, 'Creative Nonfiction', 12, 3, 5),
(78, 'Philippine Politics', 12, 3, 6),
(79, 'Trends and Networks', 12, 3, 7),
(80, 'Disciplines in Social Science', 12, 3, 8),
(81, 'Reading and Writing', 12, 4, 1),
(82, '21st Century Literature', 12, 4, 2),
(83, 'Contemporary Philippine Arts', 12, 4, 3),
(84, 'Media and Information Literacy', 12, 4, 4),
(85, 'Creative Writing', 12, 4, 5),
(86, 'Humanities 2', 12, 4, 6),
(87, 'Disaster Readiness', 12, 4, 7),
(88, 'Applied Economics', 12, 4, 8),
(89, 'Reading and Writing', 12, 5, 1),
(90, '21st Century Literature', 12, 5, 2),
(91, 'Contemporary Philippine Arts', 12, 5, 3),
(92, 'Media and Information Literacy', 12, 5, 4),
(93, 'Computer Systems Servicing 4', 12, 5, 5),
(94, 'CSS Project', 12, 5, 6),
(95, 'Practical Research 1', 12, 5, 7),
(96, 'Empowerment Technologies', 12, 5, 8),
(97, 'Reading and Writing', 12, 6, 1),
(98, '21st Century Literature', 12, 6, 2),
(99, 'Contemporary Philippine Arts', 12, 6, 3),
(100, 'Media and Information Literacy', 12, 6, 4),
(101, 'Electrical Installation 4', 12, 6, 5),
(102, 'EIM Project', 12, 6, 6),
(103, 'Practical Research 1', 12, 6, 7),
(104, 'Empowerment Technologies', 12, 6, 8),
(105, 'Reading and Writing', 12, 7, 1),
(106, '21st Century Literature', 12, 7, 2),
(107, 'Contemporary Philippine Arts', 12, 7, 3),
(108, 'Media and Information Literacy', 12, 7, 4),
(109, 'Cookery 3', 12, 7, 5),
(110, 'Food and Beverage Services', 12, 7, 6),
(111, 'Practical Research 1', 12, 7, 7),
(112, 'Empowerment Technologies', 12, 7, 8);

-- --------------------------------------------------------

--
-- Table structure for table `teacher`
--

CREATE TABLE `teacher` (
  `teacher_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `middle_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `gender` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `address1` varchar(100) NOT NULL,
  `address2` varchar(100) NOT NULL,
  `specialization` varchar(50) NOT NULL,
  `hire_date` date NOT NULL,
  `image_path` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `type`
--

CREATE TABLE `type` (
  `type_id` int(11) NOT NULL,
  `type_name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `type`
--

INSERT INTO `type` (`type_id`, `type_name`) VALUES
(1, 'Admin'),
(2, 'Student'),
(3, 'Teacher');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `type_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `password`, `type_id`) VALUES
(1, 'hdfhhdfhh', 'hdfhhdfhh20250904', 2),
(2, 'clarito', 'clarito20250904', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`admin_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `grade`
--
ALTER TABLE `grade`
  ADD PRIMARY KEY (`grade_id`),
  ADD KEY `student_id` (`student_id`),
  ADD KEY `subject_id` (`subject_id`);

--
-- Indexes for table `strands`
--
ALTER TABLE `strands`
  ADD PRIMARY KEY (`strand_id`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`student_id`),
  ADD UNIQUE KEY `LRN` (`LRN`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `student_strand`
--
ALTER TABLE `student_strand`
  ADD PRIMARY KEY (`strand_id`,`student_id`),
  ADD KEY `student_id` (`student_id`);

--
-- Indexes for table `subject`
--
ALTER TABLE `subject`
  ADD PRIMARY KEY (`subject_id`),
  ADD KEY `strand_id` (`strand_id`);

--
-- Indexes for table `teacher`
--
ALTER TABLE `teacher`
  ADD PRIMARY KEY (`teacher_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `type`
--
ALTER TABLE `type`
  ADD PRIMARY KEY (`type_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `type_id` (`type_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `admin_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `grade`
--
ALTER TABLE `grade`
  MODIFY `grade_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=217;

--
-- AUTO_INCREMENT for table `strands`
--
ALTER TABLE `strands`
  MODIFY `strand_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `student`
--
ALTER TABLE `student`
  MODIFY `student_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `subject`
--
ALTER TABLE `subject`
  MODIFY `subject_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=120;

--
-- AUTO_INCREMENT for table `teacher`
--
ALTER TABLE `teacher`
  MODIFY `teacher_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `type`
--
ALTER TABLE `type`
  MODIFY `type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `grade`
--
ALTER TABLE `grade`
  ADD CONSTRAINT `grade_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
  ADD CONSTRAINT `grade_ibfk_2` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`subject_id`);

--
-- Constraints for table `student`
--
ALTER TABLE `student`
  ADD CONSTRAINT `student_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `student_strand`
--
ALTER TABLE `student_strand`
  ADD CONSTRAINT `student_strand_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
  ADD CONSTRAINT `student_strand_ibfk_2` FOREIGN KEY (`strand_id`) REFERENCES `strands` (`strand_id`);

--
-- Constraints for table `subject`
--
ALTER TABLE `subject`
  ADD CONSTRAINT `subject_ibfk_1` FOREIGN KEY (`strand_id`) REFERENCES `strands` (`strand_id`);

--
-- Constraints for table `teacher`
--
ALTER TABLE `teacher`
  ADD CONSTRAINT `teacher_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `type` (`type_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
