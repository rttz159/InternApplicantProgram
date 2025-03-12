package entity;

/**
 *
 * @author rttz159
 */
public class Experience {

    private String experienceId;
    private String desc;
    private IndustryType industryType;
    private int duration;

    public Experience(String experienceId, String desc, IndustryType industryType, int duration) {
        this.experienceId = experienceId;
        this.desc = desc;
        this.industryType = industryType;
        this.duration = duration;
    }

    public String getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(String experienceId) {
        this.experienceId = experienceId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public IndustryType getIndustryType() {
        return industryType;
    }

    public void setIndustryType(IndustryType industryType) {
        this.industryType = industryType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Experience experience = (Experience) obj;
        return experienceId.equals(experience.experienceId);
    }

    @Override
    public int hashCode() {
        return experienceId.hashCode();
    }

    public enum IndustryType {
        TECHNOLOGY,
        HEALTHCARE,
        FINANCE,
        EDUCATION,
        MANUFACTURING,
        RETAIL,
        CONSTRUCTION,
        ENTERTAINMENT,
        TELECOMMUNICATIONS,
        TRANSPORTATION,
        ENERGY,
        AGRICULTURE,
        PHARMACEUTICAL,
        HOSPITALITY,
        REAL_ESTATE
    }

    public Experience deepCopy() {
        return new Experience(
                this.experienceId,
                this.desc,
                this.industryType,
                this.duration
        );
    }

}
