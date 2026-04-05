package com.finance.dto;

import com.finance.entity.FinancialRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecordResponse {
    private Long id;
    private BigDecimal amount;
    private String type;
    private String category;
    private LocalDate recordDate;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FinancialRecordResponse(FinancialRecord record) {
        this.id = record.getId();
        this.amount = record.getAmount();
        this.type = record.getType().toString();
        this.category = record.getCategory();
        this.recordDate = record.getRecordDate();
        this.description = record.getDescription();
        this.createdAt = record.getCreatedAt();
        this.updatedAt = record.getUpdatedAt();
    }
}
