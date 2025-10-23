package luca.pizzaioli;

public class Pizzaiolo extends Thread {

  private final Ingradiente ingrPreferito;
  private final Tavolo tavolo;

  public Pizzaiolo(Ingradiente ingr, Tavolo tavolo) {
    ingrPreferito = ingr;
    this.tavolo = tavolo;
  }

  @Override
  public void run() {
    while (true) {
      try {
        tavolo.prendiDaTavola(ingrPreferito);
      } catch (InterruptedException e) {
        System.err.println("Pizzaiolo è stato interrotto");
      }
    }
  }
}
