package com.github.vladbahlai.university.misc;

import com.github.javafaker.Faker;
import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Audience;
import com.github.vladbahlai.university.service.AudienceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = AudienceGenerator.class)
class AudienceGeneratorTest {

    @MockBean
    AudienceService service;

    @Autowired
    AudienceGenerator generator;

    @Test
    void shouldGenerateAudiences() throws UniqueConstraintException {
        Faker faker = new Faker();
        System.out.println(faker.internet().emailAddress());
        generator.generateAudienceData(10, 0, 100);
        verify(service, times(10)).saveAudience(any(Audience.class));
    }

    @Test
    void shouldNotGenerateAudiences() throws UniqueConstraintException {
        generator.generateAudienceData(100, 0, 10);
        verify(service, times(0)).saveAudience(any(Audience.class));
    }

}
