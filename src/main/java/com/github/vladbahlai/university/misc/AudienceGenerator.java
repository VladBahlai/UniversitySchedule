package com.github.vladbahlai.university.misc;

import com.github.vladbahlai.university.exception.UniqueConstraintException;
import com.github.vladbahlai.university.model.Audience;
import com.github.vladbahlai.university.service.AudienceService;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.github.vladbahlai.university.utils.RandomGenerator.generateName;

@Service
public class AudienceGenerator {

    private final AudienceService audienceService;

    public AudienceGenerator(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    public void generateAudienceData(int count, int start, int end) {
        if (end - start >= count) {
            Set<String> audienceNames = generateName(count, start, end);
            audienceNames.forEach(name -> {
                try {
                    audienceService.saveAudience(new Audience(name));
                } catch (UniqueConstraintException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}


