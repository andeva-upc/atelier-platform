package com.andeva.atelier.platform.operations.domain.model.aggregates;

import com.andeva.atelier.platform.operations.domain.model.valueobjects.ProductId;
import com.andeva.atelier.platform.operations.domain.model.valueobjects.Quantity;
import com.andeva.atelier.platform.operations.infrastructure.persistence.jpa.converters.QuantityAttributeConverter;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import com.andeva.atelier.platform.shared.infrastructure.persistence.jpa.converters.MoneyAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity representing a product associated with a work order task. This entity is part of the Work Order Task aggregate and contains details about the product, such as its ID, quantity, unit price, and total amount. It also includes auditing fields for tracking creation and modification times, as well as soft deletion handling.
 * The @SQLDelete annotation is used to implement soft deletion by updating the deleted_at timestamp instead of physically removing the record from the database. The @SQLRestriction annotation ensures that only records that have not been marked as deleted (i.e., where deleted_at is null) are returned in queries.
 * @author Joel Huamani Estefanero
 */
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "work_order_task_products")
@SQLDelete(sql = "UPDATE work_order_task_products SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class WorkOrderTaskProduct {

    @Id
    private UUID id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "product_id", nullable = false))
    private ProductId productId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "branch_id", nullable = false))
    private BranchId branchId;

    @Column(nullable = false)
    @Convert(converter = QuantityAttributeConverter.class)
    private Quantity quantity;

    @Column(name = "unit_price", nullable = false)
    @Convert(converter = MoneyAttributeConverter.class)
    private Money unitPrice;

    @Column(name = "total_amount", nullable = false)
    @Convert(converter = MoneyAttributeConverter.class)
    private Money totalAmount;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Version
    private Long version;

    /**
     * Protected no-args constructor required by JPA. This constructor is used by the JPA provider to create instances of the entity when retrieving data from the database. It should not be used directly in application code.
     */
    protected WorkOrderTaskProduct() {}

    /**
     * Constructor to create a new WorkOrderTaskProduct instance with the specified product ID, branch ID, quantity, and unit price. The total amount is calculated by multiplying the unit price by the quantity. A unique UUID is generated for the entity ID.
     * @param productId the unique identifier of the product associated with the work order task
     * @param branchId the unique identifier of the branch where the product is located
     * @param quantity the quantity of the product being used in the work order task
     * @param unitPrice the unit price of the product, which is used to calculate the total amount for the work order task product
     */
    public WorkOrderTaskProduct(ProductId productId, BranchId branchId, Quantity quantity, Money unitPrice) {
        this.id = UUID.randomUUID();
        this.productId = productId;
        this.branchId = branchId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = unitPrice.multiply(quantity.value());
    }

    /**
     * Updates the quantity of the product and recalculates the total amount based on the new quantity and the existing unit price. This method allows for modifying the quantity of the product in the work order task while ensuring that the total amount remains accurate.
     * @param newQuantity the new quantity to be set for the product. This value will be used to update the quantity field and recalculate the total amount accordingly.
     */
    public void updateQuantity(Quantity newQuantity) {
        this.quantity = newQuantity;
        this.totalAmount = this.unitPrice.multiply(newQuantity.value());
    }

    /**
     * Checks if this WorkOrderTaskProduct has been marked as deleted by verifying if the deletedAt timestamp is not null. This method is used to determine if the entity has been soft-deleted, which means it has been marked as deleted without being physically removed from the database.
     * @return true if the deletedAt timestamp is not null (indicating that the entity has been marked as deleted), false otherwise (indicating that the entity is still active and has not been marked as deleted).
     */
    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
