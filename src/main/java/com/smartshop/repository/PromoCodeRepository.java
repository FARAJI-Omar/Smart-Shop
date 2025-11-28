package com.smartshop.repository;

import com.smartshop.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode,Long> {
    boolean existsByCode(String code);
    Optional<PromoCode> findByCode(String code);
}
