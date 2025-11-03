package luca.func;

class CurWorker extends Thread {

  private final long beg;
  private final long end;
  private final Concurrent cur;

  public CurWorker(long beg, long end, Concurrent cur) {
    this.beg = beg;
    this.end = end;
    this.cur = cur;
  }

  public void run() {
    long val = 0;
    for (long i = beg; i < end; i++) {
      val += Func.eval(i);
    }

    cur.aggiungi(val);
  }
}

public class Concurrent {

  private CurWorker[] curWorkers;
  public long somma;

  int workerFiniti = 0;

  public synchronized void aggiungi(long n) {
    somma += n;
    workerFiniti++;

    notifyAll();
  }

  public synchronized void attendiRisultato() {
    while (workerFiniti != curWorkers.length) {
      try {
        wait();
      } catch (InterruptedException e) {
        throw new RuntimeException("seq interrotto");
      }
    }
  }

  public Concurrent(long N, int T) {
    curWorkers = new CurWorker[T];

    long period = N / T;

    for (int i = 0; i < T; i++) {
      curWorkers[i] = new CurWorker(period * i, period * (i + 1), this);
    }
  }

  public void run() {
    for (CurWorker curWorker : curWorkers) {
      curWorker.start();
    }
  }
}
