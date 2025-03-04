package entity;

/**
 *
 * @author rttz159
 */
public class Qualification {
    private String qualificationId;
    private QualificationType qualificationType;
    private String desc;
    private int level;
    private String institution;
    private int yearOfComplete;

    public Qualification(String qualificationId, QualificationType qualificationType, String desc, int level, String institution, int yearOfComplete) {
        this.qualificationId = qualificationId;
        this.qualificationType = qualificationType;
        this.desc = desc;
        this.level = level;
        this.institution = institution;
        this.yearOfComplete = yearOfComplete;
    }

    public String getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(String qualificationId) {
        this.qualificationId = qualificationId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public int getYearOfComplete() {
        return yearOfComplete;
    }

    public void setYearOfComplete(int yearOfComplete) {
        this.yearOfComplete = yearOfComplete;
    }

    public QualificationType getQualificationType() {
        return qualificationType;
    }

    public void setQualificationType(QualificationType qualificationType) {
        this.qualificationType = qualificationType;
    }
    
    public enum QualificationType {
        HIGH_SCHOOL_DIPLOMA,
        ASSOCIATE_DEGREE,
        BACHELOR_DEGREE,
        MASTER_DEGREE,
        DOCTORATE,
        CERTIFICATION,
        DIPLOMA,
        PROFESSIONAL_TRAINING,
        VOCATIONAL_TRAINING,
        ONLINE_COURSE
    }
}
