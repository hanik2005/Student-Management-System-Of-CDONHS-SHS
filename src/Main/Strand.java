package Main;

import java.sql.Connection;
import db.MyConnection;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Strand {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;

    // Get next available ID
    public int getMax() {
        int id = 0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(strand_id) FROM strand");
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return id + 1;
    }

    public boolean getId(int id) {
        try {
            ps = con.prepareStatement("select * from student where student_id= ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Home.stuStrandId.setText(String.valueOf(rs.getInt(1)));
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Student Id doesnt exist");

            }
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return false;
    }

//    // Insert new strand
//    public void insert(int id, int sid, String gradeLevel, String strandName, String section) {
//        String sql = "INSERT INTO strand VALUES(?,?,?,?,?)";
//        try {
//            ps = con.prepareStatement(sql);
//            ps.setInt(1, id);
//            ps.setInt(2, sid);
//            ps.setInt(3, Integer.parseInt(gradeLevel));
//            ps.setString(4, strandName);
//            ps.setString(5, section);
//
//            if (ps.executeUpdate() > 0) {
//                JOptionPane.showMessageDialog(null, "New Strand added successfully");
//            }
//        } catch (SQLException ex) {
//            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
//        }
//    }
    public void insert(int id, int sid, int gradeLevel, String strandName, String section) {
        // Fetch the subjects for the given grade level and strand
        List<String> subjects = Subject.getSubjects(gradeLevel, strandName);

        if (subjects.size() < 8) {
            JOptionPane.showMessageDialog(null, "Not enough subjects for this strand and grade level!");
            return;
        }

        String sql = "INSERT INTO strand (strand_id, student_id, grade_level, strand, section_name, subject_1, subject_2, subject_3, subject_4, subject_5, subject_6, subject_7, subject_8) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, sid);
            ps.setInt(3, gradeLevel);
            ps.setString(4, strandName);
            ps.setString(5, section);

            // Insert 8 subjects
            for (int i = 0; i < 8; i++) {
                ps.setString(6 + i, subjects.get(i));
            }

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "New Strand and Subjects added successfully");
            }
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public boolean hasSameGradeAndStrand(int studentId, int gradeLevel, String strandName) {
        String sql = "SELECT * FROM strand WHERE student_id = ? AND grade_level = ? AND strand = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, gradeLevel);
            ps.setString(3, strandName);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if record exists
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }

    public boolean isStudentAlreadyEnrolled(int studentId) {
        String sql = "SELECT * FROM strand WHERE student_id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if record exists
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }


    // Fetch all strands and show in JTable
    public void getStrandValue(JTable table, String searchValue) {
        String sql = "SELECT * FROM strand WHERE CONCAT(strand_id,student_id,grade_level,strand,section_name) LIKE ? ORDER BY strand_id DESC";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + searchValue + "%");
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[5];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

//    // Update strand details
//    public void update(int id, String gradeLevel, String strandName, String section) {
//        String sql = "UPDATE strand SET grade_level=?, strand=?, section_name=? WHERE strand_id=?";
//        try {
//            ps = con.prepareStatement(sql);
//            ps.setString(1, gradeLevel);
//            ps.setString(2, strandName);
//            ps.setString(3, section);
//            ps.setInt(4, id);
//
//            if (ps.executeUpdate() > 0) {
//                JOptionPane.showMessageDialog(null, "Strand data updated successfully");
//            }
//        } catch (SQLException ex) {
//            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
//        }
//    }

    // Delete strand record
    public void delete(int id) {
        int yesOrNo = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this strand?", "Delete Strand", JOptionPane.OK_CANCEL_OPTION, 0);
        if (yesOrNo == JOptionPane.OK_OPTION) {
            try {
                ps = con.prepareStatement("DELETE FROM strand WHERE strand_id=?");
                ps.setInt(1, id);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "Strand deleted successfully");
                }
            } catch (SQLException ex) {
                System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }

    public String getNextSection(String gradeLevel, String strandName) {
        String section = "A";
        String sql = "SELECT COUNT(*) FROM strand WHERE grade_level=? AND strand_id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, gradeLevel);
            ps.setString(2, strandName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                int index = count / 50;

                // Convert index to letters (Excel-style: A, B, ..., Z, AA, AB...)
                StringBuilder sb = new StringBuilder();
                while (index >= 0) {
                    sb.insert(0, (char) ('A' + (index % 26)));
                    index = index / 26 - 1;
                }
                section = sb.toString();
            }
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return section;
    }
}
