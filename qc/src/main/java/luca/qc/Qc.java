package luca.qc;

public class Qc {

  public static void main(String[] args) {
    Deposito dep = new Deposito(10);

    (new Produttore(dep, Pezzo.Tipo.X)).start();
    (new Produttore(dep, Pezzo.Tipo.Y)).start();

    (new Consumatore(dep)).start();

    (new Controllore(dep)).start();

    try {
      Thread.sleep(10000);
      System.exit(0);
    } catch (InterruptedException e) {
      throw new RuntimeException("Main è stato interrotto");
    }
  }
}
