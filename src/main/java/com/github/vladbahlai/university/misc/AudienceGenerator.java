package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.exception.UniqueNameConstraintException;
import com.github.vladbahlai.university.model.Audience;
import com.github.vladbahlai.university.service.AudienceService;
import com.github.vladbahlai.university.utils.RandomGenerator;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AudienceGenerator {

    private final AudienceService audienceService;

    public AudienceGenerator(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    public void generateAudienceData(int count, int start, int end) {
        if (end - start >= count) {
            Set<String> audienceNames = RandomGenerator.generateName(count, start, end);
            audienceNames.forEach(name -> {
                try {
                    audienceService.saveAudience(new Audience(name));
                } catch (UniqueNameConstraintException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}


