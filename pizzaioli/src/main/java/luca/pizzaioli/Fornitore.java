package luca.pizzaioli;

import java.util.Arrays;

public class Fornitore extends Thread {

  private final Tavolo tavolo;

  public Fornitore(Tavolo tavolo) {
    this.tavolo = tavolo;
  }

  private boolean arrayContiene(Ingradiente[] ingradienti, Ingradiente ingr) {
    for (Ingradiente scanIngr : ingradienti) {
      if (ingr == scanIngr) {
        return true;
      }
    }

    return false;
  }

  private Ingradiente[] ottieniIngradientiCasuali() {
    Ingradiente[] ingrCasuali = new Ingradiente[Ingradiente.numero() - 1];

    for (int i = 0; i < Ingradiente.numero() - 1; i++) {
      Ingradiente ingr;

      // ottieni un ingradiente nuovo
      do {
        ingr = Ingradiente.values()[(int) (Math.random() * Ingradiente.numero())];
      } while (arrayContiene(ingrCasuali, ingr));

      ingrCasuali[i] = ingr;
    }

    return ingrCasuali;
  }

  @Override
  public void run() {
    while (true) {
      Ingradiente[] ingrCasuali = ottieniIngradientiCasuali();

      try {
        tavolo.mettiInTavola(ingrCasuali);
      } catch (InterruptedException e) {
        System.err.println("Fornitore è stato interrotto");
      }
    }
  }
}
