/* *****************************************************************************
 *  Name: Chu-Cheng Fu
 *  Date: 02-11-2020
 *  Description: SeamCarver Implementation
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture processPic;

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


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        // first create the energy array
        int w = processPic.width();
        int h = processPic.height();
        double[] energyArr = new double[w * h];
        int[] seamArr = new int[w];
        // tracker for min SP in the last second row
        int minIdx = 0;
        double min = Double.MAX_VALUE;

        // local var for the energy +
        // Dynamic programming for the shortest path
        for (int j = 0; j < w; j++) {
            for (int i = 0; i < h; i++) {
                // first column
                if (j == 0) energyArr[j * h + i] = energy(j, i);

                    // rest column
                else {
                    // on the top border
                    if (i == 0) {
                        energyArr[j * h + i] =
                                Math.min(energyArr[(j - 1) * h + i], energyArr[(j - 1) * h + i + 1])
                                        + energy(j, i);
                        // record the last second column for later comparison
                        if (j == w - 2) {
                            min = energyArr[j * h + i];
                            minIdx = i;
                        }
                    }
                    // on the bottom border
                    else if (i == h - 1) {
                        energyArr[j * h + i] =
                                Math.min(energyArr[(j - 1) * h + i - 1], energyArr[(j - 1) * h + i])
                                        + energy(j, i);
                        // record the last second column for later comparison
                        if (j == w - 2 && energyArr[j * h + i] <= min) {
                            min = energyArr[j * h + i];
                            minIdx = i;
                        }
                    }
                    // within
                    else {
                        energyArr[j * h + i] = Math.min(
                                Math.min(energyArr[(j - 1) * h + i - 1], energyArr[(j - 1) * h + i])
                                , energyArr[(j - 1) * h + i + 1]) + energy(j, i);
                        // record the last second column for later comparison
                        if (j == w - 2 && energyArr[j * h + i] <= min) {
                            min = energyArr[j * h + i];
                            minIdx = i;
                        }
                    }
                }

            }
        }
        // record the last and last second column min
        seamArr[w - 1] = minIdx;
        // special case if there is only one column
        if (w > 1)
            seamArr[w - 2] = minIdx;

        // Now walk along back from the shortest path
        // to collect the entries
        for (int j = w - 2; j >= 0; --j) {
            //  the bottom entry on the left border
            if (seamArr[j + 1] == 0) {
                if (energyArr[j * h] <= energyArr[j * h + 1])
                    seamArr[j] = 0;
                else seamArr[j] = 1;
            }
            // the bottom entry on the right border
            else if (seamArr[j + 1] == h - 1) {
                if (energyArr[j * h + h - 1] <= energyArr[j * h + h - 2])
                    seamArr[j] = h - 1;
                else seamArr[j] = h - 2;
            }
            // within: compare three energy above
            else {
                double minThree = Double.MAX_VALUE;
                int minthreeIdx = -1;
                for (int i = -1; i <= 1; i++) {
                    if (energyArr[j * h + seamArr[j + 1] + i] <= minThree) {
                        minThree = energyArr[j * h + seamArr[j + 1] + i];
                        minthreeIdx = seamArr[j + 1] + i;
                    }

                }
                // assign the index afterward
                seamArr[j] = minthreeIdx;
            }
        }

        return seamArr;
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
        int w = width();
        if (seam == null) throw new IllegalArgumentException();
        // wrong length
        if (seam.length != w || h <= 1)
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

        // copy the picture first
        Picture copy = picture();
        // overwrite current pic with width = w -1
        processPic = new Picture(copy.width(), copy.height() - 1);
        h = height(); // update the height
        for (int j = 0; j < w; j++) {
            for (int i = 0; i < h; i++) {
                // pass the deleted pixel
                if (i >= seam[j])
                    processPic.setRGB(j, i, copy.getRGB(j, i + 1));

                    // fill in pixels
                else
                    processPic.setRGB(j, i, copy.getRGB(j, i));
            }

        }

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

    // Color RGB extraction function
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

    /*
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
    */
    //  unit testing (optional)
    public static void main(String[] args) {

    }
}
