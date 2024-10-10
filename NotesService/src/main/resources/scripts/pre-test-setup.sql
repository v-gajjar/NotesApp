DROP TABLE IF EXISTS `note`;
CREATE TABLE `note` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`id`)
)


