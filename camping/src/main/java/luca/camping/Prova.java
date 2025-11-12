package luca.camping;

public class Prova {

  public static void main(String[] args) {
    Campeggio camp = new Campeggio();

    System.out.println("Campeggio creato, ci sono " + camp.getOccupati() + " posti occupati");

    for (int i = 0; i < 10; i++) {
      (new Cliente(camp, 10, 5)).start();
    }

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      System.err.println("main() è stato interrotto");
      System.exit(1);
    }

    System.out.println("Campeggio chiuso, ci sono " + camp.getOccupati() + " posti occupati");
  }
}
