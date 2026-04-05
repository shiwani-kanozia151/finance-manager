package com.finance.repository;

import com.finance.entity.FinancialRecord;
import com.finance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    @Query("SELECT fr FROM FinancialRecord fr WHERE fr.user = :user AND fr.deleted = false")
    List<FinancialRecord> findByUserAndNotDeleted(@Param("user") User user);

    @Query("SELECT fr FROM FinancialRecord fr WHERE fr.user = :user AND fr.deleted = false AND fr.type = :type")
    List<FinancialRecord> findByUserAndType(@Param("user") User user, @Param("type") FinancialRecord.RecordType type);

    @Query("SELECT fr FROM FinancialRecord fr WHERE fr.user = :user AND fr.deleted = false AND fr.category = :category")
    List<FinancialRecord> findByUserAndCategory(@Param("user") User user, @Param("category") String category);

    @Query("SELECT fr FROM FinancialRecord fr WHERE fr.user = :user AND fr.deleted = false AND " +
           "fr.recordDate BETWEEN :startDate AND :endDate")
    List<FinancialRecord> findByUserAndDateRange(@Param("user") User user,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    @Query("SELECT fr FROM FinancialRecord fr WHERE fr.user = :user AND fr.deleted = false ORDER BY fr.recordDate DESC LIMIT :limit")
    List<FinancialRecord> findRecentRecords(@Param("user") User user, @Param("limit") int limit);

    @Query("SELECT SUM(fr.amount) FROM FinancialRecord fr WHERE fr.user = :user AND fr.deleted = false AND fr.type = :type")
    Long sumByUserAndType(@Param("user") User user, @Param("type") FinancialRecord.RecordType type);
}
