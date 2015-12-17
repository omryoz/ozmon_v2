package com.ozmon.taxitrader.repository;

import com.ozmon.taxitrader.domain.Car_brand;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Car_brand entity.
 */
public interface Car_brandRepository extends JpaRepository<Car_brand,Long> {

}
