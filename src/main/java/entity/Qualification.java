package entity;

/**
 *
 * @author Raymond
 */
public class Qualification {

    private String qualificationId;
    private QualificationType qualificationType;
    private String desc;
    private String institution;
    private int yearOfComplete;

    public Qualification(String qualificationId, QualificationType qualificationType, String desc, String institution, int yearOfComplete) {
        this.qualificationId = qualificationId;
        this.qualificationType = qualificationType;
        this.desc = desc;
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
    
    public int getLevel(){
        return this.qualificationType.level;
    }

    public void setQualificationType(QualificationType qualificationType) {
        this.qualificationType = qualificationType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Qualification qualification = (Qualification) obj;
        return qualificationId.equals(qualification.qualificationId);
    }

    @Override
    public int hashCode() {
        return qualificationId.hashCode();
    }

    public enum QualificationType {
        HIGH_SCHOOL_DIPLOMA(1),
        VOCATIONAL_TRAINING(2),
        ONLINE_COURSE(3),
        CERTIFICATION(4),
        PROFESSIONAL_TRAINING(5),
        ASSOCIATE_DEGREE(6),
        DIPLOMA(7),
        BACHELOR_DEGREE(8),
        MASTER_DEGREE(9),
        DOCTORATE(10);

        private final int level;

        QualificationType(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }

    public Qualification deepCopy() {
        return new Qualification(
                this.qualificationId,
                this.qualificationType,
                this.desc,
                this.institution,
                this.yearOfComplete
        );
    }

}
