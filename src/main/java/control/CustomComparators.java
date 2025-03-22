package control;

import entity.Application;
import entity.Company;
import entity.Experience;
import entity.InternPost;
import entity.Interview;
import entity.Location;
import entity.Qualification;
import entity.Skill;
import entity.Student;
import entity.User;
import java.util.Comparator;

/**
 *
 * @author rttz159
 */
public class CustomComparators {

    public static class UserByIdComparator implements Comparator<User> {

        @Override
        public int compare(User o1, User o2) {
            return o1.getUserId().compareTo(o2.getUserId());
        }

    }

    public static class StudentByAgeComparator implements Comparator<Student> {

        @Override
        public int compare(Student s1, Student s2) {
            return Integer.compare(s1.getAge(), s2.getAge());
        }
    }

    public static class CompanyByNameComparator implements Comparator<Company> {

        @Override
        public int compare(Company o1, Company o2) {
            return o1.getCompanyName().compareTo(o2.getCompanyName());
        }
    }

    public static class SkillByProficiencyLevelComparator implements Comparator<Skill> {

        @Override
        public int compare(Skill o1, Skill o2) {
            return Integer.compare(o1.getProficiencyLevel(), o2.getProficiencyLevel());
        }

    }

    public static class QualificationBylevelComparator implements Comparator<Qualification> {

        @Override
        public int compare(Qualification o1, Qualification o2) {
            return Integer.compare(o1.getLevel(), o2.getLevel());
        }

    }

    public static class LocationByCityComparator implements Comparator<Location> {

        @Override
        public int compare(Location o1, Location o2) {
            return o1.getState().compareTo(o2.getState());
        }

    }

    public static class InterviewComparator implements Comparator<Interview> {

        @Override
        public int compare(Interview o1, Interview o2) {
            int x = o1.getDate().compareTo(o2.getDate());
            if (x == 0) {
                return o1.getStart_time().compareTo(o2.getStart_time());
            }
            return x;
        }

    }

    public static class InternPostByMinSalaryComparator implements Comparator<InternPost> {

        @Override
        public int compare(InternPost o1, InternPost o2) {
            return o1.getMinMaxSalary().getX().compareTo(o2.getMinMaxSalary().getX());
        }
    }

    public static class InternPostByMaxSalaryComparator implements Comparator<InternPost> {

        @Override
        public int compare(InternPost o1, InternPost o2) {
            return o1.getMinMaxSalary().getY().compareTo(o2.getMinMaxSalary().getY());
        }
    }

    public static class ExperienceByDurationComparator implements Comparator<Experience> {

        @Override
        public int compare(Experience o1, Experience o2) {
            return Integer.compare(o1.getDuration(), o2.getDuration());
        }
    }

    public static class ApplicationByStatusComparator implements Comparator<Application> {

        @Override
        public int compare(Application o1, Application o2) {
            return o1.getStatus().compareTo(o2.getStatus());
        }
    }

}
