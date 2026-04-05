package com.finance.service;

import com.finance.dto.FinancialRecordRequest;
import com.finance.dto.FinancialRecordResponse;
import com.finance.entity.FinancialRecord;
import com.finance.entity.User;
import com.finance.repository.FinancialRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialRecordService {

    @Autowired
    private FinancialRecordRepository recordRepository;

    public FinancialRecordResponse createRecord(User user, FinancialRecordRequest request) {
        FinancialRecord.RecordType type = FinancialRecord.RecordType.valueOf(request.getType().toUpperCase());

        FinancialRecord record = new FinancialRecord(
            user,
            request.getAmount(),
            type,
            request.getCategory(),
            request.getRecordDate(),
            request.getDescription()
        );

        record = recordRepository.save(record);
        return new FinancialRecordResponse(record);
    }

    public List<FinancialRecordResponse> getRecordsByUser(User user) {
        return recordRepository.findByUserAndNotDeleted(user)
            .stream()
            .map(FinancialRecordResponse::new)
            .collect(Collectors.toList());
    }

    public FinancialRecordResponse getRecordById(Long id, User user) {
        FinancialRecord record = recordRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Record not found"));

        // Verify ownership
        if (!record.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to record");
        }

        if (record.getDeleted()) {
            throw new RuntimeException("Record not found");
        }

        return new FinancialRecordResponse(record);
    }

    public FinancialRecordResponse updateRecord(Long id, User user, FinancialRecordRequest request) {
        FinancialRecord record = recordRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Record not found"));

        // Verify ownership
        if (!record.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to record");
        }

        if (record.getDeleted()) {
            throw new RuntimeException("Record not found");
        }

        record.setAmount(request.getAmount());
        record.setType(FinancialRecord.RecordType.valueOf(request.getType().toUpperCase()));
        record.setCategory(request.getCategory());
        record.setRecordDate(request.getRecordDate());
        record.setDescription(request.getDescription());
        record.setUpdatedAt(LocalDateTime.now());

        record = recordRepository.save(record);
        return new FinancialRecordResponse(record);
    }

    public void deleteRecord(Long id, User user) {
        FinancialRecord record = recordRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Record not found"));

        // Verify ownership
        if (!record.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to record");
        }

        record.setDeleted(true);
        recordRepository.save(record);
    }

    public List<FinancialRecordResponse> getRecordsByType(User user, String type) {
        FinancialRecord.RecordType recordType = FinancialRecord.RecordType.valueOf(type.toUpperCase());
        return recordRepository.findByUserAndType(user, recordType)
            .stream()
            .map(FinancialRecordResponse::new)
            .collect(Collectors.toList());
    }

    public List<FinancialRecordResponse> getRecordsByCategory(User user, String category) {
        return recordRepository.findByUserAndCategory(user, category)
            .stream()
            .map(FinancialRecordResponse::new)
            .collect(Collectors.toList());
    }

    public List<FinancialRecordResponse> getRecordsByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return recordRepository.findByUserAndDateRange(user, startDate, endDate)
            .stream()
            .map(FinancialRecordResponse::new)
            .collect(Collectors.toList());
    }

    public List<FinancialRecordResponse> getRecentRecords(User user, int limit) {
        return recordRepository.findRecentRecords(user, limit)
            .stream()
            .map(FinancialRecordResponse::new)
            .collect(Collectors.toList());
    }
}
