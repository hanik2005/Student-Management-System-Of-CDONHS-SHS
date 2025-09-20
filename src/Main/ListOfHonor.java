package Main;

import db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ListOfHonor {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;

    public DefaultTableModel getStudentGrades(
            int gradeLevel, int strandId, int sectionId, int subjectId, String quarterSelection) {

        DefaultTableModel model = new DefaultTableModel(
                null,
                new Object[]{"LRN", "Student Name", "Average"}
        );

        // Base SQL
        StringBuilder sql = new StringBuilder(
                "SELECT s.student_id, s.LRN, "
                + "CONCAT(s.last_name, ', ', s.first_name, ' ', COALESCE(s.middle_name, '')) AS student_name, "
        );

        if ("General Average".equalsIgnoreCase(quarterSelection)) {
            // Average across ALL quarters
            sql.append("AVG(g.grade) AS average_grade ");
        } else {
            // Average for the selected quarter only
            sql.append("AVG(CASE WHEN g.quarter = ? THEN g.grade END) AS average_grade ");
        }

        sql.append("FROM student s "
                + "INNER JOIN student_strand ss ON s.student_id = ss.student_id "
                + "LEFT JOIN grade_entry g ON g.student_id = s.student_id ");

        if (subjectId > 0) {
            sql.append("AND g.subject_id = ? ");
        }

        sql.append("WHERE ss.grade_level = ? "
                + "AND ss.strand_id = ? "
                + "AND ss.section_id = ? "
                + "GROUP BY s.student_id, s.LRN, s.last_name, s.first_name, s.middle_name "
                + "ORDER BY s.last_name");

        try (Connection conn = MyConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(sql.toString())) {

            int idx = 1;

            if (!"General Average".equalsIgnoreCase(quarterSelection)) {
                pst.setInt(idx++, Integer.parseInt(quarterSelection)); // set quarter
            }
            if (subjectId > 0) {
                pst.setInt(idx++, subjectId);
            }
            pst.setInt(idx++, gradeLevel);
            pst.setInt(idx++, strandId);
            pst.setInt(idx++, sectionId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String lrn = rs.getString("LRN");
                    String studentName = rs.getString("student_name");

                    java.math.BigDecimal avgObj = rs.getBigDecimal("average_grade");
                    double avgVal = (avgObj != null) ? avgObj.doubleValue() : 60.0;

                    String avgStr = String.format("%.2f", avgVal);
                    model.addRow(new Object[]{lrn, studentName, avgStr});
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }

}
