/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import java.util.*;
/**
 *
 * @author ADMIN
 */
public class Strand {
     // Options for your combo boxes
    public static final String[] GRADE_LEVELS = {"Grade 11", "Grade 12"};
    public static final String[] STRANDS = {
        "STEM", "ABM", "HUMSS", "GAS",
        "TVL-ICT", "TVL-HE", "TVL-EIM"
    };
    public static final String[] SECTIONS = {"Section A", "Section B", "Section C"};

    // Subjects: Map<Strand, Map<Grade, List<Subjects>>>
    private static final Map<String, Map<Integer, List<String>>> strandSubjects = new HashMap<>();

    static {
        // --- STEM ---
        Map<Integer, List<String>> stem = new HashMap<>();
        stem.put(11, Arrays.asList(
                "General Mathematics",
                "Earth Science",
                "Oral Communication",
                "Reading and Writing",
                "Komunikasyon at Pananaliksik",
                "Empowerment Technologies",
                "21st Century Literature",
                "Physical Education & Health"
        ));
        stem.put(12, Arrays.asList(
                "Pre-Calculus",
                "Basic Calculus",
                "Physics 1",
                "Physics 2",
                "Practical Research 2",
                "Contemporary Philippine Arts",
                "Media & Information Literacy",
                "PE & Health"
        ));
        strandSubjects.put("STEM", stem);

        // --- ABM ---
        Map<Integer, List<String>> abm = new HashMap<>();
        abm.put(11, Arrays.asList(
                "Business Mathematics",
                "Fundamentals of Accountancy, Business & Management 1",
                "Oral Communication",
                "Komunikasyon at Pananaliksik",
                "Reading and Writing",
                "21st Century Literature",
                "Physical Education & Health",
                "Empowerment Technologies"
        ));
        abm.put(12, Arrays.asList(
                "Applied Economics",
                "Business Ethics & Social Responsibility",
                "Fundamentals of Accountancy, Business & Management 2",
                "Business Finance",
                "Organization & Management",
                "Practical Research 2",
                "Contemporary Philippine Arts",
                "PE & Health"
        ));
        strandSubjects.put("ABM", abm);

        // --- HUMSS ---
        Map<Integer, List<String>> humss = new HashMap<>();
        humss.put(11, Arrays.asList(
                "Creative Writing",
                "Introduction to Philosophy of the Human Person",
                "Oral Communication",
                "Komunikasyon at Pananaliksik",
                "Reading and Writing",
                "21st Century Literature",
                "Physical Education & Health",
                "Empowerment Technologies"
        ));
        humss.put(12, Arrays.asList(
                "Creative Nonfiction",
                "Disciplines and Ideas in Social Sciences",
                "Disciplines and Ideas in Applied Social Sciences",
                "Community Engagement, Solidarity and Citizenship",
                "Philippine Politics & Governance",
                "Trends, Networks & Critical Thinking",
                "Practical Research 2",
                "PE & Health"
        ));
        strandSubjects.put("HUMSS", humss);

        // --- GAS ---
        Map<Integer, List<String>> gas = new HashMap<>();
        gas.put(11, Arrays.asList(
                "General Mathematics",
                "Earth Science",
                "Oral Communication",
                "Komunikasyon at Pananaliksik",
                "Reading and Writing",
                "21st Century Literature",
                "Physical Education & Health",
                "Empowerment Technologies"
        ));
        gas.put(12, Arrays.asList(
                "Humanities 1",
                "Social Science 1",
                "Humanities 2",
                "Social Science 2",
                "Applied Economics",
                "Practical Research 2",
                "Contemporary Philippine Arts",
                "PE & Health"
        ));
        strandSubjects.put("GAS", gas);

        // --- TVL-ICT ---
        Map<Integer, List<String>> tvlICT = new HashMap<>();
        tvlICT.put(11, Arrays.asList(
                "Computer Systems Servicing 1",
                "Computer Programming 1",
                "Oral Communication",
                "Komunikasyon at Pananaliksik",
                "Reading and Writing",
                "21st Century Literature",
                "Physical Education & Health",
                "Empowerment Technologies"
        ));
        tvlICT.put(12, Arrays.asList(
                "Computer Systems Servicing 2",
                "Computer Programming 2",
                "Work Immersion",
                "Practical Research 2",
                "Media & Information Literacy",
                "Contemporary Philippine Arts",
                "Entrepreneurship",
                "PE & Health"
        ));
        strandSubjects.put("TVL-ICT", tvlICT);

        // --- TVL-HE (Home Economics) ---
        Map<Integer, List<String>> tvlHE = new HashMap<>();
        tvlHE.put(11, Arrays.asList(
                "Cookery 1",
                "Bread & Pastry Production 1",
                "Oral Communication",
                "Komunikasyon at Pananaliksik",
                "Reading and Writing",
                "21st Century Literature",
                "Physical Education & Health",
                "Empowerment Technologies"
        ));
        tvlHE.put(12, Arrays.asList(
                "Cookery 2",
                "Bread & Pastry Production 2",
                "Housekeeping",
                "Work Immersion",
                "Practical Research 2",
                "Contemporary Philippine Arts",
                "Entrepreneurship",
                "PE & Health"
        ));
        strandSubjects.put("TVL-HE", tvlHE);

        // --- TVL-EIM (Electrical Installation & Maintenance) ---
        Map<Integer, List<String>> tvlEIM = new HashMap<>();
        tvlEIM.put(11, Arrays.asList(
                "Electrical Installation & Maintenance 1",
                "Basic Electricity",
                "Oral Communication",
                "Komunikasyon at Pananaliksik",
                "Reading and Writing",
                "21st Century Literature",
                "Physical Education & Health",
                "Empowerment Technologies"
        ));
        tvlEIM.put(12, Arrays.asList(
                "Electrical Installation & Maintenance 2",
                "Advanced Electrical Works",
                "Work Immersion",
                "Practical Research 2",
                "Contemporary Philippine Arts",
                "Entrepreneurship",
                "Media & Information Literacy",
                "PE & Health"
        ));
        strandSubjects.put("TVL-EIM", tvlEIM);
    }

    // Method to get subjects
    public static List<String> getSubjects(String strand, int grade) {
        if (strandSubjects.containsKey(strand)) {
            return strandSubjects.get(strand).getOrDefault(grade, new ArrayList<>());
        }
        return new ArrayList<>();
    }
}
