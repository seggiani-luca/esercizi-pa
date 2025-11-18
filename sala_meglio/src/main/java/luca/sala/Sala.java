package luca.sala;

public class Sala {

  private final int N;
  private final int K;

  private int[] attese = new int[5];
  private int pazienti = 0;

  boolean aperto = true;

  public Sala(int N, int K) {
    this.N = N;
    this.K = K;
  }

  private boolean validaPrio(int prio) {
    return prio >= 1 && prio <= 5;
  }

  private boolean esisteSup(int prio) {
    for (int i = prio + 1; i < 5; i++) {
      if (attese[i] > 0) {
        return true;
      }
    }

    return false;
  }

  public synchronized void entrata(int prio) throws InterruptedException {
    if (!validaPrio(prio)) {
      throw new IllegalArgumentException("Valore priorità invalido");
    }

    System.out.println("Paziente " + Thread.currentThread().getName() + " con codice " + prio + " prova ad entrare");

    if (pazienti == N) {
      // aspetta
      if (aperto) {
        System.out.println("Sala chiusa");
      }

      aperto = false;
      attese[prio - 1]++;

      while (!aperto || esisteSup(prio)) {
        wait();
      }

      attese[prio - 1]--;
    } // altrimenti entra

    pazienti++;
    System.out.println("Paziente " + Thread.currentThread().getName() + " con codice " + prio + " entrato");
  }

  public synchronized void uscita() {
    pazienti--;
    System.out.println("Paziente " + Thread.currentThread().getName() + " uscito");

    if (pazienti == K) {
      if (!aperto) {
        System.out.println("Sala aperta");
      }
      aperto = true;
    }

    notifyAll();
  }
}
