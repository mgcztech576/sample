package com.dayone.persist.entity;
import com.dayone.model.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository public interface MemberRepository extends
        JpaRepository<MemberRepository,Long> {
    Optional<MemberEntity> findByUsername(String username);
    boolean existsByUsername(String username);}