DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
                         `uid` int(11) NOT NULL AUTO_INCREMENT,
                         `name` varchar(20) DEFAULT  NULL,
                         PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
