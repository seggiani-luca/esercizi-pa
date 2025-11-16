package luca.moby;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Traghetto {

  public static final long TIMEOUT_SELEZIONE = 5000;

  public static enum Posto {
    Libero,
    Selezionato,
    Prenotato;

    @Override
    public String toString() {
      return switch (this) {
        case Libero ->
          "L";
        case Selezionato ->
          "S";
        case Prenotato ->
          "P";
        default ->
          "?";
      };
    }
  }

  public static class Selezione {

    private final long scadenza;

    public long getScadenza() {
      return scadenza;
    }

    private final int piano;

    public int getPiano() {
      return piano;
    }

    private final int posto;

    public int getPosto() {
      return posto;
    }

    public Selezione(long scadenza, int piano, int posto) {
      this.scadenza = scadenza;
      this.piano = piano;
      this.posto = posto;
    }
  }

  private PriorityQueue<Selezione> selezioni
          // comparatore per scadenza in ordine crescente
          = new PriorityQueue<>(Comparator.comparing(Selezione::getScadenza));

  public class ControlloreTraghetto extends Thread {

    public static final long CHECK_PERIOD = 1000;

    @Override
    public void run() {
      try {
        while (true) {
          Selezione testa = selezioni.peek();
          while (testa != null && testa.getScadenza() < System.currentTimeMillis()) {
            deseleziona(testa.piano, testa.posto);

            selezioni.remove();
            testa = selezioni.peek();
          }

          Thread.sleep(CHECK_PERIOD);
        }
      } catch (InterruptedException e) {
        throw new RuntimeException("Controllore traghetto è stato interrotto");
      }
    }
  }

  public static final int FILE = 6;
  public static final int POSTI = 10;

  private Posto[][] piani;

  public int getPiani() {
    return piani.length;
  }

  public int getPosti() {
    return FILE * POSTI;
  }

  public Traghetto(int numPiani) {
    piani = new Posto[numPiani][FILE * POSTI];

    for (Posto[] piano : piani) {
      for (int j = 0; j < FILE * POSTI; j++) {
        piano[j] = Posto.Libero;
      }
    }

    (new ControlloreTraghetto()).start();
  }

  private void validaPiano(int n) {
    if (n < 0 || n >= piani.length) {
      throw new ArrayIndexOutOfBoundsException("Piano traghetto inesistente");
    }
  }

  private void validaPosto(int n, int p) {
    validaPiano(n);

    if (p < 0 || p >= FILE * POSTI) {
      throw new ArrayIndexOutOfBoundsException("Posto traghetto inesistente");
    }
  }

  public synchronized void visualizza(int n) {
    validaPiano(n);

    Posto[] piano = piani[n];

    for (int p = 0; p < POSTI; p++) {
      for (int f = 0; f < FILE; f++) {
        System.out.print(piano[f + p * FILE]);
      }
      System.out.println();
    }
  }

  public synchronized boolean seleziona(int n, int p) {
    validaPosto(n, p);

    if (piani[n][p] == Posto.Libero) {
      piani[n][p] = Posto.Selezionato;

      long scadenza = System.currentTimeMillis() + TIMEOUT_SELEZIONE;
      Selezione sel = new Selezione(scadenza, n, p);
      selezioni.add(sel);

      return true;
    }

    return false;
  }

  private synchronized void deseleziona(int n, int p) {
    if (piani[n][p] == Posto.Selezionato) {
      piani[n][p] = Posto.Libero;
    }
  }

  public synchronized boolean paga(int n, int p) {
    validaPosto(n, p);

    if (piani[n][p] == Posto.Selezionato) {
      piani[n][p] = Posto.Prenotato;
      return true;
    }

    return false;
  }

  public String posto2Stringa(int n, int p) {
    validaPosto(n, p);
    return "{piano: " + n + ", posto: " + p + ", stato: " + piani[n][p] + "}";
  }
}
