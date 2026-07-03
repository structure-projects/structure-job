-- ============================================
-- 升级说明：从 V1.0.0__INIT.sql 升级到 XXL-JOB 官方 tables_xxl_job.sql 结构
-- 此脚本假设数据库当前为 V1.0.0 结构，执行一次完成升级
-- ============================================

-- ---------------------- xxl_job_group ----------------------
-- title varchar(12) → varchar(64)
-- address_list varchar(512) → text
-- 新增 update_time

ALTER TABLE `xxl_job_group` MODIFY COLUMN `title` varchar(64) NOT NULL COMMENT '执行器名称';
ALTER TABLE `xxl_job_group` MODIFY COLUMN `address_list` text COMMENT '执行器地址列表，多地址逗号分隔';
ALTER TABLE `xxl_job_group` ADD COLUMN `update_time` datetime DEFAULT NULL COMMENT '更新时间';

-- 更新已有数据
UPDATE `xxl_job_group` SET `title` = '通用执行器Sample', `update_time` = NOW() WHERE `id` = 1;

-- ---------------------- xxl_job_registry ----------------------
-- id int(11) → bigint(20)
-- 索引 i_g_k_v: 普通索引 → 唯一索引

ALTER TABLE `xxl_job_registry` MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE `xxl_job_registry` DROP INDEX `i_g_k_v`;
ALTER TABLE `xxl_job_registry` ADD UNIQUE KEY `i_g_k_v` (`registry_group`, `registry_key`, `registry_value`) USING BTREE;

-- ---------------------- xxl_job_info ----------------------
-- 新增 schedule_type, schedule_conf, misfire_strategy
-- executor_param varchar(512) → text
-- job_cron 数据迁移到 schedule_conf 后删除 job_cron

ALTER TABLE `xxl_job_info` ADD COLUMN `schedule_type` varchar(50) NOT NULL DEFAULT 'CRON' COMMENT '调度类型';
ALTER TABLE `xxl_job_info` ADD COLUMN `schedule_conf` varchar(128) DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型';
ALTER TABLE `xxl_job_info` ADD COLUMN `misfire_strategy` varchar(50) NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略';
ALTER TABLE `xxl_job_info` MODIFY COLUMN `executor_param` text COMMENT '任务参数';

-- 将 job_cron 数据迁移到 schedule_conf
UPDATE `xxl_job_info` SET `schedule_conf` = `job_cron`, `schedule_type` = 'CRON', `misfire_strategy` = 'DO_NOTHING';

-- 数据迁移完成后删除 job_cron 列
ALTER TABLE `xxl_job_info` DROP COLUMN `job_cron`;

-- 更新已有任务描述
UPDATE `xxl_job_info` SET `job_desc` = '示例任务01', `update_time` = NOW() WHERE `id` = 1;

-- ---------------------- xxl_job_log ----------------------
-- executor_param varchar(512) → text
-- 新增索引 I_jobgroup, I_jobid

ALTER TABLE `xxl_job_log` MODIFY COLUMN `executor_param` text COMMENT '任务参数';
ALTER TABLE `xxl_job_log` ADD INDEX `I_jobgroup` (`job_group`);
ALTER TABLE `xxl_job_log` ADD INDEX `I_jobid` (`job_id`);

-- ---------------------- xxl_job_log_report ----------------------
-- 新增 update_time

ALTER TABLE `xxl_job_log_report` ADD COLUMN `update_time` datetime DEFAULT NULL COMMENT '更新时间';

-- ---------------------- xxl_job_user ----------------------
-- password varchar(50) → varchar(100)
-- 新增 token
-- 密码从 MD5 升级为 SHA256

ALTER TABLE `xxl_job_user` MODIFY COLUMN `password` varchar(100) NOT NULL COMMENT '密码加密信息';
ALTER TABLE `xxl_job_user` ADD COLUMN `token` varchar(100) DEFAULT NULL COMMENT '登录token';

UPDATE `xxl_job_user` SET `password` = '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92' WHERE `username` = 'admin';
