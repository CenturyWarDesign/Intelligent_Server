CREATE  TABLE `intelligent`.`send_log` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `gameuid` INT NULL ,
  `fromgameuid` INT NULL ,
  `behaveString` varchar(50) NULL ,
  `time` int(11) NULL ,
  `sendtime` INT NULL ,
  `status` int default 0 ,
  PRIMARY KEY (`id`) );
