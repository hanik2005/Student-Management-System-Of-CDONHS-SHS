-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 25, 2025 at 11:48 AM
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
-- Table structure for table `grade`
--

CREATE TABLE `grade` (
  `Id` int(10) NOT NULL,
  `student_id` int(10) NOT NULL,
  `grade_level` int(2) NOT NULL,
  `strand_name` varchar(30) NOT NULL,
  `section_name` varchar(5) NOT NULL,
  `subject_1` varchar(30) NOT NULL,
  `sub_grade1` double(3,2) NOT NULL,
  `subject_2` varchar(30) NOT NULL,
  `sub_grade2` double(3,2) NOT NULL,
  `subject_3` varchar(30) NOT NULL,
  `sub_grade3` double(3,2) NOT NULL,
  `subject_4` varchar(30) NOT NULL,
  `sub_grade4` double(3,2) NOT NULL,
  `subject_5` varchar(30) NOT NULL,
  `sub_grade5` double(3,2) NOT NULL,
  `subject_6` varchar(30) NOT NULL,
  `sub_grade6` double(3,2) NOT NULL,
  `subject_7` varchar(30) NOT NULL,
  `sub_grade7` double(3,2) NOT NULL,
  `subject_8` varchar(30) NOT NULL,
  `sub_grade8` double(3,2) NOT NULL,
  `quarter` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
(9, 'Maria Cristina ', '2009-08-14', 'Male', 'dhsahdsadhjadk@gmail.com', '099724827348', 'dhajdhsahdsajdhasjdha', 'hdajdhadhajdha', 'dasdsafdfadafgdgda', 'djhagdadgagdashdgahdgajd', 'CLARITO, NICK CHARLES D. – 2003–Present.pdf', 'Scholar_csr.pdf', 'D:\\PICTURES\\pic_3.jpg');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `grade`
--
ALTER TABLE `grade`
  ADD PRIMARY KEY (`Id`);

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
