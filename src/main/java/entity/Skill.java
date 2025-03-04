package entity;

/**
 *
 * @author rttz159
 */
public class Skill {
    private String skillId;
    private SkillType skilltype;
    private String name;
    private int proficiencyLevel;

    public Skill(String skillId, SkillType skilltype, String name, int proficiencyLevel) {
        this.skillId = skillId;
        this.skilltype = skilltype;
        this.name = name;
        this.proficiencyLevel = proficiencyLevel;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public SkillType getSkilltype() {
        return skilltype;
    }

    public void setSkilltype(SkillType skilltype) {
        this.skilltype = skilltype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(int proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }
    
    
    public enum SkillType {
        TECHNICAL,
        SOFT,
        LANGUAGE,
        MANAGEMENT,
        ANALYTICAL,
        CREATIVE,
        COMMUNICATION,
        LEADERSHIP,
        PROBLEM_SOLVING,
        PROJECT_MANAGEMENT,
        SALES,
        MARKETING,
        CUSTOMER_SERVICE,
        DATA_ANALYSIS,
        PROGRAMMING,
        DESIGN,
        NETWORKING,
        SECURITY,
        DATABASE_MANAGEMENT,
        CLOUD_COMPUTING
    }
}
