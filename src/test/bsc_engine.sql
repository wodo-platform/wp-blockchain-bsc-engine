CREATE TABLE `transaction`
(
    `id`                  BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`               DATETIME     NOT NULL,
    `udate`               DATETIME     DEFAULT NULL,
    `version`             BIGINT       NOT NULL,
    `status`              VARCHAR(20)  NOT NULL,
    `tx_status`           VARCHAR(20)  NOT NULL,
    `tx_hash`             VARCHAR(255) NOT NULL,
    `sender`              VARCHAR(255) NOT NULL,
    `receiver`            VARCHAR(255) NOT NULL,
    `amount`              DECIMAL      NOT NULL,
    `nonce`               BIGINT       NOT NULL,
    `gas_price`           DECIMAL      NOT NULL,
    `gas_limit`           DECIMAL      NOT NULL,
    `cumulative_gas_used` DECIMAL      DEFAULT NULL,
    `gas_used`            BIGINT       DEFAULT NULL,
    `tx_index`            VARCHAR(255) DEFAULT NULL,
    `block_hash`          VARCHAR(255) DEFAULT NULL,
    `block_number`        BIGINT       DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1;
