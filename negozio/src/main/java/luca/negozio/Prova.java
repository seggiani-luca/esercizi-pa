package luca.negozio;

public class Prova {

  private static final int NUM_VIP = 5;
  private static final int NUM_NORM = 5;

  private static final int MAX = 3;
  private static final int MAX_NORM = 2;

  public static void main(String[] args) {
    Negozio neg = new Negozio(3, 2);

    for (int i = 0; i < NUM_VIP; i++) {
      (new Cliente(neg, true)).start();
    }
    for (int i = 0; i < NUM_VIP; i++) {
      (new Cliente(neg, false)).start();
    }
  }
}
