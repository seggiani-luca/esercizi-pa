package luca.qc;

public class Controllore extends Thread {

  public static final long SLEEP_TIME = 3000;

  private final Deposito dep;

  public Controllore(Deposito dep) {
    this.dep = dep;
  }

  @Override
  public void run() {
    while (true) {
      try {
        dep.controlla();
        Thread.sleep(SLEEP_TIME);
      } catch (InterruptedException e) {
        throw new RuntimeException("Controllore è stato interrotto");
      }
    }
  }
}
