package com.smartshop.repository;

import com.smartshop.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode,Long> {
    boolean existsByCode(String code);
}
