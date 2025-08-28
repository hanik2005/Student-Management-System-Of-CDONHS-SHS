/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Main;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
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
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javax.naming.spi.DirStateFactory.Result;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class Home extends javax.swing.JFrame {

    Student student = new Student();
    Strand strand = new Strand();
    Grade grade = new Grade();
    MarksSheet marksSheet = new MarksSheet();

    int xx, xy;
    private DefaultTableModel model;
    private String imagePath;
    private int rowIndex;
    NumberFormat nf = NumberFormat.getInstance();

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Home.class.getName());
    public Webcam webcam;
    public WebcamPanel webcamPanel;
    public Timer qrScanTimer;
    public boolean isScanning = false;
    private String birthCertificatePath;
    private String form137Path;

    public Home() {
        this.setUndecorated(true);
        initComponents();
        init();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        SwingUtilities.invokeLater(() -> {
            initWebcam();
        });
        //webcam.close(); //this is a just a experiment care to erase in the future or not

    }

    private void initWebcam() {
        webcam = Webcam.getDefault();
        if (webcam == null) {
            JOptionPane.showMessageDialog(this, "No webcam detected");
            return;
        }

        webcam.setViewSize(WebcamResolution.QVGA.getSize()); //WebcamResolution.QVGA (320x240) // WebcamResolution.HD720 (1280x720)
        //WebcamResolution.HD1080 (1920x1080)
        webcamPanel = new WebcamPanel(webcam);
        webcamPanel.setFPSDisplayed(true);
        webcamPanel.setMirrored(true);

        webPanel.setLayout(new BorderLayout());
        webPanel.add(webcamPanel, BorderLayout.CENTER);
        webcamPanel.start();
        //webcam.close(); //this is a just a experiment care to erase in the future or not

        // Initialize QR scan timer (scans every 500ms)
        qrScanTimer = new Timer(500, e -> scanForQRCode());
    }

    private void scanForQRCode() {
        if (!isScanning && webcam != null && webcam.isOpen()) {
            isScanning = true;
            try {
                BufferedImage image = webcam.getImage();
                if (image != null) {
                    com.google.zxing.Result result = decodeQRCode(image);
                    if (result != null && result.getText() != null && !result.getText().isEmpty()) {
                        qrScanTimer.stop();
                        handleQRCodeResult(result.getText());
                    }
                }
            } finally {
                isScanning = false;
            }
        }
    }

    private com.google.zxing.Result decodeQRCode(BufferedImage image) {
        if (image == null) {
            return null;
        }

        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            // Correct way to set hints
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.QR_CODE));

            return new MultiFormatReader().decode(bitmap, hints);
        } catch (NotFoundException e) {
            return null; // No QR code found
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING, "QR decoding error", e);
            return null;
        }
    }

    private void handleQRCodeResult(String qrText) {
        // Split QR text by line
        String[] lines = qrText.split("\n");
        Map<String, String> dataMap = new HashMap<>();

        for (String line : lines) {
            if (line.contains(":")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    dataMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        }

        // ✅ Fill textfields if data exists
        stuID.setText(dataMap.getOrDefault("ID", ""));
        stuName.setText(dataMap.getOrDefault("Name", ""));
        try {
            String birthdate = dataMap.get("Birthdate");
            if (birthdate != null && !birthdate.isEmpty()) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                stuBirth.setDate(df.parse(birthdate));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format in QR");
        }
        stuGender.setSelectedItem(dataMap.getOrDefault("Gender", ""));
        stuEmail.setText(dataMap.getOrDefault("Email", ""));
        stuPhone.setText(dataMap.getOrDefault("Phone", ""));
        stuMotherName.setText(dataMap.getOrDefault("Mother", ""));
        stuFatherName.setText(dataMap.getOrDefault("Father", ""));
        stuAddress1.setText(dataMap.getOrDefault("Address 1", ""));
        stuAddress2.setText(dataMap.getOrDefault("Address 2", ""));
        stuBirthCer.setText(dataMap.getOrDefault("Birth Certificate", ""));
        stuForm137.setText(dataMap.getOrDefault("Form 137", ""));

        // PROBLEM IS HERE PLEASE CHECK PLEASE
        String imgPath = dataMap.get("Image");
        if (imgPath != null && !imgPath.isEmpty()) {
            File imgFile = new File(imgPath);
            if (imgFile.exists()) {
                // Display image on the panel
                imagePanel.setIcon(imageAdjust(imgPath, null));

                // Store current image path
                imagePath = imgPath;

                // ✅ Also show the path in a textfield (or label)
                imagePanel.setText(imgPath);
            } else {
                JOptionPane.showMessageDialog(this, "Image file not found at: " + imgPath);
            }
        }

        JOptionPane.showMessageDialog(this, "QR Code data loaded into form!");

        // Restart scanning after a delay
        Timer restartTimer = new Timer(2000, e -> {
            qrScanTimer.start();
            ((Timer) e.getSource()).stop();
        });
        restartTimer.setRepeats(false);
        restartTimer.start();
    }

    private void startQRScanning() {
        if (webcam != null && !qrScanTimer.isRunning()) {
            qrScanTimer.start();
            JOptionPane.showMessageDialog(this, "QR Code scanning started");
        }
    }

    private void stopQRScanning() {
        if (qrScanTimer != null && qrScanTimer.isRunning()) {
            qrScanTimer.stop();
        }
    }

    private void generateQRCode(int studentId, String studentName, String qrContent) {
        try {
            int width = 300;   // bigger QR size since more data
            int height = 300;
            String fileType = "png";

            // Folder where QR codes will be saved
            File qrFolder = new File("qrcodes");
            if (!qrFolder.exists()) {
                qrFolder.mkdir();
            }

            // File path with student ID and name
            File qrFile = new File(qrFolder, studentId + "_" + studentName.replaceAll("\\s+", "_") + ".png");

            // Generate QR code
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1); // reduce margin for more space

            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrContent, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToPath(bitMatrix, fileType, qrFile.toPath());

            JOptionPane.showMessageDialog(this, "QR Code generated and saved:\n" + qrFile.getAbsolutePath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating QR Code: " + e.getMessage());
        }
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        stuID = new javax.swing.JTextField();
        stuName = new javax.swing.JTextField();
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
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        webPanel = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        imagePanel = new javax.swing.JLabel();
        browseImg = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        StartWebcam = new javax.swing.JToggleButton();
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
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        strandId = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        stuStrandSearchField = new javax.swing.JTextField();
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
        jPanel18 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        StudentTrackTable = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        stuStrandClearBt = new javax.swing.JButton();
        stuPrint_2 = new javax.swing.JButton();
        stuSaveBt = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        stuGradeIDSearchField = new javax.swing.JTextField();
        stuGradeManageSearchButton = new javax.swing.JButton();
        jLabel68 = new javax.swing.JLabel();
        stuGradeManageGradeLevelSearchField = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        idGradeManage = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        stuGradeManageID = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        stuGradeManageGradeLevel = new javax.swing.JTextField();
        stuGradeManageSection = new javax.swing.JTextField();
        stuGradeManageStrand = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        stuGradeManageSub1 = new javax.swing.JTextField();
        stuGradeManageSub2 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        stuGradeManageSub3 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        stuGradeManageSub4 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        stuGradeManageSub5 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        subScore2 = new javax.swing.JTextField();
        subScore4 = new javax.swing.JTextField();
        subScore1 = new javax.swing.JTextField();
        subScore5 = new javax.swing.JTextField();
        subScore3 = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        stuGradeManageSave = new javax.swing.JButton();
        stuGradeManageUpdateBt = new javax.swing.JButton();
        stuGradeManageClearBt = new javax.swing.JButton();
        jPanel43 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        stuGradeManageSub6 = new javax.swing.JTextField();
        subScore6 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        stuGradeManageSub7 = new javax.swing.JTextField();
        subScore7 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        stuGradeManageSub8 = new javax.swing.JTextField();
        subScore8 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        stuGradeManageQuarter = new javax.swing.JComboBox<>();
        jPanel28 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        StudentGradeManagementTable = new javax.swing.JTable();
        jPanel41 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        stuGradeManageSearchStuField = new javax.swing.JTextField();
        stuGradeManageSearchTableBt = new javax.swing.JButton();
        stuGradeManageRefreshTable = new javax.swing.JButton();
        jPanel42 = new javax.swing.JPanel();
        jButton13 = new javax.swing.JButton();
        stuGradeManagePrint = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        stuMarksSearchField = new javax.swing.JTextField();
        stuMarksSearchBt = new javax.swing.JButton();
        jPanel34 = new javax.swing.JPanel();
        Final_Average = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        StudentMarksSheetTable = new javax.swing.JTable();
        jPanel33 = new javax.swing.JPanel();
        jButton18 = new javax.swing.JButton();
        stuMarksClearBt = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

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
        jLabel17.setText("jLabel17");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 793, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
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

        stuName.setBackground(java.awt.Color.white);
        stuName.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N

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
        jLabel3.setText("Student's Name");

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
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(stuName, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel61)
                            .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stuBirth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(stuGender, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(175, 175, 175))
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
                                .addComponent(stuForm137)))))
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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(stuName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
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
                    .addComponent(browseBirthCertificate, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(stuBirthCer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(browseForm137, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(stuForm137, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(153, 255, 204));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jPanel9.setBackground(new java.awt.Color(153, 255, 204));
        jPanel9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        javax.swing.GroupLayout webPanelLayout = new javax.swing.GroupLayout(webPanel);
        webPanel.setLayout(webPanelLayout);
        webPanelLayout.setHorizontalGroup(
            webPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 355, Short.MAX_VALUE)
        );
        webPanelLayout.setVerticalGroup(
            webPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 246, Short.MAX_VALUE)
        );

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
            .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(56, 56, 56)
                        .addComponent(browseImg, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 105, Short.MAX_VALUE))
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jButton3.setBackground(new java.awt.Color(102, 255, 255));
        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setText("Start Scanning");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        StartWebcam.setBackground(new java.awt.Color(102, 255, 255));
        StartWebcam.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        StartWebcam.setForeground(new java.awt.Color(0, 0, 0));
        StartWebcam.setText("StopWebcam");
        StartWebcam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartWebcamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(webPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(229, 229, 229)
                        .addComponent(jButton3)
                        .addGap(126, 126, 126)
                        .addComponent(StartWebcam, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(81, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(webPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StartWebcam, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
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
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(64, Short.MAX_VALUE))
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
                "Student_ID", "Student_Name", "Date Of Birth", "Gender", "Email", "Phone Number", "Father's Name", "Mother's Name", "Address Line 1", "Address Line 2", "Birth Cerificate", "Form137", "Image Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
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

        strandId.setEditable(false);
        strandId.setBackground(new java.awt.Color(204, 204, 204));
        strandId.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        strandId.setForeground(new java.awt.Color(0, 0, 0));
        strandId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strandIdActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("ID");

        jPanel36.setBackground(new java.awt.Color(153, 255, 204));
        jPanel36.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel36.setForeground(new java.awt.Color(0, 0, 0));

        jLabel66.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(0, 0, 0));
        jLabel66.setText("Student's ID");

        stuStrandSearchField.setBackground(new java.awt.Color(255, 255, 255));
        stuStrandSearchField.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        stuStrandSearchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuStrandSearchFieldActionPerformed(evt);
            }
        });
        stuStrandSearchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                stuStrandSearchFieldKeyTyped(evt);
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
                        .addComponent(stuStrandSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(stuStrandSearchField))
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
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stuStrandId)
                            .addComponent(strandId)
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
                    .addComponent(jLabel18)
                    .addComponent(strandId, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap(14, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stuSearchField_2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(stuSearchBt_2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(stuRefresh_2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(stuRefresh_2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jPanel18.setBackground(new java.awt.Color(153, 255, 204));
        jPanel18.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        StudentTrackTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Student_ID", "Grade Level", "Strand", "Section"
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
                        .addGap(0, 328, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Track", jPanel6);

        jPanel21.setBackground(new java.awt.Color(102, 255, 255));

        jPanel22.setBackground(new java.awt.Color(153, 255, 204));
        jPanel22.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel22.setForeground(new java.awt.Color(0, 0, 0));

        jPanel37.setBackground(new java.awt.Color(153, 255, 204));
        jPanel37.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel37.setForeground(new java.awt.Color(0, 0, 0));

        jLabel67.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(0, 0, 0));
        jLabel67.setText("Student's ID");

        stuGradeIDSearchField.setBackground(new java.awt.Color(255, 255, 255));
        stuGradeIDSearchField.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        stuGradeIDSearchField.setForeground(new java.awt.Color(0, 0, 0));
        stuGradeIDSearchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuGradeIDSearchFieldActionPerformed(evt);
            }
        });
        stuGradeIDSearchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                stuGradeIDSearchFieldKeyTyped(evt);
            }
        });

        stuGradeManageSearchButton.setBackground(new java.awt.Color(153, 255, 204));
        stuGradeManageSearchButton.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        stuGradeManageSearchButton.setForeground(new java.awt.Color(0, 0, 0));
        stuGradeManageSearchButton.setText("Search");
        stuGradeManageSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuGradeManageSearchButtonActionPerformed(evt);
            }
        });

        jLabel68.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(0, 0, 0));
        jLabel68.setText("Grade Level");

        stuGradeManageGradeLevelSearchField.setBackground(new java.awt.Color(255, 255, 255));
        stuGradeManageGradeLevelSearchField.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        stuGradeManageGradeLevelSearchField.setForeground(new java.awt.Color(0, 0, 0));
        stuGradeManageGradeLevelSearchField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "11", "12" }));

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel68)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(stuGradeIDSearchField, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                            .addComponent(stuGradeManageGradeLevelSearchField, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stuGradeManageSearchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addComponent(jLabel67)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel67)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stuGradeManageSearchButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(stuGradeIDSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel68)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stuGradeManageGradeLevelSearchField, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("ID");

        idGradeManage.setEditable(false);
        idGradeManage.setBackground(new java.awt.Color(204, 204, 204));
        idGradeManage.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        idGradeManage.setForeground(new java.awt.Color(0, 0, 0));

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setText("Student's ID");

        stuGradeManageID.setEditable(false);
        stuGradeManageID.setBackground(new java.awt.Color(204, 204, 204));
        stuGradeManageID.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuGradeManageID.setForeground(new java.awt.Color(0, 0, 0));

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setText("Grade Level");

        jLabel27.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 0, 0));
        jLabel27.setText("Strand");

        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 0));
        jLabel28.setText("Section");

        stuGradeManageGradeLevel.setEditable(false);
        stuGradeManageGradeLevel.setBackground(new java.awt.Color(204, 204, 204));
        stuGradeManageGradeLevel.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuGradeManageGradeLevel.setForeground(new java.awt.Color(0, 0, 0));

        stuGradeManageSection.setEditable(false);
        stuGradeManageSection.setBackground(new java.awt.Color(204, 204, 204));
        stuGradeManageSection.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuGradeManageSection.setForeground(new java.awt.Color(0, 0, 0));

        stuGradeManageStrand.setEditable(false);
        stuGradeManageStrand.setBackground(new java.awt.Color(204, 204, 204));
        stuGradeManageStrand.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuGradeManageStrand.setForeground(new java.awt.Color(0, 0, 0));

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 0, 0));
        jLabel30.setText("Subject 1");

        stuGradeManageSub1.setEditable(false);
        stuGradeManageSub1.setBackground(new java.awt.Color(204, 204, 204));
        stuGradeManageSub1.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuGradeManageSub1.setForeground(new java.awt.Color(0, 0, 0));

        stuGradeManageSub2.setEditable(false);
        stuGradeManageSub2.setBackground(new java.awt.Color(204, 204, 204));
        stuGradeManageSub2.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuGradeManageSub2.setForeground(new java.awt.Color(0, 0, 0));

        jLabel31.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 0, 0));
        jLabel31.setText("Subject2");

        stuGradeManageSub3.setEditable(false);
        stuGradeManageSub3.setBackground(new java.awt.Color(204, 204, 204));
        stuGradeManageSub3.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuGradeManageSub3.setForeground(new java.awt.Color(0, 0, 0));

        jLabel32.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 0));
        jLabel32.setText("Subject 3");

        stuGradeManageSub4.setEditable(false);
        stuGradeManageSub4.setBackground(new java.awt.Color(204, 204, 204));
        stuGradeManageSub4.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuGradeManageSub4.setForeground(new java.awt.Color(0, 0, 0));

        jLabel33.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 0, 0));
        jLabel33.setText("Subject 4");

        stuGradeManageSub5.setEditable(false);
        stuGradeManageSub5.setBackground(new java.awt.Color(204, 204, 204));
        stuGradeManageSub5.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuGradeManageSub5.setForeground(new java.awt.Color(0, 0, 0));
        stuGradeManageSub5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuGradeManageSub5ActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 0, 0));
        jLabel34.setText("Subject 5");

        subScore2.setBackground(new java.awt.Color(255, 255, 255));
        subScore2.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        subScore2.setForeground(new java.awt.Color(0, 0, 0));
        subScore2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        subScore2.setText("0.0");

        subScore4.setBackground(new java.awt.Color(255, 255, 255));
        subScore4.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        subScore4.setForeground(new java.awt.Color(0, 0, 0));
        subScore4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        subScore4.setText("0.0");

        subScore1.setBackground(new java.awt.Color(255, 255, 255));
        subScore1.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        subScore1.setForeground(new java.awt.Color(0, 0, 0));
        subScore1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        subScore1.setText("0.0");

        subScore5.setBackground(new java.awt.Color(255, 255, 255));
        subScore5.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        subScore5.setForeground(new java.awt.Color(0, 0, 0));
        subScore5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        subScore5.setText("0.0");

        subScore3.setBackground(new java.awt.Color(255, 255, 255));
        subScore3.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        subScore3.setForeground(new java.awt.Color(0, 0, 0));
        subScore3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        subScore3.setText("0.0");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(55, 55, 55)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(stuGradeManageStrand)
                                    .addComponent(stuGradeManageSection)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(stuGradeManageID)
                                    .addComponent(idGradeManage)
                                    .addComponent(stuGradeManageGradeLevel)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(55, 55, 55)
                                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(stuGradeManageSub5)
                                            .addComponent(stuGradeManageSub4, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)))
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(stuGradeManageSub2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                                            .addComponent(stuGradeManageSub1, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(stuGradeManageSub3))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(subScore2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(subScore1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(subScore4, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(subScore5, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(subScore3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)))
                        .addGap(6, 6, 6)))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(idGradeManage, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stuGradeManageID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(stuGradeManageGradeLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(stuGradeManageStrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stuGradeManageSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(stuGradeManageSub1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subScore1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stuGradeManageSub2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(subScore2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(stuGradeManageSub3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subScore3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(stuGradeManageSub4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subScore4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stuGradeManageSub5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subScore5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel23.setBackground(new java.awt.Color(153, 255, 204));
        jPanel23.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jPanel19.setBackground(new java.awt.Color(153, 255, 204));
        jPanel19.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jButton7.setBackground(new java.awt.Color(102, 255, 255));
        jButton7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 0, 0));
        jButton7.setText("Logout");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        stuGradeManageSave.setBackground(new java.awt.Color(102, 255, 255));
        stuGradeManageSave.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        stuGradeManageSave.setForeground(new java.awt.Color(0, 0, 0));
        stuGradeManageSave.setText("Save");
        stuGradeManageSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuGradeManageSaveActionPerformed(evt);
            }
        });

        stuGradeManageUpdateBt.setBackground(new java.awt.Color(102, 255, 255));
        stuGradeManageUpdateBt.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        stuGradeManageUpdateBt.setForeground(new java.awt.Color(0, 0, 0));
        stuGradeManageUpdateBt.setText("Update");
        stuGradeManageUpdateBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuGradeManageUpdateBtActionPerformed(evt);
            }
        });

        stuGradeManageClearBt.setBackground(new java.awt.Color(102, 255, 255));
        stuGradeManageClearBt.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        stuGradeManageClearBt.setForeground(new java.awt.Color(0, 0, 0));
        stuGradeManageClearBt.setText("Clear");
        stuGradeManageClearBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuGradeManageClearBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(stuGradeManageSave, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(stuGradeManageUpdateBt, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stuGradeManageClearBt, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(stuGradeManageUpdateBt, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stuGradeManageSave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stuGradeManageClearBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel43.setBackground(new java.awt.Color(153, 255, 204));
        jPanel43.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jLabel36.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 0, 0));
        jLabel36.setText("Subject 6");

        stuGradeManageSub6.setEditable(false);
        stuGradeManageSub6.setBackground(new java.awt.Color(204, 204, 204));
        stuGradeManageSub6.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuGradeManageSub6.setForeground(new java.awt.Color(0, 0, 0));

        subScore6.setBackground(new java.awt.Color(255, 255, 255));
        subScore6.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        subScore6.setForeground(new java.awt.Color(0, 0, 0));
        subScore6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        subScore6.setText("0.0");
        subScore6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subScore6ActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 0, 0));
        jLabel37.setText("Subject 7");

        stuGradeManageSub7.setEditable(false);
        stuGradeManageSub7.setBackground(new java.awt.Color(204, 204, 204));
        stuGradeManageSub7.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuGradeManageSub7.setForeground(new java.awt.Color(0, 0, 0));

        subScore7.setBackground(new java.awt.Color(255, 255, 255));
        subScore7.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        subScore7.setForeground(new java.awt.Color(0, 0, 0));
        subScore7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        subScore7.setText("0.0");

        jLabel38.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 0, 0));
        jLabel38.setText("Subject 8");

        stuGradeManageSub8.setEditable(false);
        stuGradeManageSub8.setBackground(new java.awt.Color(204, 204, 204));
        stuGradeManageSub8.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        stuGradeManageSub8.setForeground(new java.awt.Color(0, 0, 0));
        stuGradeManageSub8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuGradeManageSub8ActionPerformed(evt);
            }
        });

        subScore8.setBackground(new java.awt.Color(255, 255, 255));
        subScore8.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        subScore8.setForeground(new java.awt.Color(0, 0, 0));
        subScore8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        subScore8.setText("0.0");

        jLabel39.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(0, 0, 0));
        jLabel39.setText("Quarter");

        stuGradeManageQuarter.setBackground(new java.awt.Color(255, 255, 255));
        stuGradeManageQuarter.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        stuGradeManageQuarter.setForeground(new java.awt.Color(0, 0, 0));
        stuGradeManageQuarter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4" }));

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stuGradeManageSub6, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel43Layout.createSequentialGroup()
                        .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stuGradeManageSub8)
                            .addComponent(stuGradeManageSub7)
                            .addComponent(stuGradeManageQuarter, 0, 263, Short.MAX_VALUE))))
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(subScore6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel43Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(subScore7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(subScore8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(stuGradeManageSub6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subScore6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(stuGradeManageSub7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subScore7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stuGradeManageSub8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subScore8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stuGradeManageQuarter, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(108, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel23Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(247, Short.MAX_VALUE)))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel23Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(293, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Grade Management", jPanel21);

        jPanel28.setBackground(new java.awt.Color(102, 255, 255));

        jPanel26.setBackground(new java.awt.Color(153, 255, 204));
        jPanel26.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jPanel27.setBackground(new java.awt.Color(153, 255, 204));
        jPanel27.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel27.setPreferredSize(new java.awt.Dimension(1380, 620));

        jPanel40.setBackground(new java.awt.Color(153, 255, 204));
        jPanel40.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        StudentGradeManagementTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Student_ID", "Grade Level", "Strand", "Section", "Subject 1", "Score 1", "Subject 2", "Score 2", "Subject 3", "Score 3", "Subject 4", "Score 4", "Subject 5", "Score 5", "Subject 6", "Score 6", "Subject 7", "Score 7", "Subject 8", "Score 8", "Quarter", "Average"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        StudentGradeManagementTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        StudentGradeManagementTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StudentGradeManagementTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(StudentGradeManagementTable);

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel41.setBackground(new java.awt.Color(153, 255, 204));
        jPanel41.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jLabel35.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 0, 0));
        jLabel35.setText("Search Student");

        stuGradeManageSearchStuField.setBackground(new java.awt.Color(255, 255, 255));

        stuGradeManageSearchTableBt.setBackground(new java.awt.Color(153, 255, 204));
        stuGradeManageSearchTableBt.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        stuGradeManageSearchTableBt.setForeground(new java.awt.Color(0, 0, 0));
        stuGradeManageSearchTableBt.setText("Search");
        stuGradeManageSearchTableBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuGradeManageSearchTableBtActionPerformed(evt);
            }
        });

        stuGradeManageRefreshTable.setBackground(new java.awt.Color(153, 255, 204));
        stuGradeManageRefreshTable.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        stuGradeManageRefreshTable.setForeground(new java.awt.Color(0, 0, 0));
        stuGradeManageRefreshTable.setText("Refresh");
        stuGradeManageRefreshTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuGradeManageRefreshTableActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stuGradeManageSearchStuField, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stuGradeManageSearchTableBt, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stuGradeManageRefreshTable, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel41Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stuGradeManageSearchStuField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stuGradeManageSearchTableBt)
                    .addComponent(stuGradeManageRefreshTable))
                .addGap(16, 16, 16))
        );

        jPanel42.setBackground(new java.awt.Color(153, 255, 204));
        jPanel42.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jButton13.setBackground(new java.awt.Color(102, 255, 255));
        jButton13.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton13.setForeground(new java.awt.Color(0, 0, 0));
        jButton13.setText("Logout");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        stuGradeManagePrint.setBackground(new java.awt.Color(102, 255, 255));
        stuGradeManagePrint.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        stuGradeManagePrint.setForeground(new java.awt.Color(0, 0, 0));
        stuGradeManagePrint.setText("Print");
        stuGradeManagePrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuGradeManagePrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addContainerGap(438, Short.MAX_VALUE)
                .addComponent(stuGradeManagePrint, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(509, 509, 509))
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stuGradeManagePrint, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, 1348, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Grade Table", jPanel28);

        jPanel24.setBackground(new java.awt.Color(102, 255, 255));

        jPanel29.setBackground(new java.awt.Color(153, 255, 204));
        jPanel29.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel29.setForeground(new java.awt.Color(0, 0, 0));

        jPanel38.setBackground(new java.awt.Color(153, 255, 204));
        jPanel38.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel38.setForeground(new java.awt.Color(0, 0, 0));

        jLabel69.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(0, 0, 0));
        jLabel69.setText("Student's ID");

        stuMarksSearchField.setBackground(new java.awt.Color(255, 255, 255));
        stuMarksSearchField.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N

        stuMarksSearchBt.setBackground(new java.awt.Color(153, 255, 204));
        stuMarksSearchBt.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        stuMarksSearchBt.setForeground(new java.awt.Color(0, 0, 0));
        stuMarksSearchBt.setText("Search");
        stuMarksSearchBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuMarksSearchBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addComponent(jLabel69)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addComponent(stuMarksSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stuMarksSearchBt, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel69)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stuMarksSearchBt, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(stuMarksSearchField))
                .addContainerGap())
        );

        jPanel34.setBackground(new java.awt.Color(153, 255, 204));
        jPanel34.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));
        jPanel34.setForeground(new java.awt.Color(0, 0, 0));

        Final_Average.setFont(new java.awt.Font("Times New Roman", 1, 32)); // NOI18N
        Final_Average.setForeground(new java.awt.Color(0, 0, 0));
        Final_Average.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Final_Average.setText("G.A. : 0.0");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Final_Average, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Final_Average, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 415, Short.MAX_VALUE)
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel30.setBackground(new java.awt.Color(153, 255, 204));
        jPanel30.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jPanel31.setBackground(new java.awt.Color(153, 255, 204));
        jPanel31.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jPanel32.setBackground(new java.awt.Color(153, 255, 204));
        jPanel32.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        StudentMarksSheetTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Student_ID", "Grade Level", "Strand", "Section", "Quarter1_average", "Quarter2_average", "Quarter3_average", "Quarter4_average", "Final_Average"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        StudentMarksSheetTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane6.setViewportView(StudentMarksSheetTable);

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6)
                .addContainerGap())
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel33.setBackground(new java.awt.Color(153, 255, 204));
        jPanel33.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 204), 4, true));

        jButton18.setBackground(new java.awt.Color(102, 255, 255));
        jButton18.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton18.setForeground(new java.awt.Color(0, 0, 0));
        jButton18.setText("Logout");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        stuMarksClearBt.setBackground(new java.awt.Color(102, 255, 255));
        stuMarksClearBt.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        stuMarksClearBt.setForeground(new java.awt.Color(0, 0, 0));
        stuMarksClearBt.setText("Clear");
        stuMarksClearBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuMarksClearBtActionPerformed(evt);
            }
        });

        jButton20.setBackground(new java.awt.Color(102, 255, 255));
        jButton20.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton20.setForeground(new java.awt.Color(0, 0, 0));
        jButton20.setText("Print");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(stuMarksClearBt, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(302, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(stuMarksClearBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1378, Short.MAX_VALUE)
            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel25Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 653, Short.MAX_VALUE)
            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel25Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Marks Sheet", jPanel25);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1368, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 681, Short.MAX_VALUE)
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
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void init() {

        tableViewStudent();
        tableViewStrand();
        tableViewGrades();
        tableViewFinalGrade();
        stuID.setText(String.valueOf(student.getMax()));
        strandId.setText(String.valueOf(strand.getMax()));
        idGradeManage.setText(String.valueOf(grade.getMax()));
    }

    private void tableViewStudent() {
        student.getStudentValue(StudentTable, "");
        model = (DefaultTableModel) StudentTable.getModel();
        StudentTable.setRowHeight(30);
        StudentTable.setShowGrid(true);
        StudentTable.setGridColor(Color.black);
        StudentTable.setBackground(Color.white);
    }

    private void tableViewStrand() {
        strand.getStrandValue(StudentTrackTable, "");
        model = (DefaultTableModel) StudentTrackTable.getModel();
        StudentTrackTable.setRowHeight(30);
        StudentTrackTable.setShowGrid(true);
        StudentTrackTable.setGridColor(Color.black);
        StudentTrackTable.setBackground(Color.white);
    }

    private void tableViewGrades() {
        grade.getGradeValue(StudentGradeManagementTable, "");
        model = (DefaultTableModel) StudentGradeManagementTable.getModel();
        StudentGradeManagementTable.setRowHeight(30);
        StudentGradeManagementTable.setShowGrid(true);
        StudentGradeManagementTable.setGridColor(Color.black);
        StudentGradeManagementTable.setBackground(Color.white);
    }

    private void tableViewFinalGrade() {
        model = (DefaultTableModel) StudentMarksSheetTable.getModel();
        StudentMarksSheetTable.setRowHeight(30);
        StudentMarksSheetTable.setShowGrid(true);
        StudentMarksSheetTable.setGridColor(Color.black);
        StudentMarksSheetTable.setBackground(Color.white);
    }

    private void clearStudent() {
        stuID.setText(String.valueOf(student.getMax()));
        stuName.setText(null);
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

    private void clearStrand() {
        strandId.setText(String.valueOf(strand.getMax()));
        stuStrandId.setText(null);
        stuGradeLevel.setSelectedIndex(0);
        stuStrand.setSelectedIndex(0);
        stuSection.setSelectedIndex(0);
        StudentTrackTable.clearSelection();
        stuStrandSearchField.setText(null);

    }

    private void clearGradeManage() {
        idGradeManage.setText(String.valueOf(grade.getMax()));
        stuGradeIDSearchField.setText(null);
        stuGradeManageGradeLevelSearchField.setSelectedIndex(0);
        idGradeManage.setText(null);
        stuGradeManageID.setText(null);
        stuGradeManageGradeLevel.setText(null);
        stuGradeManageStrand.setText(null);
        stuGradeManageSection.setText(null);
        stuGradeManageSub1.setText(null);
        stuGradeManageSub2.setText(null);
        stuGradeManageSub3.setText(null);
        stuGradeManageSub4.setText(null);
        stuGradeManageSub5.setText(null);
        stuGradeManageSub6.setText(null);
        stuGradeManageSub7.setText(null);
        stuGradeManageSub8.setText(null);
        stuGradeManageQuarter.setSelectedIndex(0);
        subScore1.setText("0.0");
        subScore2.setText("0.0");
        subScore3.setText("0.0");
        subScore4.setText("0.0");
        subScore5.setText("0.0");
        subScore6.setText("0.0");
        subScore7.setText("0.0");
        subScore8.setText("0.0");
        StudentGradeManagementTable.clearSelection();

    }

    public boolean isEmptyStudent() {
        if (stuName.getText().isEmpty()) {
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


    private void stuGradeManageSub5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuGradeManageSub5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuGradeManageSub5ActionPerformed

    private void stuGradeManageSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuGradeManageSearchButtonActionPerformed
        if (stuGradeIDSearchField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student id is missing");
        } else {
            int sid = Integer.parseInt(stuGradeIDSearchField.getText());
            int gradeLevel = Integer.parseInt(stuGradeManageGradeLevelSearchField.getSelectedItem().toString());
            grade.getDetails(sid, gradeLevel);
        }
    }//GEN-LAST:event_stuGradeManageSearchButtonActionPerformed

    private void stuSaveBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuSaveBtActionPerformed
        if (stuStrandId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student id is missing");
        } else {
            int id = strand.getMax();
            int sid = Integer.parseInt(stuStrandId.getText());
            int gradeLevel = Integer.parseInt(stuGradeLevel.getSelectedItem().toString());
            String strandSelect = stuStrand.getSelectedItem().toString();
            String sectionSelect = stuSection.getSelectedItem().toString();

            if (strand.hasSameGradeAndStrand(sid, gradeLevel, strandSelect)) {
                JOptionPane.showMessageDialog(this, "This student already enrolled in " + gradeLevel + "-" + strandSelect);
                return;
            }

            if (strand.isStudentAlreadyEnrolled(sid)) {
                JOptionPane.showMessageDialog(this, "this Student is already enrolled in " + gradeLevel + "-" + strandSelect);
                return;
            } //THIS ONLY TESTING PURPOSES
            else {
                strand.insert(id, sid, gradeLevel, strandSelect, sectionSelect);
                StudentTrackTable.setModel(new DefaultTableModel(null, new Object[]{"ID", "Student_ID", "Grade_Level", "Strand", "Section"}));
                strand.getStrandValue(StudentTrackTable, "");
                clearStrand();
            }
        }
    }//GEN-LAST:event_stuSaveBtActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int a = JOptionPane.showConfirmDialog(this, "Do you want to Logout now?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            webcam.close();
            this.dispose();
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void stuStrandSearchBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuStrandSearchBtActionPerformed
        if (stuStrandSearchField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student id");

        } else {
            int id = Integer.parseInt(stuStrandSearchField.getText());
            strand.getId(id);
            stuGradeLevel.setSelectedIndex(0);

        }
    }//GEN-LAST:event_stuStrandSearchBtActionPerformed

    private void addNewBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBtActionPerformed
        if (isEmptyStudent()) {
            if (!student.isEmailExist(stuEmail.getText())) {
                if (!student.isPhoneExist(stuPhone.getText())) {
                    int id = student.getMax();
                    String sname = stuName.getText();
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
                    student.insert(id, sname, date, gender, email, phone,
                            motherName, fatherName, addressLine1, addressLine2, birthCer, form137, imagePath);

                    String qrContent = "ID: " + id
                            + "\nName: " + sname
                            + "\nBirthdate: " + date
                            + "\nGender: " + gender
                            + "\nEmail: " + email
                            + "\nPhone: " + phone
                            + "\nMother: " + motherName
                            + "\nFather: " + fatherName
                            + "\nAddress 1: " + addressLine1
                            + "\nAddress 2: " + addressLine2
                            + "\nBirth Certificate: " + birthCer
                            + "\nForm 137: " + form137
                            + "\nImage: " + imagePath;

                    // ✅ Generate QR code with all details
                    generateQRCode(id, sname, qrContent);

                    StudentTable.setModel(new DefaultTableModel(null, new Object[]{"Student ID", "Student Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name",
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //qrScanTimer.stop();

        int a = JOptionPane.showConfirmDialog(this, "Do you want to Logout now?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            webcam.close();//HERE
            this.dispose();
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void StartWebcamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartWebcamActionPerformed
        if (webcam == null) {
            initWebcam(); // Initialize webcam if not already done
        }

        if (StartWebcam.isSelected()) {
            if (webcam != null && webcam.isOpen()) {
                webcamPanel.stop();
                webcam.close();
            }
            StartWebcam.setText("Start Webcam");
        } else {
            // Stop webcam

            if (!webcam.isOpen()) {
                webcam.open();
                webcamPanel.start();
            }
            StartWebcam.setText("Stop Webcam");

        }
    }//GEN-LAST:event_StartWebcamActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (qrScanTimer.isRunning()) {
            qrScanTimer.stop();
            jButton3.setText("Start Scanning");
        } else {
            qrScanTimer.start();
            jButton3.setText("Stop Scanning");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void stuFatherNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuFatherNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuFatherNameActionPerformed

    private void stuAddress2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuAddress2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuAddress2ActionPerformed

    private void stuMotherNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuMotherNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuMotherNameActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        //LOGOUT
        int a = JOptionPane.showConfirmDialog(this, "Do you want to Logout now?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            webcam.close();//HERE PROBKEM
            this.dispose();
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void stuGradeManagePrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuGradeManagePrintActionPerformed
        try {
            MessageFormat header = new MessageFormat("Students Grade Information");
            MessageFormat footer = new MessageFormat("Page{0,number,integer}");
            StudentGradeManagementTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException ex) {
            System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_stuGradeManagePrintActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //LOGOUT
        int a = JOptionPane.showConfirmDialog(this, "Do you want to Logout now?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            webcam.close();//HERE PROBLEM
            this.dispose();
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void stuGradeManageSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuGradeManageSaveActionPerformed
        if (!stuGradeManageID.getText().isEmpty()) {
            //if (!grade.isidExist(Integer.parseInt(idGradeManage.getText()))) {
            int sid = Integer.parseInt(stuGradeManageID.getText());
            int gradeLevel = Integer.parseInt(stuGradeManageGradeLevel.getText());
            String strand = stuGradeManageStrand.getText();
            String section = stuGradeManageSection.getText();
            int quarter = Integer.parseInt(stuGradeManageQuarter.getSelectedItem().toString());
            if (!grade.isSidGradeLevelStrandQuarterExist(sid, gradeLevel, strand, section, quarter)) {
                if (isNumeric(subScore1.getText()) && isNumeric(subScore2.getText())
                        && isNumeric(subScore3.getText()) && isNumeric(subScore4.getText())
                        && isNumeric(subScore6.getText()) && isNumeric(subScore7.getText()) && isNumeric(subScore8.getText())) {
                    int id = grade.getMax();

                    String subject1 = stuGradeManageSub1.getText();
                    String subject2 = stuGradeManageSub2.getText();
                    String subject3 = stuGradeManageSub3.getText();
                    String subject4 = stuGradeManageSub4.getText();
                    String subject5 = stuGradeManageSub5.getText();
                    String subject6 = stuGradeManageSub6.getText();
                    String subject7 = stuGradeManageSub7.getText();
                    String subject8 = stuGradeManageSub8.getText();

                    double subGrade1 = Double.parseDouble(subScore1.getText());
                    double subGrade2 = Double.parseDouble(subScore2.getText());
                    double subGrade3 = Double.parseDouble(subScore3.getText());
                    double subGrade4 = Double.parseDouble(subScore4.getText());
                    double subGrade5 = Double.parseDouble(subScore5.getText());
                    double subGrade6 = Double.parseDouble(subScore6.getText());
                    double subGrade7 = Double.parseDouble(subScore7.getText());
                    double subGrade8 = Double.parseDouble(subScore8.getText());
                    double average = (subGrade1 + subGrade2 + subGrade3 + subGrade4 + subGrade5
                            + subGrade6 + subGrade7 + subGrade8) / 8;
                    average = Math.round(average * 100.0) / 100.0; // rounds to 2 decimals

                    grade.insert(id, sid, gradeLevel, strand, section, subject1, subject2, subject3, subject4, subject5, subject6, subject7,
                            subject8, subGrade1, subGrade2, subGrade3, subGrade4, subGrade5, subGrade6, subGrade7, subGrade8, quarter, Double.parseDouble(nf.format(average)));

                    insertUpdateMarkSheet(quarter, average, sid, gradeLevel, strand, section);

                    StudentGradeManagementTable.setModel(new DefaultTableModel(null, new Object[]{"ID", "Student_ID", "Grade_Level", "Strand", "Section",
                        "Subject 1", "Score 1", "Subject 2", "Score 2", "Subject 3", "Score 3", "Subject 4", "Score 4", "Subject 5", "Score 5",
                        "Subject 6", "Score 6", "Subject 7", "Score 7", "Subject 8", "Score 8", "Quarter", "Average"}));
                    grade.getGradeValue(StudentGradeManagementTable, "");
                    clearGradeManage();

                }
            } else {
                JOptionPane.showMessageDialog(this, "GradeLevel" + gradeLevel
                        + "strand" + strand + "section" + section + "Students Grades has already added");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Grade id already exist");
        }
        //} else {
        //JOptionPane.showMessageDialog(this, "No student is selected");
        //}
    }//GEN-LAST:event_stuGradeManageSaveActionPerformed

    public void insertUpdateMarkSheet(int quarter, double average, int sid, int gradeLevel, String strand, String section) {
        
        int marksId = marksSheet.getMax();
            double q1 = 0, q2 = 0, q3 = 0, q4 = 0;
            if (quarter == 1) {
                q1 = average;
            } else if (quarter == 2) {
                q2 = average;
            } else if (quarter == 3) {
                q3 = average;
            } else if (quarter == 4) {
                q4 = average;
            }
             double finalAverage = (q1 + q2 + q3 + q4 ) / 4;
        
        if (marksSheet.isSidGradeLevelStrandQuarterExist(sid, gradeLevel, strand, section)) {
            marksSheet.updateQuarter(sid, gradeLevel, strand, section, quarter, average, finalAverage);
        } else {
            

            marksSheet.insert(marksId, sid, gradeLevel, strand, section, q1, q2, q3, q4, finalAverage);
        }

    }

    private boolean isNumeric(String s) {
        if (s == null || s.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Input cannot be empty!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(s);
            return true; // Valid number
        } catch (NumberFormatException e) {
            System.out.println("wrong");
            JOptionPane.showMessageDialog(this, "Please enter a valid number (e.g., 98.0 or 51.1)", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false; // Not valid
        }
    }
    private void stuGradeManageSub8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuGradeManageSub8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuGradeManageSub8ActionPerformed

    private void subScore6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subScore6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_subScore6ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        //LOGOUT
        int a = JOptionPane.showConfirmDialog(this, "Do you want to Logout now?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            webcam.close();//HERE
            this.dispose();
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void stuMarksSearchBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuMarksSearchBtActionPerformed
        if (stuMarksSearchField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the student id field");
        } else {
            int sid = Integer.parseInt(stuMarksSearchField.getText());
            if (marksSheet.isidExist(sid)) {
                StudentMarksSheetTable.setModel(new DefaultTableModel(null, new Object[]{"ID", "Student_ID", "Grade_Level", "Strand", "Section",
                    "Quarter1_average", "Quarter2_average", "Quarter3_average", "Quarter4_average", "Average"}));
                marksSheet.getFinalGradeValue(StudentMarksSheetTable, sid);
                String GA = String.valueOf(String.format("%.2f", marksSheet.getGeneralAverage(sid)));
                Final_Average.setText("G.A. : " + GA);
            } else {
                JOptionPane.showMessageDialog(this, "No Grades Found");
            }

        }
    }//GEN-LAST:event_stuMarksSearchBtActionPerformed

    private void jPanel3MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_jPanel3MouseDragged

    private void jPanel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MousePressed
        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_jPanel3MousePressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        for (double i = 0.1; i <= 1.0; i += 0.1) {
            String s = i + "";
            float f = Float.valueOf(s);
            this.setOpacity(f);
            try {
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }//GEN-LAST:event_formWindowOpened

    private void ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearActionPerformed
        clearStudent();
    }//GEN-LAST:event_ClearActionPerformed

    private void stuPhoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stuPhoneKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_stuPhoneKeyTyped

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

    private void updateBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtActionPerformed
        if (isEmptyStudent()) {
            int id = Integer.parseInt(stuID.getText());
            if (student.isidExist(id)) {
                if (!check()) {
                    String sname = stuName.getText();
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
                    student.update(id, sname, date, gender, email, phone,
                            motherName, fatherName, addressLine1, addressLine2, birthCer, form137, imagePath);

                    String qrContent = "ID: " + id
                            + "\nName: " + sname
                            + "\nBirthdate: " + date
                            + "\nGender: " + gender
                            + "\nEmail: " + email
                            + "\nPhone: " + phone
                            + "\nMother: " + motherName
                            + "\nFather: " + fatherName
                            + "\nAddress 1: " + addressLine1
                            + "\nAddress 2: " + addressLine2
                            + "\nBirth Certificate: " + birthCer
                            + "\nForm 137: " + form137
                            + "\nImage: " + imagePath;

                    // ✅ Generate QR code with all details
                    generateQRCode(id, sname, qrContent);

                    StudentTable.setModel(new DefaultTableModel(null, new Object[]{"Student ID", "Student Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name",
                        "Mother's Name", "Address Line 1", "Address Line 2", "Birth Certificate", "Form137", "Image Path"}));
                    student.getStudentValue(StudentTable, "");
                    clearStudent();

                }

            } else {
                JOptionPane.showMessageDialog(this, "student id doesn't exist");

            }

        }
    }//GEN-LAST:event_updateBtActionPerformed

    public boolean check() {
        String newEmail = stuEmail.getText();
        String newPhone = stuPhone.getText();
        String oldEmail = model.getValueAt(rowIndex, 4).toString();
        String oldPhone = model.getValueAt(rowIndex, 5).toString();
        if (newEmail.equals(oldEmail) && newPhone.equals(oldPhone)) {
            return false;

        } else {
            if (!newEmail.equals(oldEmail)) {
                boolean x = student.isEmailExist(newEmail);
                if (x) {
                    JOptionPane.showMessageDialog(this, "the email already exist");

                }
                return x;
            }
            if (!newPhone.equals(oldPhone)) {
                boolean x = student.isPhoneExist(newPhone);
                if (x) {
                    JOptionPane.showMessageDialog(this, "the phone number already exist");

                }
                return x;
            }
//         
        }
        return false;

    }

    private void updateSection() {
        String gradeLevel = (String) stuGradeLevel.getSelectedItem();
        String strand = (String) stuStrand.getSelectedItem();

        if (gradeLevel != null && strand != null) {
            Strand strandObj = new Strand();
            String section = strandObj.getNextSection(gradeLevel, strand);

            stuSection.removeAllItems(); // Clear old items
            stuSection.addItem(section); // Set new section
        }
    }
    private void StudentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StudentTableMouseClicked
        model = (DefaultTableModel) StudentTable.getModel();
        rowIndex = StudentTable.getSelectedRow();
        stuID.setText(model.getValueAt(rowIndex, 0).toString());
        stuName.setText(model.getValueAt(rowIndex, 1).toString());

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(rowIndex, 2).toString());
            stuBirth.setDate(date);
        } catch (ParseException ex) {
            System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        String gender = model.getValueAt(rowIndex, 3).toString();
        if (gender.equals("Male")) {
            stuGender.setSelectedIndex(0);

        } else {
            stuGender.setSelectedIndex(1);

        }
        stuEmail.setText(model.getValueAt(rowIndex, 4).toString());
        stuPhone.setText(model.getValueAt(rowIndex, 5).toString());
        stuFatherName.setText(model.getValueAt(rowIndex, 6).toString());
        stuMotherName.setText(model.getValueAt(rowIndex, 7).toString());
        stuAddress1.setText(model.getValueAt(rowIndex, 8).toString());
        stuAddress2.setText(model.getValueAt(rowIndex, 9).toString());
        stuBirthCer.setText(model.getValueAt(rowIndex, 10).toString());
        stuForm137.setText(model.getValueAt(rowIndex, 11).toString());
        String path = model.getValueAt(rowIndex, 12).toString();
        imagePath = path;
        imagePanel.setIcon(imageAdjust(path, null));//get image path and called image adjust method path to image
    }//GEN-LAST:event_StudentTableMouseClicked


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

    private void stuGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuGenderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuGenderActionPerformed

    private void delBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delBtActionPerformed
        int id = Integer.parseInt(stuID.getText());
        if (student.isidExist(id)) {
            student.delete(id);
            StudentTable.setModel(new DefaultTableModel(null, new Object[]{"Student ID", "Student Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name",
                "Mother's Name", "Address Line 1", "Address Line 2", "Birth Certificate", "Form137", "Image Path"}));
            student.getStudentValue(StudentTable, "");
            clearStudent();

        } else {
            JOptionPane.showMessageDialog(this, "the student doesn't exist");
        }
    }//GEN-LAST:event_delBtActionPerformed

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

    private void stuPrint_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuPrint_1ActionPerformed
        try {
            MessageFormat header = new MessageFormat("Students Information");
            MessageFormat footer = new MessageFormat("Page{0,number,integer}");
            StudentTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException ex) {
            System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_stuPrint_1ActionPerformed

    private void stuStrandClearBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuStrandClearBtActionPerformed
        clearStrand();
    }//GEN-LAST:event_stuStrandClearBtActionPerformed

    private void stuStrandSearchFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stuStrandSearchFieldKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_stuStrandSearchFieldKeyTyped

    private void stuSearchBt_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuSearchBt_2ActionPerformed
        if (stuSearchField_2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Search field is empty");

        } else {
            StudentTrackTable.setModel(new DefaultTableModel(null, new Object[]{"ID", "Student_ID", "Grade_Level", "Strand", "Section"}));
            strand.getStrandValue(StudentTrackTable, stuSearchField_2.getText());

        }
    }//GEN-LAST:event_stuSearchBt_2ActionPerformed

    private void stuRefresh_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuRefresh_2ActionPerformed
        StudentTrackTable.setModel(new DefaultTableModel(null, new Object[]{"ID", "Student_ID", "Grade_Level", "Strand", "Section"}));
        strand.getStrandValue(StudentTrackTable, "");
        stuSearchField_2.setText(null);
    }//GEN-LAST:event_stuRefresh_2ActionPerformed

    private void stuPrint_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuPrint_2ActionPerformed
        try {
            MessageFormat header = new MessageFormat("All Strand Student Information");
            MessageFormat footer = new MessageFormat("Page{0,number,integer}");
            StudentTrackTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException ex) {
            System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_stuPrint_2ActionPerformed

    private void stuGradeIDSearchFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stuGradeIDSearchFieldKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_stuGradeIDSearchFieldKeyTyped

    private void stuStrandSearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuStrandSearchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stuStrandSearchFieldActionPerformed

    private void strandIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_strandIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_strandIdActionPerformed

    private void stuGradeIDSearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuGradeIDSearchFieldActionPerformed

    }//GEN-LAST:event_stuGradeIDSearchFieldActionPerformed

    private void stuGradeManageClearBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuGradeManageClearBtActionPerformed
        clearGradeManage();
    }//GEN-LAST:event_stuGradeManageClearBtActionPerformed

    private void StudentGradeManagementTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StudentGradeManagementTableMouseClicked
        model = (DefaultTableModel) StudentGradeManagementTable.getModel();
        rowIndex = StudentGradeManagementTable.getSelectedRow();
        idGradeManage.setText(model.getValueAt(rowIndex, 0).toString());
        stuGradeManageID.setText(model.getValueAt(rowIndex, 1).toString());
        stuGradeManageGradeLevel.setText(model.getValueAt(rowIndex, 2).toString());
        stuGradeManageStrand.setText(model.getValueAt(rowIndex, 3).toString());
        stuGradeManageSection.setText(model.getValueAt(rowIndex, 4).toString());
        stuGradeManageSub1.setText(model.getValueAt(rowIndex, 5).toString());//1
        subScore1.setText(model.getValueAt(rowIndex, 6).toString());
        stuGradeManageSub2.setText(model.getValueAt(rowIndex, 7).toString());//2
        subScore2.setText(model.getValueAt(rowIndex, 8).toString());
        stuGradeManageSub3.setText(model.getValueAt(rowIndex, 9).toString());//3
        subScore3.setText(model.getValueAt(rowIndex, 10).toString());
        stuGradeManageSub4.setText(model.getValueAt(rowIndex, 11).toString());//4
        subScore4.setText(model.getValueAt(rowIndex, 12).toString());
        stuGradeManageSub5.setText(model.getValueAt(rowIndex, 13).toString());//5
        subScore5.setText(model.getValueAt(rowIndex, 14).toString());
        stuGradeManageSub6.setText(model.getValueAt(rowIndex, 15).toString());//6
        subScore6.setText(model.getValueAt(rowIndex, 16).toString());
        stuGradeManageSub7.setText(model.getValueAt(rowIndex, 17).toString());//7
        subScore7.setText(model.getValueAt(rowIndex, 18).toString());
        stuGradeManageSub8.setText(model.getValueAt(rowIndex, 19).toString());//8
        subScore8.setText(model.getValueAt(rowIndex, 20).toString());
        String quarter = model.getValueAt(rowIndex, 21).toString();
        if (quarter.equals("1")) {
            stuGradeManageQuarter.setSelectedIndex(0);

        } else if (quarter.equals("2")) {
            stuGradeManageQuarter.setSelectedIndex(1);

        } else if (quarter.equals("3")) {
            stuGradeManageQuarter.setSelectedIndex(2);

        } else {
            stuGradeManageQuarter.setSelectedIndex(3);
        }

    }//GEN-LAST:event_StudentGradeManagementTableMouseClicked

    private void stuGradeManageUpdateBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuGradeManageUpdateBtActionPerformed
        int id = Integer.parseInt(idGradeManage.getText());
        if (grade.isidExist(id)) {
            if (isNumeric(subScore1.getText()) && isNumeric(subScore2.getText())
                    && isNumeric(subScore3.getText()) && isNumeric(subScore4.getText())
                    && isNumeric(subScore6.getText()) && isNumeric(subScore7.getText()) && isNumeric(subScore8.getText())) {
                int quarter = Integer.parseInt(stuGradeManageQuarter.getSelectedItem().toString());
                double subGrade1 = Double.parseDouble(subScore1.getText());
                double subGrade2 = Double.parseDouble(subScore2.getText());
                double subGrade3 = Double.parseDouble(subScore3.getText());
                double subGrade4 = Double.parseDouble(subScore4.getText());
                double subGrade5 = Double.parseDouble(subScore5.getText());
                double subGrade6 = Double.parseDouble(subScore6.getText());
                double subGrade7 = Double.parseDouble(subScore7.getText());
                double subGrade8 = Double.parseDouble(subScore8.getText());
                int sid = Integer.parseInt(stuGradeManageID.getText());
                int gradeLevel = Integer.parseInt(stuGradeManageGradeLevel.getText());
                String strand = stuGradeManageStrand.getText();
                String section = stuGradeManageSection.getText();
                double average = (subGrade1 + subGrade2 + subGrade3 + subGrade4 + subGrade5
                        + subGrade6 + subGrade7 + subGrade8) / 8;
                average = Math.round(average * 100.0) / 100.0; // rounds to 2 decimals
                grade.update(id, subGrade1, subGrade2, subGrade3, subGrade4, subGrade5,
                        subGrade6, subGrade7, subGrade8, quarter, Double.parseDouble(nf.format(average)));
                StudentGradeManagementTable.setModel(new DefaultTableModel(null, new Object[]{"ID", "Student_ID", "Grade_Level", "Strand", "Section",
                    "Subject 1", "Score 1", "Subject 2", "Score 2", "Subject 3", "Score 3", "Subject 4", "Score 4", "Subject 5", "Score 5",
                    "Subject 6", "Score 6", "Subject 7", "Score 7", "Subject 8", "Score 8", "Quarter", "Average"}));
                insertUpdateMarkSheet(quarter, average, sid, gradeLevel, strand, section);
                grade.getGradeValue(StudentGradeManagementTable, "");
                clearGradeManage();

            }

        } else {
            JOptionPane.showMessageDialog(this, "Grade id doesnt exist or wrong input");

        }
    }//GEN-LAST:event_stuGradeManageUpdateBtActionPerformed

    private void stuGradeManageSearchTableBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuGradeManageSearchTableBtActionPerformed
        if (stuGradeManageSearchStuField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Search field is empty");

        } else {
            StudentGradeManagementTable.setModel(new DefaultTableModel(null, new Object[]{"ID", "Student_ID", "Grade_Level", "Strand", "Section",
                "Subject 1", "Score 1", "Subject 2", "Score 2", "Subject 3", "Score 3", "Subject 4", "Score 4", "Subject 5", "Score 5",
                "Subject 6", "Score 6", "Subject 7", "Score 7", "Subject 8", "Score 8", "Quarter", "Average"}));
            grade.getGradeValue(StudentGradeManagementTable, stuGradeManageSearchStuField.getText());

        }
    }//GEN-LAST:event_stuGradeManageSearchTableBtActionPerformed

    private void stuGradeManageRefreshTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuGradeManageRefreshTableActionPerformed
        StudentGradeManagementTable.setModel(new DefaultTableModel(null, new Object[]{"ID", "Student_ID", "Grade_Level", "Strand", "Section",
            "Subject 1", "Score 1", "Subject 2", "Score 2", "Subject 3", "Score 3", "Subject 4", "Score 4", "Subject 5", "Score 5",
            "Subject 6", "Score 6", "Subject 7", "Score 7", "Subject 8", "Score 8", "Quarter", "Average"}));
        grade.getGradeValue(StudentGradeManagementTable, "");
        stuGradeManageSearchStuField.setText(null);
    }//GEN-LAST:event_stuGradeManageRefreshTableActionPerformed

    private void stuMarksClearBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuMarksClearBtActionPerformed
        stuMarksSearchField.setText(null);
        Final_Average.setText("G.A. : 0.0");
        StudentMarksSheetTable.setModel(new DefaultTableModel(null, new Object[]{"ID", "Student_ID", "Grade_Level", "Strand", "Section",
            "Quarter1_average", "Quarter2_average", "Quarter3_average", "Quarter4_average", "Average"}));
        
    }//GEN-LAST:event_stuMarksClearBtActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        try {
            MessageFormat header = new MessageFormat("Student Final Grade Information of" + stuMarksSearchField.getText());
            MessageFormat footer = new MessageFormat("Page{0,number,integer}");
            StudentMarksSheetTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException ex) {
            System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_jButton20ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Home().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Clear;
    private javax.swing.JLabel Final_Average;
    private javax.swing.JToggleButton StartWebcam;
    private javax.swing.JTable StudentGradeManagementTable;
    private javax.swing.JTable StudentMarksSheetTable;
    private javax.swing.JTable StudentTable;
    private javax.swing.JTable StudentTrackTable;
    private javax.swing.JButton addNewBt;
    private javax.swing.JButton browseBirthCertificate;
    private javax.swing.JButton browseForm137;
    private javax.swing.JButton browseImg;
    private javax.swing.JButton delBt;
    private javax.swing.JTextField idGradeManage;
    private javax.swing.JLabel imagePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
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
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton searchBt_1;
    private javax.swing.JTextField strandId;
    private javax.swing.JTextField stuAddress1;
    private javax.swing.JTextField stuAddress2;
    private com.toedter.calendar.JDateChooser stuBirth;
    private javax.swing.JTextField stuBirthCer;
    private javax.swing.JTextField stuEmail;
    private javax.swing.JTextField stuFatherName;
    private javax.swing.JTextField stuForm137;
    private javax.swing.JComboBox<String> stuGender;
    private javax.swing.JTextField stuGradeIDSearchField;
    private javax.swing.JComboBox<String> stuGradeLevel;
    private javax.swing.JButton stuGradeManageClearBt;
    public static javax.swing.JTextField stuGradeManageGradeLevel;
    private javax.swing.JComboBox<String> stuGradeManageGradeLevelSearchField;
    public static javax.swing.JTextField stuGradeManageID;
    private javax.swing.JButton stuGradeManagePrint;
    private javax.swing.JComboBox<String> stuGradeManageQuarter;
    private javax.swing.JButton stuGradeManageRefreshTable;
    private javax.swing.JButton stuGradeManageSave;
    private javax.swing.JButton stuGradeManageSearchButton;
    private javax.swing.JTextField stuGradeManageSearchStuField;
    private javax.swing.JButton stuGradeManageSearchTableBt;
    public static javax.swing.JTextField stuGradeManageSection;
    public static javax.swing.JTextField stuGradeManageStrand;
    public static javax.swing.JTextField stuGradeManageSub1;
    public static javax.swing.JTextField stuGradeManageSub2;
    public static javax.swing.JTextField stuGradeManageSub3;
    public static javax.swing.JTextField stuGradeManageSub4;
    public static javax.swing.JTextField stuGradeManageSub5;
    public static javax.swing.JTextField stuGradeManageSub6;
    public static javax.swing.JTextField stuGradeManageSub7;
    public static javax.swing.JTextField stuGradeManageSub8;
    private javax.swing.JButton stuGradeManageUpdateBt;
    private javax.swing.JTextField stuID;
    private javax.swing.JButton stuMarksClearBt;
    private javax.swing.JButton stuMarksSearchBt;
    private javax.swing.JTextField stuMarksSearchField;
    private javax.swing.JTextField stuMotherName;
    private javax.swing.JTextField stuName;
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
    private javax.swing.JComboBox<String> stuStrand;
    private javax.swing.JButton stuStrandClearBt;
    public static javax.swing.JTextField stuStrandId;
    private javax.swing.JButton stuStrandSearchBt;
    private javax.swing.JTextField stuStrandSearchField;
    private javax.swing.JTextField subScore1;
    private javax.swing.JTextField subScore2;
    private javax.swing.JTextField subScore3;
    private javax.swing.JTextField subScore4;
    private javax.swing.JTextField subScore5;
    private javax.swing.JTextField subScore6;
    private javax.swing.JTextField subScore7;
    private javax.swing.JTextField subScore8;
    private javax.swing.JButton updateBt;
    private javax.swing.JPanel webPanel;
    // End of variables declaration//GEN-END:variables
}
