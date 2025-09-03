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
        String sql = "SELECT * FROM general_average WHERE student_id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, sid);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;

            model.setRowCount(0); // clear table before adding new data

            while (rs.next()) {
                row = new Object[10];
                row[0] = rs.getInt("general_average_id");
                row[1] = rs.getInt("student_id");
                row[2] = rs.getInt("grade_level");
                row[3] = rs.getString("strand_name");
                row[4] = rs.getString("section_name");
                row[5] = rs.getDouble("quarter_1_average");
                row[6] = rs.getDouble("quarter_2_average");
                row[7] = rs.getDouble("quarter_3_average");
                row[8] = rs.getDouble("quarter_4_average");
                row[9] = rs.getDouble("final_average");
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
        double generalAverage = 0.0;
        String sql = "SELECT quarter_1_average, quarter_2_average, quarter_3_average, quarter_4_average "
                + "FROM general_average WHERE student_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double q1 = rs.getDouble("quarter_1_average");
                double q2 = rs.getDouble("quarter_2_average");
                double q3 = rs.getDouble("quarter_3_average");
                double q4 = rs.getDouble("quarter_4_average");

                generalAverage = (q1 + q2 + q3 + q4) / 4;
            }

            rs.close();
        } catch (SQLException ex) {
            System.getLogger(MarksSheet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return generalAverage;
    }
}
