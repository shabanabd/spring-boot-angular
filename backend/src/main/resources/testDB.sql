CREATE TABLE `test_db`.`applications` (
                                          `business_application_id` INT NOT NULL,
                                          `sales_agent_first_name` VARCHAR(100) NULL,
                                          `sales_agent_last_name` VARCHAR(100) NULL,
                                          `sales_agent_email` VARCHAR(100) NULL,
                                          `account_type` VARCHAR(50) NULL,
                                          `created_at` DATETIME NULL,
                                          `application_status` VARCHAR(50) NULL,
                                          `business_category` VARCHAR(50) NULL,
                                          `updated_at` DATETIME NULL,
                                          PRIMARY KEY (`business_application_id`));


CREATE TABLE `test_db`.`users` (
                                   `id` INT NOT NULL,
                                   `username` VARCHAR(45) NULL,
                                   `password` VARCHAR(45) NULL,
                                   PRIMARY KEY (`id`));
