#用户家中设备表
CREATE  TABLE `intelligent`.`user_device` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `client` INT NULL ,
  `prot` INT NULL ,
  `des` VARCHAR(100) NULL ,
  `values` double NULL ,
  `updatetime` INT NULL ,
  PRIMARY KEY (`id`) );
