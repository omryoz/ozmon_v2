package com.ozmon.taxitrader.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozmon.taxitrader.domain.Car_brand;
import com.ozmon.taxitrader.repository.Car_brandRepository;
import com.ozmon.taxitrader.repository.search.Car_brandSearchRepository;
import com.ozmon.taxitrader.web.rest.util.HeaderUtil;
import com.ozmon.taxitrader.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Car_brand.
 */
@RestController
@RequestMapping("/api")
public class Car_brandResource {

    private final Logger log = LoggerFactory.getLogger(Car_brandResource.class);
        
    @Inject
    private Car_brandRepository car_brandRepository;
    
    @Inject
    private Car_brandSearchRepository car_brandSearchRepository;
    
    /**
     * POST  /car_brands -> Create a new car_brand.
     */
    @RequestMapping(value = "/car_brands",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Car_brand> createCar_brand(@Valid @RequestBody Car_brand car_brand) throws URISyntaxException {
        log.debug("REST request to save Car_brand : {}", car_brand);
        if (car_brand.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("car_brand", "idexists", "A new car_brand cannot already have an ID")).body(null);
        }
        Car_brand result = car_brandRepository.save(car_brand);
        car_brandSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/car_brands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("car_brand", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /car_brands -> Updates an existing car_brand.
     */
    @RequestMapping(value = "/car_brands",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Car_brand> updateCar_brand(@Valid @RequestBody Car_brand car_brand) throws URISyntaxException {
        log.debug("REST request to update Car_brand : {}", car_brand);
        if (car_brand.getId() == null) {
            return createCar_brand(car_brand);
        }
        Car_brand result = car_brandRepository.save(car_brand);
        car_brandSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("car_brand", car_brand.getId().toString()))
            .body(result);
    }

    /**
     * GET  /car_brands -> get all the car_brands.
     */
    @RequestMapping(value = "/car_brands",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Car_brand>> getAllCar_brands(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Car_brands");
        Page<Car_brand> page = car_brandRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/car_brands");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /car_brands/:id -> get the "id" car_brand.
     */
    @RequestMapping(value = "/car_brands/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Car_brand> getCar_brand(@PathVariable Long id) {
        log.debug("REST request to get Car_brand : {}", id);
        Car_brand car_brand = car_brandRepository.findOne(id);
        return Optional.ofNullable(car_brand)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /car_brands/:id -> delete the "id" car_brand.
     */
    @RequestMapping(value = "/car_brands/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCar_brand(@PathVariable Long id) {
        log.debug("REST request to delete Car_brand : {}", id);
        car_brandRepository.delete(id);
        car_brandSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("car_brand", id.toString())).build();
    }

    /**
     * SEARCH  /_search/car_brands/:query -> search for the car_brand corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/car_brands/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Car_brand> searchCar_brands(@PathVariable String query) {
        log.debug("REST request to search Car_brands for query {}", query);
        return StreamSupport
            .stream(car_brandSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
