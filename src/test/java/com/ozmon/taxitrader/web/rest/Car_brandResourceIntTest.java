package com.ozmon.taxitrader.web.rest;

import com.ozmon.taxitrader.Application;
import com.ozmon.taxitrader.domain.Car_brand;
import com.ozmon.taxitrader.repository.Car_brandRepository;
import com.ozmon.taxitrader.repository.search.Car_brandSearchRepository;

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
 * Test class for the Car_brandResource REST controller.
 *
 * @see Car_brandResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Car_brandResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private Car_brandRepository car_brandRepository;

    @Inject
    private Car_brandSearchRepository car_brandSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCar_brandMockMvc;

    private Car_brand car_brand;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Car_brandResource car_brandResource = new Car_brandResource();
        ReflectionTestUtils.setField(car_brandResource, "car_brandSearchRepository", car_brandSearchRepository);
        ReflectionTestUtils.setField(car_brandResource, "car_brandRepository", car_brandRepository);
        this.restCar_brandMockMvc = MockMvcBuilders.standaloneSetup(car_brandResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        car_brand = new Car_brand();
        car_brand.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCar_brand() throws Exception {
        int databaseSizeBeforeCreate = car_brandRepository.findAll().size();

        // Create the Car_brand

        restCar_brandMockMvc.perform(post("/api/car_brands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(car_brand)))
                .andExpect(status().isCreated());

        // Validate the Car_brand in the database
        List<Car_brand> car_brands = car_brandRepository.findAll();
        assertThat(car_brands).hasSize(databaseSizeBeforeCreate + 1);
        Car_brand testCar_brand = car_brands.get(car_brands.size() - 1);
        assertThat(testCar_brand.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = car_brandRepository.findAll().size();
        // set the field null
        car_brand.setName(null);

        // Create the Car_brand, which fails.

        restCar_brandMockMvc.perform(post("/api/car_brands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(car_brand)))
                .andExpect(status().isBadRequest());

        List<Car_brand> car_brands = car_brandRepository.findAll();
        assertThat(car_brands).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCar_brands() throws Exception {
        // Initialize the database
        car_brandRepository.saveAndFlush(car_brand);

        // Get all the car_brands
        restCar_brandMockMvc.perform(get("/api/car_brands?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(car_brand.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCar_brand() throws Exception {
        // Initialize the database
        car_brandRepository.saveAndFlush(car_brand);

        // Get the car_brand
        restCar_brandMockMvc.perform(get("/api/car_brands/{id}", car_brand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(car_brand.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCar_brand() throws Exception {
        // Get the car_brand
        restCar_brandMockMvc.perform(get("/api/car_brands/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCar_brand() throws Exception {
        // Initialize the database
        car_brandRepository.saveAndFlush(car_brand);

		int databaseSizeBeforeUpdate = car_brandRepository.findAll().size();

        // Update the car_brand
        car_brand.setName(UPDATED_NAME);

        restCar_brandMockMvc.perform(put("/api/car_brands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(car_brand)))
                .andExpect(status().isOk());

        // Validate the Car_brand in the database
        List<Car_brand> car_brands = car_brandRepository.findAll();
        assertThat(car_brands).hasSize(databaseSizeBeforeUpdate);
        Car_brand testCar_brand = car_brands.get(car_brands.size() - 1);
        assertThat(testCar_brand.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteCar_brand() throws Exception {
        // Initialize the database
        car_brandRepository.saveAndFlush(car_brand);

		int databaseSizeBeforeDelete = car_brandRepository.findAll().size();

        // Get the car_brand
        restCar_brandMockMvc.perform(delete("/api/car_brands/{id}", car_brand.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Car_brand> car_brands = car_brandRepository.findAll();
        assertThat(car_brands).hasSize(databaseSizeBeforeDelete - 1);
    }
}
