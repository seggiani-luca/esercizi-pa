package luca.sala;

import java.util.PriorityQueue;

public class Sala {

  static class PresenzaPaziente implements Comparable<PresenzaPaziente> {

    private final String nome;
    private final int prio;

    public PresenzaPaziente(String nome, int prio) {
      this.nome = nome;
      this.prio = prio;
    }

    @Override
    public int compareTo(PresenzaPaziente other) {
      return -Integer.compare(this.prio, other.prio);
    }
  }

  private final int N;
  private final int K;

  private final PriorityQueue<PresenzaPaziente> attesa = new PriorityQueue<>();
  private int pazienti = 0;

  boolean aperto = true;

  public Sala(int N, int K) {
    this.N = N;
    this.K = K;
  }

  private boolean validaPrio(int prio) {
    return prio >= 1 && prio <= 5;
  }

  public synchronized void entrata(int prio) throws InterruptedException {
    if (!validaPrio(prio)) {
      throw new IllegalArgumentException("Valore priorità invalido");
    }

    System.out.println("Paziente " + Thread.currentThread().getName() + " con codice " + prio + " prova ad entrare");

    PresenzaPaziente p = new PresenzaPaziente(Thread.currentThread().getName(), prio);

    if (pazienti == N) {
      // aspetta
      if (aperto) {
        System.out.println("Sala chiusa");
      }

      aperto = false;
      attesa.add(p);

      while (!aperto || attesa.peek() != p) {
        wait();
      }

      attesa.remove();
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
