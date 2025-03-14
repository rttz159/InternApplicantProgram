package utils;

import adt.SetInterface;
import entity.Experience;
import entity.Qualification;
import entity.Skill;

/**
 *
 * @author rttz159
 */
public class QualificationChecker {

    public static boolean checkQualification(SetInterface<Qualification> studentQualifications, SetInterface<Qualification> postQualifications) {
        if (postQualifications.isEmpty()) {
            return true;
        }

        for (Qualification pq : postQualifications) {
            boolean matched = false;
            for (Qualification q : studentQualifications) {
                if (q.getQualificationType().equals(pq.getQualificationType()) && q.getLevel() >= pq.getLevel()) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                System.out.println("Missing qualification: " + pq.getQualificationType() + " (Required level: " + pq.getLevel() + ")");
                return false;
            }
        }

        return true;
    }

    public static boolean checkExperience(SetInterface<Experience> studentExperiences, SetInterface<Experience> postExperiences) {
        if (postExperiences.isEmpty()) {
            return true;
        }

        for (Experience pe : postExperiences) {
            boolean matched = false;
            for (Experience e : studentExperiences) {
                if (e.getIndustryType().equals(pe.getIndustryType()) && e.getDuration() >= pe.getDuration()) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                System.out.println("Missing experience: " + pe.getIndustryType() + " (Required: " + pe.getDuration() + " years)");
                return false;
            }
        }

        return true;
    }

    public static boolean checkSkill(SetInterface<Skill> studentSkills, SetInterface<Skill> postSkills) {
        if (postSkills.isEmpty()) {
            return true;
        }

        for (Skill ps : postSkills) {
            boolean matched = false;
            for (Skill s : studentSkills) {
                if (s.getSkilltype().equals(ps.getSkilltype()) && s.getProficiencyLevel() >= ps.getProficiencyLevel()) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                System.out.println("Missing skill: " + ps.getSkilltype() + " (Required level: " + ps.getProficiencyLevel() + ")");
                return false;
            }
        }

        return true;
    }

}
