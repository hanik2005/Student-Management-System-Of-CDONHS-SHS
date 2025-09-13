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

    public int convertStrandNameToId(String strandName) {
        String sql = "SELECT strand_id FROM strands WHERE strand_name = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, strandName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("strand_id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

   
     //* Check if student exists in student table
 
    public boolean studentExists(int studentId) {
        String sql = "SELECT COUNT(*) FROM student WHERE student_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    
     //* Check if student is already enrolled in the same grade level and strand
   
    public boolean isStudentAlreadyEnrolled(int studentId, int gradeLevel, int strandId) {
        String sql = "SELECT COUNT(*) FROM student_strand WHERE student_id = ? AND grade_level = ? AND strand_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, gradeLevel);
            ps.setInt(3, strandId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Check if student is enrolled in any strand
     */
    public boolean isStudentEnrolledInAnyStrand(int studentId) {
        String sql = "SELECT COUNT(*) FROM student_strand WHERE student_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Insert into student_strand table
     */
    public void insertStudentStrand(int studentId, int strandId, int gradeLevel, String section) {
        String sql = "INSERT INTO student_strand (strand_id, student_id, grade_level, section_name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, strandId);
            ps.setInt(2, studentId);
            ps.setInt(3, gradeLevel);
            ps.setString(4, section);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error enrolling student: " + ex.getMessage());
        }
    }

    /**
     * Load student strands table (replace your strand.getStrandValue method)
     */
    public void loadStudentStrandsTable(JTable table, String search) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing data

        String sql = "SELECT ss.strand_id, ss.student_id, ss.grade_level, s.strand_name, ss.section_name "
                + "FROM student_strand ss "
                + "JOIN strands s ON ss.strand_id = s.strand_id "
                + "WHERE ss.student_id LIKE ? OR s.strand_name LIKE ? "
                + "ORDER BY ss.student_id";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("strand_id"),
                    rs.getInt("student_id"),
                    rs.getInt("grade_level"),
                    rs.getString("strand_name"),
                    rs.getString("section_name")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
