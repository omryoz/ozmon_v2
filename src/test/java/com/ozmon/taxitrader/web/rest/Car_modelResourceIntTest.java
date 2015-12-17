package com.ozmon.taxitrader.web.rest;

import com.ozmon.taxitrader.Application;
import com.ozmon.taxitrader.domain.Car_model;
import com.ozmon.taxitrader.repository.Car_modelRepository;
import com.ozmon.taxitrader.repository.search.Car_modelSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the Car_modelResource REST controller.
 *
 * @see Car_modelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Car_modelResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private Car_modelRepository car_modelRepository;

    @Inject
    private Car_modelSearchRepository car_modelSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCar_modelMockMvc;

    private Car_model car_model;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Car_modelResource car_modelResource = new Car_modelResource();
        ReflectionTestUtils.setField(car_modelResource, "car_modelSearchRepository", car_modelSearchRepository);
        ReflectionTestUtils.setField(car_modelResource, "car_modelRepository", car_modelRepository);
        this.restCar_modelMockMvc = MockMvcBuilders.standaloneSetup(car_modelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        car_model = new Car_model();
        car_model.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCar_model() throws Exception {
        int databaseSizeBeforeCreate = car_modelRepository.findAll().size();

        // Create the Car_model

        restCar_modelMockMvc.perform(post("/api/car_models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(car_model)))
                .andExpect(status().isCreated());

        // Validate the Car_model in the database
        List<Car_model> car_models = car_modelRepository.findAll();
        assertThat(car_models).hasSize(databaseSizeBeforeCreate + 1);
        Car_model testCar_model = car_models.get(car_models.size() - 1);
        assertThat(testCar_model.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = car_modelRepository.findAll().size();
        // set the field null
        car_model.setName(null);

        // Create the Car_model, which fails.

        restCar_modelMockMvc.perform(post("/api/car_models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(car_model)))
                .andExpect(status().isBadRequest());

        List<Car_model> car_models = car_modelRepository.findAll();
        assertThat(car_models).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCar_models() throws Exception {
        // Initialize the database
        car_modelRepository.saveAndFlush(car_model);

        // Get all the car_models
        restCar_modelMockMvc.perform(get("/api/car_models?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(car_model.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCar_model() throws Exception {
        // Initialize the database
        car_modelRepository.saveAndFlush(car_model);

        // Get the car_model
        restCar_modelMockMvc.perform(get("/api/car_models/{id}", car_model.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(car_model.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCar_model() throws Exception {
        // Get the car_model
        restCar_modelMockMvc.perform(get("/api/car_models/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCar_model() throws Exception {
        // Initialize the database
        car_modelRepository.saveAndFlush(car_model);

		int databaseSizeBeforeUpdate = car_modelRepository.findAll().size();

        // Update the car_model
        car_model.setName(UPDATED_NAME);

        restCar_modelMockMvc.perform(put("/api/car_models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(car_model)))
                .andExpect(status().isOk());

        // Validate the Car_model in the database
        List<Car_model> car_models = car_modelRepository.findAll();
        assertThat(car_models).hasSize(databaseSizeBeforeUpdate);
        Car_model testCar_model = car_models.get(car_models.size() - 1);
        assertThat(testCar_model.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteCar_model() throws Exception {
        // Initialize the database
        car_modelRepository.saveAndFlush(car_model);

		int databaseSizeBeforeDelete = car_modelRepository.findAll().size();

        // Get the car_model
        restCar_modelMockMvc.perform(delete("/api/car_models/{id}", car_model.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Car_model> car_models = car_modelRepository.findAll();
        assertThat(car_models).hasSize(databaseSizeBeforeDelete - 1);
    }
}
