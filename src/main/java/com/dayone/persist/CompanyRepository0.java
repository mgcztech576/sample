//package com.dayone.persist;
//
//import com.dayone.persist.entity.CompanyEntity0;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface CompanyRepository0 extends JpaRepository<CompanyEntity0, Long> {
//
//    boolean existsByTicker(String ticker);
//
//    Optional<CompanyEntity0> findByName(String name);
//
//    Optional<CompanyEntity0> findByTicker(String ticker);
//
//    Page<CompanyEntity0> findByNameStartingWithIgnoreCase(String s, Pageable pageable);
//}
