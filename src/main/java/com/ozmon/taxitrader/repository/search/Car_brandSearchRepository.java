package com.ozmon.taxitrader.repository.search;

import com.ozmon.taxitrader.domain.Car_brand;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Car_brand entity.
 */
public interface Car_brandSearchRepository extends ElasticsearchRepository<Car_brand, Long> {
}
