-- src/main/resources/schema.sql

-- 如果表已存在，则先删除 (注意 "USER" 加了双引号)
DROP TABLE IF EXISTS "USER";

-- 创建 USER 表 (注意 "USER" 加了双引号)
CREATE TABLE "USER" (
                        ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                        NAME VARCHAR(50),
                        ACCOUNT_ID VARCHAR(100),
                        TOKEN CHAR(36),
                        GMT_CREATE BIGINT,
                        GMT_MODIFIED BIGINT
);