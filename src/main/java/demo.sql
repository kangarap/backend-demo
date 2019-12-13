/*
 Navicat MySQL Data Transfer

 Source Server         : 192.168.31.1
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : 192.168.31.1
 Source Database       : demo

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : utf-8

 Date: 12/13/2019 13:46:45 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `admin_user`
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `user_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `realname` char(30) NOT NULL DEFAULT '',
  `username` char(30) NOT NULL DEFAULT '',
  `mobile` char(11) NOT NULL DEFAULT '',
  `password` char(32) NOT NULL DEFAULT '',
  `add_time` int(10) unsigned NOT NULL DEFAULT '0',
  `last_login` char(10) NOT NULL DEFAULT '0',
  `last_ip` char(20) NOT NULL DEFAULT '0.0.0.0',
  `role_id` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1',
  `is_delete` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `log`
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `log_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(8) unsigned NOT NULL DEFAULT '0',
  `user_name` char(30) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `log_info` char(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `method` text NOT NULL,
  `params` text NOT NULL,
  `ip` char(20) NOT NULL DEFAULT '0.0.0.0',
  `pub_time` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`log_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
