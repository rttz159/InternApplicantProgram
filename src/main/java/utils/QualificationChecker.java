package utils;

import adt.SetInterface;
import entity.Experience;
import entity.Qualification;
import entity.Skill;

/**
 *
 * @author lewis
 */
public class QualificationChecker {

    public static boolean checkQualification(SetInterface<Qualification> studentQualifications, SetInterface<Qualification> postQualifications) {
        return studentQualifications.isSupSetByLevelAttributes(
                postQualifications,
                Qualification::getQualificationType
        );
    }

    public static boolean checkExperience(SetInterface<Experience> studentExperiences, SetInterface<Experience> postExperiences) {
        return studentExperiences.isSupSetByAttributes(
                postExperiences,
                Experience::getIndustryType,
                Experience::getDuration
        );
    }

    public static boolean checkSkill(SetInterface<Skill> studentSkills, SetInterface<Skill> postSkills) {
        return studentSkills.isSupSetByAttributes(
                postSkills,
                Skill::getSkilltype,
                Skill::getProficiencyLevel
        );
    }
}
