package luca.memoria;

public class Processo extends Thread {

  GestioneMemoria mem;
  boolean prio;

  public Processo(GestioneMemoria mem, boolean prio) {
    this.mem = mem;
    this.prio = prio;
  }

  @Override
  public void run() {
    try {
      Thread.sleep((long) (Math.random() * 1000));
    } catch (InterruptedException e) {
      System.err.println("Processo è stato interrotto");
      System.exit(1);
    }

    Blocco b = mem.acquisisci(prio);

    try {
      Thread.sleep((long) (Math.random() * 1000));
    } catch (InterruptedException e) {
      System.err.println("Processo è stato interrotto");
      System.exit(1);
    }

    mem.libera(b);
  }

}
