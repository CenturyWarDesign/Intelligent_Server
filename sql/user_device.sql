#用户家中设备表
CREATE  TABLE `intelligent`.`user_device` (
  `arduinoid` INT NULL ,
  `pik` INT NULL ,
  `name` VARCHAR(100) NULL ,
  `value` double NULL ,
  `updatetime` INT NULL ,
  PRIMARY KEY (`arduinoid`,`pik`) );
