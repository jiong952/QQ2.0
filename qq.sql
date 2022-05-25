/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.23 : Database - qq
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`qq` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `qq`;

/*Table structure for table `del_friend` */

DROP TABLE IF EXISTS `del_friend`;

CREATE TABLE `del_friend` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ask_del_id` varchar(255) NOT NULL COMMENT '请求删除的用户id',
  `del_id` varchar(255) NOT NULL COMMENT '被删除的用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Data for the table `del_friend` */

insert  into `del_friend`(`id`,`ask_del_id`,`del_id`) values 
(3,'b','s'),
(4,'a','s');

/*Table structure for table `friend` */

DROP TABLE IF EXISTS `friend`;

CREATE TABLE `friend` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `my_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `friend_id` varchar(255) NOT NULL COMMENT '好友id',
  `is_ask` tinyint NOT NULL COMMENT '发好友申请的人',
  `create_time` date NOT NULL COMMENT '成为好友的时间具体到某一天',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `star` tinyint DEFAULT '0' COMMENT '是否是星标好友',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

/*Data for the table `friend` */

insert  into `friend`(`id`,`my_id`,`friend_id`,`is_ask`,`create_time`,`remark`,`star`) values 
(1,'a','admin',1,'2022-05-11','这是个管理员',0),
(2,'admin','a',0,'2022-05-11','这是好人',1),
(3,'zjh','a',1,'2022-05-10',NULL,NULL),
(4,'a','zjh',0,'2022-05-10','sb',1),
(9,'a','张俊鸿',1,'2022-05-11',NULL,NULL),
(10,'张俊鸿','a',0,'2022-05-11',NULL,NULL),
(26,'ad','a',1,'2022-05-13',NULL,0),
(27,'a','ad',0,'2022-05-13',NULL,0);

/*Table structure for table `message` */

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `msg_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sender_id` varchar(255) NOT NULL COMMENT '发送者id',
  `getter_id` varchar(255) NOT NULL COMMENT '接收者id',
  `group_id` varchar(255) DEFAULT NULL COMMENT '群聊id(用来判断私聊或群聊)',
  `content` varchar(255) NOT NULL COMMENT '可以是普通消息内容，也可以是文',
  `type` int NOT NULL COMMENT '4:普通 8:群发 9:群聊 10:文件 13:好友申请',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名包含后缀',
  `success` tinyint NOT NULL COMMENT '是否发送成功',
  `sender_del` tinyint NOT NULL DEFAULT '0' COMMENT '删除聊天记录 默认0否',
  `getter_del` tinyint NOT NULL DEFAULT '0' COMMENT '删除聊天记录 默认0否',
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

/*Data for the table `message` */

insert  into `message`(`msg_id`,`sender_id`,`getter_id`,`group_id`,`content`,`type`,`send_time`,`file_name`,`success`,`sender_del`,`getter_del`) values 
(3,'a','admin',NULL,'asds',4,'2022-05-12 16:01:11',NULL,1,0,0),
(4,'admin','a',NULL,'dddd',4,'2022-05-12 16:03:11',NULL,1,0,0),
(6,'admin','a',NULL,'你好hhh',4,'2022-05-12 16:03:11',NULL,1,0,0),
(7,'admin','a',NULL,'dassda',4,'2022-05-12 16:03:11',NULL,1,0,0),
(22,'a','admin',NULL,'a',4,'2022-05-12 15:38:51',NULL,1,0,0),
(28,'a','admin',NULL,'hhhhh',8,'2022-05-12 16:51:15',NULL,1,0,0),
(30,'a','张俊鸿',NULL,'hhhhh',18,'2022-05-12 16:51:15',NULL,1,0,0),
(31,'admin','a',NULL,'dhjkahsjkdhasjk',8,'2022-05-12 16:56:47',NULL,1,0,0),
(32,'a','admin',NULL,'ndkalsjdlk',8,'2022-05-12 16:58:08',NULL,1,0,0),
(34,'a','张俊鸿',NULL,'ndkalsjdlk',8,'2022-05-12 16:58:08',NULL,1,0,0),
(35,'a','admin',NULL,'dhajshjk',8,'2022-05-13 01:04:45',NULL,1,0,0),
(37,'a','张俊鸿',NULL,'dhajshjk',8,'2022-05-13 01:04:45',NULL,1,0,0),
(44,'zjh','a',NULL,'jkasdjkals',4,'2022-05-13 02:50:36',NULL,1,0,0),
(45,'zjh','a',NULL,'C:\\Users\\Mono\\Desktop\\用户文件暂存\\a_1652410244056_pic.png',10,'2022-05-13 02:50:44','pic.png',1,0,0),
(46,'zjh','a',NULL,'nmb',4,'2022-05-13 02:50:53',NULL,1,0,0),
(50,'a','admin',NULL,'nihao',8,'2022-05-13 06:13:11',NULL,0,0,0),
(51,'a','zjh',NULL,'nihao',8,'2022-05-13 06:13:11',NULL,1,0,0),
(52,'a','张俊鸿',NULL,'nihao',8,'2022-05-13 06:13:11',NULL,0,0,0),
(53,'zjh','a',NULL,'C:\\Users\\Mono\\Desktop\\用户文件暂存\\a_1652422425862_pic.png',10,'2022-05-13 06:13:46','pic.png',1,0,0),
(54,'a','ad',NULL,'好友申请消息',13,'2022-05-13 06:15:12',NULL,1,0,0);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录账号【主键】',
  `password` varchar(255) NOT NULL COMMENT '登录密码',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称 默认为登录账号',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像存储的路径',
  `gender` tinyint DEFAULT '2' COMMENT '默认2 1男 0女',
  `age` int DEFAULT NULL COMMENT '年龄',
  `phone_number` varchar(255) DEFAULT NULL COMMENT '电话号码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`user_id`,`password`,`user_name`,`avatar`,`gender`,`age`,`phone_number`,`create_time`,`update_time`) values 
('a','a',NULL,NULL,2,NULL,NULL,'2022-05-10 17:40:22','2022-05-10 17:40:22'),
('ad','a',NULL,NULL,2,NULL,NULL,'2022-05-11 17:11:30','2022-05-11 17:11:30'),
('admin','a',NULL,NULL,2,NULL,NULL,'2022-05-10 17:39:51','2022-05-10 17:39:51'),
('bac','a',NULL,NULL,2,NULL,NULL,'2022-05-11 17:11:34','2022-05-11 17:11:34'),
('kad','a',NULL,NULL,2,NULL,NULL,'2022-05-11 17:11:42','2022-05-11 17:11:42'),
('zjh','a',NULL,NULL,2,NULL,NULL,'2022-05-10 17:39:46','2022-05-10 17:39:46'),
('张俊鸿','a',NULL,NULL,2,NULL,NULL,'2022-05-10 17:39:56','2022-05-10 17:39:56');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
