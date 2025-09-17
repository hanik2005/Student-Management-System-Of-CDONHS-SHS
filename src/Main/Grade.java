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
                new Object[]{"Student ID", "Student Name", "Grade"}
        );

        try {
            Connection conn = MyConnection.getConnection();
            String sql = "SELECT s.student_id, "
                    + "CONCAT(s.last_name, ', ', s.first_name, ' ', COALESCE(s.middle_name, '')) AS student_name, "
                    + "g.grade "
                    + "FROM student s "
                    + "INNER JOIN student_strand ss ON s.student_id = ss.student_id "
                    + "INNER JOIN section sec ON ss.section_id = sec.section_id "
                    + "LEFT JOIN grade_entry g ON g.student_id = s.student_id "
                    + "AND g.section_id = ss.section_id "
                    + "AND g.subject_id = ? "
                    + "AND g.quarter = ? "
                    + "WHERE ss.grade_level = ? "
                    + "AND ss.strand_id = ? "
                    + "AND ss.section_id = ? "
                    + "ORDER BY s.last_name";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, subjectId);
            pst.setInt(2, quarter);
            pst.setInt(3, gradeLevel);
            pst.setInt(4, strandId);
            pst.setInt(5, sectionId);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt("student_id"),
                    rs.getString("student_name"),
                    rs.getObject("grade") // grade may be null if not encoded yet
                };
                model.addRow(row);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }

    public void saveStudentGrades(int subjectId, int sectionId, int quarter, DefaultTableModel model) {
        try {
            Connection conn = MyConnection.getConnection();
            conn.setAutoCommit(false); // For transaction safety

            String checkSql = "SELECT entry_id FROM grade_entry WHERE student_id = ? AND subject_id = ? AND section_id = ? AND quarter = ?";
            String insertSql = "INSERT INTO grade_entry (student_id, subject_id, section_id, quarter, grade) VALUES (?, ?, ?, ?, ?)";
            String updateSql = "UPDATE grade_entry SET grade = ? WHERE entry_id = ?";

            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);

            for (int i = 0; i < model.getRowCount(); i++) {
                int studentId = (int) model.getValueAt(i, 0);
                Object gradeObj = model.getValueAt(i, 2);
                Double grade = gradeObj != null ? Double.parseDouble(gradeObj.toString()) : null;

                // Check if grade entry exists
                checkStmt.setInt(1, studentId);
                checkStmt.setInt(2, subjectId);
                checkStmt.setInt(3, sectionId);
                checkStmt.setInt(4, quarter);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // Update existing grade
                    int entryId = rs.getInt("entry_id");
                    updateStmt.setObject(1, grade);
                    updateStmt.setInt(2, entryId);
                    updateStmt.executeUpdate();
                } else {
                    // Insert new grade
                    insertStmt.setInt(1, studentId);
                    insertStmt.setInt(2, subjectId);
                    insertStmt.setInt(3, sectionId);
                    insertStmt.setInt(4, quarter);
                    insertStmt.setObject(5, grade);
                    insertStmt.executeUpdate();
                }
            }

            conn.commit();
            conn.close();
            JOptionPane.showMessageDialog(null, "Grades saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving grades: " + e.getMessage());
        }
    }

}
