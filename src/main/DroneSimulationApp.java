

package main;

import main.filehandling.FileHandler;
import main.imageprocessing.SimpleImageProcessor;
import main.prediction.Predictor;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DroneSimulationApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Drone Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700); 

            // Left panel for displaying sample images with title and padding
            JPanel sampleImagePanel = new JPanel(new GridLayout(2, 2, 10, 10));
            sampleImagePanel.setBackground(new Color(230, 240, 255));
            sampleImagePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JLabel sampleImageLabel = new JLabel("Sample Images");
            sampleImageLabel.setFont(new Font("Arial", Font.BOLD, 18));
            sampleImageLabel.setForeground(new Color(33, 37, 41));
            sampleImageLabel.setHorizontalAlignment(JLabel.CENTER);

            JPanel sampleImageContainer = new JPanel(new BorderLayout());
            sampleImageContainer.add(sampleImageLabel, BorderLayout.NORTH);
            sampleImageContainer.add(sampleImagePanel, BorderLayout.CENTER);

            String[] sampleImagePaths = new String[4]; // Hold sample image paths
            for (int i = 1; i <= 4; i++) {
                String sampleImagePath = "src/resources/sample_images/image" + i + ".jpg";
                sampleImagePaths[i - 1] = sampleImagePath;
                ImageIcon sampleIcon = new ImageIcon(
                        new ImageIcon(sampleImagePath)
                                .getImage()
                                .getScaledInstance(300, 300, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(sampleIcon);
                imageLabel.setHorizontalAlignment(JLabel.CENTER);
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                sampleImagePanel.add(imageLabel);
            }

            // Panel for real-time image with GridBagLayout to allow full sizing and
             JPanel realTimeImagePanel = new JPanel(new GridBagLayout());
            realTimeImagePanel.setBackground(new Color(240, 255, 240));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.gridy = 0;

            JLabel realTimeImageLabelTitle = new JLabel("Real-Time Image");
            realTimeImageLabelTitle.setFont(new Font("Arial", Font.BOLD, 18));
            realTimeImageLabelTitle.setForeground(new Color(33, 37, 41));
            realTimeImageLabelTitle.setHorizontalAlignment(JLabel.CENTER);

            gbc.gridy = 0; // First row for the title
            realTimeImagePanel.add(realTimeImageLabelTitle, gbc);

            gbc.gridy = 1; // Second row for the image
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH; // Allow the image to take full space
            JLabel realTimeImageLabel = new JLabel();
            realTimeImageLabel.setHorizontalAlignment(JLabel.CENTER);
            realTimeImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            realTimeImagePanel.add(realTimeImageLabel, gbc);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sampleImageContainer,
                    realTimeImagePanel);
            splitPane.setResizeWeight(0.5);
            splitPane.setDividerLocation(frame.getWidth() / 2);
            splitPane.setContinuousLayout(true);
            splitPane.setDividerSize(5);

            JButton uploadButton = new JButton("Upload Real-Time Image");
            uploadButton.setFont(new Font("Arial", Font.PLAIN, 16));
            uploadButton.setForeground(Color.WHITE);
            uploadButton.setBackground(new Color(70, 130, 180));
            uploadButton.setFocusPainted(false);
            uploadButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            uploadButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    ImageIcon realTimeIcon = new ImageIcon(
                            new ImageIcon(selectedFile.getPath())
                                    .getImage()
                                    .getScaledInstance(realTimeImageLabel.getWidth(), realTimeImageLabel.getHeight(),
                                            Image.SCALE_SMOOTH));
                    realTimeImageLabel.setIcon(realTimeIcon);

                    SimpleImageProcessor processor = new SimpleImageProcessor();
                    Predictor predictor = new Predictor();
                    StringBuilder comparisonResults = new StringBuilder("Crop Health Progress:\n");

                    for (int i = 0; i < sampleImagePaths.length; i++) {
                        double similarity = processor.compareImages(selectedFile, new File(sampleImagePaths[i]));
                        String prediction = predictor.predictFromImage(similarity);

                        comparisonResults.append("Sample Image ").append(i + 1)
                                .append(": Similarity = ").append(similarity).append("%\n")
                                .append("Health Status: ").append(prediction).append("\n\n");
                    }

                    JTextArea resultArea = new JTextArea(comparisonResults.toString());
                    resultArea.setEditable(false);
                    resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
                    resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    JOptionPane.showMessageDialog(frame, new JScrollPane(resultArea), "Crop Health Progress",
                            JOptionPane.INFORMATION_MESSAGE);

                    FileHandler.saveResult(comparisonResults.toString());
                }
            });

            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            mainPanel.add(splitPane, BorderLayout.CENTER);
            mainPanel.add(uploadButton, BorderLayout.SOUTH);

            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }
}
