package luca.bar;

public class Bar {

  public static void main(String[] args) {
    Bancone banco = new Bancone();

    (new Fornitore(banco)).start();

    for (int i = 0; i < 3; i++) {
      (new Barista(banco)).start();
    }

    try {
      Thread.sleep(1000 * 10);
    } catch (InterruptedException e) {
      System.err.println("main() è stato interrotto");
      System.exit(1);
    }

    System.exit(0);
  }
}
