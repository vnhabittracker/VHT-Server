-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 15, 2018 at 04:57 AM
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

-- --------------------------------------------------------

--
-- Table structure for table `about`
--

DROP TABLE IF EXISTS `about`;
CREATE TABLE `about` (
  `app_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_fqa` text COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `feedback_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `star_num` int(11) NOT NULL,
  `feedback_description` text COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `group_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `group_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_description` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `group`
--

INSERT INTO `group` (`group_id`, `user_id`, `group_name`, `group_description`) VALUES
('1', NULL, 'Sức khỏe', NULL),
('2', NULL, 'Kinh tế', NULL),
('3', NULL, 'Gia đình', NULL),
('4', NULL, 'Học tập', NULL),
('5', NULL, 'Quan hệ', NULL),
('6', NULL, 'Công việc', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `habit`
--

DROP TABLE IF EXISTS `habit`;
CREATE TABLE `habit` (
  `habit_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `group_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `monitor_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
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

-- --------------------------------------------------------

--
-- Table structure for table `habit_suggestion`
--

DROP TABLE IF EXISTS `habit_suggestion`;
CREATE TABLE `habit_suggestion` (
  `habit_name_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `habit_name_uni` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `habit_name_ascii` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `habit_name_count` int(11) NOT NULL DEFAULT '0',
  `total_track` int(11) NOT NULL DEFAULT '0',
  `success_track` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `habit_suggestion`
--

INSERT INTO `habit_suggestion` (`habit_name_id`, `group_id`, `habit_name_uni`, `habit_name_ascii`, `habit_name_count`, `total_track`, `success_track`) VALUES
('154fsd', '2', 'Đi chợ', 'di cho', 23, 35, 35),
('1dv43fsd', '3', 'Đi bộ', 'di bo', 12, 90, 20),
('a0fde794-38', '4', 'an com', 'an com', 1, 0, 0),
('dasf', '1', 'Đi Uống thuốc', 'di uong thuoc', 1, 200, 119),
('dsfgsd', '2', 'Đi Chạy bộ', 'di chay bo', 19, 11, 10),
('fsd-345', '3', 'Đi Uống rượu', 'di uong ruou', 4, 1000, 10),
('fsd545', '3', 'Đi Thức đêm', 'di thuc dem', 51, 60, 59),
('gfdh', '1', 'Đi Hút thuốc', 'di hut thuoc', 12, 100, 99),
('Gym', '1', 'Gym', 'gym', 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `monitor_date`
--

DROP TABLE IF EXISTS `monitor_date`;
CREATE TABLE `monitor_date` (
  `monitor_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `habit_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mon` tinyint(1) DEFAULT '1',
  `tue` tinyint(1) DEFAULT '1',
  `wed` tinyint(1) DEFAULT '1',
  `thu` tinyint(1) DEFAULT '1',
  `fri` tinyint(1) DEFAULT '1',
  `sat` tinyint(1) DEFAULT '1',
  `sun` tinyint(1) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reminder`
--

DROP TABLE IF EXISTS `reminder`;
CREATE TABLE `reminder` (
  `reminder_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `habit_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `remind_start_time` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `remind_end_time` text COLLATE utf8mb4_unicode_ci,
  `repeat_type` text COLLATE utf8mb4_unicode_ci,
  `reminder_description` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tracking`
--

DROP TABLE IF EXISTS `tracking`;
CREATE TABLE `tracking` (
  `tracking_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `habit_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `current_date` date DEFAULT NULL,
  `count` int(11) DEFAULT '0',
  `tracking_description` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` text COLLATE utf8mb4_unicode_ci,
  `date_of_birth` date DEFAULT NULL,
  `gender` tinyint(1) DEFAULT '-1',
  `real_name` text COLLATE utf8mb4_unicode_ci,
  `user_description` text COLLATE utf8mb4_unicode_ci,
  `created_date` date DEFAULT NULL,
  `last_login_time` date DEFAULT NULL,
  `continue_using_count` int(11) DEFAULT '0',
  `current_continue_using_count` int(11) DEFAULT '0',
  `best_continue_using_count` int(11) DEFAULT '0',
  `user_score` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `password`, `email`, `date_of_birth`, `gender`, `real_name`, `user_description`, `created_date`, `last_login_time`, `continue_using_count`, `current_continue_using_count`, `best_continue_using_count`, `user_score`) VALUES
('hzxjdv2N8tSWyyW5PMyhJ94GKbn1', 'tvtd995a3@gmail.com', 'hzxjdv2N8tSWyyW5PMyhJ94GKbn1', 'tvtd995a3@gmail.com', NULL, NULL, NULL, NULL, '2018-12-14', '2018-12-14', 1, 1, 1, 2);

--
-- Indexes for dumped tables
--

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
  ADD PRIMARY KEY (`group_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `habit`
--
ALTER TABLE `habit`
  ADD PRIMARY KEY (`habit_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `group_id` (`group_id`),
  ADD KEY `monitor_id` (`monitor_id`);

--
-- Indexes for table `habit_suggestion`
--
ALTER TABLE `habit_suggestion`
  ADD PRIMARY KEY (`habit_name_id`);

--
-- Indexes for table `monitor_date`
--
ALTER TABLE `monitor_date`
  ADD PRIMARY KEY (`monitor_id`),
  ADD KEY `monitor_date_ibfk_1` (`habit_id`);

--
-- Indexes for table `reminder`
--
ALTER TABLE `reminder`
  ADD PRIMARY KEY (`reminder_id`),
  ADD KEY `habit_id` (`habit_id`),
  ADD KEY `user_id` (`user_id`);

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
-- Constraints for dumped tables
--

--
-- Constraints for table `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `group`
--
ALTER TABLE `group`
  ADD CONSTRAINT `group_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `reminder_ibfk_1` FOREIGN KEY (`habit_id`) REFERENCES `habit` (`habit_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `reminder_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tracking`
--
ALTER TABLE `tracking`
  ADD CONSTRAINT `tracking_ibfk_1` FOREIGN KEY (`habit_id`) REFERENCES `habit` (`habit_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
