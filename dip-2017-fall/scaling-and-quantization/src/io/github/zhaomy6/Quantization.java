package io.github.zhaomy6;

import java.awt.image.BufferedImage;

public class Quantization {
    //  level: is an integer in [1, 256] defining the number of gray levels of output
    public static BufferedImage quantize(BufferedImage img, int level) {
        double stage;
        if (level == 1) {
            //  TODO
            stage = 255.0;
        } else {
            stage = 255.0 / (level - 1);
        }
        double halfStage = stage / 2;
        int width = img.getWidth();
        int height = img.getHeight();
        byte[] rawPixels = new byte[width * height];
        img.getRaster().getDataElements(0, 0, width, height, rawPixels);
        byte[] newPixels = new byte[width * height];
        int counter = 0;
        for (byte pel : rawPixels) {
            int aPel = 0xff & pel;
            //  for now, using naive algorithm
            for (int i = 0; i < level; ++i) {
                if (i * stage + halfStage > aPel) {
                    int tmpNewPel = (int) (i * stage);
                    newPixels[counter] = (byte) tmpNewPel;
                    break;
                }
            }
            ++counter;
        }
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        newImg.getRaster().setDataElements(0, 0, width, height, newPixels);
        return newImg;
    }
}
