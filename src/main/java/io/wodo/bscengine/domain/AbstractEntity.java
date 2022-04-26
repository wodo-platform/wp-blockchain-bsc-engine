package io.wodo.bscengine.domain;

import io.wodo.bscengine.converter.EnumStatusConverter;
import io.wodo.bscengine.enumtype.EnumStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AbstractEntity implements Serializable {

    @Column(name = "idate", nullable = false)
    @CreatedDate
    private LocalDateTime insertDate;

    @Column(name = "udate")
    @LastModifiedDate
    private LocalDateTime updateDate;

    @Convert(converter = EnumStatusConverter.class)
    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    private EnumStatus status;
}
