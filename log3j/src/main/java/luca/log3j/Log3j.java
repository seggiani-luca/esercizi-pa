package luca.log3j;

import java.io.IOException;

public class Log3j {

  private static final String LOG_FILE = "logs.txt";

  public static void main(String[] args) {
    Logger logger = null;
    try {
      logger = new Logger(LOG_FILE, 10);
    } catch (IOException e) {
      System.err.println("Impossibile inizializzare logger");
      System.exit(1);
    }

    for (int i = 0; i < 5; i++) {
      (new Produttore(logger, i * 10, (i + 1) * 10)).start();
    }
  }
}
