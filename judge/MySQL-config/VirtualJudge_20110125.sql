-- MySQL dump 10.13  Distrib 5.1.41, for Win32 (ia32)
--
-- Host: localhost    Database: vhoj
-- ------------------------------------------------------
-- Server version	5.1.41-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_contest`
--

DROP TABLE IF EXISTS `t_contest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_contest` (
  `C_ID` int(10) NOT NULL AUTO_INCREMENT,
  `C_TITLE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_DESCRIPTION` text COLLATE utf8_unicode_ci,
  `C_PASSWORD` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_BEGINTIME` datetime DEFAULT NULL,
  `C_ENDTIME` datetime DEFAULT NULL,
  `C_MANAGER_ID` int(10) DEFAULT NULL,
  `C_HASH_CODE` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`C_ID`),
  KEY `Index_manager_id` (`C_MANAGER_ID`),
  KEY `Index_hash_code` (`C_HASH_CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_cproblem`
--

DROP TABLE IF EXISTS `t_cproblem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_cproblem` (
  `C_ID` int(10) NOT NULL AUTO_INCREMENT,
  `C_PROBLEM_ID` int(10) DEFAULT NULL,
  `C_CONTEST_ID` int(10) DEFAULT NULL,
  `C_NUM` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_TITLE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`C_ID`),
  KEY `Index_problem_id` (`C_PROBLEM_ID`),
  KEY `Index_contest_id` (`C_CONTEST_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_description`
--

DROP TABLE IF EXISTS `t_description`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_description` (
  `C_ID` int(11) NOT NULL AUTO_INCREMENT,
  `C_DESCRIPTION` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `C_INPUT` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `C_OUTPUT` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `C_SAMPLEINPUT` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `C_SAMPLEOUTPUT` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `C_HINT` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `C_PROBLEM_ID` int(11) NOT NULL DEFAULT '0',
  `C_UPDATE_TIME` datetime DEFAULT NULL,
  `C_AUTHOR` varchar(100) DEFAULT NULL,
  `C_REMARKS` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_VOTE` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`C_ID`),
  KEY `Index_problem_id` (`C_PROBLEM_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_problem`
--

DROP TABLE IF EXISTS `t_problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_problem` (
  `C_ID` int(10) NOT NULL AUTO_INCREMENT,
  `C_TITLE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_SOURCE` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_URL` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_originOJ` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_originProb` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_MEMORYLIMIT` int(10) DEFAULT NULL,
  `C_TIMELIMIT` int(10) unsigned DEFAULT NULL,
  `C_TRIGGER_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`C_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_submission`
--

DROP TABLE IF EXISTS `t_submission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_submission` (
  `C_ID` int(10) NOT NULL AUTO_INCREMENT,
  `C_STATUS` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_TIME` int(10) unsigned DEFAULT NULL,
  `C_MEMORY` int(10) unsigned DEFAULT NULL,
  `C_SUBTIME` datetime DEFAULT NULL,
  `C_PROBLEM_ID` int(10) DEFAULT NULL,
  `C_USER_ID` int(10) DEFAULT NULL,
  `C_CONTEST_ID` int(10) DEFAULT NULL,
  `C_LANGUAGE` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `C_SOURCE` text COLLATE utf8_unicode_ci,
  `C_ISOPEN` int(10) DEFAULT NULL,
  `C_DISP_LANGUAGE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_USERNAME` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_ORIGIN_OJ` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_ORIGIN_PROB` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_IS_PRIVATE` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`C_ID`),
  KEY `Index_problem_id` (`C_PROBLEM_ID`),
  KEY `Index_user_id` (`C_USER_ID`),
  KEY `Index_contest_id` (`C_CONTEST_ID`),
  KEY `Index_username` (`C_USERNAME`),
  KEY `Index_origin_OJ` (`C_ORIGIN_OJ`),
  KEY `Index_origin_prob` (`C_ORIGIN_PROB`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
  `C_ID` int(10) NOT NULL AUTO_INCREMENT,
  `C_USERNAME` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_NICKNAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_PASSWORD` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_CREATETIME` datetime DEFAULT NULL,
  `C_QQ` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `C_SCHOOL` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `C_EMAIL` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `C_BLOG` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `C_SHARE` int(10) unsigned NOT NULL DEFAULT '1',
  `C_SUP` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`C_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_vlog`
--

DROP TABLE IF EXISTS `t_vlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_vlog` (
  `C_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `C_SESSIONID` varchar(40) DEFAULT NULL,
  `C_IP` varchar(40) DEFAULT NULL,
  `C_CREATETIME` datetime DEFAULT NULL,
  `C_DURATION` int(10) unsigned DEFAULT NULL,
  `C_REFERER` varchar(500) DEFAULT NULL,
  `C_USERAGENT` varchar(500) DEFAULT NULL,
  `C_LOGINER` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`C_ID`),
  KEY `Index_2` (`C_SESSIONID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-01-16  3:17:10
