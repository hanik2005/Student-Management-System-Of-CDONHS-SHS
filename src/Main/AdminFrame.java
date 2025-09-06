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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
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

        setTime();
        setDate();
        clearStudent();
      
        stuID.setText(String.valueOf(student.getMax()));
    }
       private void clearStudent() {
        stuID.setText(String.valueOf(student.getMax()));
        stuFname.setText(null);
        stuMiddleName.setText(null);
        stuLastName.setText(null);
        stuBirth.setDate(null);
        stuGender.setSelectedIndex(0);
        stuEmail.setText(null);
        stuPhone.setText(null);
        stuFatherName.setText(null);
        stuMotherName.setText(null);
        stuAddress1.setText(null);
        stuAddress2.setText(null);
        stuBirthCer.setText(null);
        stuForm137.setText(null);
        imagePanel.setIcon(null);
        StudentTable.clearSelection();
        imagePath = null;

    }
    public boolean isEmptyStudent() {
        if (stuFname.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student name is missing");
            return false;

        }
        if (stuBirth.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Student date of birth is missing");
            return false;

        }
        if (stuBirth.getDate().compareTo(new Date()) > 0) {
            JOptionPane.showMessageDialog(this, "No Student from the future are allowed");
            return false;

        }
        if (stuEmail.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student email is missing");
            return false;
        }
        if (!stuEmail.getText().matches("^.+@.+\\..+$")) {
            JOptionPane.showMessageDialog(this, "Invalid Email Address");
            return false;
        }
        if (stuPhone.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student phone number is missing");
            return false;
        }
        if (stuPhone.getText().length() >= 15) {
            JOptionPane.showMessageDialog(this, "Phone number is to long");
            return false;
        }
        if (stuMotherName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student Mother Name is missing");
            return false;
        }
        if (stuFatherName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student Father Name is missing");
            return false;
        }
        if (stuAddress1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address Line 1 is missing");
            return false;
        }
        if (stuAddress2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address Line 2 is missing");
            return false;
        }
        if (stuBirthCer.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Birth Certificate path is missing");
            return false;
        }
        if (stuForm137.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Form 137 path is missing");
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
        jPanel12 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        StudentTable = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        delBt = new javax.swing.JButton();
        stuPrint_1 = new javax.swing.JButton();

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
        jLabel1.setText("STUDENT MANAGEMENT SYSTEM");

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

        stuBirthCer.setBackground(java.awt.Color.white);

        stuForm137.setBackground(java.awt.Color.white);

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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(browseBirthCertificate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stuBirthCer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(browseForm137, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stuForm137, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap(202, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(464, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(76, Short.MAX_VALUE))
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
                .addGap(59, 59, 59)
                .addComponent(stuRefresh_1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(stuSearchField_1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBt_1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stuRefresh_1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel12.setBackground(new java.awt.Color(153, 255, 204));

        StudentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student_ID", "First_Name", "Middle_Name", "Last_Name", "Date Of Birth", "Gender", "Email", "Phone Number", "Father's Name", "Mother's Name", "Address Line 1", "Address Line 2", "Birth Cerificate", "Form137", "Image Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 695, Short.MAX_VALUE)
                .addContainerGap())
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

    private void browseImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseImgActionPerformed
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("* image", "jpg", "gif", "png");
        file.addChoosableFileFilter(filter);
        int output = file.showSaveDialog(file);
        if (output == JFileChooser.APPROVE_OPTION) {
            File selectFile = file.getSelectedFile();
            String path = selectFile.getAbsolutePath();
            imagePanel.setIcon(imageAdjust(path, null));
            imagePath = path;

        } else {
            JOptionPane.showMessageDialog(this, "No image selected");

        }
    }//GEN-LAST:event_browseImgActionPerformed

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
        if (isEmptyStudent()) {
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
                    student.update(id, sfname, sMidName, sLastName, date, gender, email, phone,
                        motherName, fatherName, addressLine1, addressLine2, birthCer, form137, imagePath);

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
                    StudentTable.setModel(new DefaultTableModel(null, new Object[]{"Student ID", "First Name", "Middle Name", "LastName", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name",
                        "Mother's Name", "Address Line 1", "Address Line 2", "Birth Certificate", "Form137", "Image Path"}));
            student.getStudentValue(StudentTable, "");
            clearStudent();

        }

        } else {
            JOptionPane.showMessageDialog(this, "student id doesn't exist");

        }

        }
    }//GEN-LAST:event_updateBtActionPerformed

    private void addNewBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBtActionPerformed
        int id = student.getMax();
        if (isEmptyStudent()) {
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

                    SimpleDateFormat passFormat = new SimpleDateFormat("yyyyMMdd");
                    String birthForPass = passFormat.format(stuBirth.getDate());
                    String password = sLastName.toLowerCase() + birthForPass;
                    String type = "Student";

                    student.insert(id, sFname, sMiddleName, sLastName, date, gender, email, phone,
                        motherName, fatherName, addressLine1, addressLine2, birthCer, form137, imagePath);
                    user.insert(id, id, password, type);

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
                    StudentTable.setModel(new DefaultTableModel(null, new Object[]{"Student ID", "First Name", "Middle Name", "Last Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name",
                        "Mother's Name", "Address Line 1", "Address Line 2", "Birth Certificate", "Form137", "Image Path"}));
            student.getStudentValue(StudentTable, "");
            clearStudent();
        } else {
            JOptionPane.showMessageDialog(this, "This phone number already exist");

        }

        } else {
            JOptionPane.showMessageDialog(this, "This email already exist");
        }

        }
    }//GEN-LAST:event_addNewBtActionPerformed

    private void ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearActionPerformed
        clearStudent();
    }//GEN-LAST:event_ClearActionPerformed

    private void searchBt_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBt_1ActionPerformed
        if (stuSearchField_1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Search field is empty");

        } else {
            StudentTable.setModel(new DefaultTableModel(null, new Object[]{"Student ID", "Student Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name",
                "Mother's Name", "Address Line 1", "Address Line 2", "Birth Certificate", "Form137", "Image Path"}));
    student.getStudentValue(StudentTable, stuSearchField_1.getText());

    }
    }//GEN-LAST:event_searchBt_1ActionPerformed

    private void stuRefresh_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuRefresh_1ActionPerformed
        StudentTable.setModel(new DefaultTableModel(null, new Object[]{"Student ID", "Student Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name",
            "Mother's Name", "Address Line 1", "Address Line 2", "Birth Certificate", "Form137", "Image Path"}));
student.getStudentValue(StudentTable, "");
stuSearchField_1.setText(null);
    }//GEN-LAST:event_stuRefresh_1ActionPerformed

    private void StudentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StudentTableMouseClicked
        model = (DefaultTableModel) StudentTable.getModel();
        rowIndex = StudentTable.getSelectedRow();
        stuID.setText(model.getValueAt(rowIndex, 0).toString());
        stuFname.setText(model.getValueAt(rowIndex, 1).toString());
        stuMiddleName.setText(model.getValueAt(rowIndex, 2).toString());
        stuLastName.setText(model.getValueAt(rowIndex, 3).toString());

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(rowIndex, 4).toString());
            stuBirth.setDate(date);
        } catch (ParseException ex) {
            System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        String gender = model.getValueAt(rowIndex, 5).toString();
        if (gender.equals("Male")) {
            stuGender.setSelectedIndex(0);

        } else {
            stuGender.setSelectedIndex(1);

        }
        stuEmail.setText(model.getValueAt(rowIndex, 6).toString());
        stuPhone.setText(model.getValueAt(rowIndex, 7).toString());
        stuFatherName.setText(model.getValueAt(rowIndex, 8).toString());
        stuMotherName.setText(model.getValueAt(rowIndex, 9).toString());
        stuAddress1.setText(model.getValueAt(rowIndex, 10).toString());
        stuAddress2.setText(model.getValueAt(rowIndex, 11).toString());
        stuBirthCer.setText(model.getValueAt(rowIndex, 12).toString());
        stuForm137.setText(model.getValueAt(rowIndex, 13).toString());
        String path = model.getValueAt(rowIndex, 14).toString();
        imagePath = path;
        imagePanel.setIcon(imageAdjust(path, null));//get image path and called image adjust method path to image
    }//GEN-LAST:event_StudentTableMouseClicked

    private void delBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delBtActionPerformed
        int id = Integer.parseInt(stuID.getText());
        if (student.isidExist(id)) {
            student.delete(id);
            user.delete(id);
            StudentTable.setModel(new DefaultTableModel(null, new Object[]{"Student ID", "First Name", "Middle Name", "Last Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name",
                "Mother's Name", "Address Line 1", "Address Line 2", "Birth Certificate", "Form137", "Image Path"}));
    student.getStudentValue(StudentTable, "");
    clearStudent();

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
    private javax.swing.JButton addNewBt;
    private javax.swing.JButton browseBirthCertificate;
    private javax.swing.JButton browseForm137;
    private javax.swing.JButton browseImg;
    private javax.swing.JButton delBt;
    private javax.swing.JLabel imagePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
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
    private javax.swing.JTextField stuID;
    private javax.swing.JTextField stuLastName;
    private javax.swing.JTextField stuMiddleName;
    private javax.swing.JTextField stuMotherName;
    private javax.swing.JTextField stuPhone;
    private javax.swing.JButton stuPrint_1;
    private javax.swing.JButton stuRefresh_1;
    private javax.swing.JTextField stuSearchField_1;
    private javax.swing.JLabel txtDate;
    private javax.swing.JLabel txtTime;
    private javax.swing.JButton updateBt;
    // End of variables declaration//GEN-END:variables
}
