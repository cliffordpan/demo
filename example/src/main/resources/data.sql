insert into `accounts` (`id`, `type`, `email`, `password`, `enabled`, `created_date`, `first_name`, `last_name`, `nick_name`) values
    (1, 'EMPLOYEE', 'admin@hchome.me', '{bcrypt}$2a$10$DwMlk1CeKi.pJiKBSXhKuO87lUjm.W4YJpB1dzxtlaoywCKVYame6', true, CURRENT_TIMESTAMP, 'Admin', 'Test', null),
    (2, 'EMPLOYEE', 'support@hchome.me', '{bcrypt}$2a$10$DwMlk1CeKi.pJiKBSXhKuO87lUjm.W4YJpB1dzxtlaoywCKVYame6', true, CURRENT_TIMESTAMP, 'Support', 'Test', null),
    (3, 'CLIENT', 'client1@hchome.me', '{bcrypt}$2a$10$DwMlk1CeKi.pJiKBSXhKuO87lUjm.W4YJpB1dzxtlaoywCKVYame6', true, CURRENT_TIMESTAMP, 'Client1', 'Test', 'C1'),
    (4, 'CLIENT', 'client2@hchome.me', '{bcrypt}$2a$10$DwMlk1CeKi.pJiKBSXhKuO87lUjm.W4YJpB1dzxtlaoywCKVYame6', true, CURRENT_TIMESTAMP, 'Client2', 'Test', 'C2'),
    (5, 'CLIENT', 'client3@hchome.me', '{bcrypt}$2a$10$DwMlk1CeKi.pJiKBSXhKuO87lUjm.W4YJpB1dzxtlaoywCKVYame6', false, CURRENT_TIMESTAMP, 'Client3', 'Test', 'C3');
--- reset auto_increment to 6
alter table `accounts` alter column `id` restart with 6;

insert into `account_addresses` (`acc_id`, `type`, `line1`, `line2`, `unit`, `city`, `region`, `country`, `postal`) values
    (1, 'MAILING', 'line1', null, null, 'Surrey', 'BC', 'CA', 'V1V2G2'),
    (2, 'MAILING', 'line1', null, null, 'Surrey', 'BC', 'CA', 'V1V2G2'),
    (3, 'BILLING', 'test address 1', null, null, 'Surrey', 'BC', 'CA', 'V1V2G2'),
    (3, 'SHIPPING', 'test address 2', null, null, 'Surrey', 'BC', 'CA', 'V1V2G2'),
    (5, 'MAILING', 'line1', null, null, 'Surrey', 'BC', 'CA', 'V1V2G2');

insert into `roles` (`acc_id`, `role`) values
    (1, 'ADMIN'),
    (2, 'SUPPORT');

insert into `products` (`id`, `name`, `description`, `category`, `platform`, `unit_price`) values
    (1, 'Animal Crossing (Digital)', 'Animal Crossing is a social simulation video game series developed and published by Nintendo. The series was conceptualized and created by Katsuya Eguchi and Hisashi Nogami. In Animal Crossing, the player character is a human who lives in a village inhabited by various anthropomorphic animals and can do various activities like fishing, insect catching, and fossil hunting. The series is notable for its open-ended gameplay and use of the video game console''s internal clock and calendar to simulate real passage of time.', 'Simulation', 'SWITCH', 32.99),
    (2, 'Animal Crossing', 'Animal Crossing is a social simulation video game series developed and published by Nintendo. The series was conceptualized and created by Katsuya Eguchi and Hisashi Nogami. In Animal Crossing, the player character is a human who lives in a village inhabited by various anthropomorphic animals and can do various activities like fishing, insect catching, and fossil hunting. The series is notable for its open-ended gameplay and use of the video game console''s internal clock and calendar to simulate real passage of time.', 'Simulation', 'SWITCH', 79.99),
    (3, 'The Legend of Zelda: Breath of the Wild', 'No kingdom. No memories. After a 100-year slumber, Link wakes up alone in a world he no longer remembers. Now the legendary hero must explore a vast and dangerous land and regain his memories before Hyrule is lost forever. Armed only with what he can scavenge, Link sets out to find answers and the resources needed to survive. Supported play modes: TV mode, tabletop mode and handheld mode.', 'Open world', 'SWITCH', 69.99),
    (4, 'NHL 24', 'With all-exciting gameplay features NHL 24 will bring authentic on-ice action to life. Experience the rush and fatigue of high-pressure games with the Exhaust Engine, get unparalleled player command with Vision Passing and Total Control Skill Moves, and change the momentum with Physics-Based Contact.', 'Sport', 'PS4', 89.99);
--- reset auto_increment to 5
alter table `products` alter column `id` restart with 5;

insert into `tickets` (`id`, `cid`, `eid`, `modified_by`, `modified_type`, `created_date`, `updated_date`, `closed_date`, `title`, `description`) values
    (1, 3, NULL, 3, 'CLIENT',  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, 'test title 1', 'test description 1'),
    (2, 3, 2, 1, 'EMPLOYEE',  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, 'test title 2', 'test description 2'),
    (3, 4, 2, 4, 'CLIENT',  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, 'test title 3', 'test description 3'),
    (4, 3, NULL, 1, 'EMPLOYEE',  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, 'test title 4', 'test description 4');
--- reset auto_increment to 5
alter table `tickets` alter column `id` restart with 5;
