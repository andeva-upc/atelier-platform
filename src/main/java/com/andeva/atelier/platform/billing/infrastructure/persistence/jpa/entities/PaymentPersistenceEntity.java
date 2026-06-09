package com.andeva.atelier.platform.billing.infrastructure.persistence.jpa.entities;

import com.andeva.atelier.platform.billing.domain.model.valueobjects.PaymentMethod;
import com.andeva.atelier.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@lombok.NoArgsConstructor
public class PaymentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false)
    @Convert(converter = com.andeva.atelier.platform.shared.infrastructure.persistence.jpa.converters.MoneyAttributeConverter.class)
    private com.andeva.atelier.platform.shared.domain.model.valueobjects.Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id", nullable = false)
    private VoucherPersistenceEntity voucher;
}
