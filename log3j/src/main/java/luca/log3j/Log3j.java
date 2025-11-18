package luca.log3j;

import java.io.IOException;
import java.util.Scanner;

public class Log3j {

  private static final String LOG_FILE = "logs.txt";

  public static void main(String[] args) {
    Logger logger = new Logger(LOG_FILE, 10);

    Scanner scan = new Scanner(System.in);
    while (true) {
      int ordine = scan.nextInt();
      String testo = scan.next();

      try {
        logger.log(testo, ordine);
      } catch (IOException | InterruptedException e) {
        System.err.println("Log fallito. " + e.getMessage());
      }
    }
  }
}
