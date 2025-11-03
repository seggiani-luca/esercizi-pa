package luca.memoria;

class Blocco {

  Blocco next;

  public Blocco(Blocco next) {
    this.next = next;
  }

}

public class GestioneMemoria {

  private Blocco liberi;
  private int dim_blocco;
  private int prioAttesa = 0;

  public GestioneMemoria(int m, int n) {
    // m byte, n dim. blocchi
    if (m % n != 0) {
      throw new IllegalArgumentException("Numero di byte non divisibile per dimensione blocco");
    }

    dim_blocco = n;
    int num_blocchi = m / dim_blocco;

    liberi = null;

    for (int i = 0; i < num_blocchi; i++) {
      Blocco blocco = new Blocco(liberi);
      liberi = blocco;
    }
  }

  public synchronized Blocco acquisisci(boolean prio) {
    if (prio) {
      prioAttesa++;
    }

    while (liberi == null || (!prio && prioAttesa > 0)) {
      try {
        wait();
      } catch (InterruptedException e) {
        System.err.println("acquisisci() interrotto");
        System.exit(1);
      }
    }

    if (prio) {
      prioAttesa--;
    }

    System.out.println("Acquisito un blocco" + (prio ? " prioritario" : ""));
    Blocco temp = liberi;
    liberi = liberi.next;
    return temp;
  }

  public synchronized void libera(Blocco b) {
    System.out.println("Liberato un blocco");
    b.next = liberi;
    liberi = b;

    notifyAll();
  }

  public synchronized void installa(int k) {
    // k byte
    if (k % dim_blocco != 0) {
      throw new IllegalArgumentException("Numero di byte non divisibile per dimensione blocco");
    }

    int num_blocchi = k / dim_blocco;

    for (int i = 0; i < num_blocchi; i++) {
      Blocco blocco = new Blocco(liberi);
      liberi = blocco;
    }
  }

  public void stampa() {
    Blocco blocco = liberi;

    int dim = 0;
    int num = 0;

    while (blocco != null) {
      dim += dim_blocco;
      num++;
      blocco = blocco.next;
    }

    System.out.println("\nTrovati " + num + " blocchi liberi, totale " + dim + " byte");
  }

}
