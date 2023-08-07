//package com.dayone.persist;
//
//import com.dayone.persist.entity.DividendEntity0;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface DividendRepository0 extends JpaRepository<DividendEntity0, Long> {
//    List<DividendEntity0> findAllByCompanyId(Long companyId);
//
//    @Transactional
//    void deleteAllByCompanyId(Long id);
//
//    boolean existsByCompanyIdAndDate(Long companyId, LocalDateTime date);
//}
