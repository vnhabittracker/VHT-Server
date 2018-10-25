-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 23, 2018 at 04:10 PM
-- Server version: 10.1.35-MariaDB
-- PHP Version: 7.2.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `vnhabit`
--
CREATE DATABASE IF NOT EXISTS `vnhabit` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `vnhabit`;

-- --------------------------------------------------------

--
-- Table structure for table `about`
--

CREATE TABLE `about` (
  `app_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_fqa` text COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `achievement`
--

CREATE TABLE `achievement` (
  `achievement_id` int(11) NOT NULL,
  `achievement_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `achievement_description` text COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `achievement_details`
--

CREATE TABLE `achievement_details` (
  `user_id` int(11) NOT NULL,
  `achievement_id` int(11) NOT NULL,
  `count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE `feedback` (
  `feedback_id` int(11) NOT NULL,
  `star_num` int(11) NOT NULL,
  `feedback_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `group`
--

CREATE TABLE `group` (
  `group_id` int(11) NOT NULL,
  `group_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `group_icon` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_description` text COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `group`
--

INSERT INTO `group` (`group_id`, `group_name`, `parent_id`, `group_icon`, `group_description`) VALUES
(1, 'Sức khỏe', NULL, '', ''),
(2, 'Tài chính', NULL, '', ''),
(3, 'Gia đình', NULL, '', ''),
(4, 'Học', NULL, '', ''),
(5, 'Mua sắm', NULL, '', '');

-- --------------------------------------------------------

--
-- Table structure for table `habit`
--

CREATE TABLE `habit` (
  `habit_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  `monitor_id` int(11) DEFAULT NULL,
  `habit_name` text COLLATE utf8mb4_unicode_ci,
  `habit_target` tinyint(1) DEFAULT '0',
  `habit_type` tinyint(1) DEFAULT '0',
  `monitor_type` tinyint(1) DEFAULT '0',
  `monitor_unit` text COLLATE utf8mb4_unicode_ci,
  `monitor_number` int(11) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `habit_color` text COLLATE utf8mb4_unicode_ci,
  `habit_description` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `habit`
--

INSERT INTO `habit` (`habit_id`, `user_id`, `group_id`, `monitor_id`, `habit_name`, `habit_target`, `habit_type`, `monitor_type`, `monitor_unit`, `monitor_number`, `start_date`, `end_date`, `created_date`, `habit_color`, `habit_description`) VALUES
(2, 1, 1, 2, 'chạy bộ', 1, 1, 1, 'km', 10, '2018-10-17', '2018-10-26', '2018-10-14', '#78445b77', 'chạy bộ 10km'),
(3, 2, 1, 4, 'hít đất', 1, 2, 1, 'cái', 100, '2018-10-15', '2018-10-31', '2018-10-14', '#787f8737', 'hít đất 100 cái'),
(4, 2, 3, 5, 'đưa gia đình đi du lịch', 1, 2, 0, NULL, NULL, '2018-10-14', '2018-10-31', '2018-10-14', '#78a5662e', 'đưa gia đình đi du lịch'),
(8, 3, 2, 7, 'ghi chép chi tiêu', 1, 3, 0, NULL, NULL, '2018-10-14', '2018-10-31', '2018-10-14', '#78d15e6c', 'hãy ghi chép chi tiêu cá nhân'),
(9, 1, 1, 11, 'haha', 1, 0, 1, 'cuốn', 2, '2018-10-22', '2018-10-31', '2018-10-23', '#78de0639', 'gggg'),
(10, 1, 1, 12, 'di sieu thi', 0, 0, 0, NULL, 1, '2018-10-22', '2018-10-30', '2018-10-22', '#64535bfe', 'di sieu thi');

-- --------------------------------------------------------

--
-- Table structure for table `monitor_date`
--

CREATE TABLE `monitor_date` (
  `monitor_id` int(11) NOT NULL,
  `habit_id` int(11) DEFAULT NULL,
  `mon` tinyint(1) DEFAULT '1',
  `tue` tinyint(1) DEFAULT '1',
  `wed` tinyint(1) DEFAULT '1',
  `thu` tinyint(1) DEFAULT '1',
  `fri` tinyint(1) DEFAULT '1',
  `sat` tinyint(1) DEFAULT '1',
  `sun` tinyint(1) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `monitor_date`
--

INSERT INTO `monitor_date` (`monitor_id`, `habit_id`, `mon`, `tue`, `wed`, `thu`, `fri`, `sat`, `sun`) VALUES
(2, 2, 1, 1, 1, 1, 1, 1, 1),
(4, 3, 1, 1, 1, 1, 1, 1, 1),
(5, 4, 1, 1, 1, 1, 1, 1, 1),
(7, 8, 1, 1, 1, 1, 1, 1, 1),
(11, 9, 1, 1, 1, 1, 1, 1, 1),
(12, 10, 1, 0, 1, 0, 1, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `reminder`
--

CREATE TABLE `reminder` (
  `reminder_id` int(11) NOT NULL,
  `habit_id` int(11) DEFAULT NULL,
  `reminder_time` int(11) DEFAULT NULL,
  `repeat_time` int(11) DEFAULT NULL,
  `reminder_description` text COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tracking`
--

CREATE TABLE `tracking` (
  `tracking_id` int(11) NOT NULL,
  `habit_id` int(11) NOT NULL,
  `current_date` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `count` int(11) NOT NULL DEFAULT '0',
  `tracking_description` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tracking`
--

INSERT INTO `tracking` (`tracking_id`, `habit_id`, `current_date`, `count`, `tracking_description`) VALUES
(4, 2, '2018-10-22', 5, 'update');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `username` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_of_birth` date NOT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT '-1',
  `user_icon` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `avatar` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_description` text COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `password`, `phone`, `email`, `date_of_birth`, `gender`, `user_icon`, `avatar`, `user_description`) VALUES
(1, 'user01', '12345678', '', 'user01@mail.com', '1998-04-25', 0, '', '', 'The first user'),
(2, 'user02', '87654321', '', 'user02@mail.com', '1985-08-08', 0, '', '', 'The second user'),
(3, 'user03', 'abc12345', '', 'user03@mail.com', '1995-10-20', 0, '', '', 'a new user'),
(4, 'user04', '123asd', '0967883956', 'user04@mail.com', '0000-00-00', 0, '', '', ''),
(5, 'user05', '12345678', '0976588632', 'user04@mail.com', '0000-00-00', 0, '', '', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `achievement`
--
ALTER TABLE `achievement`
  ADD PRIMARY KEY (`achievement_id`);

--
-- Indexes for table `achievement_details`
--
ALTER TABLE `achievement_details`
  ADD PRIMARY KEY (`user_id`,`achievement_id`),
  ADD KEY `achievement_id` (`achievement_id`);

--
-- Indexes for table `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`feedback_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `group`
--
ALTER TABLE `group`
  ADD PRIMARY KEY (`group_id`);

--
-- Indexes for table `habit`
--
ALTER TABLE `habit`
  ADD PRIMARY KEY (`habit_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `category_id` (`group_id`),
  ADD KEY `monitor_id` (`monitor_id`);

--
-- Indexes for table `monitor_date`
--
ALTER TABLE `monitor_date`
  ADD PRIMARY KEY (`monitor_id`),
  ADD KEY `habit_id` (`habit_id`);

--
-- Indexes for table `reminder`
--
ALTER TABLE `reminder`
  ADD PRIMARY KEY (`reminder_id`),
  ADD KEY `reminder_ibfk_1` (`habit_id`);

--
-- Indexes for table `tracking`
--
ALTER TABLE `tracking`
  ADD PRIMARY KEY (`tracking_id`),
  ADD KEY `habit_id` (`habit_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `achievement`
--
ALTER TABLE `achievement`
  MODIFY `achievement_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `feedback`
--
ALTER TABLE `feedback`
  MODIFY `feedback_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `group`
--
ALTER TABLE `group`
  MODIFY `group_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `habit`
--
ALTER TABLE `habit`
  MODIFY `habit_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `monitor_date`
--
ALTER TABLE `monitor_date`
  MODIFY `monitor_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `reminder`
--
ALTER TABLE `reminder`
  MODIFY `reminder_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tracking`
--
ALTER TABLE `tracking`
  MODIFY `tracking_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `achievement_details`
--
ALTER TABLE `achievement_details`
  ADD CONSTRAINT `achievement_details_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `achievement_details_ibfk_2` FOREIGN KEY (`achievement_id`) REFERENCES `achievement` (`achievement_id`),
  ADD CONSTRAINT `achievement_details_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `achievement_details_ibfk_4` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `habit`
--
ALTER TABLE `habit`
  ADD CONSTRAINT `habit_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `habit_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `group` (`group_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `habit_ibfk_3` FOREIGN KEY (`monitor_id`) REFERENCES `monitor_date` (`monitor_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `monitor_date`
--
ALTER TABLE `monitor_date`
  ADD CONSTRAINT `monitor_date_ibfk_1` FOREIGN KEY (`habit_id`) REFERENCES `habit` (`habit_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `reminder`
--
ALTER TABLE `reminder`
  ADD CONSTRAINT `reminder_ibfk_1` FOREIGN KEY (`habit_id`) REFERENCES `habit` (`habit_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tracking`
--
ALTER TABLE `tracking`
  ADD CONSTRAINT `tracking_ibfk_1` FOREIGN KEY (`habit_id`) REFERENCES `habit` (`habit_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
