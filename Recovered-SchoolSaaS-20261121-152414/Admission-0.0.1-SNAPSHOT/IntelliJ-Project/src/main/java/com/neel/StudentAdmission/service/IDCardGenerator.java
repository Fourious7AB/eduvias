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
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
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
            int detailsStartX;
            FontMetrics fontMetrics;
            int nameWidth;
            FontMetrics fm;
            int fontSize = 60;
            System.out.println("Loading template from: " + templatePath);
            URL templateUrl = new URL(templatePath);
            BufferedImage template = ImageIO.read(templateUrl);
            if (template == null) {
                throw new IOException("Failed to load template image from URL: " + templatePath);
            }
            Graphics2D graphics = template.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            GradientPaint backgroundGradient = new GradientPaint(0.0f, 0.0f, new Color(255, 255, 255), 0.0f, template.getHeight(), new Color(240, 240, 255));
            graphics.setPaint(backgroundGradient);
            graphics.fillRect(0, 0, template.getWidth(), template.getHeight());
            int headerHeight = 140;
            GradientPaint headerGradient = new GradientPaint(0.0f, 0.0f, new Color(102, 51, 153), template.getWidth(), 0.0f, new Color(138, 43, 226));
            graphics.setPaint(headerGradient);
            graphics.fillRect(0, 0, template.getWidth(), headerHeight);
            graphics.setColor(new Color(255, 255, 255, 30));
            for (int i = 0; i < 5; ++i) {
                int size = 80 + i * 20;
                int x = template.getWidth() - size - 20;
                int y = -size / 2;
                graphics.fillOval(x, y, size, size);
            }
            BufferedImage schoolLogo = null;
            int logoSize = 90;
            int logoX = 40;
            int logoY = (headerHeight - logoSize) / 2;
            try (InputStream inputStream = new URL(IDCardGenerator.convertAvifImageUrl((String)school.getSchoolLogo())).openStream();){
                schoolLogo = ImageIO.read(inputStream);
                if (schoolLogo != null) {
                    BufferedImage circularLogo = new BufferedImage(logoSize + 10, logoSize + 10, 2);
                    Graphics2D logoGraphics = circularLogo.createGraphics();
                    logoGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    for (int i = 5; i > 0; --i) {
                        logoGraphics.setColor(new Color(255, 255, 255, 50 + i * 30));
                        logoGraphics.fillOval(5 - i, 5 - i, logoSize + i * 2, logoSize + i * 2);
                    }
                    Ellipse2D.Double circle = new Ellipse2D.Double(5.0, 5.0, logoSize, logoSize);
                    logoGraphics.setClip(circle);
                    logoGraphics.drawImage(schoolLogo, 5, 5, logoSize, logoSize, null);
                    graphics.drawImage((Image)circularLogo, logoX - 5, logoY - 5, null);
                    logoGraphics.dispose();
                }
            }
            catch (Exception e) {
                System.err.println("Error loading school logo: " + e.getMessage());
            }
            String schoolName = school.getSchoolName();
            int schoolNameX = logoX + logoSize + 30;
            String[] schoolNameLines = IDCardGenerator.splitSchoolNameIntoTwoLines((String)schoolName);
            Font schoolNameFont = new Font("Arial", 1, fontSize);
            graphics.setFont(schoolNameFont);
            int maxWidth = template.getWidth() - schoolNameX - 50;
            boolean fontSizeAdjusted = false;
            for (fontSize = 60; fontSize > 28; fontSize -= 2) {
                graphics.setFont(new Font("Arial", 1, fontSize));
                fm = graphics.getFontMetrics();
                int line1Width = fm.stringWidth(schoolNameLines[0]);
                int line2Width = fm.stringWidth(schoolNameLines[1]);
                int maxLineWidth = Math.max(line1Width, line2Width);
                if (maxLineWidth <= maxWidth) break;
                fontSizeAdjusted = true;
            }
            schoolNameFont = new Font("Arial", 1, fontSize);
            graphics.setFont(schoolNameFont);
            fm = graphics.getFontMetrics();
            int lineHeight = fm.getHeight();
            int line1Y = headerHeight / 2 - (schoolNameLines[1].isEmpty() ? 0 : lineHeight / 4);
            int line2Y = line1Y + lineHeight;
            graphics.setColor(Color.WHITE);
            graphics.drawString(schoolNameLines[0], schoolNameX, line1Y);
            if (!schoolNameLines[1].isEmpty()) {
                graphics.drawString(schoolNameLines[1], schoolNameX, line2Y);
            }
            graphics.setColor(new Color(102, 51, 153, 10));
            for (int i = 0; i < template.getWidth(); i += 40) {
                for (int j = headerHeight + 20; j < template.getHeight(); j += 40) {
                    graphics.fillOval(i, j, 10, 10);
                }
            }
            int imageDiameter = 250;
            int imageX = (template.getWidth() - imageDiameter) / 2;
            int imageY = headerHeight + 50;
            graphics.setStroke(new BasicStroke(5.0f));
            graphics.setColor(new Color(102, 51, 153, 60));
            graphics.drawOval(imageX - 5, imageY - 5, imageDiameter + 10, imageDiameter + 10);
            graphics.setColor(new Color(102, 51, 153, 40));
            graphics.drawOval(imageX - 10, imageY - 10, imageDiameter + 20, imageDiameter + 20);
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
                studentImage = new BufferedImage(imageDiameter, imageDiameter, 1);
                Graphics2D g = studentImage.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(new Color(240, 240, 240));
                g.fillRect(0, 0, imageDiameter, imageDiameter);
                g.setColor(new Color(102, 51, 153, 80));
                g.fillOval(40, 40, imageDiameter - 80, imageDiameter - 80);
                g.setColor(new Color(102, 51, 153));
                g.setFont(new Font("Arial", 1, 30));
                FontMetrics fm2 = g.getFontMetrics();
                g.drawString("No Image", (imageDiameter - fm2.stringWidth("No Image")) / 2, imageDiameter / 2);
                g.dispose();
                System.out.println("Using placeholder image instead");
            }
            graphics.setClip(new Ellipse2D.Double(imageX, imageY, imageDiameter, imageDiameter));
            graphics.drawImage(studentImage, imageX, imageY, imageDiameter, imageDiameter, null);
            graphics.setClip(null);
            int nameY = imageY + imageDiameter + 80;
            String studentName = admission.getStudentName();
            int maxNameWidth = template.getWidth() - 100;
            int nameFontSize = 50;
            do {
                Font nameFont = new Font("Poppins", 1, nameFontSize);
                graphics.setFont(nameFont);
            } while ((nameWidth = (fontMetrics = graphics.getFontMetrics()).stringWidth(studentName)) > maxNameWidth && (nameFontSize -= 2) > 24);
            GradientPaint textGradient = new GradientPaint(0.0f, nameY - 40, new Color(102, 51, 153), 0.0f, nameY, new Color(138, 43, 226));
            int centerX = (template.getWidth() - nameWidth) / 2;
            graphics.setColor(new Color(0, 0, 0, 30));
            graphics.drawString(studentName, centerX + 2, nameY + 2);
            graphics.setPaint(textGradient);
            graphics.drawString(studentName, centerX, nameY);
            int panelX = 80;
            int panelWidth = template.getWidth() - 160;
            int panelHeight = 280;
            int panelY = nameY + 70;
            if (panelY + panelHeight > template.getHeight() - 50) {
                panelY = template.getHeight() - panelHeight - 50;
            }
            GradientPaint panelGradient = new GradientPaint(0.0f, panelY, new Color(245, 245, 255), 0.0f, panelY + panelHeight, new Color(235, 235, 255));
            graphics.setPaint(panelGradient);
            graphics.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);
            graphics.setColor(new Color(102, 51, 153, 100));
            graphics.setStroke(new BasicStroke(2.0f));
            graphics.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);
            int detailsLabelX = detailsStartX = panelX + 40;
            int detailsValueX = detailsStartX + 180;
            int detailsStartY = panelY + 60;
            int lineSpacing = 40;
            graphics.setFont(new Font("Arial", 1, 24));
            graphics.setColor(new Color(102, 51, 153));
            graphics.drawString("Class", detailsLabelX, detailsStartY);
            graphics.setFont(new Font("Arial", 0, 24));
            graphics.drawString(": " + admission.getClassName(), detailsValueX, detailsStartY);
            graphics.setFont(new Font("Arial", 1, 24));
            graphics.drawString("Section", detailsLabelX, detailsStartY + lineSpacing);
            graphics.setFont(new Font("Arial", 0, 24));
            graphics.drawString(": " + admission.getSection(), detailsValueX, detailsStartY + lineSpacing);
            graphics.setFont(new Font("Arial", 1, 24));
            graphics.drawString("Adm No", detailsLabelX, detailsStartY + 2 * lineSpacing);
            graphics.setFont(new Font("Arial", 0, 24));
            graphics.drawString(": " + admission.getAdmissionNumber(), detailsValueX, detailsStartY + 2 * lineSpacing);
            graphics.setFont(new Font("Arial", 1, 24));
            graphics.drawString("Blood Group", detailsLabelX, detailsStartY + 3 * lineSpacing);
            graphics.setFont(new Font("Arial", 0, 24));
            graphics.drawString(": " + admission.getBloodGroup(), detailsValueX, detailsStartY + 3 * lineSpacing);
            graphics.setFont(new Font("Arial", 1, 24));
            graphics.drawString("Guardian", detailsLabelX, detailsStartY + 4 * lineSpacing);
            graphics.setFont(new Font("Arial", 0, 24));
            graphics.drawString(": " + admission.getFatherName(), detailsValueX, detailsStartY + 4 * lineSpacing);
            graphics.setFont(new Font("Arial", 1, 24));
            graphics.drawString("Contact", detailsLabelX, detailsStartY + 5 * lineSpacing);
            graphics.setFont(new Font("Arial", 0, 24));
            graphics.drawString(": " + admission.getPhone(), detailsValueX, detailsStartY + 5 * lineSpacing);
            panelHeight = 330;
            int footerY = template.getHeight() - 70;
            graphics.setFont(new Font("Arial", 1, 16));
            graphics.setColor(new Color(102, 51, 153));
            graphics.setFont(new Font("Arial", 2, 14));
            String contactText = "Phone: " + (school.getSchoolPhone() != null ? school.getSchoolPhone() : "") + " | Email: " + (school.getSchoolEmail() != null ? school.getSchoolEmail() : "");
            int contactWidth = graphics.getFontMetrics().stringWidth(contactText);
            graphics.drawString(contactText, (template.getWidth() - contactWidth) / 2, footerY + 25);
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

    private static String[] splitSchoolNameIntoTwoLines(String schoolName) {
        if (schoolName.length() < 20) {
            return new String[]{schoolName, ""};
        }
        String[] words = schoolName.split(" ");
        if (words.length <= 1) {
            return new String[]{schoolName, ""};
        }
        int totalLength = schoolName.length();
        int halfLength = totalLength / 2;
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        int currentLength = 0;
        for (String word : words) {
            if (currentLength + word.length() <= halfLength) {
                if (line1.length() > 0) {
                    line1.append(" ");
                }
                line1.append(word);
                currentLength += word.length() + 1;
                continue;
            }
            if (line2.length() > 0) {
                line2.append(" ");
            }
            line2.append(word);
        }
        if (line1.length() > 0 && line2.length() == 0) {
            line1 = new StringBuilder();
            line2 = new StringBuilder();
            int midPoint = words.length / 2;
            for (int i = 0; i < words.length; ++i) {
                if (i < midPoint) {
                    if (line1.length() > 0) {
                        line1.append(" ");
                    }
                    line1.append(words[i]);
                    continue;
                }
                if (line2.length() > 0) {
                    line2.append(" ");
                }
                line2.append(words[i]);
            }
        }
        return new String[]{line1.toString(), line2.toString()};
    }

    private static String[] splitSchoolName(String schoolName) {
        if (schoolName.length() < 25) {
            return new String[]{schoolName};
        }
        String[] words = schoolName.split(" ");
        if (words.length <= 1) {
            return new String[]{schoolName};
        }
        if (words.length >= 4) {
            StringBuilder[] lines = new StringBuilder[3];
            for (int i = 0; i < lines.length; ++i) {
                lines[i] = new StringBuilder();
            }
            int totalChars = schoolName.length();
            int charsPerLine = totalChars / 3;
            int currentLine = 0;
            int currentLineLength = 0;
            for (String word : words) {
                if (currentLineLength + word.length() > charsPerLine && currentLine < 2) {
                    ++currentLine;
                    currentLineLength = 0;
                }
                if (currentLineLength > 0) {
                    lines[currentLine].append(" ");
                    ++currentLineLength;
                }
                lines[currentLine].append(word);
                currentLineLength += word.length();
            }
            return new String[]{lines[0].toString().trim(), lines[1].toString().trim(), lines[2].toString().trim()};
        }
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        int midpoint = words.length / 2;
        for (int i = 0; i < words.length; ++i) {
            if (i < midpoint) {
                if (line1.length() > 0) {
                    line1.append(" ");
                }
                line1.append(words[i]);
                continue;
            }
            if (line2.length() > 0) {
                line2.append(" ");
            }
            line2.append(words[i]);
        }
        return new String[]{line1.toString().trim(), line2.toString().trim(), ""};
    }
}

