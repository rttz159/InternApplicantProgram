package utils;

import com.fasterxml.uuid.Generators;
import entity.Qualification;

/**
 *
 * @author rttz159
 */
public class QualificationBuilder {
    private String qualificationId;
    private Qualification.QualificationType qualificationType;
    private String desc;
    private int level;
    private String institution;
    private int yearOfComplete;

    public QualificationBuilder() {
        this.qualificationId = generateUUIDv1();
    }

    private String generateUUIDv1() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    public QualificationBuilder qualificationType(Qualification.QualificationType qualificationType) {
        this.qualificationType = qualificationType;
        return this;
    }

    public QualificationBuilder desc(String desc) {
        this.desc = desc;
        return this;
    }

    public QualificationBuilder level(int level) {
        this.level = level;
        return this;
    }

    public QualificationBuilder institution(String institution) {
        this.institution = institution;
        return this;
    }

    public QualificationBuilder yearOfComplete(int yearOfComplete) {
        this.yearOfComplete = yearOfComplete;
        return this;
    }

    public Qualification build() {
        return new Qualification(qualificationId, qualificationType, desc, level, institution, yearOfComplete);
    }
}