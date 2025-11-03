package luca.negozio;

public class Cliente extends Thread {

  private Negozio neg;
  private boolean vip;

  public Cliente(Negozio neg, boolean vip) {
    this.neg = neg;
    this.vip = vip;
  }

  public void run() {
    neg.entrata(vip);

    try {
      Thread.sleep((long) (1000 + Math.random() * 1000));
    } catch (InterruptedException e) {
      System.err.println("Cliente è stato interrotto");
      System.exit(1);
    }

    neg.uscita(vip);
  }
}
