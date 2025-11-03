package luca.func;

class SeqWorker extends Thread {

  private final long N;
  private final Sequential seq;

  public SeqWorker(long N, Sequential seq) {
    this.N = N;
    this.seq = seq;
  }

  public void run() {
    long val = 0;
    for (long i = 0; i < N; i++) {
      val += Func.eval(i);
    }

    seq.aggiungi(val);
  }
}

public class Sequential {

  private SeqWorker seqWorker;
  public long somma;

  boolean finito = false;

  public synchronized void aggiungi(long n) {
    somma += n;
    finito = true;

    notifyAll();
  }

  public synchronized void attendiRisultato() {
    while (!finito) {
      try {
        wait();
      } catch (InterruptedException e) {
        throw new RuntimeException("seq interrotto");
      }
    }
  }

  public Sequential(long N) {
    seqWorker = new SeqWorker(N, this);
  }

  public void run() {
    seqWorker.start();
  }
}
