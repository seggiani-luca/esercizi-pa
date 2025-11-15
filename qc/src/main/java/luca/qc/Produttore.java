package luca.qc;

public class Produttore extends Thread {

  public static final long SLEEP_TIME = 1000;

  private final Deposito dep;
  private final Pezzo.Tipo tipo;

  public Produttore(Deposito dep, Pezzo.Tipo tipo) {
    this.dep = dep;
    this.tipo = tipo;
  }

  @Override
  public void run() {
    while (true) {
      float qual = (float) Math.random();
      Pezzo p = new Pezzo(tipo, qual);

      try {
        dep.inserisci(p);
        Thread.sleep(SLEEP_TIME);
      } catch (InterruptedException e) {
        throw new RuntimeException("Produttore è stato interrotto");
      }
    }
  }
}
