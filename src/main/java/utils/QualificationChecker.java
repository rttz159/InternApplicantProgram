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
        int totalMatch = 0;
        for (Qualification q : studentQualifications) {
            for (Qualification pq : postQualifications) {
                if (q.getQualificationType().equals(pq.getQualificationType()) && q.getLevel() >= pq.getLevel()) {
                    totalMatch++;
                    break;
                }
            }
        }
        return totalMatch == postQualifications.size();
    }

    public static boolean checkExperience(SetInterface<Experience> studentExperiences, SetInterface<Experience> postExperiences) {
        if (postExperiences.isEmpty()) {
            return true;
        }
        int totalMatch = 0;
        for (Experience e : studentExperiences) {
            for (Experience pe : postExperiences) {
                if (e.getIndustryType().equals(pe.getIndustryType()) && e.getDuration() >= pe.getDuration()) {
                    totalMatch++;
                    break;
                }
            }
        }
        return totalMatch == postExperiences.size();
    }

    public static boolean checkSkill(SetInterface<Skill> studentSkills, SetInterface<Skill> postSkills) {
        if (postSkills.isEmpty()) {
            return true;
        }
        int totalMatch = 0;
        for (Skill s : studentSkills) {
            for (Skill ps : postSkills) {
                if (s.getSkilltype().equals(ps.getSkilltype()) && s.getProficiencyLevel() >= ps.getProficiencyLevel()) {
                    totalMatch++;
                    break;
                }
            }
        }
        return totalMatch == postSkills.size();
    }
}
