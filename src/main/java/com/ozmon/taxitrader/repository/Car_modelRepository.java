package com.ozmon.taxitrader.repository;

import com.ozmon.taxitrader.domain.Car_model;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Car_model entity.
 */
public interface Car_modelRepository extends JpaRepository<Car_model,Long> {

}
