-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 16, 2018 at 07:05 AM
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
('75f17201-ec', 'd233c193-6e', '1', '43dbee12-67', 'Chạy bộ', 0, 1, 1, 'Lần', 3, '2018-11-12', NULL, '2018-11-16', '#78ed872a', 'bbb'),
('a8fb1816-9d', 'd233c193-6e', NULL, 'b4682cb8-e9', 'jjjj', 0, 0, 0, NULL, 1, '2018-11-16', NULL, '2018-11-16', '#ffffffff', ''),
('ca0f8248-35', 'd233c193-6e', NULL, '8cf0a0e2-66', 'ytryrt', 0, 0, 0, NULL, 1, '2018-11-16', NULL, '2018-11-16', '#ffffffff', '');

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
  `total_track` int(11) DEFAULT '0',
  `success_track` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `habit_suggestion`
--

INSERT INTO `habit_suggestion` (`habit_name_id`, `group_id`, `habit_name_uni`, `habit_name`, `habit_name_count`, `total_track`, `success_track`) VALUES
('', NULL, 'ytryrt', 'ytryrt', 1, 0, 0),
('154fsd', '2', 'Đi chợ', 'di cho', 12, 34, 30),
('1dv43fsd', '3', 'Đi bộ', 'di bo', 12, 90, 88),
('1fsd', '1', 'Ăn kem', 'an kem', 12, 13, 10),
('dasf', '1', 'Uống thuốc', 'uong thuoc', 1, 0, 0),
('dsfgsd', '2', 'Chạy bộ', 'chay bo', 12, 13, 10),
('e3fda411-c9', NULL, 'jjjj', 'jjjj', 1, 0, 0),
('fsd-345', '3', 'Uống rượu', 'uong ruou', 4, 34, 6),
('fsd545', '3', 'Thức đêm', 'thuc dem', 44, 67, 19),
('gfdh', '1', 'Hút thuốc', 'hut thuoc', 10, 100, 99),
('sasda', NULL, 'jhk', 'jhk', 1, 0, 0);

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
('43dbee12-67', '75f17201-ec', 1, 1, 1, 1, 1, 1, 1),
('8cf0a0e2-66', 'ca0f8248-35', 1, 1, 1, 1, 1, 1, 1),
('b4682cb8-e9', 'a8fb1816-9d', 1, 1, 1, 1, 1, 1, 1);

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
('115d8827-3e', '75f17201-ec', '2018-11-15', 3, NULL),
('94c57d1b-d7', '75f17201-ec', '2018-11-16', 2, NULL),
('a522ba52-85', '75f17201-ec', '2018-11-12', 1, 'hello'),
('a91bf676-b1', '75f17201-ec', '2018-11-14', 0, 'home qua');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` text COLLATE utf8mb4_unicode_ci,
  `email` text COLLATE utf8mb4_unicode_ci,
  `date_of_birth` date DEFAULT NULL,
  `gender` tinyint(1) DEFAULT '-1',
  `user_icon` text COLLATE utf8mb4_unicode_ci,
  `avatar` text COLLATE utf8mb4_unicode_ci,
  `user_description` text COLLATE utf8mb4_unicode_ci,
  `created_date` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `password`, `phone`, `email`, `date_of_birth`, `gender`, `user_icon`, `avatar`, `user_description`, `created_date`) VALUES
('1b153946-89', 'user03', '12345678', NULL, 'user02@mail.com', NULL, NULL, NULL, NULL, NULL, '2018-11-15'),
('d233c193-6e', 'user01', '12345678', NULL, 'user01@mail.com', NULL, 0, NULL, NULL, NULL, '2018-11-15');

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
