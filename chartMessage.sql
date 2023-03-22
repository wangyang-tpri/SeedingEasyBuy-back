SET NAMES utf8;
CREATE TABLE IF NOT EXISTS `register_user`(
    `user_id` INT AUTO_INCREMENT COMMENT '用户id',
    `user_name` VARCHAR(32)  COMMENT '用户名',
    `password` VARCHAR(32)  COMMENT '用户密码', 
    `user_account` VARCHAR(32) COMMENT '用户账号',
    `right_control` VARCHAR(32) COMMENT '用户权限',
    `group_name` VARCHAR(32) COMMENT '用户组名称',
    `box_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `chart_message` (
    `context_id` INT NOT NULL AUTO_INCREMENT COMMENT '发送信息的id',
    `user_name` VARCHAR(32) COMMENT '发送信息者',
    `context` TEXT COMMENT '信息内容',
    `box_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '信息发送时间',
    `title` VARCHAR(32) COMMENT '信息主题',
    PRIMARY KEY (`context_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `in_box` (
    `in_id` INT NOT NULL AUTO_INCREMENT COMMENT '信息接收id',
    `user_name` VARCHAR(32)  COMMENT '信息发送者',
    `context` TEXT COMMENT '信息内容',
    `box_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    `title` VARCHAR(32) COMMENT '信息主题',
    `user_id` INT COMMENT '信息接收者',
    `mode` TINYINT DEFAULT 0 COMMENT '信息的发送方式',
    PRIMARY KEY (`in_id`),
    CONSTRAINT fk_inbox FOREIGN KEY (`user_id`) REFERENCES register_user (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `success_box`(
    `success_id` INT NOT NULL AUTO_INCREMENT COMMENT '信息发送成功id',
    `user_name` VARCHAR(32) COMMENT '信息发送者',
    `context` TEXT COMMENT '信息内容',
    `box_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '失败时间',
    `mode` TINYINT DEFAULT 0 COMMENT '信息的发送方式',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '失败时的状态',
    `title` VARCHAR(32) COMMENT '信息主题',
    `touser` VARCHAR(255) COMMENT '消息接收者的姓名',
    `toparty` VARCHAR(255) COMMENT '消息接收者的部门',
    `totag` VARCHAR(255) COMMENT '消息接收者的标签',
    `safe` BOOLEAN COMMENT '是否安全发送',
    `msgType` VARCHAR(20) COMMENT '消息类型',
    `digest` VARCHAR(255) COMMENT '消息的摘要',
    `thumb_media_id` INT(20) COMMENT '图片的id',
    `show_cover_pic` BOOLEAN COMMENT '是否发送图片',
    PRIMARY KEY (`success_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



CREATE TABLE IF NOT EXISTS `send_box`(
    `send_id` INT NOT NULL AUTO_INCREMENT COMMENT '消息发送id',
    `user_name` VARCHAR(32) COMMENT '消息发送者',
    `recive_name` VARCHAR(255) COMMENT '消息接收者',
    `context` TEXT COMMENT '消息内容',
    `box_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息发送时间',
    `title` VARCHAR(32) COMMENT '消息主题',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '消息发送的状态',
    `mode` TINYINT DEFAULT 0 COMMENT '信息的发送方式',
    `user_id` INT COMMENT '发送消息的用户id',
    `image_path` VARCHAR(50) COMMENT '图片的存储路径',
    `touser` VARCHAR(255) COMMENT '消息接收者的姓名',
    `toparty` VARCHAR(255) COMMENT '消息接收者的部门',
    `totag` VARCHAR(255) COMMENT '消息接收者的标签',
    `safe` BOOLEAN COMMENT '是否安全发送',
    `msgType` VARCHAR(20) COMMENT '消息类型',
    `digest` VARCHAR(255) COMMENT '消息的摘要',
    `thumb_media_id` INT(20) COMMENT '图片的id',
    `show_cover_pic` BOOLEAN COMMENT '是否发送图片',
    PRIMARY KEY (`send_id`),
    `target_url` VARCHAR(255) COMMENT '用户浏览的url',
    CONSTRAINT fk_id FOREIGN KEY (`user_id`) REFERENCES register_user (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `fail_box` (
    `fail_id` INT NOT NULL AUTO_INCREMENT COMMENT '信息发送失败id',
    `user_name` VARCHAR(32)  COMMENT '信息发送者',
    `recive_name` VARCHAR(255) COMMENT '消息接收者',
    `context` TEXT COMMENT '信息内容',
    `box_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '失败时间',
    `mode` TINYINT DEFAULT 0 COMMENT '信息的发送方式',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '失败时的状态',
    `title` VARCHAR(32) COMMENT '信息主题',
    `touser` VARCHAR(255) COMMENT '消息接收者的姓名',
    `toparty` VARCHAR(255) COMMENT '消息接收者的部门',
    `totag` VARCHAR(255) COMMENT '消息接收者的标签',
    `safe` BOOLEAN COMMENT '是否安全发送',
    `msgType` VARCHAR(20) COMMENT '消息类型',
    `digest` VARCHAR(255) COMMENT '消息的摘要',
    `thumb_media_id` INT(20) COMMENT '图片的id',
    `show_cover_pic` BOOLEAN COMMENT '是否发送图片',
    PRIMARY KEY (`fail_id`) 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `login_user` (
    `user_id` INT NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `user_name` VARCHAR(32) NOT NULL COMMENT '用户名',
    `user_password` VARCHAR(32) NOT NULL COMMENT '用户密码',
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `user_group` (
    `group_id` INT NOT NULL AUTO_INCREMENT COMMENT '用户组id',
    `group_name` VARCHAR(32) NOT NULL DEFAULT 'TRPI' COMMENT '用户组名称',
    `group_desc` VARCHAR(32) NOT NULL DEFAULT 'TPRI' COMMENT '用户组描述',
    `group_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户组创建时间',
    `group_users` VARCHAR(255) COMMENT '用户组包含的用户',
    `user_id` INT COMMENT '创建用户组时的用户的id',
    PRIMARY KEY(`group_id`),
    CONSTRAINT fk_name FOREIGN KEY (`user_id`) REFERENCES register_user (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `recycle_bin`(
    `recycle_id` INT NOT NULL AUTO_INCREMENT COMMENT '信息回收id',
    `recycle_name` VARCHAR(32) NOT NULL DEFAULT 'recycle' COMMENT '信息名称',
    `box_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '信息回收时间',
    `user_name` VARCHAR(32) NOT NULL DEFAULT 'recycle' COMMENT '发送信息的用户',
    `context` TEXT COMMENT '信息中的内容',
    `title` VARCHAR(32) COMMENT '已删除信息的主题',
    PRIMARY KEY (`recycle_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `accept_user`(
    `accept_id` INT NOT NULL AUTO_INCREMENT COMMENT '接收信息的用户id',
    `accept_name` VARCHAR(32) COMMENT '信息接收者姓名',
    `accept_tel` CHAR(11) COMMENT '接收信息的用户手机号',
    `accept_wx` VARCHAR(32) COMMENT '接收信息的用户的微信号',
    `accept_qq` VARCHAR(20) COMMENT '接收信息的用户的qq号',
    `user_id` INT COMMENT '信息发送者',
    PRIMARY KEY(`accept_id`), 
    CONSTRAINT fk_idid FOREIGN KEY (`user_id`) REFERENCES register_user (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `chart_token` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'TOKEN的id',
    `startTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '首次获取token的时间',
    `token` VARCHAR(1024) NOT NULL COMMENT 'TOKEN的内容',
    `name` VARCHAR(25) NOT NULL COMMENT '获取到的不同的token的名称',
    PRIMARY KEY (`id`)
 ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `chart_department_list` (
    `list_id` INT NOT NULL AUTO_INCREMENT COMMENT '部门列表的id',
    `list_name` VARCHAR(32) COMMENT '部门列表的名称',
    `list_parentid` INT NOT NULL COMMENT '部门列表的上级部门',
    `list_order` INT NOT NULL COMMENT '部门列表的顺序',
    PRIMARY KEY (`list_id`)
 ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `employee` (
    `emp_id` INT NOT NULL AUTO_INCREMENT COMMENT '员工的id',
    `name` VARCHAR(32) COMMENT '员工姓名',
    `tel` CHAR(11) COMMENT '员工的手机号',
    `wx` VARCHAR(32) COMMENT '员工的微信号',
    `qq` VARCHAR(20) COMMENT '员工的qq号',
    `list_id` INT NOT NULL,
    PRIMARY KEY (`emp_id`),
    CONSTRAINT fk_iid FOREIGN KEY (`list_id`) REFERENCES chart_department_list (`list_id`) ON DELETE CASCADE ON UPDATE CASCADE
 ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `tag_list` (
    `tag_id` INT NOT NULL AUTO_INCREMENT COMMENT '标签id',
    `tag_name` VARCHAR(32) COMMENT '标签名称',
    PRIMARY KEY (`tag_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `tag_member` (
    `member_id` INT NOT NULL AUTO_INCREMENT COMMENT '标签数据id',
    `user_id` VARCHAR(32)  COMMENT '标签中的成员id',
    `party_id` INT COMMENT '标签中的部门id',
    `user_name` VARCHAR(32) COMMENT '成员名称',
    `tag_id` INT NOT NULL,
    `isEmployee` TINYINT COMMNET '用来区分部门和用户的',
    PRIMARY KEY (`member_id`),
    CONSTRAINT fk_tag FOREIGN KEY (`tag_id`) REFERENCES tag_list (`tag_id`) ON DELETE CASCADE ON UPDATE CASCADE
    
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `thumb_image` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '上传的图片id',
    `image_name` VARCHAR(100) COMMENT '图片的名称',
    `thumb_media_id` VARCHAR(200) COMMENTT '图片的缩络图id',
    PRIMARY KEY  (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `msg_template` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '模板id',
    `tem_name` VARCHAR(200) COMMENT '模板名称',
    `tem_user` VARCHAR(200) COMMENT '模板发送的用户',
    `tem_title` VARCHAR(32) COMMENT '模板的主题',
    `tem_digest` VARCHAR(255) COMMENT '模板的摘要',
    `tem_url` VARCHAR(255) COMMENT '外部连接',
    `tem_content` TEXT COMMENT '模板的内容',
    `receiver_id` VARCHAR(255) COMMENT '接收信息的用户id',
    `touser` VARCHAR(255) COMMENT '接收信息的用户',
    `toparty` VARCHAR(255) COMMENT '接收信息的部门',
    `totag` VARCHAR(255) COMMENT '接收信息的标签', 
    `user_id` INT COMMENT '发送消息的用户id',
    PRIMARY KEY (`id`),
    CONSTRAINT fk_tem FOREIGN key (`user_id`) REFERENCES register_user (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `delete_box`(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '信息发送成功id',
    `recive_name` VARCHAR(32) COMMENT '信息发送者',
    `context` TEXT COMMENT '信息内容',
    `box_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '失败时间',
    `mode` TINYINT DEFAULT 0 COMMENT '信息的发送方式',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '失败时的状态',
    `title` VARCHAR(32) COMMENT '信息主题',
    `touser` VARCHAR(255) COMMENT '消息接收者的姓名',
    `toparty` VARCHAR(255) COMMENT '消息接收者的部门',
    `totag` VARCHAR(255) COMMENT '消息接收者的标签',
    `safe` BOOLEAN COMMENT '是否安全发送',
    `msgType` VARCHAR(20) COMMENT '消息类型',
    `digest` VARCHAR(255) COMMENT '消息的摘要',
    `thumb_media_id` INT(20) COMMENT '图片的id',
    `show_cover_pic` BOOLEAN COMMENT '是否发送图片',
    PRIMARY KEY (`id`)    
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;
CREATE TABLE IF NOT EXISTS `key_word`(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT "关键词的id",
    `words` VARCHAR(50) NOT NULL COMMENT '关键词',  
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8;