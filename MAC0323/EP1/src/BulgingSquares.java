/******************************************************************************
 *  Compilation:  javac BulgingSquares.java
 *  Execution:    java BulgingSquares
 *  Dependencies: StdDraw.java, java.awt.Color
 *
 *  Program draws an optical illusion from Akiyoshi Kitaoka. The center appears
 *  to bulge outwards even though all squares are the same size.
 *
 *  meu_prompt > java BulgingSquares
 *
 *  Exercise 14 http://introcs.cs.princeton.edu/java/15inout/
 *
 *  THIS CODE IS BAD PRACTICE!! DON'T PROGRAM IN JAVA LIKE THIS!
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw; // StdDraw.setXscale, StdDraw.setYscale, ...

public class BulgingSquares {
    // constantes... vixe! use se desejar
    private static final double XMIN = -75;
    private static final double XMAX = 75;
    private static final double YMIN = -75;
    private static final double YMAX = 75;
    private static final double MINI_SIZE = 25.0 / 18;

    // Create the "chess" board (the background)
    private static double[][][] chessboard() {
        double[][][] squares = new double[15][15][2];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                StdDraw.setPenColor(((i + j) % 2) * 255, ((i + j) % 2) * 255, ((i + j) % 2) * 255);
                squares[i][j][0] = (-70.0) + i * 10;
                squares[i][j][1] = (-70.0) + j * 10;
                StdDraw.filledSquare((-70) + i * 10, (-70) + j * 10, 5);
            }
        }
        return squares;
    }

    // Receives the center of a bigger square, then
    // creates the other squares and reflect it, changing the
    // colors accordingly
    private static void drawSquare(double[] bigCenter, int[] pos) {
        double offset = 35.0 / 18;
        double a = bigCenter[0];
        double b = bigCenter[1];
        double x0 = a + offset;
        double y0 = b + offset;

        // TERNARY FOR THE WIN!!

        if (pos[0] % 2 == 0)
            StdDraw.setPenColor(pos[1] % 2 == 0 ? StdDraw.WHITE : StdDraw.BLACK);
        else
            StdDraw.setPenColor(pos[1] % 2 == 0 ? StdDraw.BLACK : StdDraw.WHITE);

        if ((a < 0 && b < 0) || (a > 0 && b > 0)) {
            StdDraw.filledSquare(x0 - 5, b - offset + 5, MINI_SIZE);
            StdDraw.filledSquare(a - offset + 5, y0 - 5, MINI_SIZE);
        }
        else {
            StdDraw.filledSquare(x0 - 5, y0 - 5, MINI_SIZE);
            StdDraw.filledSquare(a - offset + 5, b - offset + 5, MINI_SIZE);
        }
    }

    // Draw the circle of mini squares in each one
    private static void circleSquares(double[][][] squares) {
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++) {
                if (squares[i][j][0] * squares[i][j][0] + squares[i][j][1] * squares[i][j][1] <= 60 * 60)
                    if (squares[i][j][0] == 0 || squares[i][j][1] == 0)
                        drawSquareAxis(squares[i][j], new int[]{i, j});
                    else
                        drawSquare(squares[i][j], new int[]{i, j});
            }
        // Individual Squares... D:
        drawSquare(squares[6][1], new int[]{6, 1});
        drawSquare(squares[1][6], new int[]{1, 6});
        drawSquare(squares[1][8], new int[]{1, 8});
        drawSquare(squares[8][1], new int[]{8, 1});
        drawSquare(squares[13][6], new int[]{13, 6});
        drawSquare(squares[6][13], new int[]{6, 13});
        drawSquare(squares[8][13], new int[]{8, 13});
        drawSquare(squares[13][8], new int[]{13, 8});
    }

    // Draw the mini squares in the X and Y axis
    private static void drawSquareAxis(double[] bigCenter, int[] pos) {
        double offset = 35.0 / 18;
        double a = bigCenter[0];
        double b = bigCenter[1];
        double x0 = a + offset;
        double y0 = b + offset;
        if (pos[0] % 2 == 0)
            StdDraw.setPenColor(pos[1] % 2 == 0 ? StdDraw.WHITE : StdDraw.BLACK);
        else
            StdDraw.setPenColor(pos[1] % 2 == 0 ? StdDraw.BLACK : StdDraw.WHITE);
        if (a == 0) {
            if (b > 0) {
                StdDraw.filledSquare(x0 - 5, y0 - 5, MINI_SIZE);
                StdDraw.filledSquare(a - offset + 5, y0 - 5, MINI_SIZE);
            }
            else if (b < 0) {
                StdDraw.filledSquare(x0 - 5, b - offset + 5, MINI_SIZE);
                StdDraw.filledSquare(a - offset + 5, b - offset + 5, MINI_SIZE);
            }
        }
        else {
            if (a > 0) {
                StdDraw.filledSquare(x0 - 5, b - offset + 5, MINI_SIZE);
                StdDraw.filledSquare(x0 - 5, y0 - 5, MINI_SIZE);

            }
            else if (a < 0) {
                StdDraw.filledSquare(a - offset + 5, y0 - 5, MINI_SIZE);
                StdDraw.filledSquare(a - offset + 5, b - offset + 5, MINI_SIZE);
            }
        }
    }


    public static void main(String[] args) {
        StdDraw.setXscale(XMIN, XMAX);
        StdDraw.setYscale(YMIN, YMAX);
        StdDraw.enableDoubleBuffering();
        // clear the background

        StdDraw.clear(StdDraw.WHITE);

        // Draw the board
        double[][][] squares = chessboard();
        circleSquares(squares);
        // copy offscreen buffer to onscreen
        StdDraw.show();

    }

}