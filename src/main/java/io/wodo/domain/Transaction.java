package io.wodo.domain;

import io.wodo.enumtype.EnumStatus;
import io.wodo.enumtype.EnumTxStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @Column("id")
    private Long id;

    @Column("idate")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column("udate")
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;

    @Version
    private Long version;

    @Column("status")
    private EnumStatus status;

    @Column("tx_status")
    private EnumTxStatus txStatus;

    @Column("tx_hash")
    private String txHash;

    @Column("sender")
    private String sender;

    @Column("receiver")
    private String receiver;

    @Column("amount")
    private BigDecimal amount;

    @Column("nonce")
    private Long nonce;

    @Column("gas_price")
    private BigDecimal gasPrice;

    @Column("gas_limit")
    private BigDecimal gasLimit;

    @Column("cumulative_gas_used")
    private BigDecimal cumulativeGasUsed;

    @Column("gas_used")
    private BigDecimal gasUsed;

    @Column("tx_index")
    private String txIndex;

    @Column("block_hash")
    private String blockHash;

    @Column("block_number")
    private String blockNumber;
}