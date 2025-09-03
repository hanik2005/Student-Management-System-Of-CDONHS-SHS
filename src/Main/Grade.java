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
import java.util.List;
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
            ps = con.prepareStatement("SELECT * FROM strand WHERE student_id = ? AND grade_level = ?");
            ps.setInt(1, sid);
            ps.setInt(2, gradeLevel);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Basic student info
                Home.stuGradeManageID.setText(String.valueOf(rs.getInt(2)));
                Home.stuGradeManageGradeLevel.setText(String.valueOf(rs.getInt(3)));
                String strandName = rs.getString(4);
                Home.stuGradeManageStrand.setText(strandName);
                Home.stuGradeManageSection.setText(rs.getString(5));

                // ✅ Get the 8 subjects dynamically based on Grade Level and Strand
                List<String> subjects = Subject.getSubjects(gradeLevel, strandName);

                // ✅ Assign each subject to the corresponding text field
                if (subjects.size() == 8) {
                    Home.stuGradeManageSub1.setText(subjects.get(0));
                    Home.stuGradeManageSub2.setText(subjects.get(1));
                    Home.stuGradeManageSub3.setText(subjects.get(2));
                    Home.stuGradeManageSub4.setText(subjects.get(3));
                    Home.stuGradeManageSub5.setText(subjects.get(4));
                    Home.stuGradeManageSub6.setText(subjects.get(5));
                    Home.stuGradeManageSub7.setText(subjects.get(6));
                    Home.stuGradeManageSub8.setText(subjects.get(7));
                } else {
                    JOptionPane.showMessageDialog(null, "Subjects for this strand are not properly configured.");
                }

                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Student ID or Grade level doesn't exist.");
            }
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return false;
    }

    //check student id already exist
    public boolean isidExist(int id) {
        try {
            ps = con.prepareStatement("select * from grade where grade_id = ?");
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

    public boolean isSidGradeLevelStrandQuarterExist(int id, int gradeLevel, String strand, String section, int quarter) {
        try {
            ps = con.prepareStatement("SELECT * FROM grade WHERE student_id = ?"
                    + " AND grade_level = ? AND strand_name = ? AND section_name = ? AND quarter = ?");
            ps.setInt(1, id);
            ps.setInt(2, gradeLevel);
            ps.setString(3, strand);
            ps.setString(4, section);
            ps.setInt(5, quarter);

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
    public void insert(int id, int sid, int gradeLevel, String strandName, String section, String subject1, String subject2, String subject3, String subject4,
            String subject5, String subject6, String subject7, String subject8, double subGrade1, double subGrade2, double subGrade3, double subGrade4, double subGrade5, double subGrade6,
            double subGrade7, double subGrade8, int quarter, double average) {
        String sql = "INSERT INTO grade VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, sid);
            ps.setInt(3, gradeLevel);
            ps.setString(4, strandName);
            ps.setString(5, section);
            ps.setString(6, subject1);
            ps.setDouble(7, subGrade1);
            ps.setString(8, subject2);
            ps.setDouble(9, subGrade2);
            ps.setString(10, subject3);
            ps.setDouble(11, subGrade3);
            ps.setString(12, subject4);
            ps.setDouble(13, subGrade4);
            ps.setString(14, subject5);
            ps.setDouble(15, subGrade5);
            ps.setString(16, subject6);
            ps.setDouble(17, subGrade6);
            ps.setString(18, subject7);
            ps.setDouble(19, subGrade7);
            ps.setString(20, subject8);
            ps.setDouble(21, subGrade8);
            ps.setInt(22, quarter);
            ps.setDouble(23, average);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Score added successfully");
            }
        } catch (SQLException ex) {
            System.getLogger(Grade.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void update(int id, double subGrade1, double subGrade2, double subGrade3, double subGrade4,
            double subGrade5, double subGrade6, double subGrade7, double subGrade8, int quarter, double average) {

        String sql = "update grade set sub_grade1=?, sub_grade2=?, sub_grade3=?, sub_grade4=?, sub_grade5=?, sub_grade6=?, sub_grade7=?, "
                + "sub_grade8=?, quarter=?, quarter_average=? where grade_id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setDouble(1, subGrade1);
            ps.setDouble(2, subGrade2);
            ps.setDouble(3, subGrade3);
            ps.setDouble(4, subGrade4);
            ps.setDouble(5, subGrade5);
            ps.setDouble(6, subGrade6);
            ps.setDouble(7, subGrade7);
            ps.setDouble(8, subGrade8);
            ps.setInt(9, quarter);
            ps.setDouble(10, average);
            ps.setInt(11, id);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Grade Student data updated successfully ");

            }
        } catch (SQLException ex) {
            System.getLogger(Grade.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    // Fetch all grade and show in JTable
    public void getGradeValue(JTable table, String searchValue) {
        String sql = "SELECT * FROM grade WHERE CONCAT(grade_id,student_id,grade_level,strand_name,section_name) LIKE ? ORDER BY grade_id DESC";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + searchValue + "%");
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[23];
                row[0] = rs.getInt(1);
                row[1] = rs.getInt(2);
                row[2] = rs.getInt(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);//1
                row[6] = rs.getDouble(7);
                row[7] = rs.getString(8);//2
                row[8] = rs.getDouble(9);
                row[9] = rs.getString(10);//3
                row[10] = rs.getDouble(11);
                row[11] = rs.getString(12);//4
                row[12] = rs.getDouble(13);
                row[13] = rs.getString(14);//5
                row[14] = rs.getDouble(15);
                row[15] = rs.getString(16);//6
                row[16] = rs.getDouble(17);
                row[17] = rs.getString(18);//7
                row[18] = rs.getDouble(19);
                row[19] = rs.getString(20);//8
                row[20] = rs.getDouble(21);
                row[21] = rs.getInt(22);//1
                row[22] = rs.getDouble(23);
                //row[6] = rs.getString(5);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            System.getLogger(Grade.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
