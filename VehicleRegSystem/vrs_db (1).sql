-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 25, 2024 at 02:42 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `vrs_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `checkstatus`
--

CREATE TABLE `checkstatus` (
  `applicationnum` varchar(8) NOT NULL,
  `vehiclenum` varchar(10) NOT NULL,
  `applicationstatus` varchar(1) NOT NULL COMMENT 'Application status indicated by 1-5.\r\n1-Apllication Submitted\r\n2-Transferor Approved\r\n3-DMT Validator approved\r\n4-Vehicle registration document Released(PDF)\r\n5-Vehicle registration document Released(via POST)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `checkstatus`
--

INSERT INTO `checkstatus` (`applicationnum`, `vehiclenum`, `applicationstatus`) VALUES
('1234AA', 'BX-7878', '1'),
('1255AB', 'BO-7888', '3'),
('1000AA', 'AS-2323', '5'),
('0001AA', 'AB-2323', '4'),
('0525YZ', 'AS-9090', '3'),
('1687ZW', 'ASU-3434', '2');

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `indexID` int(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`indexID`, `username`, `email`, `password`) VALUES
(1, '0', '0', '123'),
(4, 'Ashan', 'ashan@vrs.com', '123'),
(5, 'John', 'john@vrs.com', '123'),
(9, 'as', 'qw@gmail.com', '123'),
(10, 'asdasd', 'asd@gmail.com', '123'),
(11, 'adsd', 'ash@ffgd.com', '123'),
(12, 'dfdfdf', 'ashan@hhhh.com', '123'),
(13, 'Lakma', 'lakma@gmail.com', 'Lakma@123'),
(14, 'shamil suraweera', 'shamilsuraweera@vrs.com', '123');

-- --------------------------------------------------------

--
-- Table structure for table `vehicle_details`
--

CREATE TABLE `vehicle_details` (
  `application_num` varchar(10) DEFAULT NULL,
  `vehicle_type` varchar(25) DEFAULT NULL,
  `vehicle_num` varchar(10) DEFAULT NULL,
  `fuel_type` varchar(20) DEFAULT NULL,
  `reg_certificate_pdf` blob DEFAULT NULL,
  `revenue_license_pdf` blob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vehicle_details`
--

INSERT INTO `vehicle_details` (`application_num`, `vehicle_type`, `vehicle_num`, `fuel_type`, `reg_certificate_pdf`, `revenue_license_pdf`) VALUES
('0001AA', 'Car', 'AB-2323', 'Petrol', '', ''),
('1687ZW', 'Car', 'ASU-3434', 'Petrol', '', ''),
('0525YZ', 'Motor Bike', 'AS-9090', 'Diesel', '', ''),
('5224VD', 'Car', 'WW-9090', 'Petrol', '', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`indexID`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `vehicle_details`
--
ALTER TABLE `vehicle_details`
  ADD UNIQUE KEY `application_num` (`application_num`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `login`
--
ALTER TABLE `login`
  MODIFY `indexID` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
