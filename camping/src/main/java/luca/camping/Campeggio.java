package luca.camping;

class Prenotazione {

  int posti;

  public int getPosti() {
    return posti;
  }

  public Prenotazione(int posti) {
    this.posti = posti;
  }
}

public class Campeggio {

  private static final int MAX_PRENOTAZIONI = 100;
  private final Prenotazione[] prenotazioni = new Prenotazione[MAX_PRENOTAZIONI];

  private int numPosti;
  private int numOccupati;

  private int numLiberi() {
    return numPosti - numOccupati;
  }

  public Campeggio(int numPosti) {
    this.numPosti = numPosti;
    this.numOccupati = 0;
  }

  public Campeggio() {
    this(200);
  }

  public int getOccupati() {
    return numOccupati;
  }

  public synchronized int prenota(int q, long t) throws PrenotazioneFallitaException {
    long elapsed = 0;

    while (q > numLiberi()) {
      try {
        long beg = System.currentTimeMillis();
        wait(t - elapsed);
        elapsed += System.currentTimeMillis() - beg;

        if (t - elapsed <= 0) {
          // sei in timeout
          throw new PrenotazioneFallitaException();
        }
      } catch (InterruptedException e) {
        System.err.println("prenota() è stata interrotta");
        System.exit(1);
      }
    }

    // crea la prenotazione
    int i = 0;
    while (i < MAX_PRENOTAZIONI) {
      if (prenotazioni[i] == null) {
        prenotazioni[i] = new Prenotazione(q);
        break;
      }

      i++;
    }

    if (i >= MAX_PRENOTAZIONI) {
      // prenotazioni esaurite
      throw new RuntimeException("Non c'è più spazio per prenotazioni");
    }

    // aggiorna stato
    numOccupati += q;

    System.out.println("Prenotazione " + i + " di " + q + " posti fatta");

    return i;
  }

  public synchronized void disdici(int p) {
    Prenotazione pren = prenotazioni[p];

    if (pren == null) {
      throw new RuntimeException("Prenotazione inesistente");
    }

    // aggiorna stato con prenotazione
    numOccupati -= pren.getPosti();
    prenotazioni[p] = null;

    System.out.println("Prenotazione " + p + " di " + pren.getPosti() + " posti disdetta");

    notifyAll();
  }
}
