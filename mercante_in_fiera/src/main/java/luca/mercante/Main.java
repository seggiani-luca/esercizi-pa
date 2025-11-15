package luca.mercante;

public class Main {

  static final int NUM_GIOCATORI = 4;

  public static void main(String[] args) {
    Partita partita = new Partita(NUM_GIOCATORI);

    for (int i = 0; i < NUM_GIOCATORI; i++) {
      (new Giocatore(partita)).start();
    }
  }
}
