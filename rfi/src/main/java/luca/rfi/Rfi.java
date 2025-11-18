package luca.rfi;

public class Rfi {

  public static final int NUM_CLIENTI = 5;

  public static void main(String[] args) {
    Treno treno = new Treno();

    for (int i = 0; i < NUM_CLIENTI; i++) {
      (new Cliente(treno)).start();
    }
  }
}
