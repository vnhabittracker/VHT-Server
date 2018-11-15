-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 15, 2018 at 01:35 PM
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
  `achievement_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `achievement_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `achievement_description` text COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `achievement_details`
--

CREATE TABLE `achievement_details` (
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `achievement_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

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

CREATE TABLE `group` (
  `group_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `group_icon` text COLLATE utf8mb4_unicode_ci,
  `group_description` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `group`
--

INSERT INTO `group` (`group_id`, `group_name`, `parent_id`, `group_icon`, `group_description`) VALUES
('1', 'Sức khỏe', NULL, '', ''),
('2', 'Tài chính', NULL, '', ''),
('3', 'Gia đình', NULL, '', ''),
('4', 'Học tập', NULL, '', ''),
('5', 'Mua sắm', NULL, '', ''),
('6', 'hhhhh', NULL, NULL, NULL),
('7', 'abc', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `habit`
--

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

--
-- Dumping data for table `habit`
--

INSERT INTO `habit` (`habit_id`, `user_id`, `group_id`, `monitor_id`, `habit_name`, `habit_target`, `habit_type`, `monitor_type`, `monitor_unit`, `monitor_number`, `start_date`, `end_date`, `created_date`, `habit_color`, `habit_description`) VALUES
('b468337e-b8', 'd233c193-6e', NULL, '30cfb386-0f', 'Chạy bộ', 0, 0, 1, 'Lần', 5, '2018-11-15', NULL, '2018-11-15', '#78ed872a', 'chat bo');

-- --------------------------------------------------------

--
-- Table structure for table `habit_suggestion`
--

CREATE TABLE `habit_suggestion` (
  `habit_name_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `habit_name_uni` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `habit_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `habit_name_count` int(11) DEFAULT '0',
  `total_track` int(11) NOT NULL DEFAULT '0',
  `success_track` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `habit_suggestion`
--

INSERT INTO `habit_suggestion` (`habit_name_id`, `group_id`, `habit_name_uni`, `habit_name`, `habit_name_count`, `total_track`, `success_track`) VALUES
('1', '1', 'Chạy bộ', 'chay bo', 10, 13, 10);

-- --------------------------------------------------------

--
-- Table structure for table `monitor_date`
--

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

--
-- Dumping data for table `monitor_date`
--

INSERT INTO `monitor_date` (`monitor_id`, `habit_id`, `mon`, `tue`, `wed`, `thu`, `fri`, `sat`, `sun`) VALUES
('30cfb386-0f', 'b468337e-b8', 1, 1, 1, 1, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `reminder`
--

CREATE TABLE `reminder` (
  `reminder_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `habit_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `reminder_time` text COLLATE utf8mb4_unicode_ci,
  `repeat_time` int(11) DEFAULT NULL,
  `reminder_description` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tracking`
--

CREATE TABLE `tracking` (
  `tracking_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `habit_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `current_date` date DEFAULT NULL,
  `count` int(11) DEFAULT '0',
  `tracking_description` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tracking`
--

INSERT INTO `tracking` (`tracking_id`, `habit_id`, `current_date`, `count`, `tracking_description`) VALUES
('1fdc3568-b8', 'b468337e-b8', '2018-11-15', 2, 'chat bo');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_of_birth` date NOT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT '-1',
  `user_icon` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `avatar` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_date` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `password`, `phone`, `email`, `date_of_birth`, `gender`, `user_icon`, `avatar`, `user_description`, `created_date`) VALUES
('d233c193-6e', 'user01', '12345678', '', 'user01@mail.com', '0000-00-00', 0, '', '', '', '2018-11-15');

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
  ADD KEY `group_id` (`group_id`),
  ADD KEY `monitor_id` (`monitor_id`);

--
-- Indexes for table `habit_suggestion`
--
ALTER TABLE `habit_suggestion`
  ADD PRIMARY KEY (`habit_name_id`),
  ADD KEY `group_id` (`group_id`);

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
  ADD KEY `habit_id` (`habit_id`);

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
-- Constraints for table `achievement_details`
--
ALTER TABLE `achievement_details`
  ADD CONSTRAINT `achievement_details_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `achievement_details_ibfk_2` FOREIGN KEY (`achievement_id`) REFERENCES `achievement` (`achievement_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `habit`
--
ALTER TABLE `habit`
  ADD CONSTRAINT `habit_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `habit_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `group` (`group_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `habit_ibfk_3` FOREIGN KEY (`monitor_id`) REFERENCES `monitor_date` (`monitor_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `habit_suggestion`
--
ALTER TABLE `habit_suggestion`
  ADD CONSTRAINT `habit_suggestion_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `group` (`group_id`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `tracking_ibfk_1` FOREIGN KEY (`habit_id`) REFERENCES `habit` (`habit_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
