package luca.bar;

public class Bancone {

  private Ingradiente ingr;

  public synchronized void setIngradiente(Ingradiente ingr) {
    if (ingr == null) {
      throw new RuntimeException("Provato ad inserire ingradiente null");
    }

    if (this.ingr == null) {
      System.out.println("[FORNITORE]\tMesso in tavola " + ingr.toString());
    } else {
      System.out.println("[FORNITORE]\tVisto deadlock, messo in tavola " + ingr.toString());
    }

    this.ingr = ingr;
    notifyAll();
  }

  public synchronized Ingradiente getIngradiente(boolean[] maschera) throws InterruptedException {
    while (ingr == null || maschera[ingr.ordinal()] != false) {
      wait();
    }

    System.out.println("[BARISTA]\t" + Thread.currentThread().getName() + " ha preso ingradiente " + ingr.toString());

    // controlla qui per listati più comprensibili
    maschera[ingr.ordinal()] = true;
    if (Barista.pronto(maschera)) {
      System.out.println("[BARISTA]\t" + Thread.currentThread().getName() + " ha fatto un cappuccino");
    }

    Ingradiente temp = ingr;
    ingr = null;

    notifyAll();
    return temp;
  }

  public synchronized Ingradiente aspetta(long attesa) throws InterruptedException {
    if (ingr != null) {
      wait(attesa);
    }

    return ingr;
  }

}
