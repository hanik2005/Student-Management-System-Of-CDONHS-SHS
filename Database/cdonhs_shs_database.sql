-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 06, 2025 at 05:20 AM
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
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `admin_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) NOT NULL,
  `gender` varchar(20) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `role` enum('Super Admin','Registrar','Guidance Admin','Finance Admin') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `general_average`
--

CREATE TABLE `general_average` (
  `student_id` int(11) NOT NULL,
  `final_average` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `general_average`
--

INSERT INTO `general_average` (`student_id`, `final_average`) VALUES
(1, 88.97),
(2, NULL);

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
-- Table structure for table `strand`
--

CREATE TABLE `strand` (
  `strand_id` int(11) NOT NULL,
  `student_id` int(10) NOT NULL,
  `grade_level` int(2) NOT NULL,
  `strand` varchar(30) NOT NULL,
  `section_name` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `strand`
--

INSERT INTO `strand` (`strand_id`, `student_id`, `grade_level`, `strand`, `section_name`) VALUES
(1, 1, 11, 'STEM', 'A'),
(2, 2, 11, 'TVL-ICT', 'A');

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
  `image_path` varchar(100) NOT NULL,
  `LRN` char(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`student_id`, `first_name`, `middle_name`, `last_name`, `date_of_birth`, `gender`, `email`, `phone_number`, `mother_name`, `father_name`, `address1`, `address2`, `birth_certificate`, `form_137`, `image_path`, `LRN`) VALUES
(1, 'Nick', 'hfhfdhd', 'hdfhhdfhh', '2025-09-04', 'Male', 'asdaf@gmail.ocm', '09976866868', 'fasfaf', 'fafasfa', 'fasfaf', 'fafafa', 'fafsaf', 'fasfaf', 'D:\\PICTURES\\boy_pick_left_1.png', NULL),
(2, 'Harvey ', 'D.', 'Clarito', '2025-09-04', 'Male', 'dasdsda@gmail.com', '09329239232', 'mama', 'papa', 'dadasda', 'dada', 'dad', 'dasda', 'D:\\PICTURES\\boy_pick_left_2.png', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `subject`
--

CREATE TABLE `subject` (
  `subject_id` int(11) NOT NULL,
  `subject_name` varchar(100) NOT NULL,
  `grade_level` int(2) NOT NULL,
  `strand_name` varchar(50) NOT NULL,
  `subject_order` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `subject`
--

INSERT INTO `subject` (`subject_id`, `subject_name`, `grade_level`, `strand_name`, `subject_order`) VALUES
(1, 'Oral Communication', 11, 'STEM', 1),
(2, 'Komunikasyon at Pananaliksik', 11, 'STEM', 2),
(3, 'General Mathematics', 11, 'STEM', 3),
(4, 'Earth and Life Science', 11, 'STEM', 4),
(5, 'Understanding Culture, Society and Politics', 11, 'STEM', 5),
(6, 'Pre-Calculus', 11, 'STEM', 6),
(7, 'Basic Calculus', 11, 'STEM', 7),
(8, 'Chemistry 1', 11, 'STEM', 8),
(9, 'Oral Communication', 11, 'ABM', 1),
(10, 'Komunikasyon at Pananaliksik', 11, 'ABM', 2),
(11, 'General Mathematics', 11, 'ABM', 3),
(12, 'Statistics and Probability', 11, 'ABM', 4),
(13, 'Understanding Culture, Society and Politics', 11, 'ABM', 5),
(14, 'Business Math', 11, 'ABM', 6),
(15, 'Fundamentals of Accountancy 1', 11, 'ABM', 7),
(16, 'Organization and Management', 11, 'ABM', 8),
(17, 'Oral Communication', 11, 'HUMSS', 1),
(18, 'Komunikasyon at Pananaliksik', 11, 'HUMSS', 2),
(19, 'General Mathematics', 11, 'HUMSS', 3),
(20, 'Statistics and Probability', 11, 'HUMSS', 4),
(21, 'Understanding Culture, Society and Politics', 11, 'HUMSS', 5),
(22, 'Creative Writing', 11, 'HUMSS', 6),
(23, 'Disciplines and Ideas in Social Sciences', 11, 'HUMSS', 7),
(24, 'Introduction to Philosophy', 11, 'HUMSS', 8),
(25, 'Oral Communication', 11, 'GAS', 1),
(26, 'Komunikasyon at Pananaliksik', 11, 'GAS', 2),
(27, 'General Mathematics', 11, 'GAS', 3),
(28, 'Statistics and Probability', 11, 'GAS', 4),
(29, 'Understanding Culture, Society and Politics', 11, 'GAS', 5),
(30, 'Humanities 1', 11, 'GAS', 6),
(31, 'Applied Economics', 11, 'GAS', 7),
(32, 'Organization and Management', 11, 'GAS', 8),
(33, 'Oral Communication', 11, 'TVL-ICT', 1),
(34, 'Komunikasyon at Pananaliksik', 11, 'TVL-ICT', 2),
(35, 'General Mathematics', 11, 'TVL-ICT', 3),
(36, 'Statistics and Probability', 11, 'TVL-ICT', 4),
(37, 'Understanding Culture, Society and Politics', 11, 'TVL-ICT', 5),
(38, 'Computer Systems Servicing 1', 11, 'TVL-ICT', 6),
(39, 'Computer Systems Servicing 2', 11, 'TVL-ICT', 7),
(40, 'Computer Systems Servicing 3', 11, 'TVL-ICT', 8),
(41, 'Oral Communication', 11, 'TVL-EIM', 1),
(42, 'Komunikasyon at Pananaliksik', 11, 'TVL-EIM', 2),
(43, 'General Mathematics', 11, 'TVL-EIM', 3),
(44, 'Statistics and Probability', 11, 'TVL-EIM', 4),
(45, 'Understanding Culture, Society and Politics', 11, 'TVL-EIM', 5),
(46, 'Electrical Installation 1', 11, 'TVL-EIM', 6),
(47, 'Electrical Installation 2', 11, 'TVL-EIM', 7),
(48, 'Electrical Installation 3', 11, 'TVL-EIM', 8),
(49, 'Oral Communication', 11, 'TVL-HE', 1),
(50, 'Komunikasyon at Pananaliksik', 11, 'TVL-HE', 2),
(51, 'General Mathematics', 11, 'TVL-HE', 3),
(52, 'Statistics and Probability', 11, 'TVL-HE', 4),
(53, 'Understanding Culture, Society and Politics', 11, 'TVL-HE', 5),
(54, 'Cookery 1', 11, 'TVL-HE', 6),
(55, 'Cookery 2', 11, 'TVL-HE', 7),
(56, 'Bread and Pastry Production', 11, 'TVL-HE', 8),
(57, 'Reading and Writing', 12, 'STEM', 1),
(58, '21st Century Literature', 12, 'STEM', 2),
(59, 'Contemporary Philippine Arts', 12, 'STEM', 3),
(60, 'Media and Information Literacy', 12, 'STEM', 4),
(61, 'Physical Science', 12, 'STEM', 5),
(62, 'Physics 2', 12, 'STEM', 6),
(63, 'Biology 1', 12, 'STEM', 7),
(64, 'Chemistry 2', 12, 'STEM', 8),
(65, 'Reading and Writing', 12, 'ABM', 1),
(66, '21st Century Literature', 12, 'ABM', 2),
(67, 'Contemporary Philippine Arts', 12, 'ABM', 3),
(68, 'Media and Information Literacy', 12, 'ABM', 4),
(69, 'Business Ethics', 12, 'ABM', 5),
(70, 'Fundamentals of Accountancy 2', 12, 'ABM', 6),
(71, 'Applied Economics', 12, 'ABM', 7),
(72, 'Business Finance', 12, 'ABM', 8),
(73, 'Reading and Writing', 12, 'HUMSS', 1),
(74, '21st Century Literature', 12, 'HUMSS', 2),
(75, 'Contemporary Philippine Arts', 12, 'HUMSS', 3),
(76, 'Media and Information Literacy', 12, 'HUMSS', 4),
(77, 'Creative Nonfiction', 12, 'HUMSS', 5),
(78, 'Philippine Politics', 12, 'HUMSS', 6),
(79, 'Trends and Networks', 12, 'HUMSS', 7),
(80, 'Disciplines in Social Science', 12, 'HUMSS', 8),
(81, 'Reading and Writing', 12, 'GAS', 1),
(82, '21st Century Literature', 12, 'GAS', 2),
(83, 'Contemporary Philippine Arts', 12, 'GAS', 3),
(84, 'Media and Information Literacy', 12, 'GAS', 4),
(85, 'Creative Writing', 12, 'GAS', 5),
(86, 'Humanities 2', 12, 'GAS', 6),
(87, 'Disaster Readiness', 12, 'GAS', 7),
(88, 'Applied Economics', 12, 'GAS', 8),
(89, 'Reading and Writing', 12, 'TVL-ICT', 1),
(90, '21st Century Literature', 12, 'TVL-ICT', 2),
(91, 'Contemporary Philippine Arts', 12, 'TVL-ICT', 3),
(92, 'Media and Information Literacy', 12, 'TVL-ICT', 4),
(93, 'Computer Systems Servicing 4', 12, 'TVL-ICT', 5),
(94, 'CSS Project', 12, 'TVL-ICT', 6),
(95, 'Practical Research 1', 12, 'TVL-ICT', 7),
(96, 'Empowerment Technologies', 12, 'TVL-ICT', 8),
(97, 'Reading and Writing', 12, 'TVL-EIM', 1),
(98, '21st Century Literature', 12, 'TVL-EIM', 2),
(99, 'Contemporary Philippine Arts', 12, 'TVL-EIM', 3),
(100, 'Media and Information Literacy', 12, 'TVL-EIM', 4),
(101, 'Electrical Installation 4', 12, 'TVL-EIM', 5),
(102, 'EIM Project', 12, 'TVL-EIM', 6),
(103, 'Practical Research 1', 12, 'TVL-EIM', 7),
(104, 'Empowerment Technologies', 12, 'TVL-EIM', 8),
(105, 'Reading and Writing', 12, 'TVL-HE', 1),
(106, '21st Century Literature', 12, 'TVL-HE', 2),
(107, 'Contemporary Philippine Arts', 12, 'TVL-HE', 3),
(108, 'Media and Information Literacy', 12, 'TVL-HE', 4),
(109, 'Cookery 3', 12, 'TVL-HE', 5),
(110, 'Food and Beverage Services', 12, 'TVL-HE', 6),
(111, 'Practical Research 1', 12, 'TVL-HE', 7),
(112, 'Empowerment Technologies', 12, 'TVL-HE', 8);

-- --------------------------------------------------------

--
-- Table structure for table `teacher`
--

CREATE TABLE `teacher` (
  `teacher_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) NOT NULL,
  `gender` varchar(20) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `specialization` varchar(50) NOT NULL,
  `hire_date` date NOT NULL,
  `image_path` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
(1, 1, 'hdfhhdfhh20250904', 'Student'),
(2, 2, 'clarito20250904', 'Student');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`admin_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `general_average`
--
ALTER TABLE `general_average`
  ADD PRIMARY KEY (`student_id`);

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
  ADD PRIMARY KEY (`student_id`),
  ADD UNIQUE KEY `LRN` (`LRN`);

--
-- Indexes for table `subject`
--
ALTER TABLE `subject`
  ADD PRIMARY KEY (`subject_id`);

--
-- Indexes for table `teacher`
--
ALTER TABLE `teacher`
  ADD PRIMARY KEY (`teacher_id`),
  ADD UNIQUE KEY `email` (`email`);

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
