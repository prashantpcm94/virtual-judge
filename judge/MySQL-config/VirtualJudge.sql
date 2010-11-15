-- MySQL dump 10.11
--
-- Host: localhost    Database: vhoj
-- ------------------------------------------------------
-- Server version	5.0.32-Debian_7etch8-log

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
-- Table structure for table `T_CONTEST`
--

DROP TABLE IF EXISTS `T_CONTEST`;
CREATE TABLE `T_CONTEST` (
  `C_ID` int(10) NOT NULL auto_increment,
  `C_TITLE` varchar(100) collate utf8_unicode_ci default NULL,
  `C_DESCRIPTION` text collate utf8_unicode_ci,
  `C_PASSWORD` varchar(40) collate utf8_unicode_ci default NULL,
  `C_BEGINTIME` datetime default NULL,
  `C_ENDTIME` datetime default NULL,
  `C_MANAGERID` int(10) default NULL,
  PRIMARY KEY  (`C_ID`),
  KEY `C_MANAGERID` (`C_MANAGERID`),
  CONSTRAINT `C_MANAGERID` FOREIGN KEY (`C_MANAGERID`) REFERENCES `T_USER` (`C_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `T_CPROBLEM`
--

DROP TABLE IF EXISTS `T_CPROBLEM`;
CREATE TABLE `T_CPROBLEM` (
  `C_ID` int(10) NOT NULL auto_increment,
  `C_PROBLEMID` int(10) default NULL,
  `C_CONTESTID` int(10) default NULL,
  `C_NUM` varchar(2) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`C_ID`),
  KEY `C_PROBLEMID` (`C_PROBLEMID`),
  KEY `C_CONTESTID` (`C_CONTESTID`),
  CONSTRAINT `C_CONTESTID` FOREIGN KEY (`C_CONTESTID`) REFERENCES `T_CONTEST` (`C_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `C_PROBLEMID` FOREIGN KEY (`C_PROBLEMID`) REFERENCES `T_PROBLEM` (`C_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `T_PROBLEM`
--

DROP TABLE IF EXISTS `T_PROBLEM`;
CREATE TABLE `T_PROBLEM` (
  `C_ID` int(10) NOT NULL auto_increment,
  `C_TITLE` varchar(100) collate utf8_unicode_ci default NULL,
  `C_DESCRIPTION` text collate utf8_unicode_ci,
  `C_INPUT` text collate utf8_unicode_ci,
  `C_OUTPUT` text collate utf8_unicode_ci,
  `C_SAMPLEINPUT` text collate utf8_unicode_ci,
  `C_SAMPLEOUTPUT` text collate utf8_unicode_ci,
  `C_HINT` text collate utf8_unicode_ci,
  `C_SOURCE` varchar(500) collate utf8_unicode_ci default NULL,
  `C_URL` varchar(500) collate utf8_unicode_ci default NULL,
  `C_originOJ` varchar(40) collate utf8_unicode_ci default NULL,
  `C_originProb` varchar(40) collate utf8_unicode_ci default NULL,
  `C_addTime` datetime default NULL,
  `C_MEMORYLIMIT` int(10) default NULL,
  `C_TIMELIMIT` int(10) unsigned default NULL,
  `C_CREATORID` int(10) unsigned default NULL,
  `C_HIDDEN` int(10) unsigned default NULL,
  PRIMARY KEY  (`C_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `T_SUBMISSION`
--

DROP TABLE IF EXISTS `T_SUBMISSION`;
CREATE TABLE `T_SUBMISSION` (
  `C_ID` int(10) NOT NULL auto_increment,
  `C_STATUS` varchar(40) collate utf8_unicode_ci default NULL,
  `C_TIME` int(10) unsigned default NULL,
  `C_MEMORY` int(10) unsigned default NULL,
  `C_SUBTIME` datetime default NULL,
  `C_PROBLEMID` int(10) default NULL,
  `C_USERID` int(10) default NULL,
  `C_CONTESTID` int(10) default NULL,
  `C_LANGUAGE` varchar(100) character set utf8 collate utf8_bin default NULL,
  `C_SOURCE` text collate utf8_unicode_ci,
  `C_ISOPEN` int(10) default NULL,
  `C_DISP_LANGUAGE` varchar(100) collate utf8_unicode_ci default NULL,
  `C_USERNAME` varchar(40) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`C_ID`),
  KEY `C_PROBLEMID2` (`C_PROBLEMID`),
  KEY `C_USERID` (`C_USERID`),
  KEY `C_CPROBLEMID` (`C_CONTESTID`),
  CONSTRAINT `C_PROBLEMID2` FOREIGN KEY (`C_PROBLEMID`) REFERENCES `T_PROBLEM` (`C_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `C_USERID` FOREIGN KEY (`C_USERID`) REFERENCES `T_USER` (`C_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `T_USER`
--

DROP TABLE IF EXISTS `T_USER`;
CREATE TABLE `T_USER` (
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
-- Table structure for table `T_VLOG`
--

DROP TABLE IF EXISTS `T_VLOG`;
CREATE TABLE `T_VLOG` (
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

-- Dump completed on 2010-11-15 13:27:43
