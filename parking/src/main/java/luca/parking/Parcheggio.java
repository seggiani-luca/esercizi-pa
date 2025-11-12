package luca.parking;

public class Parcheggio {

  private final int numPosti;
  private int lavate = 0; // punta alla prima auto non lavata
  private int entrate = 0; // punta alla prima auto che deve entrare

  public synchronized int liberi() {
    return entrate - lavate;
  }

  public Parcheggio(int numPosti) {
    this.numPosti = numPosti;
  }

  public synchronized void attendi() throws InterruptedException, ParcheggioPienoException {
    if (liberi() > numPosti) {
      throw new ParcheggioPienoException();
    }

    int mioIndice = entrate;
    System.out.println("AUTO: entrata " + mioIndice + " nel parcheggio");
    entrate++;
    notifyAll();

    while (lavate <= mioIndice) {
      wait();
    }

    System.out.println("AUTO: uscita " + mioIndice + " dal parcheggio");
  }

  public synchronized void lava() throws InterruptedException {
    while (lavate >= entrate) {
      wait();
    }

    System.out.println("OPERAIO: lavata un'auto");
    lavate++;
    notifyAll();
  }
}
