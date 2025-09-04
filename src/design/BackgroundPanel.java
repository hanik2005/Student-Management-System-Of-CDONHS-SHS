/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package design;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author ADMIN
 */
public class BackgroundPanel extends JPanel {

    private final Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        // Load image safely
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        backgroundImage = icon.getImage();
        setOpaque(false); // ensures child components are visible
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            // Smooth scaling
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2.dispose();
        }
    }
}
