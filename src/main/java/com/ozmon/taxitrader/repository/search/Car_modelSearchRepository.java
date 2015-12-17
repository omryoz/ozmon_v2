package com.ozmon.taxitrader.repository.search;

import com.ozmon.taxitrader.domain.Car_model;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Car_model entity.
 */
public interface Car_modelSearchRepository extends ElasticsearchRepository<Car_model, Long> {
}
