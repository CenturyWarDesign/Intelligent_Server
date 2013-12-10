#arduino连接表
CREATE  TABLE `intelligent`.`arduino_conn_log` (
  `arduinoid` INT NULL  ,
  `conntime` INT(11) NULL ,
  `up` int(10) NULL default 0,
  `down` int(10) NULL default 0,
  `updatetime` int(11) NULL ,
  PRIMARY KEY (`arduinoid`,`conntime`) );
