CREATE TABLE IF NOT EXISTS `account` (
  `id` varchar(255) NOT NULL,
  `created_date` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_email` (`email`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
