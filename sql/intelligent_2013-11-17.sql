# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.1.44)
# Database: intelligent
# Generation Time: 2013-11-17 03:06:56 +0000
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
  `behaveString` varchar(50) DEFAULT '',
  `time` int(11) DEFAULT NULL,
  `sendtime` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
	(1,5,1,'温度计',24.93,1384613302);

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
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;

INSERT INTO `users` (`id`, `username`, `ip`, `port`, `password`, `client_id`)
VALUES
	(1,'wanbin','192.168.1.108',123,'7a941492a0dc743544ebc71c89370a62',0),
	(2,'wanbin','192.168.1.108',123,NULL,0),
	(3,'wanbin','192.168.1.108',123,NULL,0),
	(4,'wanbin','192.168.1.108',123,NULL,0),
	(5,'wanbin','192.168.1.108',123,'7a941492a0dc743544ebc71c89370a61',0),
	(6,'wanbin','192.168.1.108',123,'7a941492a0dc743544ebc71c89370a64',5);

/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
