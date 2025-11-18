package luca.log3j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

  private class Messaggio {

    private final String testo;
    private final int ordine;

    public Messaggio(String testo, int ordine) {
      this.testo = testo;
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
      return ordine + "\t" + testo;
    }
  }

  private final String fileName;
  private final Messaggio[] buf;
  private int base = 0;

  public Logger(String fileName, int num) {
    this.fileName = fileName;
    buf = new Messaggio[num];
  }

  public synchronized void log(String testo, int ordine) throws IOException, InterruptedException {
    if (ordine < base) {
      throw new RuntimeException("Rilevato viaggio nel tempo");
    }

    Messaggio mess = new Messaggio(testo, ordine);

    System.out.println("Richiesto di loggare messaggio:\t" + mess);

    if (ordine == base) {
      // in ordine
      mess.scrivi();
      System.out.println("Messaggio loggato subito");

      base = (base + 1) % buf.length;

      while (buf[base] != null) {
        buf[base].scrivi();
        System.out.println("Avanzato messaggio:\t" + buf[base]);

        base = (base + 1) % buf.length;
      }

      notifyAll();

    } else {
      // fuori ordine

      while (ordine >= base + buf.length) {
        // non bufferizzabile
        System.out.println("Impossibile bufferizzare messaggio:\t" + mess);
        wait();
      }

      // bufferizzabile
      int idx = ordine % buf.length;

      if (buf[idx] != null && buf[idx].ordine == ordine) {
        throw new RuntimeException("Rilevati numeri d'ordine duplicati");
      }

      buf[idx] = mess;
      System.out.println("Bufferizzato messaggio:\t" + buf[idx]);
    }
  }
}
