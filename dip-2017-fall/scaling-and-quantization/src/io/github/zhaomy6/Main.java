package io.github.zhaomy6;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String [] args) throws IOException {
        //  Read Image
        String imgSrc = "./res/82.png";
        File file = new File(imgSrc);
        BufferedImage image = ImageIO.read(file);
        int imgHeight = image.getHeight();  //  256
        int imgWidth = image.getWidth();    //  384

        //  Get array of pixels
        byte[] arr = new byte[imgWidth * imgHeight];
        image.getRaster().getDataElements(0, 0, imgWidth, imgHeight, arr);
        int[][] oriPixels = new int[imgHeight][imgWidth];
        int counter = 0;
        for (byte ele : arr) {
            int tmpInt = 0xff & ele;
            oriPixels[counter / imgWidth][counter % imgWidth] = tmpInt;
            ++counter;
        }

        System.out.println("Please choose your testing exercise(Scaling/Quantization):");
        Scanner in = new Scanner(System.in);
        String mode = in.next();
        if ("Scaling".equals(mode)) {
            //   Test 1 Main loop
            //  Input from standard input: size of the target image
            while (true) {
                Scanner s = new Scanner(System.in);
                System.out.print("(width, height) >> ");
                int targetWidth = s.nextInt();
                int targetHeight = s.nextInt();
                if (targetWidth * targetHeight == 0) break;
                byte[] result = Scaling.biLinearInterpolation(oriPixels, targetHeight, targetWidth);

                //  Create a new image, type TYPE_BYTE_GRAY
                BufferedImage newImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_BYTE_GRAY);
                newImage.getRaster().setDataElements(0, 0, targetWidth, targetHeight, result);
                //  Write Image to a file
                File outputFile = new File("./res/" + targetWidth + "_" + targetHeight + "_88.png");
                ImageIO.write(newImage, "png", outputFile);
                System.out.println(">> Image " + targetWidth + " * " + targetHeight + " Output Complete");
            }
        } else if ("Quantization".equals(mode)) {
            //  Test 2 Main Loop
            //  Quantization
            while (true) {
                Scanner s = new Scanner(System.in);
                System.out.print("(grey level[2-255]) >> ");
                int level = s.nextInt();
                if (level == 0) break;
                BufferedImage outputImg = Quantization.quantize(image, level);
                File outputFile = new File("./res/gray_level_" + level + "_88.png");
                ImageIO.write(outputImg, "png", outputFile);
                System.out.println(">> Image gray level " + level + " Output Complete");
            }
        } else {
            System.out.println(mode + " do not exist, please check you input");
        }
        System.out.println("> Program Terminated <");
    }
}
