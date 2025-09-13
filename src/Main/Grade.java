/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import model.SubjectGrade;
import db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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

    public boolean getDetails(int sid, int gradeLevel) {
        try {
            // Step 1: Get student strand & section from student_strand table joined with strands
            String strandSql = "SELECT ss.student_id, ss.grade_level, ss.section_name, s.strand_name, s.strand_id "
                    + "FROM student_strand ss "
                    + "JOIN strands s ON ss.strand_id = s.strand_id "
                    + "WHERE ss.student_id = ? AND ss.grade_level = ?";

            ps = con.prepareStatement(strandSql);
            ps.setInt(1, sid);
            ps.setInt(2, gradeLevel);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Fill UI
                Home.stuGradeManageID.setText(String.valueOf(rs.getInt("student_id")));
                Home.stuGradeManageGradeLevel.setText(String.valueOf(rs.getInt("grade_level")));
                String strandName = rs.getString("strand_name");
                int strandId = rs.getInt("strand_id");
                Home.stuGradeManageStrand.setText(strandName);
                Home.stuGradeManageSection.setText(rs.getString("section_name"));

                // Step 2: Get subjects dynamically from subject table using strand_id
                String subjectSql = "SELECT subject_name FROM subject "
                        + "WHERE grade_level = ? AND strand_id = ? "
                        + "ORDER BY subject_order ASC";

                PreparedStatement ps2 = con.prepareStatement(subjectSql);
                ps2.setInt(1, gradeLevel);
                ps2.setInt(2, strandId);
                ResultSet rs2 = ps2.executeQuery();

                // Step 3: Fill subjects into UI fields
                int index = 1;
                while (rs2.next() && index <= 8) {
                    String subject = rs2.getString("subject_name");

                    switch (index) {
                        case 1 ->
                            Home.stuGradeManageSub1.setText(subject);
                        case 2 ->
                            Home.stuGradeManageSub2.setText(subject);
                        case 3 ->
                            Home.stuGradeManageSub3.setText(subject);
                        case 4 ->
                            Home.stuGradeManageSub4.setText(subject);
                        case 5 ->
                            Home.stuGradeManageSub5.setText(subject);
                        case 6 ->
                            Home.stuGradeManageSub6.setText(subject);
                        case 7 ->
                            Home.stuGradeManageSub7.setText(subject);
                        case 8 ->
                            Home.stuGradeManageSub8.setText(subject);
                    }
                    index++;
                }

                // Clear any remaining subject fields if there are fewer than 8 subjects
                for (int i = index; i <= 8; i++) {
                    switch (i) {
                        case 1 ->
                            Home.stuGradeManageSub1.setText("");
                        case 2 ->
                            Home.stuGradeManageSub2.setText("");
                        case 3 ->
                            Home.stuGradeManageSub3.setText("");
                        case 4 ->
                            Home.stuGradeManageSub4.setText("");
                        case 5 ->
                            Home.stuGradeManageSub5.setText("");
                        case 6 ->
                            Home.stuGradeManageSub6.setText("");
                        case 7 ->
                            Home.stuGradeManageSub7.setText("");
                        case 8 ->
                            Home.stuGradeManageSub8.setText("");
                    }
                }

                if (index <= 8) {
                    JOptionPane.showMessageDialog(null, "⚠ Not enough subjects found in database for this strand.");
                }

                return true;
            } else {
                JOptionPane.showMessageDialog(null, "❌ Student ID or Grade level doesn't exist.");
            }
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, "Error getting student details", ex);
            JOptionPane.showMessageDialog(null, "❌ Database error: " + ex.getMessage());
        }

        return false;
    }

    public int getSubjectId(String subjectName, int gradeLevel, String strandName) {
        int subjectId = -1; // default if not found

        String sql = "SELECT subject_id FROM subject subj "
                + "JOIN strands s ON subj.strand_id = s.strand_id "
                + "WHERE subj.subject_name = ? AND subj.grade_level = ? AND s.strand_name = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, subjectName);
            ps.setInt(2, gradeLevel);
            ps.setString(3, strandName);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                subjectId = rs.getInt("subject_id");
            }
        } catch (SQLException ex) {
            System.getLogger(Grade.class.getName()).log(System.Logger.Level.ERROR, "Error getting subject ID", ex);
        }

        return subjectId;
    }

    //check student id already exist
    public boolean isstuIdExist(int id) {
        try {
            ps = con.prepareStatement("select * from grade where student_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;

            }
        } catch (SQLException ex) {
            System.getLogger(Grade.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;

    }

    public boolean isSidGradeLevelStrandQuarterExist(int id, int quarter) {
        try {
            ps = con.prepareStatement("SELECT * FROM grade WHERE student_id = ?"
                    + " AND quarter = ?");
            ps.setInt(1, id);
            ps.setInt(2, quarter);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true; // found a record with same sid, grade level, strand, section, and quarter
            }
        } catch (SQLException ex) {
            System.getLogger(Grade.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }

    // Insert new Grade
    public void insert(int studentId, List<Integer> subjectIds, List<Double> grades, int quarter) {
        String sql = "INSERT INTO grade(student_id, subject_id, quarter, grade) VALUES (?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);

            for (int i = 0; i < subjectIds.size(); i++) {
                ps.setInt(1, studentId);
                ps.setInt(2, subjectIds.get(i));
                ps.setInt(3, quarter);
                ps.setDouble(4, grades.get(i));
                ps.addBatch(); // add each insert into batch
            }

            ps.executeBatch(); // insert all at once
            JOptionPane.showMessageDialog(null, "Grades added successfully");

        } catch (SQLException ex) {
            System.getLogger(Grade.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void update(int studentId, List<Integer> subjectIds, List<Double> grades, int quarter) {
        String sql = "UPDATE grade SET grade=? WHERE student_id=? AND subject_id=? AND quarter=?";

        try {
            ps = con.prepareStatement(sql);

            for (int i = 0; i < subjectIds.size(); i++) {
                ps.setDouble(1, grades.get(i));       // grade
                ps.setInt(2, studentId);             // student_id
                ps.setInt(3, subjectIds.get(i));     // subject_id
                ps.setInt(4, quarter);               // quarter
                ps.addBatch();
            }

            ps.executeBatch();
            JOptionPane.showMessageDialog(null, "Grades updated successfully");

        } catch (SQLException ex) {
            System.getLogger(Grade.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void getGradeValue(JTable table, String searchValue) {
        String sql = "SELECT g.student_id, "
                + "ss.grade_level, "
                + "s.strand_name, "
                + "ss.section_name, "
                + "g.quarter, "
                + "AVG(g.grade) AS quarter_average, "
                + "MAX(CASE WHEN subj.subject_order = 1 THEN subj.subject_name END) AS subject1, "
                + "MAX(CASE WHEN subj.subject_order = 1 THEN g.grade END) AS score1, "
                + "MAX(CASE WHEN subj.subject_order = 2 THEN subj.subject_name END) AS subject2, "
                + "MAX(CASE WHEN subj.subject_order = 2 THEN g.grade END) AS score2, "
                + "MAX(CASE WHEN subj.subject_order = 3 THEN subj.subject_name END) AS subject3, "
                + "MAX(CASE WHEN subj.subject_order = 3 THEN g.grade END) AS score3, "
                + "MAX(CASE WHEN subj.subject_order = 4 THEN subj.subject_name END) AS subject4, "
                + "MAX(CASE WHEN subj.subject_order = 4 THEN g.grade END) AS score4, "
                + "MAX(CASE WHEN subj.subject_order = 5 THEN subj.subject_name END) AS subject5, "
                + "MAX(CASE WHEN subj.subject_order = 5 THEN g.grade END) AS score5, "
                + "MAX(CASE WHEN subj.subject_order = 6 THEN subj.subject_name END) AS subject6, "
                + "MAX(CASE WHEN subj.subject_order = 6 THEN g.grade END) AS score6, "
                + "MAX(CASE WHEN subj.subject_order = 7 THEN subj.subject_name END) AS subject7, "
                + "MAX(CASE WHEN subj.subject_order = 7 THEN g.grade END) AS score7, "
                + "MAX(CASE WHEN subj.subject_order = 8 THEN subj.subject_name END) AS subject8, "
                + "MAX(CASE WHEN subj.subject_order = 8 THEN g.grade END) AS score8 "
                + "FROM grade g "
                + "JOIN subject subj ON g.subject_id = subj.subject_id "
                + "LEFT JOIN student_strand ss ON g.student_id = ss.student_id "
                + "LEFT JOIN strands s ON ss.strand_id = s.strand_id "
                + "WHERE CONCAT(g.student_id, ss.grade_level, s.strand_name, ss.section_name, g.quarter) LIKE ? "
                + "GROUP BY g.student_id, ss.grade_level, s.strand_name, ss.section_name, g.quarter "
                + "ORDER BY g.student_id DESC";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + searchValue + "%");
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear old data

            Object[] row;
            while (rs.next()) {
                row = new Object[23];
                row[0] = rs.getInt("student_id");
                row[1] = rs.getInt("grade_level");
                row[2] = rs.getString("strand_name"); // Changed from "strand" to "strand_name"
                row[3] = rs.getString("section_name");

                // subjects + scores
                row[4] = rs.getString("subject1");
                row[5] = rs.getDouble("score1");
                row[6] = rs.getString("subject2");
                row[7] = rs.getDouble("score2");
                row[8] = rs.getString("subject3");
                row[9] = rs.getDouble("score3");
                row[10] = rs.getString("subject4");
                row[11] = rs.getDouble("score4");
                row[12] = rs.getString("subject5");
                row[13] = rs.getDouble("score5");
                row[14] = rs.getString("subject6");
                row[15] = rs.getDouble("score6");
                row[16] = rs.getString("subject7");
                row[17] = rs.getDouble("score7");
                row[18] = rs.getString("subject8");
                row[19] = rs.getDouble("score8");

                // quarter + average
                row[20] = rs.getInt("quarter");
                row[21] = rs.getDouble("quarter_average");

                model.addRow(row);
            }
        } catch (SQLException ex) {
            System.getLogger(Grade.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public Map<Integer, SubjectGrade> getStudentGrades(int studentId, int quarter, int gradeLevel) {
        Map<Integer, SubjectGrade> grades = new HashMap<>();
        String sql = "SELECT subj.subject_order, subj.subject_name, g.grade "
                + "FROM grade g "
                + "JOIN subject subj ON g.subject_id = subj.subject_id "
                + "WHERE g.student_id = ? AND g.quarter = ? AND subj.grade_level = ? "
                + "ORDER BY subj.subject_order";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, quarter);
            ps.setInt(3, gradeLevel);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int order = rs.getInt("subject_order");
                String subjectName = rs.getString("subject_name");
                double grade = rs.getDouble("grade");
                grades.put(order, new SubjectGrade(subjectName, grade));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return grades;
    }
}
