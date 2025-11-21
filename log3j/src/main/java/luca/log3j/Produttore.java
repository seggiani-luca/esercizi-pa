package luca.log3j;

import java.io.IOException;

public class Produttore extends Thread {

  public static final String[] messaggi = {
    "initialized configuration",
    "loaded environment variables",
    "connecting to database",
    "connected to database",
    "starting HTTP server",
    "HTTP server listening on port 8080",
    "received client request",
    "validated request headers",
    "queried user table",
    "sent response to client",
    "closed database connection",
    "shutting down service"
  };

  private final Logger logger;

  private final int start; // inclusive
  private final int end; // exclusive

  public Produttore(Logger logger, int start, int end) {
    this.logger = logger;

    this.start = start;
    this.end = end;
  }

  private String messCasuale() {
    int idx = (int) (Math.random() * messaggi.length);
    return messaggi[idx];
  }

  private int[] ordineCasuale(int start, int end) {
    int[] base = new int[end - start];
    for (int i = 0; i < base.length; i++) {
      base[i] = i + start;
    }

    for (int i = 0; i < base.length; i++) {
      int j = (int) (Math.random() * base.length);

      int temp = base[i];
      base[i] = base[j];
      base[j] = temp;
    }

    return base;
  }

  @Override
  public void run() {
    for (int i : ordineCasuale(start, end)) {
      try {
        logger.log(messCasuale(), i);
      } catch (IOException | InterruptedException e) {
        System.err.println("Log fallito. " + e.getMessage());
      }
    }
  }
}
