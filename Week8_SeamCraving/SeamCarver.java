/* *****************************************************************************
 *  Name: Chu-Cheng Fu
 *  Date: 31-10-2020
 *  Description: SeamCarver Implementation
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture processPic;
    private boolean flag = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.processPic = new Picture(picture); // defensive copy
    }

    // current picture
    public Picture picture() {
        Picture currPic = new Picture(processPic); // defensive copy
        return currPic;
    }


    // width of current picture
    public int width() {
        return processPic.width();
    }

    // height of current picture
    public int height() {
        return processPic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x >= processPic.width() || x < 0 || y >= processPic.height() || y < 0)
            throw new IllegalArgumentException(x + " " + y);
        // pixels on the border
        if (x == 0 || y == 0 || x == processPic.width() - 1 || y == processPic.height() - 1)
            return 1000.0;
            // pixels within the border
        else
            return Math.sqrt(grad(x, y));

    }

    private int[] colsExtract(int x, int y) {
        int[] rgb = new int[3];
        int RGB = processPic.getRGB(x, y);
        // shift the 32-bit rgb color and then AND with 8-bit 0XFF
        rgb[0] = (RGB >> 16) & 0XFF;
        rgb[1] = (RGB >> 8) & 0XFF;
        rgb[2] = RGB & 0XFF;
        return rgb;
    }

    // private method for complex calculation
    private double grad(int x, int y) {
        double energySum = 0.0;
        // cache to avoid extract call of getRGB
        int[] colsTop = colsExtract(x, y - 1);
        int[] colsDown = colsExtract(x, y + 1);
        int[] colsLeft = colsExtract(x - 1, y);
        int[] colsRight = colsExtract(x + 1, y);
        // loop through and sum up R, G, B
        for (int i = 0; i < 3; i++) {
            energySum += Math.pow(colsTop[i] - colsDown[i], 2) + Math
                    .pow(colsLeft[i] - colsRight[i], 2);
        }
        return energySum;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seamArr;
        if (flag) {
            seamArr = findVerticalSeam();
        }
        else {
            transpose();
            seamArr = findVerticalSeam();
            flag = true;
        }


        return seamArr;
    }

    // private method for transposing pic

    private void transpose() {
        int w = processPic.height();
        int h = processPic.width();
        Picture transPic = new Picture(w, h);
        // loop through to implant pixels
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                transPic.setRGB(i, j, processPic.getRGB(j, i));
            }
        }
        processPic = transPic;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        // first create the energy array
        int w = processPic.width();
        int h = processPic.height();
        double[] energyArr = new double[h * w];
        int[] seamArr = new int[h];
        // tracker for min SP in the last second row
        int minIdx = 0;
        double min = Double.MAX_VALUE;

        // local var for the energy +
        // Dynamic programming for the shortest path
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                // first row
                if (i == 0)
                    energyArr[i * w + j] = energy(j, i);

                    // rest rows
                else {
                    // on the left border
                    if (j == 0) {
                        energyArr[i * w + j] =
                                Math.min(energyArr[(i - 1) * w + j], energyArr[(i - 1) * w + j + 1])
                                        + energy(j, i);
                        if (i == h - 2) {
                            min = energyArr[i * w + j];
                            minIdx = j;
                        }
                    }
                    // on the right border
                    else if (j == w - 1) {
                        energyArr[i * w + j] =
                                Math.min(energyArr[(i - 1) * w + j - 1], energyArr[(i - 1) * w + j])
                                        + energy(j, i);
                        if (i == h - 2 && energyArr[i * w + j] <= min) {
                            min = energyArr[i * w + j];
                            minIdx = j;
                        }
                    }
                    // within
                    else {
                        energyArr[i * w + j] = Math.min(
                                Math.min(energyArr[(i - 1) * w + j - 1], energyArr[(i - 1) * w + j])
                                , energyArr[(i - 1) * w + j + 1]) + energy(j, i);
                        if (i == h - 2 && energyArr[i * w + j] <= min) {
                            min = energyArr[i * w + j];
                            minIdx = j;
                        }
                    }
                }

            }
        }
        // record the last and last second row min
        seamArr[h - 1] = minIdx;
        if (h > 1)
            seamArr[h - 2] = minIdx;

        // Now walk along back from the shortest path
        // to collect the entries
        for (int i = h - 2; i >= 0; --i) {
            //  the bottom entry on the left border
            if (seamArr[i + 1] == 0) {
                if (energyArr[i * w] <= energyArr[i * w + 1])
                    seamArr[i] = 0;
                else seamArr[i] = 1;
            }
            // the bottom entry on the right border
            else if (seamArr[i + 1] == w - 1) {
                if (energyArr[i * w + w - 1] <= energyArr[i * w + w - 2])
                    seamArr[i] = w - 1;
                else seamArr[i] = w - 2;
            }
            // within: compare three energy above
            else {
                double minThree = Double.MAX_VALUE;
                int minthreeIdx = -1;
                for (int j = -1; j <= 1; j++) {
                    if (energyArr[i * w + seamArr[i + 1] + j] <= minThree) {
                        minThree = energyArr[i * w + seamArr[i + 1] + j];
                        minthreeIdx = seamArr[i + 1] + j;
                    }

                }
                // assign the index afterward
                seamArr[i] = minthreeIdx;
            }
        }

        return seamArr;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        int h = height();
        if (seam == null) throw new IllegalArgumentException();
        // wrong length
        if (seam.length != width() || processPic.height() <= 1)
            throw new IllegalArgumentException();

        for (int i = 0; i < seam.length; i++) {
            // out of bound
            if (seam[i] >= h || seam[i] < 0) throw new IllegalArgumentException();

            // neighbor diff larger than 1
            if (i == seam.length - 1) break; // avoid null pointer
            else {
                if (Math.abs(seam[i + 1] - seam[i]) > 1) throw new IllegalArgumentException();
            }
        }

        if (flag)
            removeVerticalSeam(seam);
        else {
            transpose();
            removeVerticalSeam(seam);
        }
        transpose();
        flag = false;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        int h = height();
        int w = width();
        // wrong length
        // 1 pixel pic
        // null arr
        if (seam.length != h || processPic.width() <= 1) throw new IllegalArgumentException();

        for (int i = 0; i < seam.length; i++) {
            // out of bound
            if (seam[i] >= w || seam[i] < 0) throw new IllegalArgumentException();

            // neighbor diff larger than 1
            if (i == seam.length - 1) break; // avoid null pointer
            else {
                if (Math.abs(seam[i + 1] - seam[i]) > 1) throw new IllegalArgumentException();
            }
        }

        // copy the picture first
        Picture copy = picture();
        // overwrite current pic with width = w -1
        processPic = new Picture(copy.width() - 1, copy.height());
        w = width(); // update the width
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                // pass the deleted pixel
                if (j >= seam[i])
                    processPic.setRGB(j, i, copy.getRGB(j + 1, i));

                    // fill in pixels
                else
                    processPic.setRGB(j, i, copy.getRGB(j, i));
            }

        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }
}
