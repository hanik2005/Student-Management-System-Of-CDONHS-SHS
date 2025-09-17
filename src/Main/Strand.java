package Main;

import java.sql.Connection;
import db.MyConnection;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
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

    public boolean getIdFromAdmin(int id) {
        try {
            ps = con.prepareStatement("select * from student where student_id= ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                AdminFrame.stuStrandId.setText(String.valueOf(rs.getInt(1)));
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Student Id doesnt exist");

            }
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return false;
    }

    public Object[] getCurrentEnrollment(int studentId) {
        String sql = "SELECT ss.grade_level, s.strand_name, ss.section_name "
                + "FROM student_strand ss "
                + "JOIN strands s ON ss.strand_id = s.strand_id "
                + "WHERE ss.student_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Object[]{
                    rs.getInt("grade_level"), // index 0: grade level
                    rs.getString("strand_name"), // index 1: strand name
                    rs.getString("section_name") // index 2: section name
                };
            }
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, "Error getting current enrollment", ex);
        }

        // Return default values if no enrollment found
        return new Object[]{0, "Not Enrolled", "N/A"};
    }

    public boolean deleteStudentStrandAndGrades(int studentId, int strandId, int gradeLevel) {
        String deleteGradesSQL = """
        DELETE g FROM grade g
        INNER JOIN subject s ON g.subject_id = s.subject_id
        WHERE g.student_id = ? AND s.grade_level IN (11, 12)
    """;

        String deleteStrandSQL = """
        DELETE FROM student_strand
        WHERE student_id = ? AND grade_level IN (11, 12)
    """;

        try (Connection conn = MyConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Delete grades
            try (PreparedStatement ps1 = conn.prepareStatement(deleteGradesSQL)) {
                ps1.setInt(1, studentId);
                int gradesDeleted = ps1.executeUpdate();
                System.out.println("Deleted " + gradesDeleted + " grades for student_id=" + studentId);
            }

            // 2. Delete strand enrollments
            try (PreparedStatement ps2 = conn.prepareStatement(deleteStrandSQL)) {
                ps2.setInt(1, studentId);
                int strandsDeleted = ps2.executeUpdate();
                System.out.println("Deleted " + strandsDeleted + " strand records for student_id=" + studentId);
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getNextSection(int gradeLevel, String strandName) {
        String nextSectionName = "A";
        try {
            int strandId = getStrandIdByName(strandName);
            if (strandId == -1) {
                return nextSectionName;
            }

            // Get all existing sections for this grade and strand, ordered by name
            String sectionSql = "SELECT section_id, section_name FROM section "
                    + "WHERE grade_level = ? AND strand_id = ? ORDER BY section_name";
            PreparedStatement psSection = con.prepareStatement(sectionSql);
            psSection.setInt(1, gradeLevel);
            psSection.setInt(2, strandId);
            ResultSet rsSections = psSection.executeQuery();

            while (rsSections.next()) {
                int sectionId = rsSections.getInt("section_id");
                String sectionName = rsSections.getString("section_name");

                // Count students in this section
                String countSql = "SELECT COUNT(*) FROM student_strand WHERE section_id = ?";
                PreparedStatement psCount = con.prepareStatement(countSql);
                psCount.setInt(1, sectionId);
                ResultSet rsCount = psCount.executeQuery();
                if (rsCount.next()) {
                    int studentCount = rsCount.getInt(1);
                    if (studentCount < 50) {
                        // Found a section with space
                        return sectionName;
                    }
                }
            }

            // If all sections are full, create a new section next in sequence
            // Get last section name
            String lastSectionSql = "SELECT section_name FROM section "
                    + "WHERE grade_level = ? AND strand_id = ? ORDER BY section_name DESC LIMIT 1";
            PreparedStatement psLast = con.prepareStatement(lastSectionSql);
            psLast.setInt(1, gradeLevel);
            psLast.setInt(2, strandId);
            ResultSet rsLast = psLast.executeQuery();

            String lastName = "D"; // default if no section exists
            if (rsLast.next()) {
                lastName = rsLast.getString("section_name");
            }

            // Generate next section name (E, F, ...)
            nextSectionName = generateNextSectionName(lastName);

            // Insert new section into the section table
            String insertSql = "INSERT INTO section (section_name, grade_level, strand_id) VALUES (?, ?, ?)";
            PreparedStatement psInsert = con.prepareStatement(insertSql);
            psInsert.setString(1, nextSectionName);
            psInsert.setInt(2, gradeLevel);
            psInsert.setInt(3, strandId);
            psInsert.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return nextSectionName;
    }

    private String generateNextSectionName(String lastSection) {
        char lastChar = lastSection.charAt(0);
        char nextChar = (char) (lastChar + 1);
        return String.valueOf(nextChar);
    }

    private int getStrandIdByName(String strandName) {
        String sql = "SELECT strand_id FROM strands WHERE strand_name = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, strandName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("strand_id");
            }
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, "Error getting strand ID", ex);
        }
        return -1;
    }

// Helper method to convert index to section name (A, B, C, ..., Z, AA, AB, etc.)
    private String convertIndexToSectionName(int index) {
        if (index < 0) {
            return "A";
        }

        StringBuilder sb = new StringBuilder();
        while (index >= 0) {
            sb.insert(0, (char) ('A' + (index % 26)));
            index = (index / 26) - 1;
        }
        return sb.toString();
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
    public boolean insertStudentStrand(int studentId, int strandId, int gradeLevel, String sectionName) {
        // Validate inputs
        if (gradeLevel < 11 || gradeLevel > 12) {
            System.out.println("Invalid grade level: " + gradeLevel);
            return false;
        }

        if (sectionName == null || sectionName.trim().isEmpty()) {
            System.out.println("Section cannot be empty");
            return false;
        }

        try {
            // Get section_id from section table
            String sectionSql = "SELECT section_id FROM section WHERE grade_level = ? AND strand_id = ? AND section_name = ?";
            PreparedStatement psSection = con.prepareStatement(sectionSql);
            psSection.setInt(1, gradeLevel);
            psSection.setInt(2, strandId);
            psSection.setString(3, sectionName.trim());
            ResultSet rs = psSection.executeQuery();

            if (!rs.next()) {
                System.out.println("Section not found: " + sectionName);
                return false;
            }

            int sectionId = rs.getInt("section_id");

            // Insert into student_strand
            String sql = "INSERT INTO student_strand (strand_id, student_id, grade_level, section_id) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, strandId);
            ps.setInt(2, studentId);
            ps.setInt(3, gradeLevel);
            ps.setInt(4, sectionId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully enrolled Student " + studentId
                        + " in Strand " + strandId
                        + " Grade " + gradeLevel
                        + " Section " + sectionName);
                return true;
            }

        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062 || ex.getMessage().contains("Duplicate entry")) {
                System.out.println("Duplicate enrollment: Student " + studentId
                        + " is already enrolled in Strand " + strandId
                        + " Grade " + gradeLevel);
            } else if (ex.getErrorCode() == 1452) {
                System.out.println("Foreign key violation: Invalid student_id, strand_id, or section_id");
            } else {
                ex.printStackTrace();
            }
        }

        return false;
    }

    public boolean updateStudentStrand(int studentId, int strandId, int gradeLevel, String section) {
        String sql = "UPDATE student_strand SET grade_level = ?, section_name = ? WHERE student_id = ? AND strand_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, gradeLevel);
            ps.setString(2, section);
            ps.setInt(3, studentId);
            ps.setInt(4, strandId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Returns true if update was successful
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, "Error updating student strand", ex);
            return false;
        }
    }

    public boolean isStudentEnrolledInStrandAndGrade(int studentId, int strandId, int gradeLevel) {
        String sql = "SELECT COUNT(*) FROM student_strand WHERE student_id = ? AND strand_id = ? AND grade_level = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, strandId);
            ps.setInt(3, gradeLevel);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            System.getLogger(Strand.class.getName()).log(System.Logger.Level.ERROR, "Error checking strand and grade enrollment", ex);
        }
        return false;
    }

    /**
     * Load student strands table (replace your strand.getStrandValue method)
     */
    public void loadStudentStrandsTable(JTable table, String search) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing data

        String sql = "SELECT ss.student_strand_id, ss.student_id, ss.grade_level, st.strand_name, sec.section_name "
                + "FROM student_strand ss "
                + "JOIN strands st ON ss.strand_id = st.strand_id "
                + "JOIN section sec ON ss.section_id = sec.section_id "
                + "WHERE ss.student_id LIKE ? OR st.strand_name LIKE ? OR sec.section_name LIKE ? "
                + "ORDER BY ss.student_id";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");
            ps.setString(3, "%" + search + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("student_strand_id"),
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
