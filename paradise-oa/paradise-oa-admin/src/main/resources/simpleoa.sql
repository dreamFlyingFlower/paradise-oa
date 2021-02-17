/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : simpleoa

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2021-02-17 17:57:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_apply
-- ----------------------------
DROP TABLE IF EXISTS `tb_apply`;
CREATE TABLE `tb_apply` (
  `apply_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '请假编号',
  `apply_date` date NOT NULL COMMENT '请假申请日期,格式yyyy-MM-dd',
  `starttime` datetime NOT NULL COMMENT '请假开始时间,格式yyyy-MM-dd HH:mm:ss',
  `finishtime` datetime NOT NULL COMMENT '请假结束时间,格式yyyy-MM-dd HH:mm:ss',
  `apply_type` int(4) NOT NULL COMMENT '请假类型,见ts_dict表APPLY_TYPE',
  `reason` varchar(128) NOT NULL COMMENT '请假原因',
  `state` int(4) NOT NULL DEFAULT '1' COMMENT '请假状态:默认1正在请假中;2已销假',
  `user_id` bigint(20) NOT NULL COMMENT '请假人编号',
  `username` varchar(32) NOT NULL COMMENT '请假人姓名',
  `approve_id` bigint(20) NOT NULL COMMENT '批准人用户编号',
  `approve_name` varchar(32) NOT NULL COMMENT '批准人姓名',
  `approve_date` date NOT NULL COMMENT '请假批准日期,格式yyyy-MM-dd',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`apply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假表';

-- ----------------------------
-- Records of tb_apply
-- ----------------------------

-- ----------------------------
-- Table structure for tb_login_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_login_log`;
CREATE TABLE `tb_login_log` (
  `login_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '登录日志编号',
  `user_id` bigint(20) NOT NULL COMMENT '登录用户编号',
  `username` varchar(32) NOT NULL COMMENT '登录用户名',
  `login_ip` varchar(32) NOT NULL COMMENT '登录用户ip',
  `login_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间,格式为yyyy-MM-dd HH:mm:ss',
  `login_place` varchar(128) DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(64) DEFAULT NULL COMMENT '浏览器',
  `os` varchar(64) DEFAULT NULL COMMENT '操作系统',
  `state` int(4) NOT NULL DEFAULT '1' COMMENT '登录状态:0失败;默认1成功',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`login_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户登录日志';

-- ----------------------------
-- Records of tb_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for tb_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_operate_log`;
CREATE TABLE `tb_operate_log` (
  `operate_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '操作编号',
  `title` varchar(32) DEFAULT NULL COMMENT '操作模块',
  `business_type` int(4) DEFAULT '0' COMMENT '业务类型:默认0其他;1新增;2修改;3删除;4授权;5导出;6导入;7清空数据',
  `method` varchar(64) DEFAULT NULL COMMENT '方法名称',
  `request_method` varchar(16) DEFAULT NULL COMMENT '请求方式',
  `operate_type` int(4) DEFAULT '1' COMMENT '操作类别:0其它;默认1后台用户;2手机端用户;见ts_dict表OPERATIOIN_TYPE',
  `operater_id` bigint(20) DEFAULT NULL COMMENT '操作人员编号',
  `operate_name` varchar(32) DEFAULT NULL COMMENT '操作人员姓名',
  `operate_ip` varchar(32) DEFAULT NULL COMMENT '操作ip地址',
  `operate_url` varchar(128) DEFAULT NULL COMMENT '操作url',
  `operate_place` varchar(128) DEFAULT NULL COMMENT '操作地址',
  `operate_param` varchar(500) DEFAULT NULL COMMENT '操作参数',
  `error_msg` varchar(255) DEFAULT NULL COMMENT '错误信息',
  `state` int(4) DEFAULT '1' COMMENT '操作状态:0异常;默认1正常',
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`operate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ----------------------------
-- Records of tb_operate_log
-- ----------------------------

-- ----------------------------
-- Table structure for tb_sign
-- ----------------------------
DROP TABLE IF EXISTS `tb_sign`;
CREATE TABLE `tb_sign` (
  `sign_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '签到编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户编号',
  `starttime` datetime DEFAULT NULL COMMENT '签到开始时间',
  `finishtime` datetime DEFAULT NULL COMMENT '签到结束时间',
  `leave` int(4) DEFAULT '0' COMMENT '是否请假:默认0不是;1是',
  `late` int(4) DEFAULT '0' COMMENT '是否迟到:默认0不是;1是',
  `early` int(4) DEFAULT '0' COMMENT '是否早退:默认0不是;1是',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`sign_id`),
  KEY `INDEX_SIGN_USER_ID` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到表';

-- ----------------------------
-- Records of tb_sign
-- ----------------------------

-- ----------------------------
-- Table structure for tr_role_depart
-- ----------------------------
DROP TABLE IF EXISTS `tr_role_depart`;
CREATE TABLE `tr_role_depart` (
  `role_id` bigint(20) NOT NULL COMMENT '角色编号',
  `depart_id` bigint(20) NOT NULL COMMENT '部门编号',
  PRIMARY KEY (`role_id`,`depart_id`),
  CONSTRAINT `FK_ROLE_DEPART_REF_ROLE` FOREIGN KEY (`role_id`) REFERENCES `ts_role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色部门关联表';

-- ----------------------------
-- Records of tr_role_depart
-- ----------------------------

-- ----------------------------
-- Table structure for tr_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tr_role_menu`;
CREATE TABLE `tr_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色编号',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单编号',
  `permissions` varchar(255) DEFAULT NULL COMMENT '权限,见ts_dict表PERMISSION,多个用逗号隔开',
  `menu_state` int(4) NOT NULL DEFAULT '1' COMMENT '菜单状态:默认1全选,2半选',
  PRIMARY KEY (`role_id`,`menu_id`),
  KEY `FK_ROLE_MENU_REF_MENU` (`menu_id`),
  CONSTRAINT `FK_ROLE_MENU_REF_MENU` FOREIGN KEY (`menu_id`) REFERENCES `ts_menu` (`menu_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ROLE_MENU_REF_ROLE` FOREIGN KEY (`role_id`) REFERENCES `ts_role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单权限表';

-- ----------------------------
-- Records of tr_role_menu
-- ----------------------------
INSERT INTO `tr_role_menu` VALUES ('3', '1', 'ALL', '1');
INSERT INTO `tr_role_menu` VALUES ('3', '2', 'ALL', '1');
INSERT INTO `tr_role_menu` VALUES ('3', '3', 'ALL', '1');

-- ----------------------------
-- Table structure for tr_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tr_user_role`;
CREATE TABLE `tr_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户编号',
  `role_id` bigint(20) NOT NULL COMMENT '角色编号',
  `default_role` int(4) NOT NULL DEFAULT '1' COMMENT '多角色时,每次登陆默认登录的角色,默认1有效',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK_USER_ROLE_ROLE_ID` (`role_id`),
  CONSTRAINT `FK_USER_ROLE_REF_USER` FOREIGN KEY (`user_id`) REFERENCES `ts_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_USER_ROLE_ROLE_ID` FOREIGN KEY (`role_id`) REFERENCES `ts_role` (`role_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色中间表';

-- ----------------------------
-- Records of tr_user_role
-- ----------------------------
INSERT INTO `tr_user_role` VALUES ('1', '1', '0');
INSERT INTO `tr_user_role` VALUES ('2', '3', '1');

-- ----------------------------
-- Table structure for ts_depart
-- ----------------------------
DROP TABLE IF EXISTS `ts_depart`;
CREATE TABLE `ts_depart` (
  `depart_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门编号',
  `depart_name` varchar(64) NOT NULL COMMENT '部门名称',
  `pid` bigint(20) NOT NULL COMMENT '上级部门编号',
  `pname` varchar(64) NOT NULL COMMENT '上级部门名称',
  `depart_desc` varchar(128) DEFAULT NULL COMMENT '部门描述',
  `sort_index` int(11) DEFAULT '1' COMMENT '排序',
  PRIMARY KEY (`depart_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ----------------------------
-- Records of ts_depart
-- ----------------------------
INSERT INTO `ts_depart` VALUES ('1', '所有部门', '0', 'TOP', null, '1');
INSERT INTO `ts_depart` VALUES ('2', '董事长', '1', '所有部门', '董事长办公室', '2');
INSERT INTO `ts_depart` VALUES ('3', '总经理', '2', '董事长', '总经理办公室', '3');
INSERT INTO `ts_depart` VALUES ('4', '副总经理', '3', '总经理', '副总经理办公室', '4');
INSERT INTO `ts_depart` VALUES ('5', '人事部', '4', '副总经理', '人事部', '5');
INSERT INTO `ts_depart` VALUES ('6', '财务部', '4', '副总经理', '财务部', '6');
INSERT INTO `ts_depart` VALUES ('7', '业务部', '4', '副总经理', null, '7');
INSERT INTO `ts_depart` VALUES ('8', '销售部', '4', '副总经理', null, '8');

-- ----------------------------
-- Table structure for ts_dict
-- ----------------------------
DROP TABLE IF EXISTS `ts_dict`;
CREATE TABLE `ts_dict` (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编号',
  `dict_name` varchar(50) NOT NULL COMMENT '字典名',
  `dict_code` varchar(50) NOT NULL COMMENT '字典编码,唯一',
  `dict_val` int(11) DEFAULT '0' COMMENT '字典值',
  `pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '上级字典编号',
  `pname` varchar(50) NOT NULL COMMENT '上级字典名',
  `sort_index` int(11) DEFAULT '1' COMMENT '排序',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `INDEX_DIC_DIC_CODE` (`dict_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COMMENT='系统字典类';

-- ----------------------------
-- Records of ts_dict
-- ----------------------------
INSERT INTO `ts_dict` VALUES ('1', '所有类型', 'TOP', '0', '0', 'None', '1');
INSERT INTO `ts_dict` VALUES ('2', '性别', 'SEX', '0', '1', '所有类型', '2');
INSERT INTO `ts_dict` VALUES ('3', '男', 'SEX1', '0', '2', '性别', '3');
INSERT INTO `ts_dict` VALUES ('4', '女', 'SEX2', '0', '2', '性别', '4');
INSERT INTO `ts_dict` VALUES ('5', '学历', 'EDUCATION', '0', '1', '所有类型', '5');
INSERT INTO `ts_dict` VALUES ('6', '小学', 'EDUCATION1', '0', '5', '学历', '6');
INSERT INTO `ts_dict` VALUES ('7', '初中', 'EDUCATION2', '0', '5', '学历', '7');
INSERT INTO `ts_dict` VALUES ('8', '中专', 'EDUCATION3', '0', '5', '学历', '8');
INSERT INTO `ts_dict` VALUES ('9', '高中', 'EDUCATION4', '0', '5', '学历', '9');
INSERT INTO `ts_dict` VALUES ('10', '大专', 'EDUCATION5', '0', '5', '学历', '10');
INSERT INTO `ts_dict` VALUES ('11', '大学', 'EDUCATION6', '0', '5', '学历', '11');
INSERT INTO `ts_dict` VALUES ('12', '硕士', 'EDUCATION7', '0', '5', '学历', '12');
INSERT INTO `ts_dict` VALUES ('13', '博士', 'EDUCATION8', '0', '5', '学历', '13');
INSERT INTO `ts_dict` VALUES ('14', '权限', 'PERMISSION', '0', '1', '所有类型', '14');
INSERT INTO `ts_dict` VALUES ('15', '所有权限', 'ALL', '0', '14', '权限', '15');
INSERT INTO `ts_dict` VALUES ('16', '新增', 'INSERT', '1', '14', '权限', '16');
INSERT INTO `ts_dict` VALUES ('17', '删除', 'DELETE', '2', '14', '权限', '17');
INSERT INTO `ts_dict` VALUES ('18', '修改', 'UPDATE', '3', '14', '权限', '18');
INSERT INTO `ts_dict` VALUES ('19', '查询', 'SELECT', '4', '14', '权限', '19');
INSERT INTO `ts_dict` VALUES ('20', '导入', 'IMPORT', '5', '14', '权限', '20');
INSERT INTO `ts_dict` VALUES ('21', '导出', 'EXPORT', '6', '14', '权限', '21');

-- ----------------------------
-- Table structure for ts_fileinfo
-- ----------------------------
DROP TABLE IF EXISTS `ts_fileinfo`;
CREATE TABLE `ts_fileinfo` (
  `file_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件编号',
  `local_name` varchar(64) CHARACTER SET utf8mb4 NOT NULL COMMENT '存储在本地的名称,规则是32uuid_yyyyMMdd.文件后缀',
  `original_name` varchar(64) CHARACTER SET utf8mb4 NOT NULL COMMENT '文件本来的名字',
  `file_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '文件类型:默认0其他;1图片;2音频;3视频;4文本;5压缩文件;',
  `file_size` decimal(10,2) DEFAULT NULL COMMENT '文件大小,单位M',
  `file_time` varchar(10) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '音视频文件时长,格式为HH:mm:ss',
  `file_suffix` varchar(10) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文件后缀,不需要点',
  `file_url` varchar(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '文件远程访问地址',
  `createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=sjis COMMENT='文件表';

-- ----------------------------
-- Records of ts_fileinfo
-- ----------------------------
INSERT INTO `ts_fileinfo` VALUES ('11', '1ft8ufjckoh4bop2t8oobeg97u_20180926.jpg', 'test02', '1', '0.26', null, 'jpg', '', '2018-09-26 10:56:33');

-- ----------------------------
-- Table structure for ts_menu
-- ----------------------------
DROP TABLE IF EXISTS `ts_menu`;
CREATE TABLE `ts_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单编号',
  `menu_name` varchar(32) NOT NULL COMMENT '菜单名称',
  `pid` bigint(20) NOT NULL COMMENT '上级菜单编号',
  `pname` varchar(32) NOT NULL COMMENT '上级菜单名称',
  `menu_path` varchar(128) NOT NULL COMMENT '菜单跳转url,可能带参数',
  `menu_view` varchar(128) NOT NULL COMMENT '菜单视图地址',
  `router_name` varchar(32) DEFAULT NULL COMMENT 'vue前端路由所需name属性',
  `menu_icon` varchar(32) DEFAULT '' COMMENT '菜单图标,必填,默认star.svg',
  `menu_i18n` varchar(32) DEFAULT NULL COMMENT '菜单国际化字段,可做唯一标识',
  `redirect` varchar(32) DEFAULT NULL COMMENT '带下级页面的上级页面重定向页面',
  `hidden` int(4) NOT NULL DEFAULT '0' COMMENT '是否隐藏:默认0不隐藏;1隐藏',
  `link` int(4) NOT NULL DEFAULT '0' COMMENT '是否外链:默认0否;1是',
  `sort_index` int(11) DEFAULT '1' COMMENT '排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ----------------------------
-- Records of ts_menu
-- ----------------------------
INSERT INTO `ts_menu` VALUES ('1', '菜单', '0', 'ROOT', '/', '/', null, 'star.svg', 'menu', null, '0', '0', '1');
INSERT INTO `ts_menu` VALUES ('2', '首页', '1', '菜单', '/', '/', null, 'star.svg', 'home', '/home', '0', '0', '2');
INSERT INTO `ts_menu` VALUES ('3', '系统管理', '1', '菜单', '/system', '/system', 'system', 'star.svg', 'sysSetting', '/system/role', '0', '0', '3');
INSERT INTO `ts_menu` VALUES ('4', '角色管理', '3', '系统管理', '/system/role', '/system/role', 'role', 'star.svg', 'roleSetting', null, '0', '0', '4');
INSERT INTO `ts_menu` VALUES ('5', '菜单管理', '3', '系统管理', '/system/menu', '/system/menu', 'menu', 'star.svg', 'menuSetting', null, '0', '0', '6');
INSERT INTO `ts_menu` VALUES ('6', '按钮管理', '3', '系统管理', '/system/button', '/system/button', 'buttons', 'star.svg', 'buttonSetting', null, '0', '0', '7');
INSERT INTO `ts_menu` VALUES ('7', '字典管理', '3', '系统管理', '/system/dic', '/system/dic', 'dic', 'star.svg', 'dicSetting', null, '0', '0', '8');
INSERT INTO `ts_menu` VALUES ('8', '业务管理', '1', '菜单', '/biz', '/biz', 'biz', 'star.svg', 'biz', '/biz/depart', '0', '0', '9');
INSERT INTO `ts_menu` VALUES ('9', '部门管理', '8', '业务管理', '/biz/depart', '/biz/depart', 'depart', 'star.svg', 'departSetting', null, '0', '0', '10');
INSERT INTO `ts_menu` VALUES ('10', '用户管理', '8', '业务管理', '/biz/user', '/biz/user', 'user', 'star.svg', 'userSetting', null, '0', '0', '11');
INSERT INTO `ts_menu` VALUES ('11', '财务管理', '1', '菜单', '/finance', '/finance', 'finance', 'star.svg', 'financeSetting', '/finance/salary', '0', '0', '12');
INSERT INTO `ts_menu` VALUES ('12', '工资管理', '11', '财务管理', '/finance/salary', '/finance/salary', 'salary', 'star.svg', 'salarySetting', '', '0', '0', '13');
INSERT INTO `ts_menu` VALUES ('13', '角色权限设置', '3', '系统管理', '/system/role/assign/:roleId', '/system/role/assign', 'assign', 'star.svg', 'assignSetting', null, '1', '0', '5');
INSERT INTO `ts_menu` VALUES ('14', '仓库管理', '1', '菜单', '/store', '/store', 'store', 'star.svg', '', '', '0', '0', '14');
INSERT INTO `ts_menu` VALUES ('19', '个人信息', '1', '菜单', '/person', '/person', 'person', null, null, '/person/info', '0', '0', '15');
INSERT INTO `ts_menu` VALUES ('20', '基本信息', '19', '个人信息', '/person/info', '/person/info', null, null, null, null, '0', '0', '16');
INSERT INTO `ts_menu` VALUES ('21', '个人申请', '19', '个人信息', '/person/apply', '/person/apply', null, null, null, null, '0', '0', '17');
INSERT INTO `ts_menu` VALUES ('22', '申请审批', '19', '个人信息', '/person/approve', '/person/approve', null, null, null, null, '0', '0', '18');
INSERT INTO `ts_menu` VALUES ('23', '考勤管理', '1', '菜单', '/attendance', '/attendance', 'attendance', 'star.svg', 'attendance', '/attendance/signed', '0', '0', '19');
INSERT INTO `ts_menu` VALUES ('24', '签到管理', '23', '考勤管理', '/attendance/signed', '/attendance/signed', 'signed', 'star.svg', 'signed', null, '0', '0', '20');
INSERT INTO `ts_menu` VALUES ('25', '考勤周报', '23', '考勤管理', '/attendance/week', '/attendance/week', 'week', 'star.svg', 'week', null, '0', '0', '21');
INSERT INTO `ts_menu` VALUES ('26', '考勤月报', '23', '考勤管理', '/attendance/month', '/attendance/month', 'month', 'star.svg', 'month', null, '0', '0', '22');
INSERT INTO `ts_menu` VALUES ('27', '考勤列表', '23', '考勤管理', '/attendance/list', '/attendance/list', 'list', 'star.svg', 'list', null, '0', '0', '23');
INSERT INTO `ts_menu` VALUES ('28', '流程管理', '1', '菜单', '/process', '/process', 'process', 'star.svg', 'process', '/process/add', '0', '0', '24');
INSERT INTO `ts_menu` VALUES ('29', '新建申请', '28', '流程管理', '/process/add', '/process/add', 'add', 'star.svg', 'add', null, '0', '0', '25');
INSERT INTO `ts_menu` VALUES ('30', '我的申请', '28', '流程管理', '/process/user', '/process/user', 'user', 'star.svg', 'user', null, '0', '0', '26');
INSERT INTO `ts_menu` VALUES ('31', '流程审核', '28', '流程管理', '/process/apply', '/process/apply', 'apply', 'star.svg', 'apply', null, '0', '0', '27');
INSERT INTO `ts_menu` VALUES ('32', '任务管理', '1', '菜单', '/task', '/task', 'task', 'star.svg', 'task', '/task/user', '0', '0', '28');
INSERT INTO `ts_menu` VALUES ('33', '我的任务', '32', '任务管理', '/task/user', '/task/user', null, '', null, null, '0', '0', '29');
INSERT INTO `ts_menu` VALUES ('34', '任务设置', '32', '任务管理', '/task/apply', '/task/apply', null, '', null, null, '0', '0', '30');
INSERT INTO `ts_menu` VALUES ('35', '工作计划', '1', '菜单', '/plan', '/plan', null, '', null, '/plan/user', '0', '0', '31');
INSERT INTO `ts_menu` VALUES ('36', '我的计划', '34', '工作计划', '/plan/user', '/plan/user', null, '', null, null, '0', '0', '32');
INSERT INTO `ts_menu` VALUES ('37', '计划管理', '34', '工作计划', '/plan/manager', '/plan/manager', null, '', null, null, '0', '0', '33');
INSERT INTO `ts_menu` VALUES ('38', '通知管理', '8', '业务管理', '/biz/notice', '/biz/notice', null, '', null, null, '0', '0', '34');
INSERT INTO `ts_menu` VALUES ('39', '状态管理', '3', '系统管理', '/system/status', '/system/status', null, '', null, null, '0', '0', '35');
INSERT INTO `ts_menu` VALUES ('40', '类型管理', '3', '系统管理', '/system/type', '/system/type', null, '', null, null, '0', '0', '36');
INSERT INTO `ts_menu` VALUES ('41', '邮件管理', '19', '个人信息', '/person/email', '/person/email', null, '', null, null, '0', '0', '37');
INSERT INTO `ts_menu` VALUES ('42', '日程管理', '19', '个人信息', '/person/schedule', '/person/schedule', null, '', null, null, '0', '0', '38');
INSERT INTO `ts_menu` VALUES ('43', '笔记管理', '19', '个人信息', '/person/notes', '/person/notes', null, '', null, null, '0', '0', '39');
INSERT INTO `ts_menu` VALUES ('44', '文件管理', '1', '菜单', '/fileinfo', '/fileinfo', null, '', null, null, '0', '0', '40');

-- ----------------------------
-- Table structure for ts_notice
-- ----------------------------
DROP TABLE IF EXISTS `ts_notice`;
CREATE TABLE `ts_notice` (
  `notice_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '公告编号',
  `notice_title` varchar(64) NOT NULL COMMENT '公告标题',
  `notice_type` int(4) NOT NULL DEFAULT '1' COMMENT '公告类型:默认1通知,见ts_dict表NOTICE_TYPE',
  `notice_content` varchar(2000) NOT NULL COMMENT '公告内容,不超过2000字',
  `state` int(4) NOT NULL DEFAULT '1' COMMENT '公告状态:0关闭;默认1正常',
  `top` int(4) DEFAULT '0' COMMENT '是否置顶:默认0否;1是',
  `createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '公告时间',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- ----------------------------
-- Records of ts_notice
-- ----------------------------

-- ----------------------------
-- Table structure for ts_role
-- ----------------------------
DROP TABLE IF EXISTS `ts_role`;
CREATE TABLE `ts_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `role_name` varchar(32) NOT NULL COMMENT '角色名',
  `role_code` varchar(32) NOT NULL COMMENT '角色编码',
  `role_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '角色类型:0不可见,只有超级管理员不可见;默认1可见',
  `role_state` int(4) NOT NULL DEFAULT '1' COMMENT '角色状态:0停用;默认1正常;2逻辑删除',
  `role_desc` varchar(128) DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `UNIQUE_ROLE_ROLE_CODE` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of ts_role
-- ----------------------------
INSERT INTO `ts_role` VALUES ('1', '超级管理员', 'SUPER_ADMIN', '0', '1', '超级管理员,只可从数据库添加');
INSERT INTO `ts_role` VALUES ('2', '管理员', 'ADMIN', '1', '1', '管理员已经是可见的最高权限');
INSERT INTO `ts_role` VALUES ('3', '董事长', 'CHAIRMAN', '1', '1', '给个啥权限,不给了');
INSERT INTO `ts_role` VALUES ('4', '总经理', 'GENERAL_MANAGER', '1', '1', '你看着吧');
INSERT INTO `ts_role` VALUES ('5', '副总经理', 'VICE_GENERAL_MANAGER', '1', '1', '你也看着吧');
INSERT INTO `ts_role` VALUES ('6', '财务经理', 'FINANCIAL_MANAGER', '1', '1', '工资算准点嘛,真的');
INSERT INTO `ts_role` VALUES ('7', '人事经理', 'PERSONNEL_MANAGER', '1', '1', '不要扣我的考勤奖');
INSERT INTO `ts_role` VALUES ('8', '业务经理', 'BUSINESS_MANAGER', '1', '1', '我已不做业务很多年');
INSERT INTO `ts_role` VALUES ('9', '后勤经理', 'LOGISTICS_MANAGER', '1', '1', '来点美女吧');
INSERT INTO `ts_role` VALUES ('10', '司法经理', 'JUDICIAL_MANAGER', '1', '1', '坚决维护码农的合法权益');
INSERT INTO `ts_role` VALUES ('11', '质管经理', 'QUALITY_MANAGER', '1', '1', '你倒是来管我呀,啦啦啦');

-- ----------------------------
-- Table structure for ts_salary
-- ----------------------------
DROP TABLE IF EXISTS `ts_salary`;
CREATE TABLE `ts_salary` (
  `salary_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工资编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户编号',
  `username` varchar(32) NOT NULL COMMENT '用户姓名',
  `basic` double(10,2) NOT NULL DEFAULT '0.00' COMMENT '基本工资,默认0',
  `check_id` bigint(20) NOT NULL COMMENT '审核人编号',
  `check_name` varchar(32) NOT NULL COMMENT '审核人姓名',
  `month` varchar(16) NOT NULL COMMENT '工资年月,格式为yyyy-MM',
  `other` double(10,2) DEFAULT '0.00' COMMENT '其他工资,默认0',
  `salary` double(10,2) NOT NULL DEFAULT '0.00' COMMENT '总工资,默认0',
  `craetetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '工资发放时间',
  PRIMARY KEY (`salary_id`),
  KEY `INDEX_SALARY_USER_ID` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工资表';

-- ----------------------------
-- Records of ts_salary
-- ----------------------------

-- ----------------------------
-- Table structure for ts_user
-- ----------------------------
DROP TABLE IF EXISTS `ts_user`;
CREATE TABLE `ts_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码,md5加密',
  `realname` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `depart_id` int(11) DEFAULT NULL COMMENT '部门编号',
  `idcard` varchar(32) DEFAULT NULL COMMENT '身份证号',
  `sex` varchar(4) DEFAULT NULL COMMENT '性别,见ts_dict表SEX',
  `age` int(11) DEFAULT '0' COMMENT '年龄',
  `address` varchar(255) DEFAULT NULL COMMENT '住址',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱地址',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号',
  `state` tinyint(4) NOT NULL DEFAULT '1' COMMENT '用户状态:0黑名单;默认1正常;2锁定;3休假;4离职中;5离职;6逻辑删除',
  `avatar` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`),
  KEY `INDEX_USER_ID_USERNAME` (`user_id`,`username`) USING BTREE,
  KEY `INDEX_CREATETIME` (`createtime`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of ts_user
-- ----------------------------
INSERT INTO `ts_user` VALUES ('1', 'admin', '$2a$10$66qXdDcVFkFCupsl.lcftejV5gXmV5RrRpA1xZMLmHw8EgttfqxWm', '飞花梦影', null, null, 'm', null, null, null, 'fsf@163.com', null, '1', 'user.png', '2018-09-12 16:57:17', '2019-09-17 14:30:07');
INSERT INTO `ts_user` VALUES ('2', 'admin1', '$2a$10$66qXdDcVFkFCupsl.lcftejV5gXmV5RrRpA1xZMLmHw8EgttfqxWm', 'fdsfd', null, null, null, null, null, null, null, null, '1', null, '2019-04-24 14:34:23', '2019-04-24 14:34:23');
INSERT INTO `ts_user` VALUES ('3', 'admin2', '$2a$10$66qXdDcVFkFCupsl.lcftejV5gXmV5RrRpA1xZMLmHw8EgttfqxWm', 'gfet55', null, null, null, null, null, null, null, null, '1', null, '2019-04-24 14:34:33', '2019-04-24 14:56:38');
INSERT INTO `ts_user` VALUES ('4', 'admin3', '$2a$10$66qXdDcVFkFCupsl.lcftejV5gXmV5RrRpA1xZMLmHw8EgttfqxWm', '34bgf', null, null, null, null, null, null, null, null, '1', null, '2019-04-24 14:34:43', '2019-04-24 14:56:41');

-- ----------------------------
-- Table structure for ts_userinfo
-- ----------------------------
DROP TABLE IF EXISTS `ts_userinfo`;
CREATE TABLE `ts_userinfo` (
  `user_id` bigint(20) NOT NULL COMMENT '用户编号',
  `interest` varchar(64) DEFAULT NULL COMMENT '兴趣爱好',
  `education` int(4) DEFAULT NULL COMMENT '学历,见ts_dict表EDUCATION',
  `university` varchar(128) DEFAULT NULL COMMENT '毕业学校',
  `professional` varchar(64) DEFAULT NULL COMMENT '专业',
  `resigndate` date DEFAULT NULL COMMENT '离职时间,格式为yyyy-MM-dd',
  `marry` int(4) DEFAULT NULL COMMENT '婚姻状态,见ts_dict表MARRY',
  `height` int(4) DEFAULT NULL COMMENT '身高,单位cm',
  `weight` double(6,2) DEFAULT NULL COMMENT '体重,单位kg',
  `blood` varchar(10) DEFAULT NULL COMMENT '血型,见ts_dict表BLOOD',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`),
  CONSTRAINT `FK_USER_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `ts_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息扩展表';

-- ----------------------------
-- Records of ts_userinfo
-- ----------------------------
INSERT INTO `ts_userinfo` VALUES ('1', null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Procedure structure for p_getDepartTree
-- ----------------------------
DROP PROCEDURE IF EXISTS `p_getDepartTree`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `p_getDepartTree`(IN `departId` int)
BEGIN
	DECLARE sTemp VARCHAR(2000);
  DECLARE sTempChd VARCHAR(2000);
 
  SET sTemp = '';
  SET sTempChd =cast(departId as CHAR);
 
  WHILE sTempChd is not null DO
    SET sTemp = concat(sTemp,',',sTempChd);
    SELECT group_concat(depart_id) INTO sTempChd FROM ti_depart WHERE FIND_IN_SET(pid,sTempChd)>0;
  END WHILE;
  SELECT sTemp;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for P_getTreeIds
-- ----------------------------
DROP PROCEDURE IF EXISTS `P_getTreeIds`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `P_getTreeIds`(IN `tableName` varchar(30),IN `parentColumn` varchar(30),IN `childColumn` varchar(30),IN `paramId` int)
BEGIN
		DECLARE result VARCHAR(2000);
	 
	  SET result = '';
	  SET @children = CAST(paramId as CHAR);
		SET @sqlStr = concat("SELECT GROUP_CONCAT(",childColumn,") INTO @children FROM ",tableName,
				" WHERE FIND_IN_SET(",parentColumn,",@children)>0;");
		PREPARE stmt from @sqlStr;
		
	  WHILE @children is not null DO
	    SET result = concat(result,',',@children);
	    EXECUTE stmt;
	  END WHILE;
		DEALLOCATE PREPARE stmt;
	  SELECT result;
END
;;
DELIMITER ;
