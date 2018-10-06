-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 06, 2018 at 07:51 AM
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

DROP TABLE IF EXISTS `about`;
CREATE TABLE IF NOT EXISTS `about` (
  `app_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_fqa` text COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `achievement`
--

DROP TABLE IF EXISTS `achievement`;
CREATE TABLE IF NOT EXISTS `achievement` (
  `achievement_id` int(11) NOT NULL AUTO_INCREMENT,
  `achievement_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `achievement_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`achievement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `achievement_details`
--

DROP TABLE IF EXISTS `achievement_details`;
CREATE TABLE IF NOT EXISTS `achievement_details` (
  `user_id` int(11) NOT NULL,
  `achievement_id` int(11) NOT NULL,
  `count` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`achievement_id`),
  KEY `achievement_id` (`achievement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
CREATE TABLE IF NOT EXISTS `category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `parrent_id` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `category_icon` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `category_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
CREATE TABLE IF NOT EXISTS `feedback` (
  `feedback_id` int(11) NOT NULL AUTO_INCREMENT,
  `star_num` int(11) NOT NULL,
  `feedback_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`feedback_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `goal`
--

DROP TABLE IF EXISTS `goal`;
CREATE TABLE IF NOT EXISTS `goal` (
  `goal_id` int(11) NOT NULL AUTO_INCREMENT,
  `goal_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `goal_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`goal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `habit`
--

DROP TABLE IF EXISTS `habit`;
CREATE TABLE IF NOT EXISTS `habit` (
  `habit_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `habit_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `habit_type` bit(11) NOT NULL,
  `unit` int(11) NOT NULL,
  `count_type` bit(11) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `created_date` date NOT NULL,
  `habit_icon` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `habit_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `category_id` int(11) NOT NULL,
  `schedule_id` int(11) NOT NULL,
  `goal_id` int(11) NOT NULL,
  PRIMARY KEY (`habit_id`),
  KEY `user_id` (`user_id`),
  KEY `category_id` (`category_id`),
  KEY `schedule_id` (`schedule_id`),
  KEY `goal_id` (`goal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reminder`
--

DROP TABLE IF EXISTS `reminder`;
CREATE TABLE IF NOT EXISTS `reminder` (
  `reminder_id` int(11) NOT NULL AUTO_INCREMENT,
  `reminder_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `sound` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `reminder_icon` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `habit_id` int(11) NOT NULL,
  PRIMARY KEY (`reminder_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
CREATE TABLE IF NOT EXISTS `schedule` (
  `schedule_id` int(11) NOT NULL AUTO_INCREMENT,
  `schedule_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `schedule_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tracking`
--

DROP TABLE IF EXISTS `tracking`;
CREATE TABLE IF NOT EXISTS `tracking` (
  `tracking_id` int(11) NOT NULL AUTO_INCREMENT,
  `habit_id` int(11) NOT NULL,
  `reminder_id` int(11) NOT NULL,
  `result` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `tracking_icon` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `tracking_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`tracking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_of_birth` date NOT NULL,
  `gender` tinyint(1) NOT NULL,
  `user_icon` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `avatar` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `password`, `email`, `date_of_birth`, `gender`, `user_icon`, `avatar`, `user_description`) VALUES
(1, 'user01', '12345678', 'user01@mail.com', '1998-04-25', 0, '', '', 'The first user'),
(2, 'user02', '87654321', 'user02@mail.com', '1985-08-08', 0, '', '', 'The second user'),
(3, 'user03', 'abc12345', 'user03@mail.com', '1995-10-20', 0, '', '', 'a new user');

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
-- Constraints for table `habit`
--
ALTER TABLE `habit`
  ADD CONSTRAINT `habit_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `habit_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`),
  ADD CONSTRAINT `habit_ibfk_3` FOREIGN KEY (`schedule_id`) REFERENCES `category` (`category_id`),
  ADD CONSTRAINT `habit_ibfk_4` FOREIGN KEY (`goal_id`) REFERENCES `goal` (`goal_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
