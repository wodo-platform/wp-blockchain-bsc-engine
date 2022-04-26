package io.wodo.bscengine.domain;

import io.wodo.bscengine.enumtype.EnumStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "wallet")
public class Wallet extends AbstractEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "secret", nullable = false)
    private String secret;

    @Column(name = "public_key", nullable = false)
    private String publicKey;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    public Wallet(EnumStatus status, BigDecimal amount) {
        super.setStatus(status);
        this.amount = amount;
    }
}