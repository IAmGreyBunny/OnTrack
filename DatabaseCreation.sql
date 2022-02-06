CREATE DATABASE ontrack;
USE ontrack;

CREATE TABLE `Rounds`(
    `ruleId` INT UNSIGNED NOT NULL,
    `roundNumber` INT NOT NULL,
    `roundInterval` INT NOT NULL
);
CREATE TABLE `RepetitionRules`(
    `ruleId` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `ruleName` VARCHAR(255) NOT NULL,
    `repeatType` VARCHAR(255) NOT NULL,
    `userId` INT UNSIGNED NOT NULL
);
CREATE TABLE `Lessons`(
    `lessonId` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `userId` INT UNSIGNED NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT NULL,
    `venue` VARCHAR(255) NULL,
    `repetitionRuleId` INT UNSIGNED NULL,
    `round` INT NOT NULL,
    `status` TINYINT(1) NOT NULL
);
ALTER TABLE
    `Lessons` ADD UNIQUE `lessons_name_unique`(`name`);
CREATE TABLE `Revisions`(
    `revisionId` INT UNSIGNED NOT NULL AUTO_INCREMENT  PRIMARY KEY,
    `userId` INT UNSIGNED NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT NULL,
    `repetitionRuleId` INT UNSIGNED NULL,
    `round` INT NOT NULL,
    `status` TINYINT(1) NOT NULL
);
ALTER TABLE
    `Revisions` ADD UNIQUE `revisions_name_unique`(`name`);
CREATE TABLE `Activities`(
    `activityId` INT UNSIGNED NOT NULL AUTO_INCREMENT  PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT NULL,
    `venue` VARCHAR(255) NULL,
    `status` TINYINT(1) NOT NULL,
    `userId` INT UNSIGNED NOT NULL
);
ALTER TABLE
    `Activities` ADD UNIQUE `activities_name_unique`(`name`);
CREATE TABLE `Exams`(
    `examId` INT UNSIGNED NOT NULL AUTO_INCREMENT  PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT NULL,
    `subject` VARCHAR(255) NULL,
    `venue` VARCHAR(255) NULL,
    `status` TINYINT(1) NOT NULL,
    `userId` INT UNSIGNED NOT NULL
);
ALTER TABLE
    `Exams` ADD UNIQUE `exams_name_unique`(`name`);
CREATE TABLE `User`(
    `userId` INT UNSIGNED NOT NULL AUTO_INCREMENT  PRIMARY KEY,
    `username` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL
);
ALTER TABLE
    `User` ADD UNIQUE `user_username_unique`(`username`);
ALTER TABLE
    `Lessons` ADD CONSTRAINT `lessons_userid_foreign` FOREIGN KEY(`userId`) REFERENCES `User`(`userId`);
ALTER TABLE
    `Activities` ADD CONSTRAINT `activities_userid_foreign` FOREIGN KEY(`userId`) REFERENCES `User`(`userId`);
ALTER TABLE
    `Exams` ADD CONSTRAINT `exams_userid_foreign` FOREIGN KEY(`userId`) REFERENCES `User`(`userId`);
ALTER TABLE
    `Revisions` ADD CONSTRAINT `revisions_userid_foreign` FOREIGN KEY(`userId`) REFERENCES `User`(`userId`);
ALTER TABLE
    `RepetitionRules` ADD CONSTRAINT `repetitionrules_userid_foreign` FOREIGN KEY(`userId`) REFERENCES `User`(`userId`);
ALTER TABLE
    `Revisions` ADD CONSTRAINT `revisions_repetitionruleid_foreign` FOREIGN KEY(`repetitionRuleId`) REFERENCES `RepetitionRules`(`ruleId`);
ALTER TABLE
    `Lessons` ADD CONSTRAINT `lessons_repetitionruleid_foreign` FOREIGN KEY(`repetitionRuleId`) REFERENCES `RepetitionRules`(`ruleId`);
ALTER TABLE
    `Rounds` ADD CONSTRAINT `rounds_ruleid_foreign` FOREIGN KEY(`ruleId`) REFERENCES `RepetitionRules`(`ruleId`);