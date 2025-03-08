package control;

import com.fasterxml.uuid.Generators;
import entity.Skill;

/**
 *
 * @author rttz159
 */
public class SkillBuilder {
    private String skillId;
    private Skill.SkillType skillType;
    private String name;
    private int proficiencyLevel;

    public SkillBuilder() {
        this.skillId = generateUUIDv1();
    }

    private String generateUUIDv1() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    public SkillBuilder skillType(Skill.SkillType skillType) {
        this.skillType = skillType;
        return this;
    }

    public SkillBuilder name(String name) {
        this.name = name;
        return this;
    }

    public SkillBuilder proficiencyLevel(int proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
        return this;
    }

    public Skill build() {
        return new Skill(skillId, skillType, name, proficiencyLevel);
    }
}
