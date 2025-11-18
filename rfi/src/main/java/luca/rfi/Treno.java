package luca.rfi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Treno {

  private static class Prenotazione {

    private static int counter = 0;

    private final int id;
    private final int[] prenotati;

    public Prenotazione(int[] prenotati) {
      id = counter++;
      this.prenotati = prenotati;
    }

    public void print() {
      System.out.println("\t\tCodice prenotazione: " + id);
      System.out.println("\t\tPosti prenotati: " + Arrays.toString(prenotati));
    }
  }

  public static final int DEFAULT_POSTI = 100;

  private int numPosti;
  private boolean[] posti;
  private List<Prenotazione> prenotazioni = new ArrayList<>();

  public Treno() {
    this(DEFAULT_POSTI);
  }

  public Treno(int numPosti) {
    this.numPosti = numPosti;
    this.posti = new boolean[numPosti];
  }

  private int[] ottieniLiberi(int quanti) {
    int[] liberi = new int[quanti];
    int j = 0;

    for (int i = 0; i < numPosti; i++) {
      if (!posti[i]) {
        liberi[j++] = i;

        if (j == quanti) {
          return liberi;
        }
      }
    }

    return null;
  }

  private Prenotazione ottieniPrenotazione(int quale) {
    for (Prenotazione pren : prenotazioni) {
      if (pren.id == quale) {
        return pren;
      }
    }

    return null;
  }

  public synchronized int prenota(int quanti, int timeout) throws InterruptedException, PrenotazioneFallitaException {
    System.out.println("[CLIENTE]\t" + Thread.currentThread().getName() + " vuole prenotare " + quanti + " posti in " + timeout + " millisecondi");

    int[] liberi = ottieniLiberi(quanti);

    while (liberi == null) {
      long begTime = System.currentTimeMillis();

      wait(timeout);

      long endTime = System.currentTimeMillis();
      long deltaTime = endTime - begTime;
      timeout -= deltaTime;

      if (timeout <= 0) {
        System.out.println("[CLIENTE]\t" + Thread.currentThread().getName() + " non riesce a prenotare i " + quanti + " posti");
        throw new PrenotazioneFallitaException();
      }

      liberi = ottieniLiberi(quanti);
    }

    System.out.println("[CLIENTE]\t" + Thread.currentThread().getName() + " riesce a prenotare i " + quanti + " posti");

    for (int libero : liberi) {
      posti[libero] = true;
    }

    Prenotazione pren = new Prenotazione(liberi);
    prenotazioni.add(pren);

    pren.print();

    return pren.id;
  }

  public synchronized void disdici(int quale) {
    Prenotazione pren = ottieniPrenotazione(quale);
    if (pren == null) {
      throw new RuntimeException("Prenotazione richiesta inesistente");
    }

    System.out.println("[CLIENTE]\t" + Thread.currentThread().getName() + " disdice la prenotazione " + quale);
    pren.print();

    for (int libero : pren.prenotati) {
      posti[libero] = false;
    }

    notifyAll();
  }
}
