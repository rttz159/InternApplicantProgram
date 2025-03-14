package utils;

import entity.*;
import adt.SetInterface;

/**
 *
 * @author rttz159
 */
public class SimilarityCalculator {

    public static double calculateSimilarity(Student student, InternPost post) {
        double qualificationScore = calculateQualificationScore(student.getStudentQualifications(), post.getInternPostQualifications());
        double experienceScore = calculateExperienceScore(student.getStudentExperiences(), post.getInterPostExperiences());
        double skillScore = calculateSkillScore(student.getStudentSkills(), post.getInternPostSkills());

        double w1 = 0.4;
        double w2 = 0.3;
        double w3 = 0.3;

        return (w1 * qualificationScore) + (w2 * experienceScore) + (w3 * skillScore) ;
    }

    public static double calculateQualificationScore(SetInterface<Qualification> studentQualifications, SetInterface<Qualification> postQualifications) {
        if (postQualifications.isEmpty()) {
            return 1.0;
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
        return (double) totalMatch / postQualifications.size();
    }

    public static double calculateExperienceScore(SetInterface<Experience> studentExperiences, SetInterface<Experience> postExperiences) {
        if (postExperiences.isEmpty()) {
            return 1.0;
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
        return (double) totalMatch / postExperiences.size();
    }

    public static double calculateSkillScore(SetInterface<Skill> studentSkills, SetInterface<Skill> postSkills) {
        if (postSkills.isEmpty()) {
            return 1.0;
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
        return (double) totalMatch / postSkills.size();
    }

    public static double calculateLocationDistance(Location studentLocation, Location postLocation) {
        return Location.MalaysianRegion.distanceBetween(Location.MalaysianRegion.valueOf(studentLocation.getState()), Location.MalaysianRegion.valueOf(postLocation.getState()));
    }
}
