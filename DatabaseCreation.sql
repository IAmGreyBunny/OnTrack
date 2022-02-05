CREATE DATABASE ontrack;
USE ontrack;

CREATE TABLE `Lesson_rule`(
    `lessonId` INT UNSIGNED NOT NULL,
    `ruleId` INT UNSIGNED NOT NULL
);
CREATE TABLE `Revision_rule`(
    `revisionId` INT UNSIGNED NOT NULL,
    `ruleId` INT UNSIGNED NOT NULL
);
CREATE TABLE `Exams`(
    `examId` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `desc` TEXT NOT NULL,
    `status` TINYINT(1) NOT NULL,
    `userId` INT UNSIGNED NOT NULL
);
ALTER TABLE
    `Exams` ADD UNIQUE `exams_name_unique`(`name`);
CREATE TABLE `Revision`(
    `revisionId` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `userId` INT UNSIGNED NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `desc` TEXT NOT NULL,
    `status` TINYINT(1) NOT NULL,
    `round` INT NOT NULL
);
ALTER TABLE
    `Revision` ADD UNIQUE `revision_name_unique`(`name`);
CREATE TABLE `Lessons`(
    `lessonId` INT UNSIGNED NOT NULL AUTO_INCREMENT  PRIMARY KEY,
    `userId` INT UNSIGNED NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `desc` TEXT NOT NULL,
    `status` TINYINT(1) NOT NULL,
    `round` INT NOT NULL
);
ALTER TABLE
    `Lessons` ADD UNIQUE `lessons_name_unique`(`name`);
CREATE TABLE `Activity`(
    `activityId` INT UNSIGNED NOT NULL AUTO_INCREMENT  PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `desc` TEXT NOT NULL,
    `status` TINYINT(1) NOT NULL,
    `userId` INT UNSIGNED NOT NULL
);
ALTER TABLE
    `Activity` ADD UNIQUE `activity_name_unique`(`name`);
CREATE TABLE `RepetitionRule`(
    `ruleId` INT UNSIGNED NOT NULL AUTO_INCREMENT  PRIMARY KEY,
    `ruleName` VARCHAR(255) NOT NULL,
    `repeatType` VARCHAR(255) NOT NULL,
    `userId` INT UNSIGNED NOT NULL
);
CREATE TABLE `User`(
    `userId` INT UNSIGNED NOT NULL AUTO_INCREMENT  PRIMARY KEY,
    `username` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL
);
ALTER TABLE
    `User` ADD UNIQUE `user_username_unique`(`username`);
CREATE TABLE `Rounds`(
    `ruleId` INT UNSIGNED NOT NULL,
    `roundNumber` INT NOT NULL,
    `roundInterval` INT NOT NULL
);
ALTER TABLE
    `Lessons` ADD CONSTRAINT `lessons_userid_foreign` FOREIGN KEY(`userId`) REFERENCES `User`(`userId`);
ALTER TABLE
    `Activity` ADD CONSTRAINT `activity_userid_foreign` FOREIGN KEY(`userId`) REFERENCES `User`(`userId`);
ALTER TABLE
    `Exams` ADD CONSTRAINT `exams_userid_foreign` FOREIGN KEY(`userId`) REFERENCES `User`(`userId`);
ALTER TABLE
    `Revision` ADD CONSTRAINT `revision_userid_foreign` FOREIGN KEY(`userId`) REFERENCES `User`(`userId`);
ALTER TABLE
    `RepetitionRule` ADD CONSTRAINT `repetitionrule_userid_foreign` FOREIGN KEY(`userId`) REFERENCES `User`(`userId`);
ALTER TABLE
    `Revision_rule` ADD CONSTRAINT `revision_rule_revisionid_foreign` FOREIGN KEY(`revisionId`) REFERENCES `Revision`(`revisionId`);
ALTER TABLE
    `Lesson_rule` ADD CONSTRAINT `lesson_rule_lessonid_foreign` FOREIGN KEY(`lessonId`) REFERENCES `Lessons`(`lessonId`);
ALTER TABLE
    `Revision_rule` ADD CONSTRAINT `revision_rule_ruleid_foreign` FOREIGN KEY(`ruleId`) REFERENCES `RepetitionRule`(`ruleId`);
ALTER TABLE
    `Lesson_rule` ADD CONSTRAINT `lesson_rule_ruleid_foreign` FOREIGN KEY(`ruleId`) REFERENCES `RepetitionRule`(`ruleId`);
ALTER TABLE
    `Rounds` ADD CONSTRAINT `rounds_ruleid_foreign` FOREIGN KEY(`ruleId`) REFERENCES `RepetitionRule`(`ruleId`);