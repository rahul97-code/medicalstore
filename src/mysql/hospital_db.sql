-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 30, 2014 at 10:55 AM
-- Server version: 5.5.27
-- PHP Version: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `hospital_db`
--
CREATE DATABASE IF NOT EXISTS `hospital_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `hospital_db`;

-- --------------------------------------------------------

--
-- Table structure for table `admin_account`
--

CREATE TABLE IF NOT EXISTS `admin_account` (
  `admin_id` int(200) NOT NULL AUTO_INCREMENT,
  `admin_username` varchar(250) NOT NULL,
  `admin_password` varchar(250) NOT NULL,
  `last_login` varchar(250) NOT NULL DEFAULT '0000-00-00',
  `admin_text1` varchar(250) NOT NULL DEFAULT 'N/A',
  `admin_text2` varchar(250) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `amountreceipt`
--

CREATE TABLE IF NOT EXISTS `amountreceipt` (
  `receipt_id` int(200) NOT NULL AUTO_INCREMENT,
  `receipt_type` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt_amount` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt_username` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt_ref_doctor` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt_patientname` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt__date` date NOT NULL DEFAULT '0000-00-00',
  `receipt_time` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt_text1` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt_text2` varchar(250) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`receipt_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `cancel_detail`
--

CREATE TABLE IF NOT EXISTS `cancel_detail` (
  `can_id` int(200) NOT NULL AUTO_INCREMENT,
  `can_cat` varchar(250) NOT NULL,
  `p_id` varchar(250) NOT NULL,
  `p_name` varchar(250) NOT NULL,
  `reciept_no` varchar(250) NOT NULL,
  `can_amount` varchar(250) NOT NULL,
  `cancelled_by` varchar(250) NOT NULL,
  `generate_date` date NOT NULL,
  `date` date NOT NULL,
  `time` varchar(250) NOT NULL,
  `remarks` varchar(250) NOT NULL DEFAULT 'N/A',
  `text1` varchar(250) DEFAULT NULL,
  `text2` varchar(250) DEFAULT NULL,
  `text3` varchar(250) DEFAULT NULL,
  `text4` varchar(250) DEFAULT NULL,
  `text5` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`can_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `departments_detail`
--

CREATE TABLE IF NOT EXISTS `departments_detail` (
  `department_id` int(250) NOT NULL AUTO_INCREMENT,
  `department_name` varchar(250) NOT NULL,
  `department_username` varchar(250) NOT NULL,
  `department_password` varchar(250) NOT NULL,
  `department_type` varchar(250) NOT NULL,
  `department_room_name` varchar(250) NOT NULL,
  `department_room_no` varchar(250) NOT NULL,
  `last_login` varchar(250) NOT NULL DEFAULT 'N/A',
  `department_status` varchar(250) NOT NULL,
  `entry_user` varchar(250) NOT NULL,
  `creation_date` date NOT NULL,
  `department_text1` varchar(250) NOT NULL DEFAULT 'null',
  `department_text2` varchar(250) NOT NULL DEFAULT 'null',
  `department_text3` varchar(250) NOT NULL DEFAULT 'null',
  `department_text4` varchar(250) NOT NULL DEFAULT 'null',
  PRIMARY KEY (`department_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `department_stock_register`
--

CREATE TABLE IF NOT EXISTS `department_stock_register` (
  `stock_item_id` int(250) NOT NULL AUTO_INCREMENT,
  `department_id` varchar(250) NOT NULL,
  `department_name` varchar(250) NOT NULL,
  `user_id` varchar(250) NOT NULL DEFAULT 'n/a',
  `user_name` varchar(250) NOT NULL DEFAULT 'n/a',
  `item_id` varchar(250) NOT NULL,
  `item_name` varchar(250) NOT NULL,
  `item_desc` varchar(250) NOT NULL,
  `total_stock` varchar(250) NOT NULL,
  `last_issued` date NOT NULL,
  `expiry_date` date NOT NULL,
  `text1` varchar(250) NOT NULL DEFAULT 'null',
  `text2` varchar(250) NOT NULL DEFAULT 'null',
  `text3` varchar(250) NOT NULL DEFAULT 'null',
  `text4` varchar(250) NOT NULL DEFAULT 'null',
  `text5` varchar(250) NOT NULL DEFAULT 'null',
  `text6` varchar(250) NOT NULL DEFAULT 'null',
  PRIMARY KEY (`stock_item_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `dept_pills_register`
--

CREATE TABLE IF NOT EXISTS `dept_pills_register` (
  `entry_id` int(250) NOT NULL AUTO_INCREMENT,
  `dept_id` varchar(250) NOT NULL,
  `dept_name` varchar(250) NOT NULL,
  `doctor_id` varchar(250) NOT NULL,
  `doctor_name` varchar(250) NOT NULL,
  `user_id` varchar(250) NOT NULL,
  `user_name` varchar(250) NOT NULL,
  `p_id` varchar(250) NOT NULL,
  `p_name` varchar(250) NOT NULL,
  `item_id` varchar(250) NOT NULL,
  `item_name` varchar(250) NOT NULL,
  `item_desc` varchar(250) NOT NULL,
  `item_price` varchar(250) NOT NULL,
  `item_qty` varchar(250) NOT NULL,
  `total_price` varchar(250) NOT NULL,
  `date` date NOT NULL,
  `time` varchar(250) NOT NULL,
  `ipd_or_misc_number` varchar(250) NOT NULL,
  `enry_type` varchar(250) NOT NULL,
  `text1` varchar(250) NOT NULL DEFAULT 'n/a',
  `text2` varchar(250) NOT NULL DEFAULT 'n/a',
  `text3` varchar(250) NOT NULL DEFAULT 'n/a',
  `text4` varchar(250) NOT NULL DEFAULT 'n/a',
  `text5` varchar(250) NOT NULL DEFAULT 'n/a',
  `text6` varchar(250) NOT NULL DEFAULT 'n/a',
  PRIMARY KEY (`entry_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `doctor_detail`
--

CREATE TABLE IF NOT EXISTS `doctor_detail` (
  `doc_id` int(255) NOT NULL AUTO_INCREMENT,
  `doc_name` varchar(255) NOT NULL,
  `doc_username` varchar(255) NOT NULL,
  `doc_password` varchar(255) NOT NULL,
  `doc_specialization` varchar(255) NOT NULL,
  `doc_qualification` varchar(255) DEFAULT NULL,
  `doc_opdroom` varchar(255) NOT NULL,
  `doc_telephone` varchar(255) DEFAULT NULL,
  `doc_address` varchar(255) DEFAULT NULL,
  `doc_hindiname` varchar(255) DEFAULT NULL,
  `doc_lastlogin` varchar(255) DEFAULT '2014-02-28 05:19:03 PM',
  `doc_active` varchar(255) NOT NULL DEFAULT '1',
  `doc_text1` varchar(255) DEFAULT NULL,
  `doc_text2` varchar(255) DEFAULT '0',
  PRIMARY KEY (`doc_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `exam_amountreceipt`
--

CREATE TABLE IF NOT EXISTS `exam_amountreceipt` (
  `exam_receiptid` int(200) NOT NULL AUTO_INCREMENT,
  `exam_namecat` varchar(250) NOT NULL,
  `exam_doctor_ref` varchar(250) NOT NULL,
  `exam_patientname` varchar(250) NOT NULL,
  `exam_amount` varchar(250) NOT NULL,
  `exam_username` varchar(250) NOT NULL,
  `exam_date` date NOT NULL DEFAULT '0000-00-00',
  `exam_time` varchar(250) NOT NULL,
  `exam_text1` varchar(250) NOT NULL DEFAULT 'n/a',
  `exam_text2` varchar(250) NOT NULL DEFAULT 'n/a',
  `exan_text3` varchar(250) NOT NULL DEFAULT 'n/a',
  `exam_text4` varchar(250) NOT NULL DEFAULT 'n/a',
  PRIMARY KEY (`exam_receiptid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `exam_entery`
--

CREATE TABLE IF NOT EXISTS `exam_entery` (
  `exam_id` int(10) NOT NULL AUTO_INCREMENT,
  `exam_name` varchar(255) NOT NULL,
  `exam_nameid` varchar(255) NOT NULL,
  `exam_pid` varchar(255) NOT NULL,
  `exam_pname` varchar(255) NOT NULL,
  `exam_doctorreff` varchar(255) NOT NULL,
  `exam_date` date NOT NULL,
  `exam_charges` varchar(255) NOT NULL,
  `exam_chargespaid` varchar(255) DEFAULT 'Yes',
  `exam_performed` varchar(255) NOT NULL DEFAULT 'No',
  `exam_operator` varchar(255) DEFAULT NULL,
  `exam_room` varchar(255) DEFAULT NULL,
  `exam_reportno` varchar(255) DEFAULT '0',
  `exam_reportname1` varchar(255) DEFAULT NULL,
  `exam_reportname2` varchar(255) DEFAULT NULL,
  `exam_reportname3` varchar(255) DEFAULT NULL,
  `exam_reportname4` varchar(255) DEFAULT NULL,
  `exam_result1` varchar(255) DEFAULT NULL,
  `exam_result2` varchar(255) DEFAULT NULL,
  `exam_result3` varchar(255) DEFAULT NULL,
  `exam_result4` varchar(255) DEFAULT NULL,
  `exam_result5` varchar(255) DEFAULT NULL,
  `exam_sample1` varchar(255) DEFAULT NULL,
  `exam_sample2` varchar(255) DEFAULT NULL,
  `exam_sample3` varchar(255) DEFAULT NULL,
  `exam_sample4` varchar(255) DEFAULT NULL,
  `exam_sample5` varchar(255) DEFAULT NULL,
  `exam_note1` varchar(255) DEFAULT NULL,
  `exam_note2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`exam_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `exam_master`
--

CREATE TABLE IF NOT EXISTS `exam_master` (
  `exam_code` int(10) NOT NULL,
  `exam_desc` varchar(255) NOT NULL,
  `exam_subcat` varchar(255) NOT NULL,
  `exam_lab` varchar(255) DEFAULT NULL,
  `exam_room` varchar(255) DEFAULT NULL,
  `exam_operator` varchar(255) DEFAULT NULL,
  `exam_text1` varchar(255) DEFAULT NULL,
  `exam_text2` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `hms_detail`
--

CREATE TABLE IF NOT EXISTS `hms_detail` (
  `detail_id` int(11) NOT NULL AUTO_INCREMENT,
  `detail_desc` varchar(255) NOT NULL,
  PRIMARY KEY (`detail_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `insurance_detail`
--

CREATE TABLE IF NOT EXISTS `insurance_detail` (
  `ins_id` int(10) NOT NULL AUTO_INCREMENT,
  `ins_name` varchar(255) NOT NULL,
  `ins_detail` varchar(255) NOT NULL,
  `ins_ratepercentage` varchar(255) DEFAULT '0',
  `ins_ratetype` varchar(255) DEFAULT NULL,
  `ins_test6` varchar(255) DEFAULT NULL,
  `ins_test7` varchar(255) DEFAULT NULL,
  `ins_test8` varchar(255) DEFAULT NULL,
  `ins_test9` varchar(255) DEFAULT NULL,
  `ins_test10` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ins_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `invoice_entry`
--

CREATE TABLE IF NOT EXISTS `invoice_entry` (
  `invoice_id` int(250) NOT NULL AUTO_INCREMENT,
  `invoice_id2` varchar(250) NOT NULL,
  `invoice_order_no` varchar(250) NOT NULL,
  `invoice_supplier_id` varchar(250) NOT NULL,
  `invoice_supplier_name` varchar(250) NOT NULL,
  `invoice_date` date NOT NULL,
  `invoice_time` varchar(250) NOT NULL,
  `invoice_due_date` date NOT NULL DEFAULT '0000-00-00',
  `invoice_paid` varchar(250) NOT NULL,
  `invoice_entry_user` varchar(250) NOT NULL,
  `invoice_total_amount` varchar(250) NOT NULL,
  `invoice_tax` varchar(250) NOT NULL,
  `invoice_discount` varchar(250) NOT NULL,
  `invoice_final_amount` varchar(250) NOT NULL,
  `invoice_text1` varchar(250) NOT NULL DEFAULT 'null',
  `invoice_text2` varchar(250) NOT NULL DEFAULT 'null',
  `invoice_text3` varchar(250) NOT NULL DEFAULT 'null',
  `invoice_text4` varchar(250) NOT NULL DEFAULT 'null',
  `invoice_text5` varchar(250) NOT NULL DEFAULT 'null',
  `invoice_text6` varchar(250) NOT NULL DEFAULT 'null',
  `invoice_text7` varchar(250) NOT NULL DEFAULT 'null',
  PRIMARY KEY (`invoice_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `invoice_items`
--

CREATE TABLE IF NOT EXISTS `invoice_items` (
  `invoice_item_id` int(250) NOT NULL AUTO_INCREMENT,
  `item_id` varchar(250) NOT NULL,
  `invoice_item_name` varchar(250) NOT NULL,
  `invoice_item_desc` varchar(250) NOT NULL,
  `invoice_id` int(250) NOT NULL,
  `item_meas_units` varchar(250) NOT NULL,
  `item_qty` varchar(250) NOT NULL,
  `item_unit_price` varchar(250) NOT NULL,
  `item_discount` varchar(250) NOT NULL,
  `item_tax` varchar(250) NOT NULL,
  `invoice_value` varchar(250) NOT NULL,
  `item_expiry_date` date NOT NULL,
  `item_date` date NOT NULL,
  `item_time` varchar(250) NOT NULL,
  `item_entry_user` varchar(250) NOT NULL,
  `item_text1` varchar(250) NOT NULL DEFAULT 'null',
  `item_text2` varchar(250) NOT NULL DEFAULT 'null',
  `item_text3` varchar(250) NOT NULL DEFAULT 'null',
  `item_text4` varchar(250) NOT NULL DEFAULT 'null',
  `item_text5` varchar(250) NOT NULL DEFAULT 'null',
  `item_text6` varchar(250) NOT NULL DEFAULT 'null',
  PRIMARY KEY (`invoice_item_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ipd_bed_details`
--

CREATE TABLE IF NOT EXISTS `ipd_bed_details` (
  `change_id` int(255) NOT NULL AUTO_INCREMENT,
  `ipd_id` varchar(255) NOT NULL,
  `p_id` varchar(255) NOT NULL DEFAULT 'N/A',
  `p_name` varchar(255) NOT NULL DEFAULT 'N/A',
  `ipd_building` varchar(255) NOT NULL,
  `ipd_ward` varchar(255) NOT NULL,
  `ipd_room` varchar(255) NOT NULL,
  `ipd_bed_no` varchar(255) NOT NULL,
  `start_date` date NOT NULL,
  `start_time` varchar(255) NOT NULL,
  `end_date` date NOT NULL DEFAULT '0000-00-00',
  `end_time` varchar(255) NOT NULL DEFAULT 'n/a',
  `note1` varchar(255) NOT NULL DEFAULT 'N/A',
  `note2` varchar(255) NOT NULL DEFAULT 'N/A',
  `text1` varchar(255) NOT NULL DEFAULT 'N/A',
  `text2` varchar(255) NOT NULL DEFAULT 'N/A',
  `text3` varchar(255) NOT NULL DEFAULT 'N/A',
  `text4` varchar(255) NOT NULL DEFAULT 'N/A',
  `text5` varchar(255) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`change_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ipd_bill`
--

CREATE TABLE IF NOT EXISTS `ipd_bill` (
  `bill_id` int(200) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(250) NOT NULL,
  PRIMARY KEY (`bill_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ipd_doctor_incharge`
--

CREATE TABLE IF NOT EXISTS `ipd_doctor_incharge` (
  `handover_charge_id` int(255) NOT NULL AUTO_INCREMENT,
  `ipd_id` varchar(255) NOT NULL,
  `doctor_name` varchar(255) NOT NULL,
  `doctor_id` varchar(255) NOT NULL,
  `p_id` varchar(255) NOT NULL DEFAULT 'N/A',
  `p_name` varchar(255) NOT NULL DEFAULT 'N/A',
  `start_date` date NOT NULL,
  `start_time` varchar(255) NOT NULL,
  `end_date` date NOT NULL DEFAULT '0000-00-00',
  `end_time` varchar(255) NOT NULL DEFAULT 'n/a',
  `note1` varchar(255) NOT NULL DEFAULT 'N/A',
  `note2` varchar(255) NOT NULL DEFAULT 'N/A',
  `text1` varchar(255) NOT NULL DEFAULT 'N/A',
  `text2` varchar(255) NOT NULL DEFAULT 'N/A',
  `text3` varchar(255) NOT NULL DEFAULT 'N/A',
  `text4` varchar(255) NOT NULL DEFAULT 'N/A',
  `text5` varchar(255) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`handover_charge_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='doctor incharge in ipd entry';

-- --------------------------------------------------------

--
-- Table structure for table `ipd_entery`
--

CREATE TABLE IF NOT EXISTS `ipd_entery` (
  `ipd_id` int(255) NOT NULL AUTO_INCREMENT,
  `ipd_id2` varchar(255) NOT NULL DEFAULT 'N/A',
  `p_id` varchar(255) NOT NULL,
  `p_name` varchar(255) NOT NULL,
  `ipd_doctor_refference` varchar(255) NOT NULL DEFAULT 'N/A',
  `ipd_building` varchar(255) NOT NULL,
  `ipd_ward` varchar(255) NOT NULL,
  `ipd_room` varchar(255) NOT NULL,
  `ipd_bed_no` varchar(255) NOT NULL,
  `ipd_ward_code` varchar(255) NOT NULL,
  `ipd_entry_date` date NOT NULL,
  `ipd_enter_time` varchar(255) NOT NULL,
  `ipd_advanced_payment` varchar(255) NOT NULL DEFAULT '00',
  `ipd_total_charges` varchar(255) NOT NULL DEFAULT '00',
  `ipd_balance` varchar(255) NOT NULL DEFAULT '00',
  `ipd_recieved_amount` varchar(255) NOT NULL DEFAULT '00',
  `ipd_payment_recieved` varchar(255) NOT NULL DEFAULT 'No',
  `ipd_discharge_date` date NOT NULL DEFAULT '0000-00-00',
  `ipd_discharge_time` varchar(255) DEFAULT 'N/A',
  `ipd_discharged` varchar(255) NOT NULL DEFAULT 'No',
  `ipd-entry_user` varchar(250) NOT NULL DEFAULT 'N/A',
  `ipd_text1` varchar(255) NOT NULL DEFAULT 'N/A',
  `ipd_text2` varchar(255) NOT NULL DEFAULT 'N/A',
  `ipd_text3` varchar(255) NOT NULL DEFAULT 'N/A',
  `ipd_text4` varchar(255) NOT NULL DEFAULT 'N/A',
  `ipd_text5` varchar(255) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`ipd_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='IPD PATIENT ENTRY';

-- --------------------------------------------------------

--
-- Table structure for table `ipd_expenses`
--

CREATE TABLE IF NOT EXISTS `ipd_expenses` (
  `expense_id` int(250) NOT NULL AUTO_INCREMENT,
  `ipd_id` varchar(250) NOT NULL,
  `expense_name` varchar(250) NOT NULL,
  `expense_desc` varchar(250) NOT NULL,
  `expense_amount` varchar(250) NOT NULL,
  `charges_id` varchar(250) NOT NULL DEFAULT '0',
  `expense_date` date NOT NULL DEFAULT '0000-00-00',
  `expense_time` varchar(250) NOT NULL,
  `expense_text1` varchar(250) NOT NULL DEFAULT 'N/A',
  `expense_text2` varchar(250) NOT NULL DEFAULT 'N/A',
  `expense_text3` varchar(250) NOT NULL DEFAULT 'N/A',
  `expense_text4` varchar(250) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`expense_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ipd_payments`
--

CREATE TABLE IF NOT EXISTS `ipd_payments` (
  `payment_id` int(250) NOT NULL AUTO_INCREMENT,
  `ipd_id` varchar(250) NOT NULL,
  `p_id` varchar(250) NOT NULL,
  `p_name` varchar(250) NOT NULL,
  `payment_type` varchar(250) NOT NULL,
  `payment_desc` varchar(250) NOT NULL,
  `payment_amount` varchar(250) NOT NULL,
  `payment_date` date NOT NULL,
  `payment_time` varchar(250) NOT NULL,
  `payment_entry_user` varchar(250) NOT NULL,
  `payment_text1` varchar(250) NOT NULL DEFAULT 'N/A',
  `payment_text2` varchar(250) NOT NULL DEFAULT 'N/A',
  `payment_text3` varchar(250) NOT NULL DEFAULT 'N/A',
  `payment_text4` varchar(250) NOT NULL DEFAULT 'N/A',
  `payment_text5` varchar(250) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`payment_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `issued_department_register`
--

CREATE TABLE IF NOT EXISTS `issued_department_register` (
  `issued_id` int(250) NOT NULL AUTO_INCREMENT,
  `department_id` varchar(250) NOT NULL,
  `department_name` varchar(250) NOT NULL,
  `person_name` varchar(250) NOT NULL,
  `persone_id` varchar(250) NOT NULL DEFAULT 'non',
  `intent_slip_no` varchar(250) NOT NULL,
  `date` date NOT NULL,
  `time` varchar(250) NOT NULL,
  `item_id` varchar(250) NOT NULL,
  `item_name` varchar(250) NOT NULL,
  `item_desc` varchar(250) NOT NULL,
  `issued_qty` varchar(250) NOT NULL,
  `previouse_stock` varchar(250) NOT NULL,
  `consumable` varchar(250) NOT NULL,
  `return_date` varchar(250) NOT NULL DEFAULT '0000-00-00',
  `item_returned` varchar(250) NOT NULL DEFAULT 'Yes',
  `expiry_date` varchar(250) NOT NULL,
  `issued_by` varchar(250) NOT NULL,
  `issued_text1` varchar(250) NOT NULL DEFAULT 'null',
  `issued_text2` varchar(250) NOT NULL DEFAULT 'null',
  `issued_text3` varchar(250) NOT NULL DEFAULT 'null',
  `issued_text4` varchar(250) NOT NULL DEFAULT 'null',
  `issued_text5` varchar(250) NOT NULL DEFAULT 'null',
  `issued_text6` varchar(250) NOT NULL DEFAULT 'null',
  `issued_text7` varchar(250) NOT NULL DEFAULT 'null',
  PRIMARY KEY (`issued_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `items_detail`
--

CREATE TABLE IF NOT EXISTS `items_detail` (
  `item_id` int(250) NOT NULL AUTO_INCREMENT,
  `item_name` varchar(250) NOT NULL,
  `item_desc` varchar(250) NOT NULL,
  `item_brand` varchar(250) NOT NULL,
  `item_salt_name` varchar(250) NOT NULL,
  `item_treatment` varchar(250) NOT NULL,
  `item_lab` varchar(250) NOT NULL,
  `item_surgery` varchar(250) NOT NULL,
  `item_drug` varchar(250) NOT NULL,
  `item_imaging` varchar(250) NOT NULL,
  `item_inpatient` varchar(250) NOT NULL,
  `item_medic_income` varchar(250) NOT NULL,
  `item_category` varchar(250) NOT NULL,
  `item_meas_unit` varchar(250) NOT NULL,
  `item_type` varchar(250) NOT NULL,
  `item_tax_type` varchar(250) NOT NULL,
  `item_tax_value` varchar(250) NOT NULL,
  `item_purchase_price` varchar(250) NOT NULL,
  `item_prevouse_price` varchar(250) NOT NULL DEFAULT '0',
  `item_selling_price` varchar(250) NOT NULL,
  `item_total_stock` varchar(250) NOT NULL,
  `item_generic_name` varchar(250) NOT NULL,
  `item_minimum_units` varchar(250) NOT NULL,
  `item_status` varchar(250) NOT NULL,
  `item_expiry_date` date NOT NULL,
  `item_date` date NOT NULL,
  `item_time` varchar(250) NOT NULL,
  `item_entry_user` varchar(250) NOT NULL,
  `item_text1` varchar(250) NOT NULL DEFAULT 'null',
  `item_text2` varchar(250) DEFAULT 'null',
  `item_text3` varchar(250) NOT NULL DEFAULT 'null',
  `item_text4` varchar(250) NOT NULL DEFAULT 'null',
  `item_text5` varchar(250) NOT NULL DEFAULT 'null',
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `mailsetting`
--

CREATE TABLE IF NOT EXISTS `mailsetting` (
  `entry_id` int(200) NOT NULL AUTO_INCREMENT,
  `email_id` varchar(250) NOT NULL,
  `password` varchar(250) NOT NULL,
  `email_account_type` varchar(250) NOT NULL,
  PRIMARY KEY (`entry_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `misc_amount_reciept`
--

CREATE TABLE IF NOT EXISTS `misc_amount_reciept` (
  `misc_amount_reciept` int(250) NOT NULL AUTO_INCREMENT,
  `misc_desc` varchar(250) DEFAULT NULL,
  `misc_p_id` varchar(250) NOT NULL,
  `misc_date` date NOT NULL,
  `misc_text` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`misc_amount_reciept`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `misc_entry`
--

CREATE TABLE IF NOT EXISTS `misc_entry` (
  `misc_id` int(200) NOT NULL AUTO_INCREMENT,
  `misc_amount_reciept` varchar(250) NOT NULL,
  `misc_namecat` varchar(250) NOT NULL,
  `misc_desc` varchar(250) NOT NULL,
  `misc_doctor_ref` varchar(250) NOT NULL,
  `misc_p_id` varchar(250) NOT NULL,
  `misc_p_name` varchar(250) NOT NULL,
  `misc_amount` varchar(250) NOT NULL,
  `misc_username` varchar(250) NOT NULL,
  `misc_date` date NOT NULL DEFAULT '0000-00-00',
  `misc_time` varchar(250) NOT NULL,
  `misc_text1` varchar(250) NOT NULL DEFAULT 'n/a',
  `misc_text2` varchar(250) NOT NULL DEFAULT 'n/a',
  `misc_text3` varchar(250) NOT NULL DEFAULT 'n/a',
  `misc_text4` varchar(250) NOT NULL DEFAULT 'n/a',
  PRIMARY KEY (`misc_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `opd_entery`
--

CREATE TABLE IF NOT EXISTS `opd_entery` (
  `opd_id` int(255) NOT NULL AUTO_INCREMENT,
  `p_id` varchar(255) NOT NULL,
  `p_name` varchar(255) NOT NULL,
  `opd_doctor` varchar(255) NOT NULL,
  `opd_doctorid` varchar(255) NOT NULL,
  `opd_date` date NOT NULL,
  `opd_symptom` varchar(255) DEFAULT NULL,
  `opd_prescription` varchar(255) DEFAULT NULL,
  `opd_token` varchar(255) NOT NULL,
  `opd_diseasestype` varchar(255) NOT NULL,
  `opd_type` varchar(255) DEFAULT NULL,
  `opd_refferal` varchar(255) DEFAULT NULL,
  `opd_charge` varchar(255) DEFAULT NULL,
  `opd_entry_user` varchar(255) NOT NULL,
  `opd_entry_time` varchar(255) NOT NULL,
  `opd_text1` varchar(255) DEFAULT NULL,
  `opd_text2` varchar(255) DEFAULT NULL,
  `opd_text3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`opd_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `patient_detail`
--

CREATE TABLE IF NOT EXISTS `patient_detail` (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT 'running number',
  `pid1` varchar(255) NOT NULL,
  `pid2` varchar(255) NOT NULL,
  `p_name` varchar(255) NOT NULL,
  `p_agetype` varchar(10) NOT NULL,
  `p_age` varchar(255) NOT NULL,
  `p_birthdate` date DEFAULT NULL,
  `p_sex` varchar(255) NOT NULL,
  `p_addresss` text NOT NULL,
  `p_city` varchar(255) NOT NULL,
  `p_telephone` varchar(255) NOT NULL,
  `p_bloodtype` varchar(255) NOT NULL,
  `p_guardiantype` varchar(10) NOT NULL,
  `p_father_husband` varchar(255) NOT NULL,
  `p_insurancetype` varchar(255) NOT NULL,
  `p_insurance_no` varchar(255) DEFAULT ' ',
  `p_lastopd` date DEFAULT '0000-00-00',
  `p_text1` varchar(255) DEFAULT NULL,
  `p_text2` varchar(255) DEFAULT NULL,
  `p_text3` varchar(255) DEFAULT NULL,
  `p_text4` varchar(255) DEFAULT NULL,
  `p_text5` varchar(255) DEFAULT NULL,
  `p_text6` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `price_master`
--

CREATE TABLE IF NOT EXISTS `price_master` (
  `item_code` int(10) NOT NULL AUTO_INCREMENT,
  `item_desc` varchar(255) NOT NULL,
  `item-charges` varchar(255) NOT NULL,
  `item_charges2` varchar(255) NOT NULL DEFAULT '0',
  `item_charges3` varchar(255) NOT NULL DEFAULT '0',
  `item_charges4` varchar(255) NOT NULL DEFAULT '0',
  `item_category` varchar(255) NOT NULL,
  `item_text1` varchar(255) DEFAULT NULL,
  `item_text2` varchar(255) DEFAULT NULL,
  `item-text3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`item_code`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `reception_detail`
--

CREATE TABLE IF NOT EXISTS `reception_detail` (
  `reception_id` int(255) NOT NULL AUTO_INCREMENT,
  `reception_name` varchar(255) NOT NULL,
  `reception_username` varchar(255) NOT NULL,
  `reception_password` varchar(255) NOT NULL,
  `reception_telephone` varchar(255) DEFAULT NULL,
  `reception_address` varchar(255) DEFAULT NULL,
  `reception_qualification` varchar(255) DEFAULT NULL,
  `reception_hindiname` varchar(255) DEFAULT NULL,
  `reception_lastlogin` varchar(255) DEFAULT ' ',
  `reception_active` varchar(255) NOT NULL DEFAULT '1',
  `reception_text1` varchar(255) DEFAULT NULL,
  `reception_text2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`reception_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `report_type`
--

CREATE TABLE IF NOT EXISTS `report_type` (
  `report_type_id` int(200) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(250) NOT NULL,
  PRIMARY KEY (`report_type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `return_department_register`
--

CREATE TABLE IF NOT EXISTS `return_department_register` (
  `return_id` int(250) NOT NULL AUTO_INCREMENT,
  `department_id` varchar(250) NOT NULL,
  `department_name` varchar(250) NOT NULL,
  `person_name` varchar(250) NOT NULL,
  `persone_id` varchar(250) NOT NULL DEFAULT 'non',
  `intent_slip_no` varchar(250) NOT NULL,
  `date` date NOT NULL,
  `time` varchar(250) NOT NULL,
  `item_id` varchar(250) NOT NULL,
  `item_name` varchar(250) NOT NULL,
  `item_desc` varchar(250) NOT NULL,
  `return_qty` varchar(250) NOT NULL,
  `return_reason` varchar(250) NOT NULL,
  `consumable` varchar(250) NOT NULL,
  `return_date` varchar(250) NOT NULL DEFAULT '0000-00-00',
  `item_returned` varchar(250) NOT NULL DEFAULT 'Yes',
  `expiry_date` varchar(250) NOT NULL,
  `return_by` varchar(250) NOT NULL,
  `return_text1` varchar(250) NOT NULL DEFAULT 'null',
  `return_text2` varchar(250) NOT NULL DEFAULT 'null',
  `return_text3` varchar(250) NOT NULL DEFAULT 'null',
  `return_text4` varchar(250) NOT NULL DEFAULT 'null',
  `return_text5` varchar(250) NOT NULL DEFAULT 'null',
  `return_text6` varchar(250) NOT NULL DEFAULT 'null',
  `return_text7` varchar(250) NOT NULL DEFAULT 'null',
  PRIMARY KEY (`return_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `return_invoice_entry`
--

CREATE TABLE IF NOT EXISTS `return_invoice_entry` (
  `return_invoice_id` int(250) NOT NULL AUTO_INCREMENT,
  `return_invoice_id2` varchar(250) NOT NULL,
  `return_invoice_supplier_id` varchar(250) NOT NULL,
  `return_invoice_supplier_name` varchar(250) NOT NULL,
  `return_invoice_date` date NOT NULL,
  `return_invoice_time` varchar(250) NOT NULL,
  `return_invoice_entry_user` varchar(250) NOT NULL,
  `return_invoice_total_amount` varchar(250) NOT NULL,
  `return_invoice_tax` varchar(250) NOT NULL,
  `return_invoice_discount` varchar(250) NOT NULL,
  `return_invoice_final_amount` varchar(250) NOT NULL,
  `return_invoice_text1` varchar(250) NOT NULL DEFAULT 'null',
  `return_invoice_text2` varchar(250) NOT NULL DEFAULT 'null',
  `return_invoice_text3` varchar(250) NOT NULL DEFAULT 'null',
  `return_invoice_text4` varchar(250) NOT NULL DEFAULT 'null',
  `return_invoice_text5` varchar(250) NOT NULL DEFAULT 'null',
  `return_invoice_text6` varchar(250) NOT NULL DEFAULT 'null',
  `return_invoice_text7` varchar(250) NOT NULL DEFAULT 'null',
  PRIMARY KEY (`return_invoice_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `return_invoice_items`
--

CREATE TABLE IF NOT EXISTS `return_invoice_items` (
  `return_invoice_item_id` int(250) NOT NULL AUTO_INCREMENT,
  `item_id` varchar(250) NOT NULL,
  `return_invoice_item_name` varchar(250) NOT NULL,
  `return_invoice_item_desc` varchar(250) NOT NULL,
  `return_invoice_id` int(250) NOT NULL,
  `item_meas_units` varchar(250) NOT NULL,
  `item_qty` varchar(250) NOT NULL,
  `item_unit_price` varchar(250) NOT NULL,
  `item_discount` varchar(250) NOT NULL,
  `item_tax` varchar(250) NOT NULL,
  `return_invoice_value` varchar(250) NOT NULL,
  `item_expiry_date` date NOT NULL,
  `item_date` date NOT NULL,
  `item_time` varchar(250) NOT NULL,
  `item_entry_user` varchar(250) NOT NULL,
  `item_text1` varchar(250) NOT NULL DEFAULT 'null',
  `item_text2` varchar(250) NOT NULL DEFAULT 'null',
  `item_text3` varchar(250) NOT NULL DEFAULT 'null',
  `item_text4` varchar(250) NOT NULL DEFAULT 'null',
  `item_text5` varchar(250) NOT NULL DEFAULT 'null',
  `item_text6` varchar(250) NOT NULL DEFAULT 'null',
  PRIMARY KEY (`return_invoice_item_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `store_detail`
--

CREATE TABLE IF NOT EXISTS `store_detail` (
  `store_id` int(255) NOT NULL AUTO_INCREMENT,
  `store_name` varchar(255) NOT NULL,
  `store_username` varchar(255) NOT NULL,
  `store_password` varchar(255) NOT NULL,
  `store_telephone` varchar(255) DEFAULT NULL,
  `store_address` varchar(255) DEFAULT NULL,
  `store_qualification` varchar(255) DEFAULT NULL,
  `store_hindiname` varchar(255) DEFAULT NULL,
  `store_lastlogin` varchar(255) DEFAULT ' ',
  `store_active` varchar(255) NOT NULL DEFAULT '1',
  `store_text1` varchar(255) DEFAULT NULL,
  `store_text2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`store_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `supplier_detail`
--

CREATE TABLE IF NOT EXISTS `supplier_detail` (
  `supplier_id` int(250) NOT NULL AUTO_INCREMENT,
  `supplier_name` varchar(250) NOT NULL,
  `display_name` varchar(250) NOT NULL,
  `supplier_mobile` varchar(250) NOT NULL,
  `supplier_email` varchar(250) NOT NULL,
  `supplier_street` varchar(250) NOT NULL,
  `supplier_city` varchar(250) NOT NULL,
  `supplier_state` varchar(250) NOT NULL,
  `supplier_zipcode` varchar(250) NOT NULL,
  `supplier_region` varchar(250) NOT NULL,
  `supplier_phone` varchar(250) NOT NULL,
  `supplier_fax` varchar(250) NOT NULL,
  `supplier_fiscal_code` varchar(250) NOT NULL,
  `supplier_regno` varchar(250) NOT NULL,
  `supplier_tax_id` varchar(250) NOT NULL,
  `supplier_manager` varchar(250) NOT NULL,
  `supplier_bank` varchar(250) NOT NULL,
  `supplier_account` varchar(250) NOT NULL,
  `supplier_credits` varchar(250) NOT NULL,
  `supplier_debits` varchar(250) NOT NULL,
  `supplier_rating` varchar(250) NOT NULL,
  `supplier_status` varchar(250) NOT NULL,
  `supplier_date` date NOT NULL,
  `supplier_time` varchar(250) NOT NULL,
  `supplier_entry_user` varchar(250) NOT NULL,
  `supplier_text1` varchar(250) NOT NULL DEFAULT 'null',
  `supplier_text2` varchar(250) NOT NULL DEFAULT 'null',
  `supplier_text3` varchar(250) NOT NULL DEFAULT 'null',
  `supplier_text4` varchar(250) NOT NULL DEFAULT 'null',
  `supplier_text5` varchar(250) NOT NULL DEFAULT 'null',
  `supplier_text6` varchar(250) NOT NULL DEFAULT 'null',
  `supplier_text7` varchar(250) NOT NULL DEFAULT 'null',
  `supplier_text8` varchar(250) NOT NULL DEFAULT 'null',
  `supplier_text9` varchar(250) NOT NULL DEFAULT 'null',
  `supplier_text10` varchar(250) NOT NULL DEFAULT 'null',
  PRIMARY KEY (`supplier_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='supplier detail table';

-- --------------------------------------------------------

--
-- Table structure for table `systemnews`
--

CREATE TABLE IF NOT EXISTS `systemnews` (
  `news` varchar(255) NOT NULL DEFAULT 'Good Day'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `test_operator_detail`
--

CREATE TABLE IF NOT EXISTS `test_operator_detail` (
  `operator_id` int(255) NOT NULL AUTO_INCREMENT,
  `operator_name` varchar(255) NOT NULL,
  `operator_username` varchar(255) NOT NULL,
  `operator_password` varchar(255) NOT NULL,
  `operator_labname` varchar(255) NOT NULL,
  `operator_qualification` varchar(255) DEFAULT NULL,
  `operator_opdroom` varchar(255) NOT NULL,
  `operator_telephone` varchar(255) DEFAULT NULL,
  `operator_address` varchar(255) DEFAULT NULL,
  `operator_hindiname` varchar(255) DEFAULT NULL,
  `operator_lastlogin` varchar(255) DEFAULT ' ',
  `operator_active` varchar(255) NOT NULL DEFAULT '1',
  `operator_text1` varchar(255) DEFAULT NULL,
  `operator_text2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`operator_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `test_output_reference`
--

CREATE TABLE IF NOT EXISTS `test_output_reference` (
  `ref_id` int(200) NOT NULL AUTO_INCREMENT,
  `exam_id` varchar(250) NOT NULL,
  `ref_testname` varchar(250) NOT NULL,
  `ref_testsubname` varchar(250) NOT NULL DEFAULT 'abc',
  `ref_patientsex` varchar(250) NOT NULL,
  `ref_patienttype` varchar(250) NOT NULL,
  `ref_agemin` int(250) NOT NULL,
  `ref_agemax` int(250) NOT NULL,
  `ref_phy_para1` varchar(250) NOT NULL DEFAULT '0',
  `ref_phy_para2` varchar(250) NOT NULL DEFAULT '0',
  `ref_phy_para3` varchar(250) NOT NULL DEFAULT '0',
  `ref_phy_para4` varchar(250) NOT NULL DEFAULT '0',
  `ref_phy_para5` varchar(250) NOT NULL DEFAULT '0',
  `ref_phy_para6` varchar(250) NOT NULL DEFAULT '0',
  `ref_phy_para7` varchar(250) NOT NULL DEFAULT '0',
  `ref_phy_para8` varchar(250) NOT NULL DEFAULT '0',
  `ref_phy_para9` varchar(250) NOT NULL DEFAULT '0',
  `ref_phy_para10` varchar(250) NOT NULL DEFAULT '0',
  `ref_chem_para1` varchar(250) NOT NULL DEFAULT '0',
  `ref_chem_para2` varchar(250) NOT NULL DEFAULT '0',
  `ref_chem_para3` varchar(250) NOT NULL DEFAULT '0',
  `ref_chem_para4` varchar(250) NOT NULL DEFAULT '0',
  `ref_chem_para5` varchar(250) NOT NULL DEFAULT '0',
  `ref_chem_para6` varchar(250) NOT NULL DEFAULT '0',
  `ref_chem_para7` varchar(250) NOT NULL DEFAULT '0',
  `ref_chem_para8` varchar(250) NOT NULL DEFAULT '0',
  `ref_chem_para9` varchar(250) NOT NULL DEFAULT '0',
  `ref_chem_para10` varchar(250) NOT NULL DEFAULT '0',
  `ref_meh_para1` varchar(250) NOT NULL DEFAULT '0',
  `ref_meh_para2` varchar(250) NOT NULL DEFAULT '0',
  `ref_meh_para3` varchar(250) NOT NULL DEFAULT '0',
  `ref_meh_para4` varchar(250) NOT NULL DEFAULT '0',
  `ref_meh_para5` varchar(250) NOT NULL DEFAULT '0',
  `ref_meh_para6` varchar(250) NOT NULL DEFAULT '0',
  `ref_meh_para7` varchar(250) NOT NULL DEFAULT '0',
  `ref_meh_para8` varchar(250) NOT NULL DEFAULT '0',
  `ref_meh_para9` varchar(250) NOT NULL DEFAULT '0',
  `ref_meh_para10` varchar(250) NOT NULL DEFAULT '0',
  `ref_selected` varchar(250) NOT NULL DEFAULT '000',
  `ref_comments` varchar(250) NOT NULL,
  `ref_text1` varchar(250) DEFAULT NULL,
  `ref_text2` varchar(250) DEFAULT NULL,
  `ref_text3` varchar(250) DEFAULT NULL,
  `ref_text4` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`ref_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `test_record_no`
--

CREATE TABLE IF NOT EXISTS `test_record_no` (
  `test_record_counter` int(200) NOT NULL AUTO_INCREMENT,
  `p_id` varchar(250) NOT NULL,
  `report_name` varchar(250) NOT NULL,
  `date_time` varchar(250) NOT NULL,
  PRIMARY KEY (`test_record_counter`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `test_reference`
--

CREATE TABLE IF NOT EXISTS `test_reference` (
  `ref_id` int(200) NOT NULL AUTO_INCREMENT,
  `exam_id` varchar(250) NOT NULL,
  `ref_testname` varchar(250) NOT NULL,
  `ref_testsubname` varchar(250) NOT NULL DEFAULT 'abc',
  `ref_patientsex` varchar(250) NOT NULL,
  `ref_patienttype` varchar(250) NOT NULL,
  `ref_agemin` int(250) NOT NULL,
  `ref_agemax` int(250) NOT NULL,
  `ref_lowerlimit` varchar(250) NOT NULL,
  `ref_upperlimit` varchar(250) NOT NULL,
  `ref_units` varchar(250) NOT NULL,
  `ref_comments` varchar(250) NOT NULL,
  `ref_text1` varchar(250) DEFAULT NULL,
  `ref_text2` varchar(250) DEFAULT NULL,
  `ref_text3` varchar(250) DEFAULT NULL,
  `ref_text4` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`ref_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `test_results`
--

CREATE TABLE IF NOT EXISTS `test_results` (
  `result_id` int(200) NOT NULL AUTO_INCREMENT,
  `exam_counter` varchar(250) NOT NULL,
  `examcat_id` varchar(250) NOT NULL,
  `exam_sub_name` varchar(250) NOT NULL,
  `lower_limit` varchar(250) NOT NULL DEFAULT '0',
  `upper_limit` varchar(250) NOT NULL DEFAULT '0',
  `result_value` varchar(250) NOT NULL DEFAULT '0',
  `comments` varchar(250) NOT NULL DEFAULT '0',
  `phy_parameter1` varchar(250) NOT NULL DEFAULT '0',
  `phy_parameter2` varchar(250) NOT NULL DEFAULT '0',
  `phy_parameter3` varchar(250) NOT NULL DEFAULT '0',
  `phy_parameter4` varchar(250) NOT NULL DEFAULT '0',
  `phy_parameter5` varchar(250) NOT NULL DEFAULT '0',
  `phy_parameter6` varchar(250) NOT NULL DEFAULT '0',
  `phy_parameter7` varchar(250) NOT NULL DEFAULT '0',
  `phy_parameter8` varchar(250) NOT NULL DEFAULT '0',
  `phy_parameter9` varchar(250) NOT NULL DEFAULT '0',
  `phy_parameter10` varchar(250) NOT NULL DEFAULT '0',
  `chem_parameter1` varchar(250) NOT NULL DEFAULT '0',
  `chem_parameter2` varchar(250) NOT NULL DEFAULT '0',
  `chem_parameter3` varchar(250) NOT NULL DEFAULT '0',
  `chem_parameter4` varchar(250) NOT NULL DEFAULT '0',
  `chem_parameter5` varchar(250) NOT NULL DEFAULT '0',
  `chem_parameter6` varchar(250) NOT NULL DEFAULT '0',
  `chem_parameter7` varchar(250) NOT NULL DEFAULT '0',
  `chem_parameter8` varchar(250) NOT NULL DEFAULT '0',
  `chem_parameter9` varchar(250) NOT NULL DEFAULT '0',
  `chem_parameter10` varchar(250) NOT NULL DEFAULT '0',
  `meh_parameter1` varchar(250) NOT NULL DEFAULT '0',
  `meh_parameter2` varchar(250) NOT NULL DEFAULT '0',
  `meh_parameter3` varchar(250) NOT NULL DEFAULT '0',
  `meh_parameter4` varchar(250) NOT NULL DEFAULT '0',
  `meh_parameter5` varchar(250) NOT NULL DEFAULT '0',
  `meh_parameter6` varchar(250) NOT NULL DEFAULT '0',
  `meh_parameter7` varchar(250) NOT NULL DEFAULT '0',
  `meh_parameter8` varchar(250) NOT NULL DEFAULT '0',
  `meh_parameter9` varchar(250) NOT NULL DEFAULT '0',
  `meh_parameter10` varchar(250) NOT NULL DEFAULT '0',
  `results1` varchar(250) NOT NULL DEFAULT '0',
  `results2` varchar(250) NOT NULL DEFAULT '0',
  `result_cat` varchar(250) NOT NULL DEFAULT '000',
  `result_text1` varchar(250) DEFAULT NULL,
  `result_tesx2` varchar(250) DEFAULT NULL,
  `result_text3` varchar(250) DEFAULT NULL,
  `result_text4` varchar(250) DEFAULT NULL,
  `result_text5` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`result_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ward_management`
--

CREATE TABLE IF NOT EXISTS `ward_management` (
  `ward_id` int(255) NOT NULL AUTO_INCREMENT,
  `building_name` varchar(255) NOT NULL,
  `ward_name` varchar(255) NOT NULL,
  `ward_room_no` varchar(255) NOT NULL,
  `bed_no` varchar(255) NOT NULL,
  `ward_category` varchar(255) NOT NULL DEFAULT 'WARD',
  `p_id` varchar(255) NOT NULL DEFAULT 'N/A',
  `p_name` varchar(255) NOT NULL DEFAULT 'N/A',
  `bed_filled` varchar(255) NOT NULL DEFAULT '0',
  `ward_entry_date` date NOT NULL DEFAULT '0000-00-00',
  `ward_incharge` varchar(255) NOT NULL DEFAULT 'N/A',
  `ward_text1` varchar(255) NOT NULL DEFAULT 'N/A',
  `ward_text2` varchar(255) NOT NULL DEFAULT 'N/A',
  `ward_text3` varchar(255) NOT NULL DEFAULT 'N/A',
  `ward_text4` varchar(255) NOT NULL DEFAULT 'N/A',
  `ward_text5` varchar(255) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`ward_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='WARD MANAGEMENT';

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
