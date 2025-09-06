/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author ADMIN
 */
public class MarksSheet {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;

    public int getMax() {
        int id = 0;
        Statement st;

        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(general_average_id) from general_average");
            while (rs.next()) {
                id = rs.getInt(1);

            }
        } catch (SQLException ex) {
            System.getLogger(Grade.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return id + 1;

    }

    //check student id already exist
    public boolean isidExist(int id) {
        try {
            ps = con.prepareStatement("select * from grade where student_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;

            }
        } catch (SQLException ex) {
            System.getLogger(MarksSheet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;

    }
    // Insert new Grade

    public void insert(int id, int sid, int gradeLevel, String strandName, String section, double quarterAv1, double quarterAv2,
            double quarterAv3, double quarterAv4, double finalGrade) {
        String sql = "INSERT INTO general_average VALUES(?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, sid);
            ps.setInt(3, gradeLevel);
            ps.setString(4, strandName);
            ps.setString(5, section);
            ps.setDouble(6, quarterAv1);
            ps.setDouble(7, quarterAv2);
            ps.setDouble(8, quarterAv3);
            ps.setDouble(9, quarterAv4);
            ps.setDouble(10, finalGrade);

            if (ps.executeUpdate() > 0) {
                //JOptionPane.showMessageDialog(null, "Score added successfully");
            }
        } catch (SQLException ex) {
            System.getLogger(MarksSheet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void update(int id, double quarterAv1, double quarterAv2, double quarterAv3, double quarterAv4, double finalGrade) {

        String sql = "update general_average set quarter_1_average=?, "
                + "quarter_2_average=?, quarter_3_average=?, quarter_4_average=?, final_average=? where general_average_id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setDouble(1, quarterAv1);
            ps.setDouble(2, quarterAv2);
            ps.setDouble(3, quarterAv3);
            ps.setDouble(4, quarterAv4);
            ps.setDouble(5, finalGrade);
            ps.setInt(6, id);

            if (ps.executeUpdate() > 0) {
                //JOptionPane.showMessageDialog(null, "Grade Student data updated successfully ");

            }
        } catch (SQLException ex) {
            System.getLogger(Grade.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }
    // ✅ Fetch student final_grade into JTable

    public void getFinalGradeValue(JTable table, int sid) {
        String sql = "SELECT s.grade_level, s.strand AS strand_name, s.section_name "
                + "FROM strand s WHERE s.student_id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, sid);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // clear table before adding new data

            if (rs.next()) {
                int gradeLevel = rs.getInt("grade_level");
                String strand = rs.getString("strand_name");
                String section = rs.getString("section_name");

                Object[] row = new Object[10]; // SID, GradeLevel, Strand, Section, Q1-Q4, FinalAvg, Status
                row[0] = sid;
                row[1] = gradeLevel;
                row[2] = strand;
                row[3] = section;

                boolean allQuartersFilled = true;
                double total = 0;
                int quartersWithGrades = 0;

                // Calculate averages per quarter
                for (int q = 1; q <= 4; q++) {
                    String avgSql = "SELECT AVG(grade) AS quarter_avg FROM grade WHERE student_id = ? AND quarter = ?";
                    PreparedStatement psAvg = con.prepareStatement(avgSql);
                    psAvg.setInt(1, sid);
                    psAvg.setInt(2, q);
                    ResultSet rsAvg = psAvg.executeQuery();
                    if (rsAvg.next()) {
                        double quarterAvg = rsAvg.getDouble("quarter_avg");
                        if (quarterAvg > 0) {
                            row[3 + q] = Math.round(quarterAvg * 100.0) / 100.0; // Q1-Q4 in row[4]-row[7]
                            total += quarterAvg;
                            quartersWithGrades++;
                        } else {
                            row[3 + q] = null;
                            allQuartersFilled = false;
                        }
                    } else {
                        row[3 + q] = null;
                        allQuartersFilled = false;
                    }
                }

                // Compute final average only if all 4 quarters have grades
                Double finalAverage = null;
                if (quartersWithGrades == 4) {
                    finalAverage = Math.round((total / 4) * 100.0) / 100.0;
                }

                // Insert or update final average in general_average table
                String checkSql = "SELECT student_id FROM general_average WHERE student_id = ?";
                PreparedStatement psCheck = con.prepareStatement(checkSql);
                psCheck.setInt(1, sid);
                ResultSet rsCheck = psCheck.executeQuery();

                if (rsCheck.next()) {
                    // Update
                    String updateSql = "UPDATE general_average SET final_average=? WHERE student_id=?";
                    PreparedStatement psUpdate = con.prepareStatement(updateSql);
                    if (finalAverage != null) {
                        psUpdate.setDouble(1, finalAverage);
                    } else {
                        psUpdate.setNull(1, java.sql.Types.DOUBLE);
                    }
                    psUpdate.setInt(2, sid);
                    psUpdate.executeUpdate();
                } else {
                    // Insert
                    String insertSql = "INSERT INTO general_average(student_id, final_average) VALUES(?, ?)";
                    PreparedStatement psInsert = con.prepareStatement(insertSql);
                    psInsert.setInt(1, sid);
                    if (finalAverage != null) {
                        psInsert.setDouble(2, finalAverage);
                    } else {
                        psInsert.setNull(2, java.sql.Types.DOUBLE);
                    }
                    psInsert.executeUpdate();
                }

                // Set final average and status
                row[8] = finalAverage; // Final Average
                if (!allQuartersFilled || finalAverage == null) {
                    row[9] = "Incomplete";
                } else if (finalAverage >= 75) {
                    row[9] = "Pass";
                } else {
                    row[9] = "Fail";
                }

                model.addRow(row);
            }
        } catch (SQLException ex) {
            System.getLogger(MarksSheet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void updateQuarter(int sid, int gradeLevel, String strand, String section, int quarter, double average,
            double finalAverage) {
        String column = "";
        if (quarter == 1) {
            column = "quarter_1_average";
        } else if (quarter == 2) {
            column = "quarter_2_average";
        } else if (quarter == 3) {
            column = "quarter_3_average";
        } else if (quarter == 4) {
            column = "quarter_4_average";
        }

        String sql = "UPDATE general_average SET " + column + "=?, final_average=? WHERE student_id=? AND grade_level=? AND strand_name=? AND section_name=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, average);
            ps.setDouble(2, finalAverage);
            ps.setInt(3, sid);
            ps.setInt(4, gradeLevel);
            ps.setString(5, strand);
            ps.setString(6, section);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isSidGradeLevelStrandQuarterExist(int id, int gradeLevel, String strand, String section) {
        try {
            ps = con.prepareStatement("SELECT * FROM general_average WHERE student_id = ?"
                    + " AND grade_level = ? AND strand_name = ? AND section_name = ?");
            ps.setInt(1, id);
            ps.setInt(2, gradeLevel);
            ps.setString(3, strand);
            ps.setString(4, section);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true; // found a record with same sid, grade level, strand, section, and quarter
            }
        } catch (SQLException ex) {
            System.getLogger(Grade.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }

    public double[] getQuarters(int sid, int gradeLevel, String strand, String section) {
        double[] quarters = new double[4]; // q1, q2, q3, q4

        String sql = "SELECT quarter_1_average, quarter_2_average, quarter_3_average, quarter_4_average "
                + "FROM general_average WHERE student_id=? AND grade_level=? AND strand_name=? AND section_name=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sid);
            ps.setInt(2, gradeLevel);
            ps.setString(3, strand);
            ps.setString(4, section);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    quarters[0] = rs.getDouble("quarter_1_average");
                    quarters[1] = rs.getDouble("quarter_2_average");
                    quarters[2] = rs.getDouble("quarter_3_average");
                    quarters[3] = rs.getDouble("quarter_4_average");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quarters;
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
            System.getLogger(MarksSheet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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
            System.getLogger(MarksSheet.class.getName())
                    .log(System.Logger.Level.ERROR, (String) null, ex);
        }

        // Only compute average if all 4 quarters have grades
        if (filledQuarters == 4) {
            return Math.round((total / 4) * 100.0) / 100.0;
        } else {
            return null; // or 0 if you prefer, but null indicates incomplete
        }
    }

    public void insertUpdateGeneralAverage(int studentId) {
        Double finalAverage = computeFinalAverage(studentId);

        // Only update if all 4 quarters are filled
        if (finalAverage != null) {
            try {
                // Check if row already exists
                String checkSql = "SELECT student_id FROM general_average WHERE student_id = ?";
                PreparedStatement psCheck = con.prepareStatement(checkSql);
                psCheck.setInt(1, studentId);
                ResultSet rs = psCheck.executeQuery();

                if (rs.next()) {
                    // Update existing row
                    String updateSql = "UPDATE general_average SET final_average=? WHERE student_id=?";
                    PreparedStatement psUpdate = con.prepareStatement(updateSql);
                    psUpdate.setDouble(1, finalAverage);
                    psUpdate.setInt(2, studentId);
                    psUpdate.executeUpdate();
                } else {
                    // Insert new row
                    String insertSql = "INSERT INTO general_average(student_id, final_average) VALUES(?, ?)";
                    PreparedStatement psInsert = con.prepareStatement(insertSql);
                    psInsert.setInt(1, studentId);
                    psInsert.setDouble(2, finalAverage);
                    psInsert.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
            ex.printStackTrace();
            return false;
        }
    }

}
