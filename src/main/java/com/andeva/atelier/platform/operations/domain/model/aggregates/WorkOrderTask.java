package com.andeva.atelier.platform.operations.domain.model.aggregates;

import com.andeva.atelier.platform.operations.domain.model.valueobjects.*;
import com.andeva.atelier.platform.operations.infrastructure.persistence.jpa.converters.TaskDescriptionAttributeConverter;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import com.andeva.atelier.platform.shared.infrastructure.persistence.jpa.converters.MoneyAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "work_order_tasks")
@SQLDelete(sql = "UPDATE work_order_tasks SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class WorkOrderTask {

    @Id
    private UUID id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "service_id", nullable = false))
    private ServiceId serviceId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "branch_id", nullable = false))
    private BranchId branchId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "assigned_mechanic_id", nullable = false))
    private MechanicId assignedMechanicId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkOrderTaskStatus status;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Convert(converter = TaskDescriptionAttributeConverter.class)
    private TaskDescription description;

    @Column(name = "price", nullable = false)
    @Convert(converter = MoneyAttributeConverter.class)
    private Money price;

    private Instant startedAt;

    private Instant completedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_task_id")
    private List<WorkOrderTaskProduct> products = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedBy
    private UUID createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    private UUID updatedBy;

    @Version
    private Long version;
    protected WorkOrderTask() {}

    public WorkOrderTask(ServiceId serviceId, BranchId branchId, MechanicId mechanicId, TaskDescription description, Money laborPrice) {
        this.id = UUID.randomUUID();
        this.serviceId = serviceId;
        this.branchId = branchId;
        this.assignedMechanicId = mechanicId;
        this.description = description;
        this.price = laborPrice;
        this.status = WorkOrderTaskStatus.PENDING;
    }

    public void addProduct(ProductId productId, Quantity quantity, Money unitPrice) {
        if (this.status == WorkOrderTaskStatus.COMPLETED) {
            throw new IllegalStateException("operations.error.task.cannotModifyCompletedTask");
        }

        Optional<WorkOrderTaskProduct> existingProduct = this.products.stream()
                .filter(p -> p.getProductId().equals(productId) && !p.isDeleted()).findFirst();

        if (existingProduct.isPresent()) {
            WorkOrderTaskProduct product = existingProduct.get();
            Money oldTotalAmount = product.getTotalAmount();
            product.updateQuantity(new Quantity(product.getQuantity().value() + quantity.value()));
            this.price = this.price.minus(oldTotalAmount).plus(product.getTotalAmount());
        } else {
          WorkOrderTaskProduct product = new WorkOrderTaskProduct(productId, this.branchId, quantity, unitPrice);
          this.products.add(product);
          this.price = this.price.plus(product.getTotalAmount());
        }
    }

    public void removeProduct(ProductId productId) {
        if (this.status == WorkOrderTaskStatus.COMPLETED) {
            throw new IllegalStateException("operations.error.task.cannotModifyCompletedTask");
        }

        WorkOrderTaskProduct product = this.products.stream()
                .filter(p -> p.getProductId().equals(productId) && !p.isDeleted())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("operations.error.taskProduct.notFound"));

        this.price = this.price.minus(product.getTotalAmount());
        this.products.remove(product);
    }

    public void start() {
        this.status = this.status.transitionTo(WorkOrderTaskStatus.DOING);
        this.startedAt = Instant.now();
    }

    public boolean complete() {
        this.status = this.status.transitionTo(WorkOrderTaskStatus.COMPLETED);
        this.completedAt = Instant.now();
        return true;
    }

    public boolean reopen() {
        this.status = this.status.transitionTo(WorkOrderTaskStatus.DOING);
        this.completedAt = null;
        return true;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
