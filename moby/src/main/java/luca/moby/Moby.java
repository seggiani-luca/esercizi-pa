package luca.moby;

public class Moby {

  public static final int PIANI = 2;
  public static final int NUM_CLIENTI = 10;

  public static void stampaTraghetto(Traghetto trag) {
    for (int n = 0; n < trag.getPiani(); n++) {
      System.out.println("--- Piano " + n + " ---");
      trag.visualizza(n);
      System.out.println();
    }
  }

  public static void main(String[] args) {
    Traghetto trag = new Traghetto(PIANI);
    stampaTraghetto(trag);

    System.out.println("Seleziono piano 1, posto 10");
    trag.seleziona(1, 10);
    stampaTraghetto(trag);

    System.out.println("Aspetto 7 secondi");
    try {
      Thread.sleep(6000);
    } catch (InterruptedException e) {
      System.err.println("main() è stato interroto");
      System.exit(1);
    }

    stampaTraghetto(trag);

    System.exit(0);
  }
}
