package utils;

import com.fasterxml.uuid.Generators;
import entity.Experience;

/**
 *
 * @author rttz159
 */
public class ExperienceBuilder {
    private String experienceId;
    private String desc;
    private Experience.IndustryType industryType;
    private int duration;

    public ExperienceBuilder() {
        this.experienceId = generateUUIDv1();
    }

    private String generateUUIDv1() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    public ExperienceBuilder desc(String desc) {
        this.desc = desc;
        return this;
    }

    public ExperienceBuilder industryType(Experience.IndustryType industryType) {
        this.industryType = industryType;
        return this;
    }

    public ExperienceBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public Experience build() {
        return new Experience(experienceId, desc, industryType, duration);
    }
}