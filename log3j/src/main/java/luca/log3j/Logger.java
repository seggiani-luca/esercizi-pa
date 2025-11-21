package luca.log3j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

public class Logger {

  private class Messaggio {

    private final String testo;
    private final String chi;
    private final int ordine;

    public Messaggio(String testo, String chi, int ordine) {
      this.testo = testo;
      this.chi = chi;
      this.ordine = ordine;
    }

    public void scrivi() throws IOException {
      File file = new File(fileName);

      try (FileWriter writer = new FileWriter(file, true)) {
        writer.write(this + "\n");
      }
    }

    @Override
    public String toString() {
      return ordine + "\t" + chi + "\t" + testo;
    }
  }

  private final String fileName;
  private final Messaggio[] buf;
  private int base = 0;

  public Logger(String fileName, int num) throws IOException {
    this.fileName = fileName;
    buf = new Messaggio[num];

    File file = new File(fileName);

    try (FileWriter writer = new FileWriter(file, true)) {
      writer.write("\n--- " + Instant.now().toString() + " ---\n");
    }
  }

  public synchronized void log(String testo, int ordine) throws IOException, InterruptedException {
    if (ordine < base) {
      throw new RuntimeException("Rilevato viaggio nel tempo");
    }

    Messaggio mess = new Messaggio(testo, Thread.currentThread().getName(), ordine);

    System.out.println("Richiesto di loggare (base: " + base + "):\t" + mess);

    while (ordine >= base + buf.length) {
      // fuori ordine non bufferizzabile
      System.out.println("Impossibile bufferizzare (base: " + base + "):\t" + mess);
      wait();
    }

    if (ordine == base) {
      // in ordine
      mess.scrivi();
      System.out.println("Messaggio loggato subito:\t" + mess);

      base++;

      int baseIdx = base % buf.length;
      while (buf[baseIdx] != null) {
        buf[baseIdx].scrivi();
        System.out.println("Avanzato messaggio:\t" + buf[baseIdx]);

        buf[baseIdx] = null;
        base++;
        baseIdx = base % buf.length;
      }

      notifyAll();

    } else {
      // fuori ordine bufferizzabile
      int ordineIdx = ordine % buf.length;

      if (buf[ordineIdx] != null) {
        throw new RuntimeException("Rilevati numeri d'ordine duplicati");
      }

      buf[ordineIdx] = mess;
      System.out.println("Bufferizzato messaggio:\t" + buf[ordineIdx]);
    }
  }
}
