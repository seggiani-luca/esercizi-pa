package luca.code_prio;

import java.util.Arrays;

class Queue {

  private Messaggio buf[];
  private int beg, end;

  Queue(int dim) {
    buf = new Messaggio[dim];
  }

  public boolean piena() {
    return ((end + 1) % buf.length) == beg;
  }

  public boolean vuota() {
    return beg == end;
  }

  public int quanti() {
    return (end - beg + buf.length) % buf.length;
  }

  public int dim() {
    return buf.length;
  }

  public synchronized void inserisci(Messaggio m) {
    while (piena()) {
      try {
        wait();
      } catch (InterruptedException e) {
        throw new RuntimeException("Queue.inserisci() svegliato");
      }
    }

    buf[end] = m;
    end = (end + 1) % buf.length;
    notifyAll();
  }

  public synchronized Messaggio estrai() {
    while (vuota()) {
      try {
        wait();
      } catch (InterruptedException e) {
        throw new RuntimeException("Queue.estrai() svegliato");
      }
    }

    Messaggio temp = buf[beg];
    beg = (beg + 1) % buf.length;
    notifyAll();
    return temp;
  }

  public String toString() {
    return Arrays.toString(buf);
  }
}

public class Deposito {

  Queue queue;
  Queue prioQueue;

  public Deposito(int dim, int dimPrio) {
    queue = new Queue(dim);
    prioQueue = new Queue(dimPrio);
  }

  public Deposito() {
    this(10, 10);
  }

  public synchronized void inserisci(Messaggio m) {
    if (m.getUrg()) {
      prioQueue.inserisci(m);
    } else {
      queue.inserisci(m);
    }

    notifyAll();

    System.out.println("Inserito messaggio (dep. mio): " + m);
  }

  public synchronized Messaggio[] estrai(int quanti) throws IllegalArgumentException {
    if (quanti > queue.dim() + prioQueue.dim()) {
      throw new IllegalArgumentException("quanti sbagliato per Messaggio.estrai()");
    }

    int totQuanti = prioQueue.quanti() + queue.quanti();
    while (totQuanti < quanti && prioQueue.vuota()) {
      try {
        wait();
      } catch (InterruptedException e) {
        throw new RuntimeException("Deposito.estrai() svegliato");
      }

      totQuanti = prioQueue.quanti() + queue.quanti();
    }

    int len = totQuanti >= quanti ? quanti : totQuanti;
    Messaggio res[] = new Messaggio[len];

    for (int i = 0; i < len; i++) {
      res[i] = prioQueue.vuota() ? queue.estrai() : prioQueue.estrai();
    }

    System.out.println("Estratti messaggi (dep. mio): " + Arrays.toString(res));

    return res;
  }
}
