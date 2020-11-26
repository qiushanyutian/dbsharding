/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : order_01

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2020-11-26 19:42:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
