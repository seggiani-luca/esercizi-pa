package luca.proc;

import java.io.IOException;
import java.util.Arrays;

public class Proc {

  private static final String FILE_NAME = "bytes.txt";
  private static final int BLOCK_SIZE = 2;

  public static void main(String[] args) {
    MaxFileProcessor max = null;

    System.out.println("Creo FileProcessor per " + FILE_NAME);
    try {
      max = new MaxFileProcessor(FILE_NAME);
    } catch (IOException e) {
      System.err.println("Impossibile creare FileProcessor per " + FILE_NAME + ". " + e.getMessage());
      System.exit(1);
    }

    System.out.println("Inizio elaborazione su " + FILE_NAME);
    byte[] res = null;
    try {
      res = max.process(BLOCK_SIZE);
    } catch (InterruptedException e) {
      System.err.println("Impossibile elaborare file " + FILE_NAME + ". " + e.getMessage());
      System.exit(1);
    }

    System.out.print("Ottenuto risultato: [");
    for (int i = 0; i < res.length; i++) {
      char c = (char) res[i];
      System.out.print(c);

      if (i != res.length - 1) {
        System.out.print(", ");
      }
    }
    System.out.println("]");
  }
}
