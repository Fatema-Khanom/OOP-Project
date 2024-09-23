package main.imageprocessing;

import java.io.File;

public abstract class ImageProcessor {
    public abstract double compareImages(File image1, File image2);
}
