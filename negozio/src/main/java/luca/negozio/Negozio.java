package luca.negozio;

public class Negozio {

  private int vip;
  private int norm;

  private final int max;
  private final int maxNorm;

  int vipAttesa = 0;

  private boolean chiuso = false;

  public Negozio(int max, int maxNorm) {
    if (maxNorm > max) {
      throw new IllegalArgumentException("maxNorm non può essere maggiore di norm");
    }

    this.max = max;
    this.maxNorm = maxNorm;
  }

  private int num() {
    return vip + norm;
  }

  public synchronized void entrata(boolean isVip) {
    if (isVip) {
      vipAttesa++;
    }

    while (num() >= max || (!isVip && norm >= maxNorm) || (!isVip && vipAttesa > 0)) {
      try {
        System.out.println((isVip ? "Vip " : "Non vip ") + Thread.currentThread().getName() + " alla porta");
        wait();
      } catch (InterruptedException e) {
        System.err.println("entrata() è stato interrotto");
        System.exit(1);
      }
    }

    System.out.println((isVip ? "Vip " : "Non vip ") + Thread.currentThread().getName() + " entrato");
    if (isVip) {
      vipAttesa--;
      vip++;
    } else {
      norm++;
    }

    if (num() >= max && !chiuso) {
      System.out.println("Negozio chiuso per troppa gente");
      chiuso = true;
    }
    if (norm >= maxNorm && !chiuso) {
      System.out.println("Negozio chiuso per pochi vip");
      chiuso = true;
    }
  }

  public synchronized void uscita(boolean isVip) {
    System.out.println((isVip ? "Vip " : "Non vip ") + Thread.currentThread().getName() + " uscito");
    if (isVip) {
      vip--;
    } else {
      norm--;
    }

    if (num() < max && norm < maxNorm && chiuso) {
      System.out.println("Negozio riaperto");
      chiuso = false;
    }

    notifyAll();
  }

}
