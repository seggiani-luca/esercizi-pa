package luca.gimp;

import java.io.StringReader;
import java.util.Scanner;

public class Image {

  public static class Channel {

    public static final int Red = 0;
    public static final int Green = 1;
    public static final int Blue = 2;

    public static final int[] values = {0, 1, 2};
  }

  private final int[][][] image = new int[3][][];

  public int[][] getChannel(int which) {
    return image[which];
  }

  private final int rows;

  public int getRows() {
    return rows;
  }

  private final int cols;

  public int getCols() {
    return cols;
  }

  public Image(String f) {
    Scanner scanner = new Scanner(new StringReader(f));
    rows = scanner.nextInt();
    cols = scanner.nextInt();

    for (int chan : Channel.values) {
      image[chan] = new int[rows][cols];
      for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
          image[chan][r][c] = scanner.nextInt();
        }
      }
    }
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(rows);

    builder.append(" ");

    builder.append(cols);

    builder.append("\n");

    for (int chan : Channel.values) {
      for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
          builder.append(image[chan][r][c]);

          builder.append(" ");
        }

        builder.append("\n");
      }

      builder.append("\n");
    }

    return builder.toString();
  }
}
