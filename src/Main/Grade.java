/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import model.SubjectGrade;
import db.MyConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.ComboItem;

/**
 *
 * @author ADMIN
 */
public class Grade {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;

    public int getMax() {
        int id = 0;
        Statement st;

        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(grade_id) from grade");
            while (rs.next()) {
                id = rs.getInt(1);

            }
        } catch (SQLException ex) {
            System.getLogger(Grade.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return id + 1;

    }

    public int getSelectedSectionId(JComboBox sectionBox) {
        ComboItem item = (ComboItem) sectionBox.getSelectedItem();
        return item != null ? item.getId() : -1;
    }

    public int getSelectedSubjectId(JComboBox subjectBox) {
        ComboItem item = (ComboItem) subjectBox.getSelectedItem();
        return item != null ? item.getId() : -1;
    }

    public void loadStrands(JComboBox strandBox, int gradeLevel) {
        strandBox.removeAllItems();
        try (PreparedStatement pst = con.prepareStatement(
                "SELECT strand_id, strand_name FROM strands WHERE strand_id IN "
                + "(SELECT DISTINCT strand_id FROM section WHERE grade_level = ?)")) {

            pst.setInt(1, gradeLevel);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                strandBox.addItem(new ComboItem(rs.getInt("strand_id"), rs.getString("strand_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadSections(JComboBox sectionBox, int strandId, int gradeLevel) {
        sectionBox.removeAllItems();
        try (PreparedStatement pst = con.prepareStatement(
                "SELECT section_id, section_name FROM section WHERE strand_id = ? AND grade_level = ?")) {
            pst.setInt(1, strandId);
            pst.setInt(2, gradeLevel);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                sectionBox.addItem(new ComboItem(rs.getInt("section_id"), rs.getString("section_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadSubjects(JComboBox subjectBox, int strandId, int gradeLevel) {
        subjectBox.removeAllItems();
        try (PreparedStatement pst = con.prepareStatement(
                "SELECT subject_id, subject_name FROM subject WHERE strand_id = ? AND grade_level = ? ORDER BY subject_order")) {
            pst.setInt(1, strandId);
            pst.setInt(2, gradeLevel);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                subjectBox.addItem(new ComboItem(rs.getInt("subject_id"), rs.getString("subject_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DefaultTableModel getStudentGrades(int gradeLevel, int strandId, int sectionId, int subjectId, int quarter) {
        DefaultTableModel model = new DefaultTableModel(
                null,
                new Object[]{"LRN", "Student Name", "Grade"}
        );

        try (Connection conn = MyConnection.getConnection()) {
            String sql = """
            SELECT s.LRN,
                   CONCAT(s.last_name, ', ', s.first_name, ' ', COALESCE(s.middle_name, '')) AS student_name,
                   g.grade
            FROM student s
            INNER JOIN student_strand ss ON s.student_id = ss.student_id
            INNER JOIN section sec ON ss.section_id = sec.section_id
            LEFT JOIN grade_entry g ON g.student_id = s.student_id
                                    AND g.section_id = ss.section_id
                                    AND g.subject_id = ?
                                    AND g.quarter = ?
            WHERE ss.grade_level = ?
              AND ss.strand_id = ?
              AND ss.section_id = ?
            ORDER BY s.last_name
        """;

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, subjectId);
            pst.setInt(2, quarter);
            pst.setInt(3, gradeLevel);
            pst.setInt(4, strandId);
            pst.setInt(5, sectionId);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String lrn = rs.getString("LRN");
                String studentName = rs.getString("student_name");

                BigDecimal gradeObj = (BigDecimal) rs.getObject("grade");
                Double grade = null;
                if (gradeObj != null) {
                    grade = gradeObj.doubleValue();
                }

                // If null, just display 60 but don’t insert yet
                if (grade == null) {
                    grade = 60.00;
                }

                model.addRow(new Object[]{lrn, studentName, grade});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }

//    public void saveStudentGrades(int subjectId, int sectionId, int quarter, DefaultTableModel model) {
//        try (Connection conn = MyConnection.getConnection()) {
//            conn.setAutoCommit(false); // transaction safety
//
//            String checkSql = "SELECT entry_id FROM grade_entry WHERE student_id = ? AND subject_id = ? AND section_id = ? AND quarter = ?";
//            String insertSql = "INSERT INTO grade_entry (student_id, subject_id, section_id, quarter, grade) VALUES (?, ?, ?, ?, ?)";
//            String updateSql = "UPDATE grade_entry SET grade = ? WHERE entry_id = ?";
//
//            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
//            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
//            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
//
//            for (int i = 0; i < model.getRowCount(); i++) {
//                int studentId = (int) model.getValueAt(i, 0);
//                Object gradeObj = model.getValueAt(i, 2);
//
//                // Default to 60 if null
//                Double grade = gradeObj != null ? Double.parseDouble(gradeObj.toString()) : 60.0;
//
//                // ❌ Prevent saving grades below 60
//                if (grade < 60) {
//                    JOptionPane.showMessageDialog(null,
//                            "Grade for Student ID " + studentId + " cannot be below 60.",
//                            "Invalid Grade",
//                            JOptionPane.WARNING_MESSAGE);
//                    conn.rollback(); // cancel transaction
//                    return; // stop saving further
//                }
//
//                // Check if grade entry exists
//                checkStmt.setInt(1, studentId);
//                checkStmt.setInt(2, subjectId);
//                checkStmt.setInt(3, sectionId);
//                checkStmt.setInt(4, quarter);
//                ResultSet rs = checkStmt.executeQuery();
//
//                if (rs.next()) {
//                    // Update existing grade
//                    int entryId = rs.getInt("entry_id");
//                    updateStmt.setDouble(1, grade);
//                    updateStmt.setInt(2, entryId);
//                    updateStmt.executeUpdate();
//                } else {
//                    // Insert new grade
//                    insertStmt.setInt(1, studentId);
//                    insertStmt.setInt(2, subjectId);
//                    insertStmt.setInt(3, sectionId);
//                    insertStmt.setInt(4, quarter);
//                    insertStmt.setDouble(5, grade);
//                    insertStmt.executeUpdate();
//                }
//            }
//
//            conn.commit();
//            JOptionPane.showMessageDialog(null, "Grades saved successfully!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error saving grades: " + e.getMessage());
//        }
//    }
    public void saveStudentGrades(int subjectId, int sectionId, int quarter, DefaultTableModel model) {
        try (Connection conn = MyConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Queries
            String getStudentIdSql = "SELECT student_id FROM student WHERE lrn = ?";
            String checkSql = "SELECT entry_id FROM grade_entry WHERE student_id = ? AND subject_id = ? AND section_id = ? AND quarter = ?";
            String insertSql = "INSERT INTO grade_entry (student_id, subject_id, section_id, quarter, grade) VALUES (?, ?, ?, ?, ?)";
            String updateSql = "UPDATE grade_entry SET grade = ? WHERE entry_id = ?";

            PreparedStatement getStudentIdStmt = conn.prepareStatement(getStudentIdSql);
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);

            for (int i = 0; i < model.getRowCount(); i++) {
                String lrn = model.getValueAt(i, 0).toString(); // first column = LRN
                Object gradeObj = model.getValueAt(i, 2);

                Double grade = gradeObj != null ? Double.parseDouble(gradeObj.toString()) : 60.0;

                // ❌ Prevent saving grades below 60
                if (grade < 60) {
                    JOptionPane.showMessageDialog(null,
                            "Grade for LRN " + lrn + " cannot be below 60.",
                            "Invalid Grade",
                            JOptionPane.WARNING_MESSAGE);
                    conn.rollback();
                    return;
                }

                // 🔎 Find student_id from LRN
                getStudentIdStmt.setString(1, lrn);
                ResultSet studentRs = getStudentIdStmt.executeQuery();
                if (!studentRs.next()) {
                    JOptionPane.showMessageDialog(null,
                            "No student found with LRN: " + lrn,
                            "Student Not Found",
                            JOptionPane.WARNING_MESSAGE);
                    conn.rollback();
                    return;
                }
                int studentId = studentRs.getInt("student_id");

                // Check if grade entry exists
                checkStmt.setInt(1, studentId);
                checkStmt.setInt(2, subjectId);
                checkStmt.setInt(3, sectionId);
                checkStmt.setInt(4, quarter);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    int entryId = rs.getInt("entry_id");
                    updateStmt.setDouble(1, grade);
                    updateStmt.setInt(2, entryId);
                    updateStmt.executeUpdate();
                } else {
                    insertStmt.setInt(1, studentId);
                    insertStmt.setInt(2, subjectId);
                    insertStmt.setInt(3, sectionId);
                    insertStmt.setInt(4, quarter);
                    insertStmt.setDouble(5, grade);
                    insertStmt.executeUpdate();
                }
            }

            conn.commit();
            JOptionPane.showMessageDialog(null, "Grades saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving grades: " + e.getMessage());
        }
    }

    public DefaultTableModel getStudentFormGrades(int studentId, int gradeLevel) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Subject", "First Quarter", "Second Quarter", "Third Quarter", "Fourth Quarter", "Final Grade", "Remarks"}, 0
        );

        String sql = """
        SELECT subj.subject_name,
               MAX(CASE WHEN g.quarter = 1 THEN g.grade END) AS first_quarter,
               MAX(CASE WHEN g.quarter = 2 THEN g.grade END) AS second_quarter,
               MAX(CASE WHEN g.quarter = 3 THEN g.grade END) AS third_quarter,
               MAX(CASE WHEN g.quarter = 4 THEN g.grade END) AS fourth_quarter
        FROM subject subj
        INNER JOIN student_strand ss 
               ON subj.strand_id = ss.strand_id 
              AND subj.grade_level = ss.grade_level
              AND ss.student_id = ?
        LEFT JOIN grade_entry g 
               ON subj.subject_id = g.subject_id 
              AND g.student_id = ss.student_id
        WHERE ss.grade_level = ?
          AND ss.student_id = ?
        GROUP BY subj.subject_id, subj.subject_name
        ORDER BY subj.subject_name
    """;

        double totalFinalGrades = 0.0;
        int subjectsWithFinal = 0;
        boolean allSubjectsComplete = true; // track if every subject has 4 quarters

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, gradeLevel);
            ps.setInt(3, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String subject = rs.getString("subject_name");
                    String q1s = rs.getString("first_quarter");
                    String q2s = rs.getString("second_quarter");
                    String q3s = rs.getString("third_quarter");
                    String q4s = rs.getString("fourth_quarter");

                    String displayQ1 = (q1s == null) ? "N/A" : q1s;
                    String displayQ2 = (q2s == null) ? "N/A" : q2s;
                    String displayQ3 = (q3s == null) ? "N/A" : q3s;
                    String displayQ4 = (q4s == null) ? "N/A" : q4s;

                    double sum = 0.0;
                    int count = 0;

                    try {
                        if (q1s != null) {
                            sum += Double.parseDouble(q1s);
                            count++;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                    try {
                        if (q2s != null) {
                            sum += Double.parseDouble(q2s);
                            count++;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                    try {
                        if (q3s != null) {
                            sum += Double.parseDouble(q3s);
                            count++;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                    try {
                        if (q4s != null) {
                            sum += Double.parseDouble(q4s);
                            count++;
                        }
                    } catch (NumberFormatException ignored) {
                    }

                    String finalGradeStr = "N/A";
                    String remarks = "Pending";

                    if (count > 0) {
                        double finalGradeVal = sum / count;
                        finalGradeStr = String.format("%.2f", finalGradeVal);

                        if (count == 4) {
                            remarks = (finalGradeVal >= 75.0) ? "Passed" : "Failed";
                            totalFinalGrades += finalGradeVal;
                            subjectsWithFinal++;
                        } else {
                            allSubjectsComplete = false; // not all quarters filled for this subject
                        }
                    } else {
                        allSubjectsComplete = false;
                    }

                    model.addRow(new Object[]{subject, displayQ1, displayQ2, displayQ3, displayQ4, finalGradeStr, remarks});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add General Average row ONLY if all subjects have 4 quarters
        if (allSubjectsComplete && subjectsWithFinal > 0) {
            double generalAverage = totalFinalGrades / subjectsWithFinal;
            String generalAverageStr = String.format("%.2f", generalAverage);
            String generalRemarks = (generalAverage >= 75.0) ? "Passed" : "Failed";

            model.addRow(new Object[]{"GENERAL AVERAGE", "", "", "", "", generalAverageStr, generalRemarks});
        }

        return model;
    }
    // Get Strand name by strandId

}
