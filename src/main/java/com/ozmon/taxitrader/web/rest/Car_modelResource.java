package com.ozmon.taxitrader.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozmon.taxitrader.domain.Car_model;
import com.ozmon.taxitrader.repository.Car_modelRepository;
import com.ozmon.taxitrader.repository.search.Car_modelSearchRepository;
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
 * REST controller for managing Car_model.
 */
@RestController
@RequestMapping("/api")
public class Car_modelResource {

    private final Logger log = LoggerFactory.getLogger(Car_modelResource.class);
        
    @Inject
    private Car_modelRepository car_modelRepository;
    
    @Inject
    private Car_modelSearchRepository car_modelSearchRepository;
    
    /**
     * POST  /car_models -> Create a new car_model.
     */
    @RequestMapping(value = "/car_models",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Car_model> createCar_model(@Valid @RequestBody Car_model car_model) throws URISyntaxException {
        log.debug("REST request to save Car_model : {}", car_model);
        if (car_model.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("car_model", "idexists", "A new car_model cannot already have an ID")).body(null);
        }
        Car_model result = car_modelRepository.save(car_model);
        car_modelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/car_models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("car_model", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /car_models -> Updates an existing car_model.
     */
    @RequestMapping(value = "/car_models",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Car_model> updateCar_model(@Valid @RequestBody Car_model car_model) throws URISyntaxException {
        log.debug("REST request to update Car_model : {}", car_model);
        if (car_model.getId() == null) {
            return createCar_model(car_model);
        }
        Car_model result = car_modelRepository.save(car_model);
        car_modelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("car_model", car_model.getId().toString()))
            .body(result);
    }

    /**
     * GET  /car_models -> get all the car_models.
     */
    @RequestMapping(value = "/car_models",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Car_model>> getAllCar_models(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Car_models");
        Page<Car_model> page = car_modelRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/car_models");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /car_models/:id -> get the "id" car_model.
     */
    @RequestMapping(value = "/car_models/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Car_model> getCar_model(@PathVariable Long id) {
        log.debug("REST request to get Car_model : {}", id);
        Car_model car_model = car_modelRepository.findOne(id);
        return Optional.ofNullable(car_model)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /car_models/:id -> delete the "id" car_model.
     */
    @RequestMapping(value = "/car_models/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCar_model(@PathVariable Long id) {
        log.debug("REST request to delete Car_model : {}", id);
        car_modelRepository.delete(id);
        car_modelSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("car_model", id.toString())).build();
    }

    /**
     * SEARCH  /_search/car_models/:query -> search for the car_model corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/car_models/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Car_model> searchCar_models(@PathVariable String query) {
        log.debug("REST request to search Car_models for query {}", query);
        return StreamSupport
            .stream(car_modelSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
