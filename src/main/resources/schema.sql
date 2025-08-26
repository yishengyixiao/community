-- src/main/resources/schema.sql
DROP TABLE IF EXISTS "USER";

CREATE TABLE "USER" (
                        ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                        NAME VARCHAR(50),
                        ACCOUNT_ID VARCHAR(100),
                        TOKEN CHAR(36),
                        GMT_CREATE BIGINT,
                        GMT_MODIFIED BIGINT,
                        AVATAR_URL VARCHAR(256),
    -- 新增对应的列
                        BIO VARCHAR(256),
                        PUBLIC_REPOS INT,
                        FOLLOWERS INT,
                        FOLLOWING INT,
                        HTML_URL VARCHAR(256)
);