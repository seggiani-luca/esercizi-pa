package luca.qc;

public class Consumatore extends Thread {

  public static final long SLEEP_TIME = 1000;

  private final Deposito dep;

  public Consumatore(Deposito dep) {
    this.dep = dep;
  }

  @Override
  public void run() {
    while (true) {
      try {
        Pezzo[] pezzi = dep.estrai();
        Thread.sleep(SLEEP_TIME);
      } catch (InterruptedException e) {
        throw new RuntimeException("Consumatore è stato interrotto");
      }
    }
  }
}
