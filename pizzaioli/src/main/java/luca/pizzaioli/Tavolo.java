package luca.pizzaioli;

public class Tavolo {

  public Ingradiente[] ingradienti = new Ingradiente[Ingradiente.numero() - 1];

  public boolean vuoto() {
    for (Ingradiente ingr : ingradienti) {
      if (ingr != null) {
        return false;
      }
    }

    return true;
  }

  public boolean pieno() {
    for (Ingradiente ingr : ingradienti) {
      if (ingr == null) {
        return false;
      }
    }

    return true;
  }

  public void svuota() {
    ingradienti = new Ingradiente[2];
  }

  public boolean mancaIngradiente(Ingradiente ingradienteMancante) {
    for (Ingradiente ingr : ingradienti) {
      if (ingr == ingradienteMancante) {
        return false;
      }
    }

    return true;
  }

  public synchronized void mettiInTavola(Ingradiente[] ingradienti) throws InterruptedException {
    // aspetta che il tavolo sia vuoto
    while (!vuoto()) {
      wait();
    }

    System.out.print("Fornitore ha messo in tavola: ");
    for (Ingradiente ingr : ingradienti) {
      System.out.print(ingr.name() + " ");
    }
    System.out.println();

    // per riferimento
    this.ingradienti = ingradienti;
    notifyAll();
  }

  public synchronized boolean prendiDaTavola(Ingradiente ingrMancante) throws InterruptedException {
    // aspetta che il tavolo sia pieno
    while (!pieno() || mancaIngradiente(ingrMancante)) {
      wait();
    }

    svuota();
    notifyAll();
    return true;
  }
}
