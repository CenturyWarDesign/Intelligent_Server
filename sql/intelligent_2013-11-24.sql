# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.1.44)
# Database: intelligent
# Generation Time: 2013-11-24 07:43:53 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table send_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `send_log`;

CREATE TABLE `send_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gameuid` int(11) DEFAULT NULL,
  `fromgameuid` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `behaveString` varchar(500) DEFAULT '',
  `time` int(11) DEFAULT NULL,
  `sendtime` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `send_log` WRITE;
/*!40000 ALTER TABLE `send_log` DISABLE KEYS */;

INSERT INTO `send_log` (`id`, `gameuid`, `fromgameuid`, `status`, `behaveString`, `time`, `sendtime`)
VALUES
	(1,0,0,0,'10_4_1_2',1385201721,1385201719),
	(2,0,0,0,'10_4_1_2',1385201724,1385201722),
	(3,0,0,0,'10_4_0_2',1385201726,1385201724),
	(4,0,0,0,'10_4_1_2',1385201881,1385201879),
	(5,0,0,0,'10_4_1_2',1385201922,1385201920),
	(6,0,0,0,'10_4_1_2',1385202030,1385202028);

/*!40000 ALTER TABLE `send_log` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_device
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_device`;

CREATE TABLE `user_device` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client` int(11) DEFAULT NULL,
  `prot` int(11) DEFAULT NULL,
  `des` varchar(100) DEFAULT NULL,
  `values` float DEFAULT NULL,
  `updatetime` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `user_device` WRITE;
/*!40000 ALTER TABLE `user_device` DISABLE KEYS */;

INSERT INTO `user_device` (`id`, `client`, `prot`, `des`, `values`, `updatetime`)
VALUES
	(1,5,1,'温度计',95.31,1385202811);

/*!40000 ALTER TABLE `user_device` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `ip` varchar(45) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `client_id` int(11) DEFAULT '0',
  `sec` varchar(32) DEFAULT '',
  `bluetoothmac` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;

INSERT INTO `users` (`id`, `username`, `ip`, `port`, `password`, `client_id`, `sec`, `bluetoothmac`)
VALUES
	(1,'lijincheng','192.168.1.108',123,'7a941492a0dc743544ebc71c89370a62',0,'',NULL),
	(2,'wanbin','192.168.1.108',123,NULL,0,'',NULL),
	(3,'wanbin','192.168.1.108',123,NULL,0,'',NULL),
	(4,'wanbin','192.168.1.108',123,'7a941492a0dc743544ebc71c89370a64',5,'',NULL),
	(5,'wanbin','192.168.1.108',123,'7a941492a0dc743544ebc71c89370a61',0,'',NULL),
	(6,'wanbin','192.168.1.108',123,'e10adc3949ba59abbe56e057f20f883e',5,'PHYGjwrWjF5/9hykUOoYug==',NULL),
	(7,'lijincheng',NULL,2,'b000731e3f138bdfeba6516d4d11c0b1',1,'miWHQYp9mUNg5WMDIJK7HQ==','20:13:09:30:14:48');

/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
