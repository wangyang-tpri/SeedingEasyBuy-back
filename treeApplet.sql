SET NAMES utf8;
CREATE TABLE IF NOT EXISTS `tree_user`(
    `user_id` INT AUTO_INCREMENT COMMENT '用户id',
    `user_name` VARCHAR(32)  COMMENT '用户名',
    `password` VARCHAR(32)  COMMENT '用户密码', 
    `mobile` VARCHAR(50) COMMENT '手机号码',
    `user_account` VARCHAR(32) COMMENT '用户账号',
    `right_control` VARCHAR(32) COMMENT '用户权限',
    `nick_name` VARCHAR( 100 ) COMMENT '用户昵称',
    `box_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
CREATE TABLE IF NOT EXISTS `tree_detail` (
    `detail_id` INT AUTO_INCREMENT COMMENT '树木id',
    `name` VARCHAR(50) COMMENT '树木名称',
    `age` VARCHAR(20) COMMENT '树龄',
    `address` VARCHAR(100) COMMENT '地址',
    `phone` VARCHAR(50) COMMENT '联系手机号码',
    `image` VARCHAR(100) COMMENT '树的照片',
    `height` VARCHAR(50) COMMENT '树的高度',
    `diameter` VARCHAR(20) COMMENT '树的直径',
    `user_id` INT COMMENT '发现者',
    `box_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '树木发现时间',
    PRIMARY KEY (`detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;