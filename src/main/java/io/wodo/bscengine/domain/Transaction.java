package io.wodo.bscengine.domain;

import io.wodo.bscengine.enumtype.EnumTxStatus;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transaction")
public class Transaction extends AbstractEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tx_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumTxStatus txStatus;

    @Column(name = "tx_hash", nullable = false)
    private String txHash;

    @Column(name = "from", nullable = false)
    private String from;

    @Column(name = "to", nullable = false)
    private String to;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "nonce", nullable = false)
    private String nonce;

    @Column(name = "gas_price", nullable = false)
    private BigDecimal gasPrice;

    @Column(name = "gas_limit", nullable = false)
    private BigDecimal gasLimit;

    @Column(name = "tx_index", nullable = false)
    private BigInteger txIndex;

    @Column(name = "block_hash", nullable = false)
    private String blockHash;

    @Column(name = "block_number", nullable = false)
    private BigInteger blockNumber;

    @Column(name = "cumulative_gas_used", nullable = false)
    private String cumulativeGasUsed;

    @Column(name = "gas_used", nullable = false)
    private BigInteger gasUsed;

    @Column(name = "contract_address", nullable = false)
    private String contractAddress;

    @Column(name = "root", nullable = false)
    private String root;

    @Column(name = "logs_bloom", nullable = false)
    private String logsBloom;

    @ManyToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id", nullable = false)
    private Wallet wallet;
}