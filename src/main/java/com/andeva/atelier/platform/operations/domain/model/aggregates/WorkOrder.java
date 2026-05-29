package com.andeva.atelier.platform.operations.domain.model.aggregates;

import com.andeva.atelier.platform.operations.domain.model.events.ProductReservationCanceledEvent;
import com.andeva.atelier.platform.operations.domain.model.events.ProductReservedEvent;
import com.andeva.atelier.platform.operations.domain.model.events.WorkOrderPaidEvent;
import com.andeva.atelier.platform.operations.domain.model.valueobjects.*;
import com.andeva.atelier.platform.operations.infrastructure.persistence.jpa.converters.DiagnosticSummaryAttributeConverter;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.*;
import com.andeva.atelier.platform.shared.infrastructure.persistence.jpa.converters.MileageAttributeConverter;
import com.andeva.atelier.platform.shared.infrastructure.persistence.jpa.converters.MoneyAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "work_orders")
@SQLDelete(sql = "UPDATE work_orders SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class WorkOrder extends AbstractAggregateRoot<WorkOrder> {

    @Id
    private UUID id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "appointment_id", nullable = false))
    private AppointmentId appointmentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "branch_id", nullable = false))
    private BranchId branchId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "vehicle_id", nullable = false))
    private VehicleId vehicleId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "customer_id", nullable = false))
    private CustomerId customerId;

    @Column(name = "internal_number", nullable = false, updatable = false)
    private Integer internalNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkOrderStatus status;

    @Column(name = "diagnostic_summary", nullable = false, columnDefinition = "TEXT")
    @Convert(converter = DiagnosticSummaryAttributeConverter.class)
    private DiagnosticSummary diagnosticSummary;

    @Column(name = "mileage_in", nullable = false)
    @Convert(converter = MileageAttributeConverter.class)
    private Mileage mileageIn;

    @Column(name = "total_amount", nullable = false)
    @Convert(converter = MoneyAttributeConverter.class)
    private Money totalAmount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id")
    private List<WorkOrderTask> tasks = new ArrayList<>();

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

    protected WorkOrder() {}

    public WorkOrder(AppointmentId appointmentId, BranchId branchId, VehicleId vehicleId, CustomerId customerId, Integer internalNumber, DiagnosticSummary diagnosticSummary, Mileage mileageIn) {
        this.id = UUID.randomUUID();
        this.appointmentId = appointmentId;
        this.branchId = branchId;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.internalNumber = internalNumber;
        this.diagnosticSummary = diagnosticSummary;
        this.mileageIn = mileageIn;
        this.status = WorkOrderStatus.PENDING;
        this.totalAmount = Money.ZERO;
    }

    public void addTask(ServiceId serviceId, MechanicId mechanicId, TaskDescription description, Money laborPrice) {
        if (this.status == WorkOrderStatus.COMPLETED || this.status == WorkOrderStatus.PAID) {
            throw new IllegalStateException("operations.error.workOrder.cannotModifyClosedOrder");
        }
        WorkOrderTask task = new WorkOrderTask(serviceId, this.branchId, mechanicId, description, laborPrice);
        this.tasks.add(task);
        recalculateTotalAmount();
    }

    public void addProductToTask(UUID taskId, ProductId productId, Quantity quantity, Money unitPrice) {
        if (this.status == WorkOrderStatus.COMPLETED || this.status == WorkOrderStatus.PAID) {
            throw new IllegalStateException("operations.error.workOrder.cannotModifyClosedOrder");
        }
        WorkOrderTask task = findTaskOrThrow(taskId);
        task.addProduct(productId, quantity, unitPrice);
        recalculateTotalAmount();
        // EVENTO: Notifica al inventario que reserve temporalmente la cantidad agregada
        this.registerEvent(new ProductReservedEvent(this, this.branchId, productId, quantity));
    }

    public void removeProductFromTask(UUID taskId, ProductId productId) {
        if (this.status == WorkOrderStatus.COMPLETED || this.status == WorkOrderStatus.PAID) {
            throw new IllegalStateException("operations.error.workOrder.cannotModifyClosedOrder");
        }
        WorkOrderTask task = findTaskOrThrow(taskId);

        WorkOrderTaskProduct product = task.getProducts().stream()
                .filter(p -> p.getProductId().equals(productId) && !p.isDeleted())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("operations.error.taskProduct.notFound"));

        Quantity returnedQuantity = product.getQuantity();
        task.removeProduct(productId);
        recalculateTotalAmount();
        // EVENTOS DE COMPENSACIÓN: Liberamos stock de todos sus productos activos
        this.registerEvent(new ProductReservationCanceledEvent(this, this.branchId, productId, returnedQuantity));
    }

    public void removeTask(UUID taskId) {
        if (this.status == WorkOrderStatus.COMPLETED || this.status == WorkOrderStatus.PAID) {
            throw new IllegalStateException("operations.error.workOrder.cannotModifyClosedOrder");
        }
        WorkOrderTask task = findTaskOrThrow(taskId);
        if (task.getStatus() == WorkOrderTaskStatus.COMPLETED) {
            throw new IllegalStateException("operations.error.workOrder.cannotDeleteCompletedTask");
        }

        for (WorkOrderTaskProduct product : task.getProducts()) {
            if (!product.isDeleted()) {
                // EVENTOS DE COMPENSACIÓN: Liberamos stock de todos sus productos activos
                this.registerEvent(new ProductReservationCanceledEvent(this, this.branchId, product.getProductId(), product.getQuantity()));
            }
        }
        this.tasks.remove(task);
        recalculateTotalAmount();
    }

    public void delete() {
        if (this.status == WorkOrderStatus.PAID) {
            throw new IllegalStateException("operations.error.workOrder.cannotDeletePaidOrder");
        }
        this.deletedAt = Instant.now();
        for (WorkOrderTask task : this.tasks) {
            if (!task.isDeleted()) {
                for (WorkOrderTaskProduct product : task.getProducts()) {
                    if (!product.isDeleted()) {
                        // EVENTOS DE COMPENSACIÓN: Devolvemos stock de todos los productos en todas las tareas
                        this.registerEvent(new ProductReservationCanceledEvent(this, this.branchId, product.getProductId(), product.getQuantity()));
                    }
                }
            }
        }
    }

    public void startTask(UUID taskId) {
        if (this.status == WorkOrderStatus.COMPLETED || this.status == WorkOrderStatus.PAID) {
            throw new IllegalStateException("operations.error.workOrder.cannotModifyClosedOrder");
        }

        WorkOrderTask task = findTaskOrThrow(taskId);
        task.start(); // Llama al start() interno de la tarea

        checkAutoCompletion(); // Auto-promociona la orden a IN_PROGRESS si estaba en PENDING
    }

    public void completeTask(UUID taskId) {
        WorkOrderTask task = findTaskOrThrow(taskId);
        if (task.complete()) {
            checkAutoCompletion();
        }
    }

    public void reopenTask(UUID taskId) {
        if (this.status == WorkOrderStatus.PAID) {
            throw new IllegalStateException("operations.error.workOrder.cannotReopenTaskOfPaidOrder");
        }
        WorkOrderTask task = findTaskOrThrow(taskId);
        if (task.reopen()) {
            if (this.status == WorkOrderStatus.COMPLETED) {
                this.status = WorkOrderStatus.IN_PROGRESS;
            }
        }
    }

    public void startWork() {
        this.status = this.status.transitionTo(WorkOrderStatus.IN_PROGRESS);
    }

    public void completeWorkOrder() {
        boolean allTasksCompleted = this.tasks.stream()
                .allMatch(t -> t.getStatus() == WorkOrderTaskStatus.COMPLETED);
        if (!allTasksCompleted) {
            throw new IllegalStateException("operations.error.workOrder.pendingTasksExist");
        }
        this.status = this.status.transitionTo(WorkOrderStatus.COMPLETED);
    }

    public void markAsPaid() {
        this.status = this.status.transitionTo(WorkOrderStatus.PAID);

        List<WorkOrderTaskProduct> dispatchedProducts = new ArrayList<>();
        for (WorkOrderTask task : this.tasks) {
            if (!task.isDeleted()) {
                for (WorkOrderTaskProduct product : task.getProducts()) {
                    if (!product.isDeleted()) {
                        dispatchedProducts.add(product);
                    }
                }
            }
        }
        // EVENTO: Notifica que haga efectiva la salida física real del almacén
        this.registerEvent(new WorkOrderPaidEvent(this, this.branchId, dispatchedProducts));
    }

    private void recalculateTotalAmount() {
        this.totalAmount = this.tasks.stream()
                .map(WorkOrderTask::getPrice)
                .reduce(Money.ZERO, Money::plus);
    }

    private WorkOrderTask findTaskOrThrow(UUID taskId) {
        return this.tasks.stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("operations.error.task.notFound"));
    }

    private void checkAutoCompletion() {
        if (this.status == WorkOrderStatus.PENDING) {
            this.status = WorkOrderStatus.IN_PROGRESS;
        }
    }
}
