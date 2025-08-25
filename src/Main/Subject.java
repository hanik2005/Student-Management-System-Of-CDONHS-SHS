package Main;

import java.util.*;

public class Subject {


    private static final Map<String, List<String>> grade11Subjects = new HashMap<>();
    private static final Map<String, List<String>> grade12Subjects = new HashMap<>();

    static {
        // ✅ Grade 11 Subjects (8 each)
        grade11Subjects.put("STEM", Arrays.asList(
            "Oral Communication", "Komunikasyon at Pananaliksik",
            "General Mathematics", "Earth and Life Science",
            "Understanding Culture, Society and Politics",
            "Pre-Calculus", "Basic Calculus", "Chemistry 1"
        ));

        grade11Subjects.put("ABM", Arrays.asList(
            "Oral Communication", "Komunikasyon at Pananaliksik",
            "General Mathematics", "Statistics and Probability",
            "Understanding Culture, Society and Politics",
            "Business Math", "Fundamentals of Accountancy 1", "Organization and Management"
        ));

        grade11Subjects.put("HUMSS", Arrays.asList(
            "Oral Communication", "Komunikasyon at Pananaliksik",
            "General Mathematics", "Statistics and Probability",
            "Understanding Culture, Society and Politics",
            "Creative Writing", "Disciplines and Ideas in Social Sciences", "Introduction to Philosophy"
        ));

        grade11Subjects.put("GAS", Arrays.asList(
            "Oral Communication", "Komunikasyon at Pananaliksik",
            "General Mathematics", "Statistics and Probability",
            "Understanding Culture, Society and Politics",
            "Humanities 1", "Applied Economics", "Organization and Management"
        ));

        grade11Subjects.put("TVL-ICT", Arrays.asList(
            "Oral Communication", "Komunikasyon at Pananaliksik",
            "General Mathematics", "Statistics and Probability",
            "Understanding Culture, Society and Politics",
            "Computer Systems Servicing 1", "Computer Systems Servicing 2", "Computer Systems Servicing 3"
        ));

        grade11Subjects.put("TVL-EIM", Arrays.asList(
            "Oral Communication", "Komunikasyon at Pananaliksik",
            "General Mathematics", "Statistics and Probability",
            "Understanding Culture, Society and Politics",
            "Electrical Installation 1", "Electrical Installation 2", "Electrical Installation 3"
        ));

        grade11Subjects.put("TVL-HE", Arrays.asList(
            "Oral Communication", "Komunikasyon at Pananaliksik",
            "General Mathematics", "Statistics and Probability",
            "Understanding Culture, Society and Politics",
            "Cookery 1", "Cookery 2", "Bread and Pastry Production"
        ));

        // ✅ Grade 12 Subjects (8 each)
        grade12Subjects.put("STEM", Arrays.asList(
            "Reading and Writing", "21st Century Literature",
            "Contemporary Philippine Arts", "Media and Information Literacy",
            "Physical Science", "Physics 2", "Biology 1", "Chemistry 2"
        ));

        grade12Subjects.put("ABM", Arrays.asList(
            "Reading and Writing", "21st Century Literature",
            "Contemporary Philippine Arts", "Media and Information Literacy",
            "Business Ethics", "Fundamentals of Accountancy 2", "Applied Economics", "Business Finance"
        ));

        grade12Subjects.put("HUMSS", Arrays.asList(
            "Reading and Writing", "21st Century Literature",
            "Contemporary Philippine Arts", "Media and Information Literacy",
            "Creative Nonfiction", "Philippine Politics", "Trends and Networks", "Disciplines in Social Science"
        ));

        grade12Subjects.put("GAS", Arrays.asList(
            "Reading and Writing", "21st Century Literature",
            "Contemporary Philippine Arts", "Media and Information Literacy",
            "Creative Writing", "Humanities 2", "Disaster Readiness", "Applied Economics"
        ));

        grade12Subjects.put("TVL-ICT", Arrays.asList(
            "Reading and Writing", "21st Century Literature",
            "Contemporary Philippine Arts", "Media and Information Literacy",
            "Computer Systems Servicing 4", "CSS Project", "Practical Research 1", "Empowerment Technologies"
        ));

        grade12Subjects.put("TVL-EIM", Arrays.asList(
            "Reading and Writing", "21st Century Literature",
            "Contemporary Philippine Arts", "Media and Information Literacy",
            "Electrical Installation 4", "EIM Project", "Practical Research 1", "Empowerment Technologies"
        ));

        grade12Subjects.put("TVL-HE", Arrays.asList(
            "Reading and Writing", "21st Century Literature",
            "Contemporary Philippine Arts", "Media and Information Literacy",
            "Cookery 3", "Food and Beverage Services", "Practical Research 1", "Empowerment Technologies"
        ));
    }

    // ✅ Method to get subjects based on Grade Level and Strand
    public static List<String> getSubjects(int gradeLevel, String strand) {
        strand = strand.toUpperCase(); // Normalize input
        if (gradeLevel == 11) {
            return grade11Subjects.getOrDefault(strand, Collections.emptyList());
        } else if (gradeLevel == 12) {
            return grade12Subjects.getOrDefault(strand, Collections.emptyList());
        }
        return Collections.emptyList();
    }
}


