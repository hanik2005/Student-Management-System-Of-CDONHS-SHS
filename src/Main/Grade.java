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
            ResultSet rs = st.executeQuery("select max(id) from grade");
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
}
