package luca.memoria;

public class Memoria {

  private static final int NUM_PRIO = 10;
  private static final int NUM_NORM = 10;

  public static void main(String[] args) {
    GestioneMemoria mem = new GestioneMemoria(1024, 64);

    mem.stampa();

    for (int i = 0; i < NUM_PRIO; i++) {
      (new Processo(mem, true)).start();
    }
    for (int i = 0; i < NUM_NORM; i++) {
      (new Processo(mem, false)).start();
    }

    try {
      Thread.sleep(2500);
    } catch (InterruptedException e) {
      System.err.println("Main è stato interrotto");
      System.exit(1);
    }

    mem.stampa();
  }
}
