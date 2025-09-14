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

public class MarksSheet {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;

    // Since general_average only has student_id and final_average, we don't need getMax() for IDs
    public boolean isIdExistInGradeLevel(int studentId, int gradeLevel) {
        String sql = "SELECT 1 FROM student_strand WHERE student_id = ? AND grade_level = ? LIMIT 1";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, gradeLevel);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            System.getLogger(MarksSheet.class.getName()).log(System.Logger.Level.ERROR, "Error checking student grade level", ex);
            return false;
        }
    }

    public double getGeneralAverage(int sid) {
        double finalAverage = 0;
        String sql = "SELECT final_average FROM general_average WHERE student_id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, sid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                finalAverage = rs.getDouble("final_average");
            }
        } catch (SQLException ex) {
            System.getLogger(MarksSheet.class.getName()).log(System.Logger.Level.ERROR, "Error getting general average", ex);
        }
        return finalAverage;
    }

    public Double computeFinalAverage(int studentId) {
        double total = 0;
        int filledQuarters = 0;
        String sql = "SELECT AVG(grade) AS quarter_avg FROM grade WHERE student_id = ? AND quarter = ?";

        try {
            for (int q = 1; q <= 4; q++) {
                ps = con.prepareStatement(sql);
                ps.setInt(1, studentId);
                ps.setInt(2, q);
                ResultSet rs = ps.executeQuery();

                if (rs.next() && rs.getDouble("quarter_avg") > 0) {
                    total += rs.getDouble("quarter_avg");
                    filledQuarters++;
                }
            }
        } catch (SQLException ex) {
            System.getLogger(MarksSheet.class.getName()).log(System.Logger.Level.ERROR, "Error computing final average", ex);
        }

        // Only compute average if all 4 quarters have grades
        if (filledQuarters == 4) {
            return Math.round((total / 4) * 100.0) / 100.0;
        } else {
            return null; // null indicates incomplete
        }
    }

    public boolean isGeneralAverageExist(int studentId) {
        try {
            String sql = "SELECT student_id FROM general_average WHERE student_id = ?";
            PreparedStatement psCheck = con.prepareStatement(sql);
            psCheck.setInt(1, studentId);
            ResultSet rs = psCheck.executeQuery();
            return rs.next(); // true if row exists
        } catch (SQLException ex) {
            System.getLogger(MarksSheet.class.getName()).log(System.Logger.Level.ERROR, "Error checking general average", ex);
            return false;
        }
    }

    // Removed methods that are no longer needed due to simplified table structure:
    // - update() method with multiple quarter averages
    // - updateQuarter() method
    // - isSidGradeLevelStrandQuarterExist() method
    // - getQuarters() method
    // New method to get quarter averages for display purposes
    public double[] getQuarterAverages(int studentId) {
        double[] quarters = new double[4]; // q1, q2, q3, q4

        for (int q = 1; q <= 4; q++) {
            String sql = "SELECT AVG(grade) AS quarter_avg FROM grade WHERE student_id = ? AND quarter = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, studentId);
                ps.setInt(2, q);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    quarters[q - 1] = rs.getDouble("quarter_avg");
                }
            } catch (SQLException ex) {
                System.getLogger(MarksSheet.class.getName()).log(System.Logger.Level.ERROR, "Error getting quarter averages", ex);
            }
        }

        return quarters;
    }

    // Get student details with calculated quarter averages
    public ResultSet getStudentMarksDetails(int studentId, int gradeLevel) {
        String sql = "SELECT ss.student_id, subj.grade_level, s.strand_name, ss.section_name, "
                + "AVG(CASE WHEN g.quarter = 1 THEN g.grade END) as quarter1_avg, "
                + "AVG(CASE WHEN g.quarter = 2 THEN g.grade END) as quarter2_avg, "
                + "AVG(CASE WHEN g.quarter = 3 THEN g.grade END) as quarter3_avg, "
                + "AVG(CASE WHEN g.quarter = 4 THEN g.grade END) as quarter4_avg "
                + "FROM student_strand ss "
                + "JOIN strands s ON ss.strand_id = s.strand_id "
                + "LEFT JOIN grade g ON ss.student_id = g.student_id "
                + "JOIN subject subj ON g.subject_id = subj.subject_id " // ✅ link to subject
                + "WHERE ss.student_id = ? AND subj.grade_level = ? " // ✅ filter by chosen grade level
                + "GROUP BY ss.student_id, subj.grade_level, s.strand_name, ss.section_name"; // ✅ use subj.grade_level

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, gradeLevel);
            return ps.executeQuery();
        } catch (SQLException ex) {
            System.getLogger(MarksSheet.class.getName()).log(System.Logger.Level.ERROR, "Error getting student marks details", ex);
            return null;
        }
    }

// Check if student has grades in all quarters
    public boolean hasCompleteQuarters(int studentId) {
        String sql = "SELECT COUNT(DISTINCT quarter) as quarters_count "
                + "FROM grade WHERE student_id = ? AND grade > 0";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("quarters_count") == 4;
            }
        } catch (SQLException ex) {
            System.getLogger(MarksSheet.class.getName()).log(System.Logger.Level.ERROR, "Error checking quarters", ex);
        }
        return false;
    }

// Calculate final average from quarters
    public Double calculateFinalAverageFromQuarters(double q1, double q2, double q3, double q4) {
        if (q1 > 0 && q2 > 0 && q3 > 0 && q4 > 0) {
            return Math.round(((q1 + q2 + q3 + q4) / 4.0) * 100.0) / 100.0;
        }
        return null;
    }

}
