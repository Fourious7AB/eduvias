/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.model.School
 *  com.neel.StudentAdmission.service.IDCardGenerator
 */
package com.neel.StudentAdmission.service;

import com.neel.StudentAdmission.model.Admission;
import com.neel.StudentAdmission.model.School;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;

/*
 * Exception performing whole class analysis ignored.
 */
public class IDCardGenerator {
    public static void generateIDCard(String templatePath, String outputPath, Admission admission, School school) {
        try {
            System.out.println("Loading template from: " + templatePath);
            URL templateUrl = new URL(templatePath);
            BufferedImage template = ImageIO.read(templateUrl);
            if (template == null) {
                throw new IOException("Failed to load template image from URL: " + templatePath);
            }
            Graphics2D graphics = template.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setFont(new Font("Poppins", 1, 36));
            graphics.setColor(Color.BLACK);
            String schoolName = school.getSchoolName();
            FontMetrics fm = graphics.getFontMetrics();
            int schoolNameWidth = fm.stringWidth(schoolName);
            int maxWidth = template.getWidth() - 100;
            if (schoolNameWidth > maxWidth) {
                int scaledFontSize = (int)(36.0 * (double)maxWidth / (double)schoolNameWidth);
                graphics.setFont(new Font("Poppins", 1, scaledFontSize));
                fm = graphics.getFontMetrics();
                schoolNameWidth = fm.stringWidth(schoolName);
            }
            int schoolNameX = (template.getWidth() - schoolNameWidth) / 2;
            graphics.drawString(schoolName, schoolNameX, 50);
            graphics.setFont(new Font("Poppins", 1, 26));
            graphics.setColor(Color.BLACK);
            graphics.drawString("Admission No: " + admission.getAdmissionNumber(), 193, 762);
            graphics.setFont(new Font("Poppins", 1, 50));
            graphics.setColor(Color.BLUE);
            String studentName = admission.getStudentName();
            FontMetrics fontMetrics = graphics.getFontMetrics();
            int nameWidth = fontMetrics.stringWidth(studentName);
            int centerX = (template.getWidth() - nameWidth) / 2;
            graphics.drawString(studentName, centerX, 479);
            graphics.setFont(new Font("Poppins", 1, 26));
            graphics.setColor(Color.BLACK);
            graphics.drawString("Class: " + admission.getClassName(), 192, 646);
            graphics.setFont(new Font("Times New Roman", 1, 26));
            graphics.setColor(Color.BLACK);
            graphics.drawString("Section: " + admission.getSection(), 193, 705);
            graphics.setFont(new Font("Poppins", 1, 26));
            graphics.setColor(Color.BLACK);
            graphics.drawString("Blood Group: " + admission.getBloodGroup(), 193, 820);
            BufferedImage schoolLogo = null;
            try (InputStream inputStream = new URL(school.getSchoolLogo()).openStream();){
                schoolLogo = ImageIO.read(inputStream);
            }
            if (schoolLogo != null) {
                int logoX = 50;
                int logoY = 20;
                int logoWidth = 120;
                int logoHeight = 120;
                graphics.drawImage(schoolLogo, logoX, logoY, logoWidth, logoHeight, null);
            }
            BufferedImage studentImage = null;
            String studentImagePath = admission.getStudentImage();
            System.out.println("Loading student image from: " + studentImagePath);
            try {
                if (studentImagePath == null || studentImagePath.isEmpty()) {
                    throw new IOException("Student image path is empty or null");
                }
                if (studentImagePath.startsWith("http") || studentImagePath.startsWith("https")) {
                    URL imageUrl = new URL(studentImagePath);
                    if (studentImagePath.toLowerCase().endsWith(".avif")) {
                        String convertedUrl = IDCardGenerator.convertAvifImageUrl((String)studentImagePath);
                        imageUrl = new URL(convertedUrl);
                        System.out.println("Converting AVIF image to JPG: " + convertedUrl);
                    }
                    try (InputStream inputStream = imageUrl.openStream();){
                        studentImage = ImageIO.read(inputStream);
                    }
                } else {
                    File studentImageFile = new File(studentImagePath);
                    if (studentImageFile.exists()) {
                        studentImage = ImageIO.read(studentImageFile);
                    } else {
                        throw new IOException("Student image file does not exist: " + studentImagePath);
                    }
                }
                if (studentImage == null) {
                    throw new IOException("Failed to read student image from: " + studentImagePath);
                }
            }
            catch (Exception e) {
                System.out.println("Error loading student image: " + e.getMessage());
                studentImage = new BufferedImage(308, 308, 1);
                Graphics2D g = studentImage.createGraphics();
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, 308, 308);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", 1, 20));
                g.drawString("No Image", 100, 150);
                g.dispose();
                System.out.println("Using placeholder image instead");
            }
            int imageX = 172;
            int imageY = 104;
            int imageDiameter = 308;
            graphics.setClip(new Ellipse2D.Double(imageX, imageY, imageDiameter, imageDiameter));
            graphics.drawImage(studentImage, imageX, imageY, imageDiameter, imageDiameter, null);
            graphics.setClip(null);
            File outputFile = new File(outputPath);
            if (!outputFile.getParentFile().exists()) {
                System.out.println("Creating output directory: " + outputFile.getParentFile().getAbsolutePath());
                outputFile.getParentFile().mkdirs();
            }
            graphics.dispose();
            ImageIO.write((RenderedImage)template, "png", outputFile);
            System.out.println("ID Card generated successfully at: " + outputPath);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating ID card: " + e.getMessage(), e);
        }
    }

    private static String convertAvifImageUrl(String avifUrl) {
        if (avifUrl.contains("cloudinary.com") && avifUrl.contains("/upload/")) {
            return avifUrl.replace("/upload/", "/upload/f_jpg,q_auto/");
        }
        return avifUrl;
    }
}

