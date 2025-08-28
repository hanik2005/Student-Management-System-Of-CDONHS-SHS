-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 28, 2025 at 06:23 PM
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
-- Table structure for table `final_grade`
--

CREATE TABLE `final_grade` (
  `id` int(10) NOT NULL,
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
-- Dumping data for table `final_grade`
--

INSERT INTO `final_grade` (`id`, `student_id`, `grade_level`, `strand_name`, `section_name`, `quarter_1_average`, `quarter_2_average`, `quarter_3_average`, `quarter_4_average`, `final_average`) VALUES
(1, 1, 11, 'STEM', 'A', 89.50, 86.88, 90.75, 93.50, 22.00),
(2, 2, 11, 'TVL-HE', 'A', 23.13, 15.38, 0.00, 0.00, 23.13),
(3, 3, 12, 'TVL-EIM', 'A', 77.38, 19.25, 0.00, 0.00, 77.38),
(4, 4, 11, 'GAS', 'A', 11.50, 90.00, 28.88, 0.00, 7.22);

-- --------------------------------------------------------

--
-- Table structure for table `grade`
--

CREATE TABLE `grade` (
  `id` int(10) NOT NULL,
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
  `Average` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `grade`
--

INSERT INTO `grade` (`id`, `student_id`, `grade_level`, `strand_name`, `section_name`, `subject_1`, `sub_grade1`, `subject_2`, `sub_grade2`, `subject_3`, `sub_grade3`, `subject_4`, `sub_grade4`, `subject_5`, `sub_grade5`, `subject_6`, `sub_grade6`, `subject_7`, `sub_grade7`, `subject_8`, `sub_grade8`, `quarter`, `Average`) VALUES
(1, 1, 11, 'STEM', 'A', 'Oral Communication', 89.00, 'Komunikasyon at Pananaliksik', 87.00, 'General Mathematics', 90.00, 'Earth and Life Science', 69.00, 'Understanding Culture, Society and Politics', 89.00, 'Pre-Calculus', 98.00, 'Basic Calculus', 95.00, 'Chemistry 1', 99.00, 1, 89.50),
(2, 2, 11, 'TVL-HE', 'A', 'Oral Communication', 93.00, 'Komunikasyon at Pananaliksik', 92.00, 'General Mathematics', 0.00, 'Statistics and Probability', 0.00, 'Understanding Culture, Society and Politics', 0.00, 'Cookery 1', 0.00, 'Cookery 2', 0.00, 'Bread and Pastry Production', 0.00, 1, 23.13),
(3, 2, 11, 'TVL-HE', 'A', 'Oral Communication', 32.00, 'Komunikasyon at Pananaliksik', 91.00, 'General Mathematics', 0.00, 'Statistics and Probability', 0.00, 'Understanding Culture, Society and Politics', 0.00, 'Cookery 1', 0.00, 'Cookery 2', 0.00, 'Bread and Pastry Production', 0.00, 2, 15.38),
(4, 1, 11, 'STEM', 'A', 'Oral Communication', 88.00, 'Komunikasyon at Pananaliksik', 88.00, 'General Mathematics', 88.00, 'Earth and Life Science', 88.00, 'Understanding Culture, Society and Politics', 88.00, 'Pre-Calculus', 77.00, 'Basic Calculus', 90.00, 'Chemistry 1', 88.00, 2, 86.88),
(5, 1, 11, 'STEM', 'A', 'Oral Communication', 99.00, 'Komunikasyon at Pananaliksik', 99.00, 'General Mathematics', 88.00, 'Earth and Life Science', 88.00, 'Understanding Culture, Society and Politics', 88.00, 'Pre-Calculus', 88.00, 'Basic Calculus', 88.00, 'Chemistry 1', 88.00, 3, 90.75),
(6, 1, 11, 'STEM', 'A', 'Oral Communication', 99.00, 'Komunikasyon at Pananaliksik', 88.00, 'General Mathematics', 88.00, 'Earth and Life Science', 88.00, 'Understanding Culture, Society and Politics', 88.00, 'Pre-Calculus', 99.00, 'Basic Calculus', 99.00, 'Chemistry 1', 99.00, 4, 93.50),
(7, 3, 12, 'TVL-EIM', 'A', 'Reading and Writing', 33.00, '21st Century Literature', 77.00, 'Contemporary Philippine Arts', 78.00, 'Media and Information Literacy', 76.00, 'Electrical Installation 4', 89.00, 'EIM Project', 89.00, 'Practical Research 1', 88.00, 'Empowerment Technologies', 89.00, 1, 77.38),
(8, 3, 12, 'TVL-EIM', 'A', 'Reading and Writing', 77.00, '21st Century Literature', 77.00, 'Contemporary Philippine Arts', 0.00, 'Media and Information Literacy', 0.00, 'Electrical Installation 4', 0.00, 'EIM Project', 0.00, 'Practical Research 1', 0.00, 'Empowerment Technologies', 0.00, 2, 19.25),
(9, 4, 11, 'GAS', 'A', 'Oral Communication', 92.00, 'Komunikasyon at Pananaliksik', 0.00, 'General Mathematics', 0.00, 'Statistics and Probability', 0.00, 'Understanding Culture, Society and Politics', 0.00, 'Humanities 1', 0.00, 'Applied Economics', 0.00, 'Organization and Management', 0.00, 1, 11.50),
(10, 4, 11, 'GAS', 'A', 'Oral Communication', 93.00, 'Komunikasyon at Pananaliksik', 89.00, 'General Mathematics', 87.00, 'Statistics and Probability', 66.00, 'Understanding Culture, Society and Politics', 88.00, 'Humanities 1', 99.00, 'Applied Economics', 99.00, 'Organization and Management', 99.00, 2, 90.00),
(11, 4, 11, 'GAS', 'A', 'Oral Communication', 77.00, 'Komunikasyon at Pananaliksik', 77.00, 'General Mathematics', 77.00, 'Statistics and Probability', 0.00, 'Understanding Culture, Society and Politics', 0.00, 'Humanities 1', 0.00, 'Applied Economics', 0.00, 'Organization and Management', 0.00, 3, 28.88);

-- --------------------------------------------------------

--
-- Table structure for table `strand`
--

CREATE TABLE `strand` (
  `id` int(10) NOT NULL,
  `student_id` int(10) NOT NULL,
  `grade_level` int(2) NOT NULL,
  `strand` varchar(30) NOT NULL,
  `section` varchar(30) NOT NULL,
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

INSERT INTO `strand` (`id`, `student_id`, `grade_level`, `strand`, `section`, `subject_1`, `subject_2`, `subject_3`, `subject_4`, `subject_5`, `subject_6`, `subject_7`, `subject_8`) VALUES
(1, 1, 11, 'STEM', 'A', 'Oral Communication', 'Komunikasyon at Pananaliksik', 'General Mathematics', 'Earth and Life Science', 'Understanding Culture, Society and Politics', 'Pre-Calculus', 'Basic Calculus', 'Chemistry 1'),
(2, 2, 11, 'TVL-HE', 'A', 'Oral Communication', 'Komunikasyon at Pananaliksik', 'General Mathematics', 'Statistics and Probability', 'Understanding Culture, Society and Politics', 'Cookery 1', 'Cookery 2', 'Bread and Pastry Production'),
(3, 3, 12, 'TVL-EIM', 'A', 'Reading and Writing', '21st Century Literature', 'Contemporary Philippine Arts', 'Media and Information Literacy', 'Electrical Installation 4', 'EIM Project', 'Practical Research 1', 'Empowerment Technologies'),
(4, 4, 11, 'GAS', 'A', 'Oral Communication', 'Komunikasyon at Pananaliksik', 'General Mathematics', 'Statistics and Probability', 'Understanding Culture, Society and Politics', 'Humanities 1', 'Applied Economics', 'Organization and Management');

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `Id` int(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `date_of_birth` date NOT NULL,
  `gender` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phone_number` varchar(50) NOT NULL,
  `mother_name` varchar(50) NOT NULL,
  `father_name` varchar(50) NOT NULL,
  `address1` varchar(50) NOT NULL,
  `address2` varchar(50) NOT NULL,
  `birth_certificate` varchar(100) NOT NULL,
  `form_137` varchar(100) NOT NULL,
  `image_path` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`Id`, `name`, `date_of_birth`, `gender`, `email`, `phone_number`, `mother_name`, `father_name`, `address1`, `address2`, `birth_certificate`, `form_137`, `image_path`) VALUES
(1, 'Nick Charles D. Clarito', '2005-08-20', 'Male', 'nidu.clarito@gmail.com', '09944719534', 'dasdadlkaldkald', 'ndassjhdahd', 'dasndajdajd', 'sajdsahdakhdak', 'dadadada', 'dadadsdad', 'D:\\PICTURES\\anime-moon-landscape.jpg'),
(2, 'Andry D. Clarito', '2004-08-13', 'Male', 'andryclarito@gmail.com', '099944719534', 'Maria Cristina D. Clarito', 'Randy A. Clarito', 'Blk 4 Buena Macasandig Cagayan De Oro City', 'Magsaysay', 'D:\\DOCUMENTATION\\FELISILDA_&_CLARITO.pdf', 'D:\\DOCUMENTATION\\Group 1 - Client Interview Submission (1).pdf', 'D:\\PICTURES\\anime-moon-landscape.jpg'),
(3, 'Harvey D. Clarito', '2025-08-12', 'Male', 'dadads@gmail.com', '083829389', 'Papap', 'mama', 'djadjaldajdkasdjkad', 'Father', 'CLARITO, NICK CHARLES D..pdf', 'Scholar_csr_FELISILDA.pdf', 'D:\\PICTURES\\boy_pick_down_1.png'),
(4, 'Miss Cristina ', '2025-08-03', 'Male', 'dahdgsadsdhsshkk@gmail.com', '09943746376434', 'jfjahfshfjhfahfkajfh', 'jhfasjfjfhafhajf', 'hfajffkfbabfaffafaf', 'hfsafhafgahgfhsgfkafk', 'Scholar_csr_FELISILDA.pdf', 'SAINT VINCENT DE PAUL CHAPEL.pdf', 'D:\\PICTURES\\pic.jpg'),
(5, 'Randy Clarito', '2025-08-07', 'Male', 'joker@gmail.com', '09944719534', 'mama randy', 'Papa randy', 'dasssdaddad', 'dadadadasadad', 'FELISILDA_&_CLARITO.pdf', 'Scholar_csr.pdf', 'D:\\PICTURES\\Narita_Taishin.png'),
(7, 'Student', '2025-08-01', 'Male', 'jdhsadhajdh@gmail.com', '09944719534', 'maama', 'paapa', 'hasgdhagdhagda', 'dhagdasgdsagsajhd', 'CLARITO, NICK CHARLES D..pdf', 'Scholar_csr_FELISILDA.pdf', 'D:\\PICTURES\\pic_3.jpg'),
(8, 'Andrea Bagtong', '2014-05-24', 'Male', 'Andrea@gmail.com', '09944719529313', 'gadfgadfagdgahd', 'dashdjdhakjd', 'dadasdadad', 'dadasda', 'Scholar_csr_FELISILDA.pdf', 'Scholar_csr.pdf', 'D:\\PICTURES\\GRADES_SECOND_SEMESTER.jpg'),
(9, 'Maria Cristina ', '2009-08-14', 'Male', 'dhsahdsadhjadk@gmail.com', '099724827348', 'dhajdhsahdsajdhasjdha', 'hdajdhadhajdha', 'dasdsafdfadafgdgda', 'djhagdadgagdashdgahdgajd', 'CLARITO, NICK CHARLES D. – 2003–Present.pdf', 'Scholar_csr.pdf', 'D:\\PICTURES\\pic_3.jpg'),
(10, 'Harbbey', '2017-08-12', 'Male', 'harbbey@gmail.com', '0994284724', 'papar', 'mama', 'lot100', 'lot500', 'dsadd', 'dasdda', 'D:\\PICTURES\\boy_pick_down_2.png');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `final_grade`
--
ALTER TABLE `final_grade`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `grade`
--
ALTER TABLE `grade`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `strand`
--
ALTER TABLE `strand`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`Id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
