package com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.entities;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "inventory_products")
@EntityListeners(AuditingEntityListener.class)
public class ProductJpaEntity {
    @Id
    private UUID id;
    private String branchId;
    private String category;
    private String name;
    private String sku;
    private Integer currentStock;
    private Integer reservedStock;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductBatchJpaEntity> batches = new ArrayList<>();
    public ProductJpaEntity() {}
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public Integer getCurrentStock() { return currentStock; }
    public void setCurrentStock(Integer currentStock) { this.currentStock = currentStock; }
    public Integer getReservedStock() { return reservedStock; }
    public void setReservedStock(Integer reservedStock) { this.reservedStock = reservedStock; }
    public List<ProductBatchJpaEntity> getBatches() { return batches; }
    public void setBatches(List<ProductBatchJpaEntity> batches) { this.batches = batches; }
}
