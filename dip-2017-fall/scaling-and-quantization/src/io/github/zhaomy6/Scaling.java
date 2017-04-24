package io.github.zhaomy6;

public class Scaling {
    //  Return (x', y'), the relative position of new matrix
    public static double[] getRelativePosition(int x, int y, int newHeight, int newWidth,
                                               int originHeight, int originWidth) {
        double oriX = x * ((double) originHeight / (double) newHeight);
        double oriY = y * ((double) originWidth / (double) newWidth);
        return new double[]{oriX, oriY};
    }

    //  Return gray levels in the new image matrix
    public static byte[] biLinearInterpolation(int[][] oriPixels, int newHeight, int newWidth) {
        int oriHeight = oriPixels.length;
        int oriWidth = 0;
        if (oriHeight > 0) {
            oriWidth = oriPixels[0].length;
        }
        byte[] newLinearPixels = new byte[newHeight * newWidth];
        int result;
        for (int i = 0; i < newHeight; ++i) {
            for (int j = 0; j < newWidth; ++j) {
                double[] tmpPos = Scaling.getRelativePosition(i, j, newHeight, newWidth,
                        oriHeight, oriWidth);

                int x = (int) tmpPos[0];   //  int part of tmpPos.x
                int y = (int) tmpPos[1];   //  int part of tmpPos.y
                double u = tmpPos[0] - x;  //  decimal part of tmpPos.x
                double v = tmpPos[1] - y;  //  decimal part of tmpPos.y

                //  Border judging: 3 situations
                if (x == oriHeight - 1) {
                    if (y == oriWidth - 1) {        //  (height - 1, width - 1)
                        result = oriPixels[x][y];
                    } else {                        //  row height - 1
                        result = (int) ((1 - v) * oriPixels[x][y] +
                                v * oriPixels[x][y + 1]);
                    }
                } else if (y  == oriWidth - 1) {    //  column width - 1
                    result = (int) ((1 - u) * oriPixels[x][y] +
                            u * oriPixels[x + 1][y]);
                } else {                            //  not on the border
                    result = (int) ((1 - u) * (1 - v) * oriPixels[x][y] +
                            (1 - u) * v * oriPixels[x][y + 1] +
                            u * (1 - v) * oriPixels[x + 1][y] +
                            u * v * oriPixels[x + 1][y + 1]);
                }
                newLinearPixels[i * newWidth + j] = (byte) result;
            }
        }
        return newLinearPixels;
    }
}
