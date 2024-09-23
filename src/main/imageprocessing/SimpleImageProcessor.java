package main.imageprocessing;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SimpleImageProcessor extends ImageProcessor {

    @Override
    public double compareImages(File image1, File image2) {
        try {
            BufferedImage img1 = ImageIO.read(image1);
            BufferedImage img2 = ImageIO.read(image2);

            int width1 = img1.getWidth();
            int height1 = img1.getHeight();
            int width2 = img2.getWidth();
            int height2 = img2.getHeight();

            if (width1 != width2 || height1 != height2) {
                System.out.println("Images have different dimensions!");
                return 0.0;
            }

            long diff = 0;
            for (int y = 0; y < height1; y++) {
                for (int x = 0; x < width1; x++) {
                    int rgb1 = img1.getRGB(x, y);
                    int rgb2 = img2.getRGB(x, y);
                    int r1 = (rgb1 >> 16) & 0xff;
                    int g1 = (rgb1 >> 8) & 0xff;
                    int b1 = rgb1 & 0xff;
                    int r2 = (rgb2 >> 16) & 0xff;
                    int g2 = (rgb2 >> 8) & 0xff;
                    int b2 = rgb2 & 0xff;
                    diff += Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
                }
            }

            double totalPixels = width1 * height1 * 3;
            double avgDiff = diff / totalPixels;
            return 100 - (avgDiff / 255) * 100;
        } catch (IOException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
