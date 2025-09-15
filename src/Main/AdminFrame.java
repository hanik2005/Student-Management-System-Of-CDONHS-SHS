/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Main;

import design.BackgroundPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.spi.DirStateFactory.Result;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author ADMIN
 */
public class AdminFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminFrame.class.getName());

    Student student = new Student();
    Strand strand = new Strand();
    Grade grade = new Grade();
    User user = new User();
    Home home = new Home();
    Teacher teacher = new Teacher();
    MarksSheet marksSheet = new MarksSheet();

    int xx, xy;
    private DefaultTableModel model;
    private String imagePath;
    private int rowIndex;
    NumberFormat nf = NumberFormat.getInstance();
    private String birthCertificatePath;
    private String form137Path;

    /**
     * Creates new form AdminFrame
     */
    public AdminFrame() {
        initComponents();
        init();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void init() {

        //setBackgroundPanel();
        setTime();
        setDate();
        tableViewStudent();
        tableViewTeacher();
        tableViewStrand();
//        tableViewGrades();
//        tableViewFinalGrade();
        stuID.setText(String.valueOf(student.getMax()));
        teacherID.setText(String.valueOf(teacher.getMax()));
        //strandId.setText(String.valueOf(strand.getMax()));
        //idGradeManage.setText(String.valueOf(grade.getMax()));
    }

    public void tableViewStudent() {
        student.getStudentValue(StudentTable, "");
        model = (DefaultTableModel) StudentTable.getModel();
        StudentTable.setRowHeight(30);
        StudentTable.setShowGrid(true);
        StudentTable.setGridColor(Color.black);
        StudentTable.setBackground(Color.white);
    }

    private void tableViewStrand() {
        strand.loadStudentStrandsTable(StudentTrackTable, "");
        model = (DefaultTableModel) StudentTrackTable.getModel();
        StudentTrackTable.setRowHeight(30);
        StudentTrackTable.setShowGrid(true);
        StudentTrackTable.setGridColor(Color.black);
        StudentTrackTable.setBackground(Color.white);
    }

    private void tableViewTeacher() {
        teacher.getTeacherValue(TeacherTable, "");
        model = (DefaultTableModel) TeacherTable.getModel();
        TeacherTable.setRowHeight(30);
        TeacherTable.setShowGrid(true);
        TeacherTable.setGridColor(Color.black);
        TeacherTable.setBackground(Color.white);
    }

    private void clearTeacher() {
        teacherID.setText(String.valueOf(teacher.getMax()));
        teacherFirstName.setText(null);
        teacherMidName.setText(null);
        teacherLastName.setText(null);
        teacherBirth.setDate(null);
        teacherGender.setSelectedIndex(0);
        teacherEmail.setText(null);
        teacherPhone.setText(null);
        teacherAddress1.setText(null);
        teacherAddress2.setText(null);
        teacherImagePanel.setIcon(null);
        teacherStrand.setSelectedIndex(0);
        TeacherTable.clearSelection();
        imagePath = null;

    }

    public boolean isEmptyTeacher() {
        if (teacherFirstName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student name is missing");
            return false;

        }
        if (teacherBirth.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Student date of birth is missing");
            return false;

        }
        if (teacherBirth.getDate().compareTo(new Date()) > 0) {
            JOptionPane.showMessageDialog(this, "No Student from the future are allowed");
            return false;

        }
        if (teacherEmail.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student email is missing");
            return false;
        }
        if (!teacherEmail.getText().matches("^.+@.+\\..+$")) {
            JOptionPane.showMessageDialog(this, "Invalid Email Address");
            return false;
        }
        if (teacherPhone.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student phone number is missing");
            return false;
        }
        if (teacherPhone.getText().length() >= 15) {
            JOptionPane.showMessageDialog(this, "Phone number is to long");
            return false;
        }
        if (teacherAddress1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address Line 1 is missing");
            return false;
        }
        if (teacherAddress2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address Line 2 is missing");
            return false;
        }
        if (imagePath == null) {
            JOptionPane.showMessageDialog(this, "Please add your image");
            return false;
        }
        return true;

    }

    public boolean check() {
        int id = Integer.parseInt(stuID.getText()); // current student ID
        String newEmail = stuEmail.getText();
        String newPhone = stuPhone.getText();
        String oldEmail = model.getValueAt(rowIndex, 4).toString();
        String oldPhone = model.getValueAt(rowIndex, 5).toString();
        if (newEmail.equals(oldEmail) && newPhone.equals(oldPhone)) {
            return false;

        } else {
            if (!newEmail.equals(oldEmail)) {
                boolean x = student.isEmailExist(newEmail, id);
                if (x) {
                    JOptionPane.showMessageDialog(this, "the email already exist");

                }
                return x;
            }
            if (!newPhone.equals(oldPhone)) {
                boolean x = student.isPhoneExist(newPhone, id);
                if (x) {
                    JOptionPane.showMessageDialog(this, "the phone number already exist");

                }
                return x;
            }
//         
        }
        return false;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtTime = new javax.swing.JLabel();
        txtDate = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        stuID = new javax.swing.JTextField();
        stuFname = new javax.swing.JTextField();
        stuMotherName = new javax.swing.JTextField();
        stuAddress1 = new javax.swing.JTextField();
        stuAddress2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        stuBirth = new com.toedter.calendar.JDateChooser();
        jLabel62 = new javax.swing.JLabel();
        stuGender = new javax.swing.JComboBox<>();
        jLabel63 = new javax.swing.JLabel();
        stuEmail = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        stuPhone = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        stuFatherName = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        browseBirthCertificate = new javax.swing.JButton();
        browseForm137 = new javax.swing.JButton();
        stuBirthCer = new javax.swing.JTextField();
        stuForm137 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        stuMiddleName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        stuLastName = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        imagePanel = new javax.swing.JLabel();
        browseImg = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        stuLRN = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        updateBt = new javax.swing.JButton();
        addNewBt = new javax.swing.JButton();
        Clear = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        stuSearchField_1 = new javax.swing.JTextField();
        searchBt_1 = new javax.swing.JButton();
        stuRefresh_1 = new javax.swing.JButton();
        stuSort_1 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        StudentTable = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        delBt = new javax.swing.JButton();
        stuPrint_1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        stuStrandSearchAdminField = new javax.swing.JTextField();
        stuStrandSearchBt = new javax.swing.JButton();
        stuStrandId = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        stuGradeLevel = new javax.swing.JComboBox<>();
        stuStrand = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        stuSection = new javax.swing.JComboBox<>();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        stuSearchField_2 = new javax.swing.JTextField();
        stuSearchBt_2 = new javax.swing.JButton();
        stuRefresh_2 = new javax.swing.JButton();
        stuSort_2 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        StudentTrackTable = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        stuStrandClearBt = new javax.swing.JButton();
        stuPrint_2 = new javax.swing.JButton();
        stuSaveBt = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        teacherID = new javax.swing.JTextField();
        teacherFirstName = new javax.swing.JTextField();
        teacherAddress1 = new javax.swing.JTextField();
        teacherAddress2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        teacherBirth = new com.toedter.calendar.JDateChooser();
        jLabel68 = new javax.swing.JLabel();
        teacherGender = new javax.swing.JComboBox<>();
        jLabel69 = new javax.swing.JLabel();
        teacherEmail = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        teacherPhone = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        teacherMidName = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        teacherLastName = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        teacherStrand = new javax.swing.JComboBox<>();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        teacherImagePanel = new javax.swing.JLabel();
        teacherBrowseImg = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        updateBt1 = new javax.swing.JButton();
        teacherAddNewBt = new javax.swing.JButton();
        teacherClear = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        teacherSearchField_3 = new javax.swing.JTextField();
        teacherSearchBt_2 = new javax.swing.JButton();
        teacherRefresh_3 = new javax.swing.JButton();
        teacherSort_3 = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TeacherTable = new javax.swing.JTable();
        jPanel30 = new javax.swing.JPanel();
        teacherDelBt = new javax.swing.JButton();
        teacherPrint_3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(135, 255, 204));

        jPanel3.setBackground(new java.awt.Color(102, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel3MouseDragged(evt);
            }
        });
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel3MousePressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 43)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("ADMIN PANEL");

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/logo_resized_50x50.jpg"))); // NOI18N

        txtTime.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        txtTime.setForeground(new java.awt.Color(0, 0, 0));

        txtDate.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        txtDate.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 793, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDate, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
                .addGap(144, 144, 144)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setForeground(new java.awt.Color(0, 0, 0));
        jTabbedPane1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(102, 255, 255));

        jPanel4.setBackground(new java.awt.Color(153, 255, 204));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel4.setForeground(new java.awt.Color(0, 0, 0));

        stuID.setEditable(false);
        stuID.setBackground(new java.awt.Color(204, 204, 204));
        stuID.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N

        stuFname.setBackground(java.awt.Color.white);
        stuFname.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N

        stuMotherName.setBackground(java.awt.Color.white);
        stuMotherName.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuMotherName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuMotherNameActionPerformed(evt);
            }
        });

        stuAddress1.setBackground(java.awt.Color.white);
        stuAddress1.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N

        stuAddress2.setBackground(java.awt.Color.white);
        stuAddress2.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuAddress2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuAddress2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Student's ID");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("First Name");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Mother's Name");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Address Line 1");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Address Line 2");

        jLabel61.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(0, 0, 0));
        jLabel61.setText("Date Of Birth");

        stuBirth.setBackground(java.awt.Color.white);
        stuBirth.setDateFormatString("yyyy-MM-dd");

        jLabel62.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(0, 0, 0));
        jLabel62.setText("Gender");

        stuGender.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        stuGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuGenderActionPerformed(evt);
            }
        });

        jLabel63.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(0, 0, 0));
        jLabel63.setText("Email");

        stuEmail.setBackground(java.awt.Color.white);
        stuEmail.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N

        jLabel64.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(0, 0, 0));
        jLabel64.setText("Phone Number");

        stuPhone.setBackground(java.awt.Color.white);
        stuPhone.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                stuPhoneKeyTyped(evt);
            }
        });

        jLabel65.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(0, 0, 0));
        jLabel65.setText("Father's Name");

        stuFatherName.setBackground(java.awt.Color.white);
        stuFatherName.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuFatherName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuFatherNameActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Birth Certificate");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Form 137");

        browseBirthCertificate.setBackground(new java.awt.Color(102, 255, 255));
        browseBirthCertificate.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        browseBirthCertificate.setForeground(new java.awt.Color(0, 0, 0));
        browseBirthCertificate.setText("Browse");
        browseBirthCertificate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseBirthCertificateActionPerformed(evt);
            }
        });

        browseForm137.setBackground(new java.awt.Color(102, 255, 255));
        browseForm137.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        browseForm137.setForeground(new java.awt.Color(0, 0, 0));
        browseForm137.setText("Browse");
        browseForm137.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseForm137ActionPerformed(evt);
            }
        });

        stuBirthCer.setEditable(false);
        stuBirthCer.setBackground(new java.awt.Color(204, 204, 204));
        stuBirthCer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuBirthCerActionPerformed(evt);
            }
        });

        stuForm137.setEditable(false);
        stuForm137.setBackground(new java.awt.Color(204, 204, 204));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Middle Name");

        stuMiddleName.setBackground(java.awt.Color.white);
        stuMiddleName.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Last Name");

        stuLastName.setBackground(java.awt.Color.white);
        stuLastName.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(stuID, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(stuMotherName, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(stuAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel61)
                            .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(stuGender, 0, 133, Short.MAX_VALUE)
                            .addComponent(stuBirth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(stuEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(stuPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(stuFatherName, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(browseBirthCertificate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stuBirthCer))
                            .addComponent(stuAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(browseForm137)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stuForm137))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(31, 31, 31)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stuFname, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stuMiddleName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(stuLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stuID, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(stuFname, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(stuMiddleName, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(stuLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel61)
                    .addComponent(stuBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stuGender, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel63)
                    .addComponent(stuEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel64)
                    .addComponent(stuPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel65)
                    .addComponent(stuFatherName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(stuMotherName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(stuAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(stuAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stuBirthCer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(browseBirthCertificate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stuForm137, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(browseForm137)
                        .addComponent(jLabel14)))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(153, 255, 204));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jPanel9.setBackground(new java.awt.Color(153, 255, 204));
        jPanel9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jPanel14.setBackground(new java.awt.Color(153, 255, 204));
        jPanel14.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel14.setForeground(new java.awt.Color(0, 0, 0));

        jPanel15.setBackground(new java.awt.Color(204, 204, 204));
        jPanel15.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 2, true));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
        );

        browseImg.setBackground(new java.awt.Color(102, 255, 255));
        browseImg.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        browseImg.setForeground(new java.awt.Color(0, 0, 0));
        browseImg.setText("Browse");
        browseImg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseImgActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Image");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(browseImg)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(56, 56, 56)
                        .addComponent(browseImg, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(221, Short.MAX_VALUE))
        );

        jPanel39.setBackground(new java.awt.Color(153, 255, 204));
        jPanel39.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel39.setForeground(new java.awt.Color(0, 0, 0));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("LRN");

        stuLRN.setBackground(java.awt.Color.white);
        stuLRN.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuLRN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                stuLRNKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel16))
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(stuLRN, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stuLRN)
                .addGap(62, 62, 62))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(153, 255, 204));
        jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jButton1.setBackground(new java.awt.Color(102, 255, 255));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("Logout");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        updateBt.setBackground(new java.awt.Color(102, 255, 255));
        updateBt.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        updateBt.setForeground(new java.awt.Color(0, 0, 0));
        updateBt.setText("Update");
        updateBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtActionPerformed(evt);
            }
        });

        addNewBt.setBackground(new java.awt.Color(102, 255, 255));
        addNewBt.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        addNewBt.setForeground(new java.awt.Color(0, 0, 0));
        addNewBt.setText("Add New");
        addNewBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewBtActionPerformed(evt);
            }
        });

        Clear.setBackground(new java.awt.Color(102, 255, 255));
        Clear.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        Clear.setForeground(new java.awt.Color(0, 0, 0));
        Clear.setText("Clear");
        Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addNewBt, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(updateBt, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Clear, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(updateBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addNewBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Student", jPanel2);

        jPanel10.setBackground(new java.awt.Color(153, 255, 255));

        jPanel11.setBackground(new java.awt.Color(153, 255, 204));
        jPanel11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Search Student");

        stuSearchField_1.setBackground(new java.awt.Color(255, 255, 255));

        searchBt_1.setBackground(new java.awt.Color(153, 255, 204));
        searchBt_1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        searchBt_1.setForeground(new java.awt.Color(0, 0, 0));
        searchBt_1.setText("Search");
        searchBt_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBt_1ActionPerformed(evt);
            }
        });

        stuRefresh_1.setBackground(new java.awt.Color(153, 255, 204));
        stuRefresh_1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        stuRefresh_1.setForeground(new java.awt.Color(0, 0, 0));
        stuRefresh_1.setText("Refresh");
        stuRefresh_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuRefresh_1ActionPerformed(evt);
            }
        });

        stuSort_1.setBackground(new java.awt.Color(153, 255, 204));
        stuSort_1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        stuSort_1.setForeground(new java.awt.Color(0, 0, 0));
        stuSort_1.setText("Sort");
        stuSort_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuSort_1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stuSearchField_1, javax.swing.GroupLayout.PREFERRED_SIZE, 687, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(searchBt_1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(stuRefresh_1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(stuSort_1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(stuSearchField_1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBt_1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stuRefresh_1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stuSort_1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel12.setBackground(new java.awt.Color(153, 255, 204));

        StudentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student_ID", "User_ID", "First_Name", "Middle_Name", "Last_Name", "Date Of Birth", "Gender", "Email", "Phone Number", "Father's Name", "Mother's Name", "Address Line 1", "Address Line 2", "Birth Cerificate", "Form137", "Image Path", "LRN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        StudentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StudentTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(StudentTable);

        jPanel13.setBackground(new java.awt.Color(153, 255, 255));
        jPanel13.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 4, true));

        delBt.setBackground(new java.awt.Color(102, 255, 255));
        delBt.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        delBt.setForeground(new java.awt.Color(0, 0, 0));
        delBt.setText("Delete");
        delBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delBtActionPerformed(evt);
            }
        });

        stuPrint_1.setBackground(new java.awt.Color(102, 255, 255));
        stuPrint_1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        stuPrint_1.setForeground(new java.awt.Color(0, 0, 0));
        stuPrint_1.setText("Print");
        stuPrint_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuPrint_1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(368, Short.MAX_VALUE)
                .addComponent(stuPrint_1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(delBt, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(504, 504, 504))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(delBt, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stuPrint_1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1344, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Student Table", jPanel10);

        jPanel6.setBackground(new java.awt.Color(102, 255, 255));

        jPanel7.setBackground(new java.awt.Color(153, 255, 204));
        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel7.setForeground(new java.awt.Color(0, 0, 0));

        jPanel36.setBackground(new java.awt.Color(153, 255, 204));
        jPanel36.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel36.setForeground(new java.awt.Color(0, 0, 0));

        jLabel66.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(0, 0, 0));
        jLabel66.setText("Student's ID");

        stuStrandSearchAdminField.setBackground(new java.awt.Color(255, 255, 255));
        stuStrandSearchAdminField.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        stuStrandSearchAdminField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuStrandSearchAdminFieldActionPerformed(evt);
            }
        });
        stuStrandSearchAdminField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                stuStrandSearchAdminFieldKeyTyped(evt);
            }
        });

        stuStrandSearchBt.setBackground(new java.awt.Color(153, 255, 204));
        stuStrandSearchBt.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        stuStrandSearchBt.setForeground(new java.awt.Color(0, 0, 0));
        stuStrandSearchBt.setText("Search");
        stuStrandSearchBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuStrandSearchBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addComponent(jLabel66)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addComponent(stuStrandSearchAdminField, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stuStrandSearchBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel66)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stuStrandSearchBt, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(stuStrandSearchAdminField))
                .addContainerGap())
        );

        stuStrandId.setEditable(false);
        stuStrandId.setBackground(new java.awt.Color(204, 204, 204));
        stuStrandId.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuStrandId.setForeground(new java.awt.Color(0, 0, 0));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("Student's ID");

        stuGradeLevel.setBackground(new java.awt.Color(255, 255, 255));
        stuGradeLevel.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        stuGradeLevel.setForeground(new java.awt.Color(0, 0, 0));
        stuGradeLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "11", "12" }));
        stuGradeLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuGradeLevelActionPerformed(evt);
            }
        });

        stuStrand.setBackground(new java.awt.Color(255, 255, 255));
        stuStrand.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        stuStrand.setForeground(new java.awt.Color(0, 0, 0));
        stuStrand.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "STEM", "ABM", "HUMSS", "GAS", "TVL-ICT", "TVL-EIM", "TVL-HE" }));
        stuStrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuStrandActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setText("Grade Level");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setText("Strand");

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 0));
        jLabel25.setText("Section");

        stuSection.setBackground(new java.awt.Color(255, 255, 255));
        stuSection.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        stuSection.setForeground(new java.awt.Color(0, 0, 0));
        stuSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuSectionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stuStrandId)
                            .addComponent(stuSection, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stuStrand, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(stuGradeLevel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(6, 6, 6)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stuStrandId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stuGradeLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stuStrand, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stuSection, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
        );

        jPanel16.setBackground(new java.awt.Color(153, 255, 204));
        jPanel16.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jPanel17.setBackground(new java.awt.Color(153, 255, 204));
        jPanel17.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jPanel35.setBackground(new java.awt.Color(153, 255, 204));
        jPanel35.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setText("Search Student");

        stuSearchBt_2.setBackground(new java.awt.Color(153, 255, 204));
        stuSearchBt_2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        stuSearchBt_2.setForeground(new java.awt.Color(0, 0, 0));
        stuSearchBt_2.setText("Search");
        stuSearchBt_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuSearchBt_2ActionPerformed(evt);
            }
        });

        stuRefresh_2.setBackground(new java.awt.Color(153, 255, 204));
        stuRefresh_2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        stuRefresh_2.setForeground(new java.awt.Color(0, 0, 0));
        stuRefresh_2.setText("Refresh");
        stuRefresh_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuRefresh_2ActionPerformed(evt);
            }
        });

        stuSort_2.setBackground(new java.awt.Color(153, 255, 204));
        stuSort_2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        stuSort_2.setForeground(new java.awt.Color(0, 0, 0));
        stuSort_2.setText("Sort");
        stuSort_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuSort_2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stuSearchField_2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stuSearchBt_2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(stuSort_2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stuRefresh_2)
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stuSearchField_2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stuSearchBt_2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stuRefresh_2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stuSort_2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jPanel18.setBackground(new java.awt.Color(153, 255, 204));
        jPanel18.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        StudentTrackTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Strand_ID", "Student_ID", "Grade Level", "Strand", "Section"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(StudentTrackTable);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2)
                    .addContainerGap()))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel20.setBackground(new java.awt.Color(153, 255, 204));
        jPanel20.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jButton2.setBackground(new java.awt.Color(102, 255, 255));
        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setText("Logout");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        stuStrandClearBt.setBackground(new java.awt.Color(102, 255, 255));
        stuStrandClearBt.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        stuStrandClearBt.setForeground(new java.awt.Color(0, 0, 0));
        stuStrandClearBt.setText("Clear");
        stuStrandClearBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuStrandClearBtActionPerformed(evt);
            }
        });

        stuPrint_2.setBackground(new java.awt.Color(102, 255, 255));
        stuPrint_2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        stuPrint_2.setForeground(new java.awt.Color(0, 0, 0));
        stuPrint_2.setText("Print");
        stuPrint_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuPrint_2ActionPerformed(evt);
            }
        });

        stuSaveBt.setBackground(new java.awt.Color(102, 255, 255));
        stuSaveBt.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        stuSaveBt.setForeground(new java.awt.Color(0, 0, 0));
        stuSaveBt.setText("Save");
        stuSaveBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuSaveBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(stuSaveBt, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(stuStrandClearBt, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(stuPrint_2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(stuSaveBt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stuPrint_2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stuStrandClearBt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 342, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Student Strand", jPanel6);

        jPanel19.setBackground(new java.awt.Color(102, 255, 255));

        jPanel21.setBackground(new java.awt.Color(153, 255, 204));
        jPanel21.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel21.setForeground(new java.awt.Color(0, 0, 0));

        teacherID.setEditable(false);
        teacherID.setBackground(new java.awt.Color(204, 204, 204));
        teacherID.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N

        teacherFirstName.setBackground(java.awt.Color.white);
        teacherFirstName.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N

        teacherAddress1.setBackground(java.awt.Color.white);
        teacherAddress1.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N

        teacherAddress2.setBackground(java.awt.Color.white);
        teacherAddress2.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        teacherAddress2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherAddress2ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Teacher's ID");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("First Name");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("Address Line 1");

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setText("Address Line 2");

        jLabel67.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(0, 0, 0));
        jLabel67.setText("Date Of Birth");

        teacherBirth.setBackground(java.awt.Color.white);
        teacherBirth.setDateFormatString("yyyy-MM-dd");

        jLabel68.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(0, 0, 0));
        jLabel68.setText("Gender");

        teacherGender.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        teacherGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        teacherGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherGenderActionPerformed(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(0, 0, 0));
        jLabel69.setText("Email");

        teacherEmail.setBackground(java.awt.Color.white);
        teacherEmail.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N

        jLabel70.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(0, 0, 0));
        jLabel70.setText("Phone Number");

        teacherPhone.setBackground(java.awt.Color.white);
        teacherPhone.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        teacherPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                teacherPhoneKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Middle Name");

        teacherMidName.setBackground(java.awt.Color.white);
        teacherMidName.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N

        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 0));
        jLabel28.setText("Last Name");

        teacherLastName.setBackground(java.awt.Color.white);
        teacherLastName.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N

        jLabel32.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 0));
        jLabel32.setText("Strand");

        teacherStrand.setBackground(new java.awt.Color(255, 255, 255));
        teacherStrand.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        teacherStrand.setForeground(new java.awt.Color(0, 0, 0));
        teacherStrand.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "STEM", "ABM", "HUMSS", "GAS", "TVL-ICT", "TVL-EIM", "TVL-HE" }));
        teacherStrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherStrandActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(teacherID, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(teacherAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel69)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(teacherEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(teacherPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addComponent(teacherAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(31, 31, 31)))
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(teacherFirstName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(teacherMidName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(teacherLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel67)
                                    .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(teacherGender, 0, 133, Short.MAX_VALUE)
                                    .addComponent(teacherBirth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addGap(68, 68, 68)
                                .addComponent(teacherStrand, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teacherID, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(teacherFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(teacherMidName, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(teacherLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel67)
                    .addComponent(teacherBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(teacherGender, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel69)
                    .addComponent(teacherEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel70)
                    .addComponent(teacherPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teacherAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teacherAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(teacherStrand, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel22.setBackground(new java.awt.Color(153, 255, 204));
        jPanel22.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jPanel23.setBackground(new java.awt.Color(153, 255, 204));
        jPanel23.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jPanel24.setBackground(new java.awt.Color(153, 255, 204));
        jPanel24.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel24.setForeground(new java.awt.Color(0, 0, 0));

        jPanel25.setBackground(new java.awt.Color(204, 204, 204));
        jPanel25.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 2, true));

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(teacherImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(teacherImagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
        );

        teacherBrowseImg.setBackground(new java.awt.Color(102, 255, 255));
        teacherBrowseImg.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        teacherBrowseImg.setForeground(new java.awt.Color(0, 0, 0));
        teacherBrowseImg.setText("Browse");
        teacherBrowseImg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherBrowseImgActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 0, 0));
        jLabel29.setText("Image");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel29))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(teacherBrowseImg)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addGap(56, 56, 56)
                        .addComponent(teacherBrowseImg, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(233, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel26.setBackground(new java.awt.Color(153, 255, 204));
        jPanel26.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jButton3.setBackground(new java.awt.Color(102, 255, 255));
        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setText("Logout");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        updateBt1.setBackground(new java.awt.Color(102, 255, 255));
        updateBt1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        updateBt1.setForeground(new java.awt.Color(0, 0, 0));
        updateBt1.setText("Update");
        updateBt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBt1ActionPerformed(evt);
            }
        });

        teacherAddNewBt.setBackground(new java.awt.Color(102, 255, 255));
        teacherAddNewBt.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        teacherAddNewBt.setForeground(new java.awt.Color(0, 0, 0));
        teacherAddNewBt.setText("Add New");
        teacherAddNewBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherAddNewBtActionPerformed(evt);
            }
        });

        teacherClear.setBackground(new java.awt.Color(102, 255, 255));
        teacherClear.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        teacherClear.setForeground(new java.awt.Color(0, 0, 0));
        teacherClear.setText("Clear");
        teacherClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(teacherAddNewBt, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(updateBt1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(teacherClear, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(teacherAddNewBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(teacherClear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateBt1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Teacher", jPanel19);

        jPanel27.setBackground(new java.awt.Color(153, 255, 255));

        jPanel28.setBackground(new java.awt.Color(153, 255, 204));
        jPanel28.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jLabel31.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 0, 0));
        jLabel31.setText("Search Teacher");

        teacherSearchField_3.setBackground(new java.awt.Color(255, 255, 255));

        teacherSearchBt_2.setBackground(new java.awt.Color(153, 255, 204));
        teacherSearchBt_2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        teacherSearchBt_2.setForeground(new java.awt.Color(0, 0, 0));
        teacherSearchBt_2.setText("Search");
        teacherSearchBt_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherSearchBt_2ActionPerformed(evt);
            }
        });

        teacherRefresh_3.setBackground(new java.awt.Color(153, 255, 204));
        teacherRefresh_3.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        teacherRefresh_3.setForeground(new java.awt.Color(0, 0, 0));
        teacherRefresh_3.setText("Refresh");
        teacherRefresh_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherRefresh_3ActionPerformed(evt);
            }
        });

        teacherSort_3.setBackground(new java.awt.Color(153, 255, 204));
        teacherSort_3.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        teacherSort_3.setForeground(new java.awt.Color(0, 0, 0));
        teacherSort_3.setText("Sort");
        teacherSort_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherSort_3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacherSearchField_3, javax.swing.GroupLayout.PREFERRED_SIZE, 687, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(teacherSearchBt_2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(teacherRefresh_3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(teacherSort_3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(teacherSearchField_3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(teacherSearchBt_2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(teacherRefresh_3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(teacherSort_3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel29.setBackground(new java.awt.Color(153, 255, 204));

        TeacherTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Teacher_ID", "User_ID", "First_Name", "Middle_Name", "Last_Name", "Date Of Birth", "Gender", "Email", "Phone Number", "Address Line 1", "Address Line 2", "Strand_name", "hire_date", "Image Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TeacherTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TeacherTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TeacherTable);

        jPanel30.setBackground(new java.awt.Color(153, 255, 255));
        jPanel30.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 4, true));

        teacherDelBt.setBackground(new java.awt.Color(102, 255, 255));
        teacherDelBt.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        teacherDelBt.setForeground(new java.awt.Color(0, 0, 0));
        teacherDelBt.setText("Delete");
        teacherDelBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherDelBtActionPerformed(evt);
            }
        });

        teacherPrint_3.setBackground(new java.awt.Color(102, 255, 255));
        teacherPrint_3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        teacherPrint_3.setForeground(new java.awt.Color(0, 0, 0));
        teacherPrint_3.setText("Print");
        teacherPrint_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherPrint_3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addContainerGap(368, Short.MAX_VALUE)
                .addComponent(teacherPrint_3, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(teacherDelBt, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(504, 504, 504))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teacherDelBt, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(teacherPrint_3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1344, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Teacher Table", jPanel27);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 695, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel3MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_jPanel3MouseDragged

    private void jPanel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MousePressed
        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_jPanel3MousePressed

    private void stuMotherNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuMotherNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuMotherNameActionPerformed

    private void stuAddress2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuAddress2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuAddress2ActionPerformed

    private void stuGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuGenderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuGenderActionPerformed

    private void stuPhoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stuPhoneKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_stuPhoneKeyTyped

    private void stuFatherNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuFatherNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuFatherNameActionPerformed

    private void browseBirthCertificateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseBirthCertificateActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        // PDF filter only
        FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("PDF Documents", "pdf");
        fileChooser.setFileFilter(pdfFilter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            birthCertificatePath = selectedFile.getAbsolutePath(); // keep full path
            String pdfName = selectedFile.getName(); // only filename

            // show only file name in textfield
            stuBirthCer.setText(pdfName);

            // make it look clickable
            stuBirthCer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // add mouse click event to open PDF
            stuBirthCer.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    try {
                        if (Desktop.isDesktopSupported() && birthCertificatePath != null) {
                            Desktop.getDesktop().open(new File(birthCertificatePath));
                        } else {
                            JOptionPane.showMessageDialog(null, "Desktop not supported or path is null.");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Unable to open PDF: " + e.getMessage());
                    }
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "No PDF selected");
        }
    }//GEN-LAST:event_browseBirthCertificateActionPerformed

    private void browseForm137ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseForm137ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        // PDF filter only
        FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("PDF Documents", "pdf");
        fileChooser.setFileFilter(pdfFilter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            form137Path = selectedFile.getAbsolutePath(); // keep full path
            String pdfName = selectedFile.getName(); // only filename

            // show only file name in textfield
            stuForm137.setText(pdfName);

            // make it look clickable
            stuForm137.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // add mouse click event to open PDF
            stuForm137.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    try {
                        if (Desktop.isDesktopSupported() && form137Path != null) {
                            Desktop.getDesktop().open(new File(form137Path));
                        } else {
                            JOptionPane.showMessageDialog(null, "Desktop not supported or path is null.");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Unable to open PDF: " + e.getMessage());
                    }
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "No PDF selected");
        }
    }//GEN-LAST:event_browseForm137ActionPerformed

    private void stuBirthCerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuBirthCerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuBirthCerActionPerformed

    private void browseImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseImgActionPerformed
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("* image", "jpg", "gif", "png");
        file.addChoosableFileFilter(filter);
        int output = file.showSaveDialog(file);
        if (output == JFileChooser.APPROVE_OPTION) {
            File selectFile = file.getSelectedFile();
            String path = selectFile.getAbsolutePath();
            imagePanel.setIcon(home.imageAdjust(path, null, imagePanel));
            imagePath = path;

        } else {
            JOptionPane.showMessageDialog(this, "No image selected");

        }
    }//GEN-LAST:event_browseImgActionPerformed

    private void stuLRNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stuLRNKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_stuLRNKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //qrScanTimer.stop();

        int a = JOptionPane.showConfirmDialog(this, "Do you want to Logout now?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            this.dispose();
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void updateBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtActionPerformed
        if (home.isEmptyStudent()) {
            int id = Integer.parseInt(stuID.getText());
            if (student.isidExist(id)) {
                if (!check()) {
                    String sfname = stuFname.getText();
                    String sMidName = stuMiddleName.getText();
                    String sLastName = stuLastName.getText();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = dateFormat.format(stuBirth.getDate());
                    String gender = stuGender.getSelectedItem().toString();
                    String email = stuEmail.getText();
                    String phone = stuPhone.getText();
                    String motherName = stuMotherName.getText();
                    String fatherName = stuFatherName.getText();
                    String addressLine1 = stuAddress1.getText();
                    String addressLine2 = stuAddress2.getText();
                    String birthCer = stuBirthCer.getText();
                    String form137 = stuForm137.getText();
                    String stuLrn = stuLRN.getText();
                    student.update(id, sfname, sMidName, sLastName, date, gender, email, phone,
                            motherName, fatherName, addressLine1, addressLine2, birthCer, form137, imagePath, stuLrn);

                    StudentTable.setModel(new DefaultTableModel(null, new Object[]{"Student ID", "User_ID", "First Name", "Middle Name", "Last Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name",
                        "Mother's Name", "Address Line 1", "Address Line 2", "Birth Certificate", "Form137", "Image Path", "LRN"}));
                    student.getStudentValue(StudentTable, "");
                    home.clearStudent();

                }

            } else {
                JOptionPane.showMessageDialog(this, "student id doesn't exist");

            }

        }
    }//GEN-LAST:event_updateBtActionPerformed

    private void addNewBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBtActionPerformed
        int id = student.getMax();
        int username = student.getMax();
        int userId = user.getMax();
        if (home.isEmptyStudent()) {
            if (!student.isEmailExist(stuEmail.getText(), id)) {
                if (!student.isPhoneExist(stuPhone.getText(), id)) {

                    String sFname = stuFname.getText();
                    String sMiddleName = stuMiddleName.getText();
                    String sLastName = stuLastName.getText();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = dateFormat.format(stuBirth.getDate());
                    String gender = stuGender.getSelectedItem().toString();
                    String email = stuEmail.getText();
                    String phone = stuPhone.getText();
                    String motherName = stuMotherName.getText();
                    String fatherName = stuFatherName.getText();
                    String addressLine1 = stuAddress1.getText();
                    String addressLine2 = stuAddress2.getText();
                    String birthCer = stuBirthCer.getText();
                    String form137 = stuForm137.getText();
                    String stuLrn = stuLRN.getText();

                    SimpleDateFormat passFormat = new SimpleDateFormat("yyyyMMdd");
                    String birthForPass = passFormat.format(stuBirth.getDate());
                    String password = sLastName.toLowerCase() + birthForPass;
                    int type_id = 2; // student

                    user.insert(userId, username, password, type_id);

                    student.insert(id, userId, sFname, sMiddleName, sLastName, date, gender, email, phone,
                            motherName, fatherName, addressLine1, addressLine2, birthCer, form137, imagePath, stuLrn);

                    //                    String qrContent = "ID: " + id
                    //                            + "\nName: " + sname
                    //                            + "\nBirthdate: " + date
                    //                            + "\nGender: " + gender
                    //                            + "\nEmail: " + email
                    //                            + "\nPhone: " + phone
                    //                            + "\nMother: " + motherName
                    //                            + "\nFather: " + fatherName
                    //                            + "\nAddress 1: " + addressLine1
                    //                            + "\nAddress 2: " + addressLine2
                    //                            + "\nBirth Certificate: " + birthCer
                    //                            + "\nForm 137: " + form137
                    //                            + "\nImage: " + imagePath;
                    //
                    //                    // ✅ Generate QR code with all details
                    //                    generateQRCode(id, sname, qrContent);
                    StudentTable.setModel(new DefaultTableModel(null, new Object[]{"Student ID", "User_ID", "First Name", "Middle Name", "Last Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name",
                        "Mother's Name", "Address Line 1", "Address Line 2", "Birth Certificate", "Form137", "Image Path", "LRN"}));
                    student.getStudentValue(StudentTable, stuSearchField_1.getText());
                    home.clearStudent();
                } else {
                    JOptionPane.showMessageDialog(this, "This phone number already exist");

                }

            } else {
                JOptionPane.showMessageDialog(this, "This email already exist");
            }

        }
    }//GEN-LAST:event_addNewBtActionPerformed

    private void ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearActionPerformed
        home.clearStudent();
    }//GEN-LAST:event_ClearActionPerformed

    private void searchBt_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBt_1ActionPerformed
        if (stuSearchField_1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Search field is empty");

        } else {
            StudentTable.setModel(new DefaultTableModel(null, new Object[]{"Student ID", "User_ID", "First Name", "Middle Name", "Last Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name",
                "Mother's Name", "Address Line 1", "Address Line 2", "Birth Certificate", "Form137", "Image Path", "LRN"}));
            student.getStudentValue(StudentTable, stuSearchField_1.getText());

        }
    }//GEN-LAST:event_searchBt_1ActionPerformed

    private void stuRefresh_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuRefresh_1ActionPerformed
        StudentTable.setModel(new DefaultTableModel(null, new Object[]{"Student ID", "User_ID", "First Name", "Middle Name", "Last Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name",
            "Mother's Name", "Address Line 1", "Address Line 2", "Birth Certificate", "Form137", "Image Path", "LRN"}));
        student.getStudentValue(StudentTable, "");
        stuSearchField_1.setText(null);
    }//GEN-LAST:event_stuRefresh_1ActionPerformed

    private void stuSort_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuSort_1ActionPerformed
        DefaultTableModel model = (DefaultTableModel) StudentTable.getModel();

        // Attach TableRowSorter to your table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        StudentTable.setRowSorter(sorter);

        // Example: Sort by Student ID (numerically) and then by Student Name (alphabetically)
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int studentIdCol = 0;  // Student ID is column 0
        int studentNameCol = 1; // Student Name is column 1

        // First sort by ID (ascending)
        sortKeys.add(new RowSorter.SortKey(studentIdCol, SortOrder.ASCENDING));

        // Then sort by Name (ascending)
        sortKeys.add(new RowSorter.SortKey(studentNameCol, SortOrder.ASCENDING));

        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }//GEN-LAST:event_stuSort_1ActionPerformed

    private void StudentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StudentTableMouseClicked
        model = (DefaultTableModel) StudentTable.getModel();
        rowIndex = StudentTable.getSelectedRow();
        stuID.setText(model.getValueAt(rowIndex, 0).toString());
        stuFname.setText(model.getValueAt(rowIndex, 2).toString());
        stuMiddleName.setText(model.getValueAt(rowIndex, 3).toString());
        stuLastName.setText(model.getValueAt(rowIndex, 4).toString());

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(rowIndex, 5).toString());
            stuBirth.setDate(date);
        } catch (ParseException ex) {
            System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        String gender = model.getValueAt(rowIndex, 6).toString();
        if (gender.equals("Male")) {
            stuGender.setSelectedIndex(0);

        } else {
            stuGender.setSelectedIndex(1);

        }
        stuEmail.setText(model.getValueAt(rowIndex, 7).toString());
        stuPhone.setText(model.getValueAt(rowIndex, 8).toString());
        stuFatherName.setText(model.getValueAt(rowIndex, 9).toString());
        stuMotherName.setText(model.getValueAt(rowIndex, 10).toString());
        stuAddress1.setText(model.getValueAt(rowIndex, 11).toString());
        stuAddress2.setText(model.getValueAt(rowIndex, 12).toString());
        stuBirthCer.setText(model.getValueAt(rowIndex, 13).toString());
        stuForm137.setText(model.getValueAt(rowIndex, 14).toString());
        String path = model.getValueAt(rowIndex, 15).toString();
        imagePath = path;
        imagePanel.setIcon(home.imageAdjust(path, null, imagePanel));//get image path and called image adjust method path to image
        stuLRN.setText(model.getValueAt(rowIndex, 16).toString());
    }//GEN-LAST:event_StudentTableMouseClicked

    private void delBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delBtActionPerformed
        int id = Integer.parseInt(stuID.getText());
        if (student.isidExist(id)) {
            student.delete(id);
            user.delete(id);
            StudentTable.setModel(new DefaultTableModel(null, new Object[]{"Student ID", "User_ID", "First Name", "Middle Name", "Last Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name",
                "Mother's Name", "Address Line 1", "Address Line 2", "Birth Certificate", "Form137", "Image Path", "LRN"}));
            student.getStudentValue(StudentTable, "");
            home.clearStudent();

        } else {
            JOptionPane.showMessageDialog(this, "the student doesn't exist");
        }
    }//GEN-LAST:event_delBtActionPerformed

    private void stuPrint_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuPrint_1ActionPerformed
        try {
            MessageFormat header = new MessageFormat("Students Information");
            MessageFormat footer = new MessageFormat("Page{0,number,integer}");
            StudentTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException ex) {
            System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_stuPrint_1ActionPerformed

    private void stuStrandSearchAdminFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuStrandSearchAdminFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuStrandSearchAdminFieldActionPerformed

    private void stuStrandSearchAdminFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stuStrandSearchAdminFieldKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_stuStrandSearchAdminFieldKeyTyped

    private void stuStrandSearchBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuStrandSearchBtActionPerformed
        if (stuStrandSearchAdminField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student id");

        } else {
            System.out.println(stuStrandSearchAdminField.getText());
            int id = Integer.parseInt(stuStrandSearchAdminField.getText());
            strand.getId(id);
            stuGradeLevel.setSelectedIndex(0);

        }
    }//GEN-LAST:event_stuStrandSearchBtActionPerformed

    public void updateSection() {
        String gradeLevel = (String) stuGradeLevel.getSelectedItem();
        String strand = (String) stuStrand.getSelectedItem();

        if (gradeLevel != null && strand != null) {
            Strand strandObj = new Strand();
            String section = strandObj.getNextSection(gradeLevel, strand);

            stuSection.removeAllItems(); // Clear old items
            stuSection.addItem(section); // Set new section
        }
    }
    private void stuGradeLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuGradeLevelActionPerformed
       updateSection();

        //stuStrand.addItem("TVL-ICT"); //how to add item inside combo box
    }//GEN-LAST:event_stuGradeLevelActionPerformed

    private void stuStrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuStrandActionPerformed
        updateSection();
    }//GEN-LAST:event_stuStrandActionPerformed

    private void stuSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuSectionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuSectionActionPerformed

    private void stuSearchBt_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuSearchBt_2ActionPerformed
        if (stuSearchField_2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Search field is empty");

        } else {
            StudentTrackTable.setModel(new DefaultTableModel(null, new Object[]{"ID", "Student_ID", "Grade_Level", "Strand", "Section"}));
            strand.loadStudentStrandsTable(StudentTrackTable, stuSearchField_2.getText());

        }
    }//GEN-LAST:event_stuSearchBt_2ActionPerformed

    private void stuRefresh_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuRefresh_2ActionPerformed
        StudentTrackTable.setModel(new DefaultTableModel(null, new Object[]{"ID", "Student_ID", "Grade_Level", "Strand", "Section"}));
        strand.loadStudentStrandsTable(StudentTrackTable, "");
        stuSearchField_2.setText(null);
    }//GEN-LAST:event_stuRefresh_2ActionPerformed

    private void stuSort_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuSort_2ActionPerformed
        DefaultTableModel model = (DefaultTableModel) StudentTrackTable.getModel();

        // Attach TableRowSorter to your table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        StudentTrackTable.setRowSorter(sorter);

        // Example: Sort by Student ID (numerically) and then by Student Name (alphabetically)
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int IdCol = 0;  // Student ID is column 0
        int studentStrandIdCol = 1; // Student Name is column 1

        // First sort by ID (ascending)
        sortKeys.add(new RowSorter.SortKey(IdCol, SortOrder.ASCENDING));

        // Then sort by Name (ascending)
        sortKeys.add(new RowSorter.SortKey(studentStrandIdCol, SortOrder.ASCENDING));

        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }//GEN-LAST:event_stuSort_2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int a = JOptionPane.showConfirmDialog(this, "Do you want to Logout now?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            this.dispose();
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void stuStrandClearBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuStrandClearBtActionPerformed
        home.clearStrand();
    }//GEN-LAST:event_stuStrandClearBtActionPerformed

    private void stuPrint_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuPrint_2ActionPerformed
//        try {
//            // === 1. Ask user to select a strand ===
//            String[] strands = {"STEM", "ABM", "HUMSS", "TVL-ICT", "TVL-EIM", "TVL-HE", "GAS"};
//            String selectedStrand = (String) JOptionPane.showInputDialog(
//                this,
//                "Select Strand to Print:",
//                "Choose Strand",
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                strands,
//                strands[0] // default
//            );
//
//            if (selectedStrand == null) {
//                return; // user cancelled
//            }
//
//            // === 2. Create new model for combined report ===
//            DefaultTableModel combinedModel = new DefaultTableModel(
//                new Object[]{"Student ID", "Student Name", "Gender", "Strand", "Grade Level", "Section"}, 0);
//
//            DefaultTableModel studentModel = (DefaultTableModel) StudentTable.getModel();
//            DefaultTableModel trackModel = (DefaultTableModel) StudentTrackTable.getModel();
//
//            // === 2a. Get column indexes ===
//            int trackIdCol = getColumnIndex(trackModel, "Student_ID");
//            int trackStrandCol = getColumnIndex(trackModel, "Strand");
//            int gradeLevelCol = getColumnIndex(trackModel, "Grade Level");
//            int sectionCol = getColumnIndex(trackModel, "Section");
//
//            int studentIdCol = getColumnIndex(studentModel, "Student_ID");
//            int firstNameCol = getColumnIndex(studentModel, "First_Name");
//            int middleNameCol = getColumnIndex(studentModel, "Middle_Name");
//            int lastNameCol = getColumnIndex(studentModel, "Last_Name");
//            int genderCol = getColumnIndex(studentModel, "Gender");
//
//            // === 3. Loop and join tables ===
//            for (int i = 0; i < studentModel.getRowCount(); i++) {
//                Object studentId = studentModel.getValueAt(i, studentIdCol);
//                String studentName = studentModel.getValueAt(i, firstNameCol) + " "
//                + studentModel.getValueAt(i, middleNameCol) + " "
//                + studentModel.getValueAt(i, lastNameCol);
//                Object gender = studentModel.getValueAt(i, genderCol);
//
//                Object strand = "";
//                Object gradeLevel = "";
//                Object section = "";
//
//                for (int j = 0; j < trackModel.getRowCount(); j++) {
//                    Object trackId = trackModel.getValueAt(j, trackIdCol);
//                    Object trackStrand = trackModel.getValueAt(j, trackStrandCol);
//
//                    if (studentId.toString().equals(trackId.toString())
//                        && selectedStrand.equalsIgnoreCase(trackStrand.toString().trim())) {
//                        strand = trackStrand;
//                        gradeLevel = trackModel.getValueAt(j, gradeLevelCol);
//                        section = trackModel.getValueAt(j, sectionCol);
//
//                        combinedModel.addRow(new Object[]{studentId, studentName, gender, strand, gradeLevel, section});
//                        break;
//                    }
//                }
//            }
//
//            if (combinedModel.getRowCount() == 0) {
//                JOptionPane.showMessageDialog(this, "No students found for strand: " + selectedStrand);
//                return;
//            }
//
//            // === 4. Ask user where to save the PDF ===
//            JFileChooser fileChooser = new JFileChooser();
//            fileChooser.setDialogTitle("Save PDF Report");
//            fileChooser.setSelectedFile(new java.io.File("StudentReport_" + selectedStrand + ".pdf"));
//
//            int userSelection = fileChooser.showSaveDialog(this);
//            if (userSelection != JFileChooser.APPROVE_OPTION) {
//                return; // user cancelled
//            }
//
//            java.io.File pdfFile = fileChooser.getSelectedFile();
//
//            // Ensure file has .pdf extension
//            if (!pdfFile.getName().toLowerCase().endsWith(".pdf")) {
//                pdfFile = new java.io.File(pdfFile.getAbsolutePath() + ".pdf");
//            }
//
//            // === 5. Generate PDF ===
//            Document document = new Document();
//            PdfWriter.getInstance(document, new java.io.FileOutputStream(pdfFile));
//            document.open();
//
//            // Title
//            document.add(new com.itextpdf.text.Paragraph("Student Information Report - " + selectedStrand));
//            document.add(new com.itextpdf.text.Paragraph(" "));
//
//            // Table
//            PdfPTable pdfTable = new PdfPTable(combinedModel.getColumnCount());
//
//            // Add headers
//            for (int col = 0; col < combinedModel.getColumnCount(); col++) {
//                pdfTable.addCell(new com.itextpdf.text.Phrase(combinedModel.getColumnName(col)));
//            }
//
//            // Add rows
//            for (int row = 0; row < combinedModel.getRowCount(); row++) {
//                for (int col = 0; col < combinedModel.getColumnCount(); col++) {
//                    Object value = combinedModel.getValueAt(row, col);
//                    pdfTable.addCell(new com.itextpdf.text.Phrase(value != null ? value.toString() : ""));
//                }
//            }
//
//            document.add(pdfTable);
//            document.close();
//
//            JOptionPane.showMessageDialog(this, "PDF saved successfully:\n" + pdfFile.getAbsolutePath());
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//        }
    }//GEN-LAST:event_stuPrint_2ActionPerformed

    private void stuSaveBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuSaveBtActionPerformed
        if (stuStrandId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID is missing");
            return;
        }

        try {
            int studentId = Integer.parseInt(stuStrandId.getText());
            int gradeLevel = Integer.parseInt(stuGradeLevel.getSelectedItem().toString());
            String strandName = stuStrand.getSelectedItem().toString();
            String sectionSelect = stuSection.getSelectedItem().toString();

            int strandId = strand.convertStrandNameToId(strandName);
            if (strandId == -1) {
                JOptionPane.showMessageDialog(this, "Invalid strand selected");
                return;
            }

            // Check if student exists
            if (!strand.studentExists(studentId)) {
                JOptionPane.showMessageDialog(this, "Student with ID " + studentId + " does not exist");
                return;
            }

            // Check if student already has this exact enrollment (same strand + same grade level)
            if (strand.isStudentEnrolledInStrandAndGrade(studentId, strandId, gradeLevel)) {
                Object[] currentEnrollment = strand.getCurrentEnrollment(studentId);
                String currentStrand = (String) currentEnrollment[1];
                String currentSection = (String) currentEnrollment[2];

                JOptionPane.showMessageDialog(this,
                        "Student is already enrolled in " + currentStrand
                        + " Grade " + gradeLevel
                        + " Section " + currentSection);
                return;
            }

            // Check if student is already enrolled in ANY strand
            if (strand.isStudentEnrolledInAnyStrand(studentId)) {
                // Get current enrollment details
                Object[] currentEnrollment = strand.getCurrentEnrollment(studentId);
                int currentGradeLevel = (int) currentEnrollment[0];
                String currentStrand = (String) currentEnrollment[1];
                String currentSection = (String) currentEnrollment[2];

                // Check if it's the same strand but different grade level (promotion)
                if (currentStrand.equals(strandName)) {
                    // Same strand, different grade level - PROMOTION (INSERT NEW RECORD)
                    int response = JOptionPane.showConfirmDialog(
                            this,
                            "Student Promotion:\n\n"
                            + "Current: " + currentStrand + " - Grade " + currentGradeLevel + " - Section " + currentSection
                            + "\nNew: " + strandName + " - Grade " + gradeLevel + " - Section " + sectionSelect
                            + "\n\nPromote student to next grade level?\n"
                            + "(A new enrollment record will be created)",
                            "Confirm Student Promotion",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (response == JOptionPane.YES_OPTION) {
                        // INSERT new enrollment record for promotion
                        boolean success = strand.insertStudentStrand(studentId, strandId, gradeLevel, sectionSelect);
                        if (success) {
                            JOptionPane.showMessageDialog(this, "Student promoted to Grade " + gradeLevel);
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to promote student");
                        }
                    }
                } else {
                    // Different strand - TRANSFER
                    int response = JOptionPane.showConfirmDialog(
                            this,
                            "Student Strand Transfer:\n\n"
                            + "Student ID: " + studentId
                            + "\nCurrent: " + currentStrand + " - Grade " + currentGradeLevel + " - Section " + currentSection
                            + "\nNew: " + strandName + " - Grade " + gradeLevel + " - Section " + sectionSelect
                            + "\n\nConfirm to transfer this student?\n"
                            + "⚠ This will DELETE all existing records in " + currentStrand
                            + " (Grade " + currentGradeLevel + ") before inserting new enrollment.",
                            "Confirm Strand Transfer",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (response == JOptionPane.YES_OPTION) {
                        // ✅ Get current strandId
                        int currentStrandId = strand.convertStrandNameToId(currentStrand);

                        // ✅ Delete old grades + strand under this strandId and grade level
                        boolean deleted = strand.deleteStudentStrandAndGrades(studentId, currentStrandId, currentGradeLevel);

                        if (deleted) {
                            // ✅ Insert new enrollment for transfer
                            boolean success = strand.insertStudentStrand(studentId, strandId, gradeLevel, sectionSelect);
                            if (success) {
                                JOptionPane.showMessageDialog(this,
                                        "Student ID " + studentId + " transferred successfully:\n"
                                        + "From " + currentStrand + " (Grade " + currentGradeLevel + ")\n"
                                        + "To " + strandName + " (Grade " + gradeLevel + ")");
                            } else {
                                JOptionPane.showMessageDialog(this, "Failed to insert new enrollment after transfer");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to transfer student (delete failed)");
                        }
                    }
                }
            } else {
                // New enrollment - student not enrolled in any strand yet
                int response = JOptionPane.showConfirmDialog(
                        this,
                        "New Student Enrollment:\n\n"
                        + "Student ID: " + studentId
                        + "\nStrand: " + strandName
                        + "\nGrade Level: " + gradeLevel
                        + "\nSection: " + sectionSelect
                        + "\n\nConfirm enrollment?",
                        "Confirm New Enrollment",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (response == JOptionPane.YES_OPTION) {
                    boolean success = strand.insertStudentStrand(studentId, strandId, gradeLevel, sectionSelect);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Student enrolled in " + strandName);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to enroll student");
                    }
                }
            }

            // Refresh table and clear fields
            StudentTrackTable.setModel(new DefaultTableModel(null, new Object[]{"ID", "Student_ID", "Grade_Level", "Strand", "Section"}));
            strand.loadStudentStrandsTable(StudentTrackTable, "");
            home.clearStrand();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric Student ID");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }//GEN-LAST:event_stuSaveBtActionPerformed

    private void teacherAddress2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherAddress2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teacherAddress2ActionPerformed

    private void teacherGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherGenderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teacherGenderActionPerformed

    private void teacherPhoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_teacherPhoneKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_teacherPhoneKeyTyped

    private void teacherBrowseImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherBrowseImgActionPerformed
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("* image", "jpg", "gif", "png");
        file.addChoosableFileFilter(filter);
        int output = file.showSaveDialog(file);
        if (output == JFileChooser.APPROVE_OPTION) {
            File selectFile = file.getSelectedFile();
            String path = selectFile.getAbsolutePath();
            teacherImagePanel.setIcon(home.imageAdjust(path, null, teacherImagePanel));
            imagePath = path;

        } else {
            JOptionPane.showMessageDialog(this, "No image selected");

        }
    }//GEN-LAST:event_teacherBrowseImgActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int a = JOptionPane.showConfirmDialog(this, "Do you want to Logout now?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            this.dispose();
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void updateBt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBt1ActionPerformed
        if (isEmptyTeacher()) {
            int id = Integer.parseInt(teacherID.getText());
            if (teacher.isidExist(id)) {
                if (!check()) {
                    String tfname = teacherFirstName.getText();
                    String tMidName = teacherMidName.getText();
                    String tLastName = teacherLastName.getText();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = dateFormat.format(teacherBirth.getDate());
                    String gender = teacherGender.getSelectedItem().toString();
                    String email = teacherEmail.getText();
                    String phone = teacherPhone.getText();
                    String addressLine1 = teacherAddress1.getText();
                    String addressLine2 = teacherAddress2.getText();
                    String teacherStrandOption = teacherStrand.getSelectedItem().toString();
                    int strandId = strand.convertStrandNameToId(teacherStrandOption);
                    
                    teacher.update(id, tfname, tMidName, tLastName, date, gender, email, phone, addressLine1, addressLine2, imagePath, strandId);

                    TeacherTable.setModel(new DefaultTableModel(null, new Object[]{"Teacher ID", "User_ID", "First Name", "Middle Name", "Last Name", "Date of Birth", "Gender", "Email", "Phone Number",
                        "Address Line 1", "Address Line 2", "Strand Name", "hire_date", "Image Path"}));
                    teacher.getTeacherValue(TeacherTable, "");
                    clearTeacher();

                }

            } else {
                JOptionPane.showMessageDialog(this, "teacher id doesn't exist");

            }

        }
    }//GEN-LAST:event_updateBt1ActionPerformed

    private void teacherAddNewBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherAddNewBtActionPerformed
        int id = teacher.getMax();
        int username = teacher.getMax();
        int userId = user.getMax();
        if (isEmptyTeacher()) {
            if (!teacher.isEmailExist(teacherEmail.getText(), id)) {
                if (!student.isPhoneExist(teacherPhone.getText(), id)) {

                    String tFname = teacherFirstName.getText();
                    String tMiddleName = teacherMidName.getText();
                    String tLastName = teacherLastName.getText();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = dateFormat.format(teacherBirth.getDate());
                    String gender = teacherGender.getSelectedItem().toString();
                    String email = teacherEmail.getText();
                    String phone = teacherPhone.getText();
                    String addressLine1 = teacherAddress1.getText();
                    String addressLine2 = teacherAddress2.getText();
                    String teacherStrandOption = teacherStrand.getSelectedItem().toString();
                    int strandId = strand.convertStrandNameToId(teacherStrandOption);
                    LocalDate hire_date = LocalDate.now();
                    String hire_date_String = hire_date.toString();

                    SimpleDateFormat passFormat = new SimpleDateFormat("yyyyMMdd");
                    String birthForPass = passFormat.format(teacherBirth.getDate());
                    String password = tLastName.toLowerCase() + birthForPass;
                    int type_id = 3; // student

                    user.insert(userId, username, password, type_id);

                    teacher.insert(id, userId, tFname, tMiddleName, tLastName, date, gender, email, phone,
                            addressLine1, addressLine2, imagePath, strandId, hire_date_String);

//                    String qrContent = "ID: " + id
//                            + "\nName: " + sname
//                            + "\nBirthdate: " + date
//                            + "\nGender: " + gender
//                            + "\nEmail: " + email
//                            + "\nPhone: " + phone
//                            + "\nMother: " + motherName
//                            + "\nFather: " + fatherName
//                            + "\nAddress 1: " + addressLine1
//                            + "\nAddress 2: " + addressLine2
//                            + "\nBirth Certificate: " + birthCer
//                            + "\nForm 137: " + form137
//                            + "\nImage: " + imagePath;
//
//                    // ✅ Generate QR code with all details
//                    generateQRCode(id, sname, qrContent);
                    TeacherTable.setModel(new DefaultTableModel(null, new Object[]{"Teacher ID", "User_ID", "First Name", "Middle Name", "Last Name", "Date of Birth", "Gender", "Email", "Phone Number",
                        "Address Line 1", "Address Line 2", "Strand Name", "hire_date", "Image Path"}));
                    teacher.getTeacherValue(TeacherTable, "");
                    clearTeacher();
                } else {
                    JOptionPane.showMessageDialog(this, "This phone number already exist");

                }

            } else {
                JOptionPane.showMessageDialog(this, "This email already exist");
            }

        }
    }//GEN-LAST:event_teacherAddNewBtActionPerformed

    private void teacherClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherClearActionPerformed
        clearTeacher();
    }//GEN-LAST:event_teacherClearActionPerformed

    private void teacherSearchBt_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherSearchBt_2ActionPerformed
        if (teacherSearchField_3.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Search field is empty");

        } else {
            TeacherTable.setModel(new DefaultTableModel(null, new Object[]{"Teacher ID", "User_ID", "First Name", "Middle Name", "Last Name", "Date of Birth", "Gender", "Email", "Phone Number",
                "Address Line 1", "Address Line 2", "Strand Name", "hire_date", "Image Path"}));
            teacher.getTeacherValue(TeacherTable, teacherSearchField_3.getText());
        }
    }//GEN-LAST:event_teacherSearchBt_2ActionPerformed

    private void teacherRefresh_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherRefresh_3ActionPerformed
        TeacherTable.setModel(new DefaultTableModel(null, new Object[]{"Teacher ID", "User_ID", "First Name", "Middle Name", "Last Name", "Date of Birth", "Gender", "Email", "Phone Number",
            "Address Line 1", "Address Line 2", "Strand Name", "hire_date", "Image Path"}));
        teacher.getTeacherValue(TeacherTable, "");
        teacherSearchField_3.setText(null);
    }//GEN-LAST:event_teacherRefresh_3ActionPerformed

    private void teacherSort_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherSort_3ActionPerformed
        DefaultTableModel model = (DefaultTableModel) StudentTable.getModel();

        // Attach TableRowSorter to your table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        TeacherTable.setRowSorter(sorter);

        // Example: Sort by Student ID (numerically) and then by Student Name (alphabetically)
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int teacherIdCol = 0;  // Student ID is column 0
        int teacherNameCol = 2; // Student Name is column 1

        // First sort by ID (ascending)
        sortKeys.add(new RowSorter.SortKey(teacherIdCol, SortOrder.ASCENDING));

        // Then sort by Name (ascending)
        sortKeys.add(new RowSorter.SortKey(teacherNameCol, SortOrder.ASCENDING));

        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }//GEN-LAST:event_teacherSort_3ActionPerformed

    private void TeacherTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TeacherTableMouseClicked
        model = (DefaultTableModel) TeacherTable.getModel();
        rowIndex = TeacherTable.getSelectedRow();
        teacherID.setText(model.getValueAt(rowIndex, 0).toString());
        teacherFirstName.setText(model.getValueAt(rowIndex, 2).toString());
        teacherMidName.setText(model.getValueAt(rowIndex, 3).toString());
        teacherLastName.setText(model.getValueAt(rowIndex, 4).toString());

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(rowIndex, 5).toString());
            teacherBirth.setDate(date);
        } catch (ParseException ex) {
            System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        String gender = model.getValueAt(rowIndex, 6).toString();
        if (gender.equals("Male")) {
            teacherGender.setSelectedIndex(0);

        } else {
            teacherGender.setSelectedIndex(1);

        }
        teacherEmail.setText(model.getValueAt(rowIndex, 7).toString());
        teacherPhone.setText(model.getValueAt(rowIndex, 8).toString());
        teacherAddress1.setText(model.getValueAt(rowIndex, 9).toString());
        teacherAddress2.setText(model.getValueAt(rowIndex, 10).toString());
        String strandSelect = model.getValueAt(rowIndex, 11).toString();

        //STEM, ABM, HUMSS, GAS, TVL-ICT, TVL-EIM, TVL-HE
        if (strandSelect.equals("STEM")) {
            teacherStrand.setSelectedIndex(0);
        }
        if (strandSelect.equals("ABM")) {
            teacherStrand.setSelectedIndex(1);
        }
        if (strandSelect.equals("HUMSS")) {
            teacherStrand.setSelectedIndex(2);
        }
        if (strandSelect.equals("GAS")) {
            teacherStrand.setSelectedIndex(3);
        }
        if (strandSelect.equals("TVL-ICT")) {
            teacherStrand.setSelectedIndex(4);
        }
        if (strandSelect.equals("TVL-EIM")) {
            teacherStrand.setSelectedIndex(5);
        }
        if (strandSelect.equals("TVL-HE")) {
            teacherStrand.setSelectedIndex(6);
        }

        String path = model.getValueAt(rowIndex, 13).toString();
        imagePath = path;
        teacherImagePanel.setIcon(home.imageAdjust(path, null, teacherImagePanel));//get image path and called image adjust method path to image
    }//GEN-LAST:event_TeacherTableMouseClicked

    private void teacherDelBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherDelBtActionPerformed
        int id = Integer.parseInt(teacherID.getText());
        if (teacher.isidExist(id)) {
            teacher.delete(id);
            user.delete(id);
            TeacherTable.setModel(new DefaultTableModel(null, new Object[]{"Teacher ID", "User_ID", "First Name", "Middle Name", "Last Name", "Date of Birth", "Gender", "Email", "Phone Number",
                "Address Line 1", "Address Line 2", "Strand Name", "hire_date", "Image Path"}));
            teacher.getTeacherValue(TeacherTable, "");
            clearTeacher();

        } else {
            JOptionPane.showMessageDialog(this, "the teacher doesn't exist");
        }
    }//GEN-LAST:event_teacherDelBtActionPerformed

    private void teacherPrint_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherPrint_3ActionPerformed
        try {
            MessageFormat header = new MessageFormat("Teachers Information");
            MessageFormat footer = new MessageFormat("Page{0,number,integer}");
            TeacherTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException ex) {
            System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_teacherPrint_3ActionPerformed

    private void teacherStrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherStrandActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teacherStrandActionPerformed

    private ImageIcon imageAdjust(String path, byte[] pic) {
        ImageIcon myImage = null;
        if (path != null) {
            myImage = new ImageIcon(path);
        } else {
            myImage = new ImageIcon(pic);

        }
        Image img = myImage.getImage();
        Image newImage = img.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(newImage);
        return icon;

    }

    public void setTime() {
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
            java.util.Date date = new java.util.Date();
            java.text.SimpleDateFormat tf = new java.text.SimpleDateFormat("hh:mm:ss a");
            txtTime.setText(tf.format(date));
        });
        timer.start();
    }

    public void setDate() {
        java.util.Date date = new java.util.Date();
        // EEEE = full day name (e.g., Thursday)
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("EEEE, MMMM dd, yyyy");
        txtDate.setText(df.format(date));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new AdminFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Clear;
    private javax.swing.JTable StudentTable;
    private javax.swing.JTable StudentTrackTable;
    private javax.swing.JTable TeacherTable;
    private javax.swing.JButton addNewBt;
    private javax.swing.JButton browseBirthCertificate;
    private javax.swing.JButton browseForm137;
    private javax.swing.JButton browseImg;
    private javax.swing.JButton delBt;
    private javax.swing.JLabel imagePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton searchBt_1;
    private javax.swing.JTextField stuAddress1;
    private javax.swing.JTextField stuAddress2;
    private com.toedter.calendar.JDateChooser stuBirth;
    private javax.swing.JTextField stuBirthCer;
    private javax.swing.JTextField stuEmail;
    private javax.swing.JTextField stuFatherName;
    private javax.swing.JTextField stuFname;
    private javax.swing.JTextField stuForm137;
    private javax.swing.JComboBox<String> stuGender;
    private javax.swing.JComboBox<String> stuGradeLevel;
    private javax.swing.JTextField stuID;
    private javax.swing.JTextField stuLRN;
    private javax.swing.JTextField stuLastName;
    private javax.swing.JTextField stuMiddleName;
    private javax.swing.JTextField stuMotherName;
    private javax.swing.JTextField stuPhone;
    private javax.swing.JButton stuPrint_1;
    private javax.swing.JButton stuPrint_2;
    private javax.swing.JButton stuRefresh_1;
    private javax.swing.JButton stuRefresh_2;
    private javax.swing.JButton stuSaveBt;
    private javax.swing.JButton stuSearchBt_2;
    private javax.swing.JTextField stuSearchField_1;
    private javax.swing.JTextField stuSearchField_2;
    private javax.swing.JComboBox<String> stuSection;
    private javax.swing.JButton stuSort_1;
    private javax.swing.JButton stuSort_2;
    private javax.swing.JComboBox<String> stuStrand;
    private javax.swing.JButton stuStrandClearBt;
    public static javax.swing.JTextField stuStrandId;
    private javax.swing.JTextField stuStrandSearchAdminField;
    private javax.swing.JButton stuStrandSearchBt;
    private javax.swing.JButton teacherAddNewBt;
    private javax.swing.JTextField teacherAddress1;
    private javax.swing.JTextField teacherAddress2;
    private com.toedter.calendar.JDateChooser teacherBirth;
    private javax.swing.JButton teacherBrowseImg;
    private javax.swing.JButton teacherClear;
    private javax.swing.JButton teacherDelBt;
    private javax.swing.JTextField teacherEmail;
    private javax.swing.JTextField teacherFirstName;
    private javax.swing.JComboBox<String> teacherGender;
    private javax.swing.JTextField teacherID;
    private javax.swing.JLabel teacherImagePanel;
    private javax.swing.JTextField teacherLastName;
    private javax.swing.JTextField teacherMidName;
    private javax.swing.JTextField teacherPhone;
    private javax.swing.JButton teacherPrint_3;
    private javax.swing.JButton teacherRefresh_3;
    private javax.swing.JButton teacherSearchBt_2;
    private javax.swing.JTextField teacherSearchField_3;
    private javax.swing.JButton teacherSort_3;
    private javax.swing.JComboBox<String> teacherStrand;
    private javax.swing.JLabel txtDate;
    private javax.swing.JLabel txtTime;
    private javax.swing.JButton updateBt;
    private javax.swing.JButton updateBt1;
    // End of variables declaration//GEN-END:variables
}
