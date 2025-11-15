package luca.mercante;

public class Partita {

  public static final int NUM_CARTE = Carta.NUM * Seme.values().length;

  private final Carta[] carte = new Carta[NUM_CARTE];
  private final boolean[] possCarte = new boolean[NUM_CARTE];
  private int numDisponibili = NUM_CARTE;

  private final int numGiocatori;
  private int mazziGiocati = 0;

  private final Carta cartaVincente;

  public Partita(int numGiocatori) {
    for (Seme seme : Seme.values()) {
      for (int i = 1; i <= 10; i++) {
        carte[Carta.indice(i, seme)] = new Carta(i, seme);
      }
    }

    this.numGiocatori = numGiocatori;
    cartaVincente = Carta.casuale();
    System.out.println("Il banco ha estratto la carta vincente: " + cartaVincente);
  }

  public synchronized Carta compraCarta(int num, Seme seme)
          throws IllegalArgumentException, CartaNonDisponibileException {
    if (!Carta.valida(num, seme)) {
      throw new IllegalArgumentException("Impossibile comprare carta");
    }

    int idx = Carta.indice(num, seme);
    if (!possCarte[idx]) {
      possCarte[idx] = true;
      numDisponibili--;

      notifyAll();

      return carte[idx];
    }

    throw new CartaNonDisponibileException("Carta già venduta");
  }

  public synchronized Carta[] carteDisponibili() {
    Carta[] disponibili = new Carta[numDisponibili];

    int j = 0;
    for (int i = 0; i < NUM_CARTE; i++) {
      if (!possCarte[i]) {
        disponibili[j++] = carte[i];
      }
    }

    return disponibili;
  }

  public synchronized boolean attendiEstrazione(Carta[] cartePossedute)
          throws InterruptedException {
    mazziGiocati++;
    notifyAll();

    while (mazziGiocati != numGiocatori || numDisponibili != 0) {
      wait();
    }

    for (Carta carta : cartePossedute) {
      if (carta.equals(cartaVincente)) {
        return true;
      }
    }

    return false;
  }
}
