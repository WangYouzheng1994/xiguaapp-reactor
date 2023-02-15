/*
 *     Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2022/10/01 下午5:32 >
 *
 *     Send:1125698980@qq.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 Navicat Premium Data Transfer

 Source Server         : mysqlip
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : mysqlip:3306
 Source Schema         : msg

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 28/10/2020 11:41:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for notify_config
-- ----------------------------
DROP TABLE IF EXISTS `notify_config`;
CREATE TABLE `notify_config` (
  `id` bigint NOT NULL COMMENT 'id',
  `name` varchar(100) DEFAULT NULL COMMENT '配置名称',
  `type` varchar(100) DEFAULT NULL COMMENT '通知类型',
  `provider` varchar(255) DEFAULT NULL COMMENT '服务提供商',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `configuration` varchar(255) DEFAULT NULL COMMENT '配置信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_del` tinyint(1) DEFAULT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of notify_config
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
