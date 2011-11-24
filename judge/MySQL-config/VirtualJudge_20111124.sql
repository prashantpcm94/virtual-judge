-- MySQL dump 10.11
--
-- Host: localhost    Database: vhoj
-- ------------------------------------------------------
-- Server version	5.0.32-Debian_7etch12-log

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
CREATE TABLE `t_contest` (
  `C_ID` int(10) NOT NULL auto_increment,
  `C_TITLE` varchar(100) collate utf8_unicode_ci default NULL,
  `C_DESCRIPTION` text collate utf8_unicode_ci,
  `C_PASSWORD` varchar(40) collate utf8_unicode_ci default NULL,
  `C_BEGINTIME` datetime default NULL,
  `C_ENDTIME` datetime default NULL,
  `C_MANAGER_ID` int(10) default NULL,
  `C_HASH_CODE` varchar(40) collate utf8_unicode_ci default NULL,
  `C_REPLAY_STATUS_ID` int(10) unsigned default NULL,
  `C_ANNOUNCEMENT` text collate utf8_unicode_ci,
  `C_ENABLE_TIME_MACHINE` int(1) unsigned default NULL,
  PRIMARY KEY  (`C_ID`),
  KEY `Index_manager_id` (`C_MANAGER_ID`),
  KEY `Index_hash_code` (`C_HASH_CODE`),
  KEY `Index_replay_status_id` (`C_REPLAY_STATUS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `t_cproblem`
--

DROP TABLE IF EXISTS `t_cproblem`;
CREATE TABLE `t_cproblem` (
  `C_ID` int(10) NOT NULL auto_increment,
  `C_PROBLEM_ID` int(10) default NULL,
  `C_CONTEST_ID` int(10) default NULL,
  `C_NUM` varchar(2) collate utf8_unicode_ci default NULL,
  `C_TITLE` varchar(100) collate utf8_unicode_ci default NULL,
  `C_DESCRIPTION_ID` int(10) unsigned default NULL,
  PRIMARY KEY  (`C_ID`),
  KEY `Index_problem_id` (`C_PROBLEM_ID`),
  KEY `Index_contest_id` (`C_CONTEST_ID`),
  KEY `Index_description_id` (`C_DESCRIPTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `t_description`
--

DROP TABLE IF EXISTS `t_description`;
CREATE TABLE `t_description` (
  `C_ID` int(11) NOT NULL auto_increment,
  `C_DESCRIPTION` text character set utf8 collate utf8_unicode_ci,
  `C_INPUT` text character set utf8 collate utf8_unicode_ci,
  `C_OUTPUT` text character set utf8 collate utf8_unicode_ci,
  `C_SAMPLEINPUT` text character set utf8 collate utf8_unicode_ci,
  `C_SAMPLEOUTPUT` text character set utf8 collate utf8_unicode_ci,
  `C_HINT` text character set utf8 collate utf8_unicode_ci,
  `C_PROBLEM_ID` int(11) NOT NULL default '0',
  `C_UPDATE_TIME` datetime default NULL,
  `C_AUTHOR` varchar(100) default NULL,
  `C_REMARKS` varchar(500) character set utf8 collate utf8_unicode_ci default NULL,
  `C_VOTE` int(10) unsigned default NULL,
  PRIMARY KEY  (`C_ID`),
  KEY `Index_problem_id` (`C_PROBLEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `t_problem`
--

DROP TABLE IF EXISTS `t_problem`;
CREATE TABLE `t_problem` (
  `C_ID` int(10) NOT NULL auto_increment,
  `C_TITLE` varchar(100) collate utf8_unicode_ci default NULL,
  `C_SOURCE` varchar(500) collate utf8_unicode_ci default NULL,
  `C_URL` varchar(500) collate utf8_unicode_ci default NULL,
  `C_originOJ` varchar(40) collate utf8_unicode_ci default NULL,
  `C_originProb` varchar(40) collate utf8_unicode_ci default NULL,
  `C_MEMORYLIMIT` int(10) default NULL,
  `C_TIMELIMIT` int(10) unsigned default NULL,
  `C_TRIGGER_TIME` datetime default NULL,
  PRIMARY KEY  (`C_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `t_replay_status`
--

DROP TABLE IF EXISTS `t_replay_status`;
CREATE TABLE `t_replay_status` (
  `C_ID` int(10) unsigned NOT NULL auto_increment,
  `C_DATA` mediumtext collate utf8_unicode_ci,
  PRIMARY KEY  (`C_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `t_submission`
--

DROP TABLE IF EXISTS `t_submission`;
CREATE TABLE `t_submission` (
  `C_ID` int(10) NOT NULL auto_increment,
  `C_STATUS` varchar(100) collate utf8_unicode_ci default NULL,
  `C_TIME` int(10) unsigned default NULL,
  `C_MEMORY` int(10) unsigned default NULL,
  `C_SUBTIME` datetime default NULL,
  `C_PROBLEM_ID` int(10) default NULL,
  `C_USER_ID` int(10) default NULL,
  `C_CONTEST_ID` int(10) default NULL,
  `C_LANGUAGE` varchar(100) character set utf8 collate utf8_bin default NULL,
  `C_SOURCE` text collate utf8_unicode_ci,
  `C_ISOPEN` int(10) default NULL,
  `C_DISP_LANGUAGE` varchar(100) collate utf8_unicode_ci default NULL,
  `C_USERNAME` varchar(40) collate utf8_unicode_ci default NULL,
  `C_ORIGIN_OJ` varchar(40) collate utf8_unicode_ci default NULL,
  `C_ORIGIN_PROB` varchar(40) collate utf8_unicode_ci default NULL,
  `C_IS_PRIVATE` int(10) unsigned default '0',
  `C_ADDITIONAL_INFO` text collate utf8_unicode_ci,
  `C_REAL_RUNID` varchar(40) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`C_ID`),
  KEY `Index_problem_id` (`C_PROBLEM_ID`),
  KEY `Index_user_id` (`C_USER_ID`),
  KEY `Index_contest_id` (`C_CONTEST_ID`),
  KEY `Index_username` (`C_USERNAME`),
  KEY `Index_origin_prob` (`C_ORIGIN_PROB`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `C_ID` int(10) NOT NULL auto_increment,
  `C_USERNAME` varchar(40) collate utf8_unicode_ci default NULL,
  `C_NICKNAME` varchar(100) collate utf8_unicode_ci default NULL,
  `C_PASSWORD` varchar(40) collate utf8_unicode_ci default NULL,
  `C_CREATETIME` datetime default NULL,
  `C_QQ` varchar(20) collate utf8_unicode_ci NOT NULL,
  `C_SCHOOL` varchar(100) collate utf8_unicode_ci NOT NULL,
  `C_EMAIL` varchar(100) collate utf8_unicode_ci NOT NULL,
  `C_BLOG` varchar(1000) collate utf8_unicode_ci NOT NULL,
  `C_SHARE` int(10) unsigned NOT NULL default '1',
  `C_SUP` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`C_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `t_vlog`
--

DROP TABLE IF EXISTS `t_vlog`;
CREATE TABLE `t_vlog` (
  `C_ID` int(10) unsigned NOT NULL auto_increment,
  `C_SESSIONID` varchar(40) default NULL,
  `C_IP` varchar(40) default NULL,
  `C_CREATETIME` datetime default NULL,
  `C_DURATION` int(10) unsigned default NULL,
  `C_REFERER` varchar(500) default NULL,
  `C_USERAGENT` varchar(500) default NULL,
  `C_LOGINER` int(10) unsigned default NULL,
  PRIMARY KEY  (`C_ID`),
  KEY `Index_2` (`C_SESSIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-11-17 14:47:43
