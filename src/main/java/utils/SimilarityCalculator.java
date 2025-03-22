package utils;

import entity.*;

/**
 *
 * @author Raymond
 */
public class SimilarityCalculator {

    public static double calculateSimilarity(Student student, InternPost post) {
        double qualificationScore = student.getStudentQualifications().fulfillmentScore(
                post.getInternPostQualifications(),
                Qualification::getQualificationType,
                Qualification::getLevel
        );

        double experienceScore = student.getStudentExperiences().fulfillmentScore(
                post.getInterPostExperiences(),
                Experience::getIndustryType,
                Experience::getDuration
        );

        double skillScore = student.getStudentSkills().fulfillmentScore(
                post.getInternPostSkills(),
                Skill::getSkilltype,
                Skill::getProficiencyLevel
        );

        double w1 = 0.4;
        double w2 = 0.3;
        double w3 = 0.3;

        return (w1 * qualificationScore) + (w2 * experienceScore) + (w3 * skillScore);
    }

    public static double calculateLocationDistance(Location studentLocation, Location postLocation) {
        return Location.MalaysianRegion.distanceBetween(Location.MalaysianRegion.valueOf(studentLocation.getState()), Location.MalaysianRegion.valueOf(postLocation.getState()));
    }
}
