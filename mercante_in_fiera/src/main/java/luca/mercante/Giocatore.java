package luca.mercante;

public class Giocatore extends Thread {

  static final int NUM_COMPRATE = 10;
  static int giocatori = 0;

  private int num;
  private Partita partita;
  private Carta[] comprate = new Carta[NUM_COMPRATE];

  public Giocatore(Partita partita) {
    num = giocatori++;
    this.partita = partita;
  }

  @Override
  public void run() {
    int j = 0;
    while (j < NUM_COMPRATE) {
      Carta casuale = Carta.casuale();
      try {
        Carta comprata = partita.compraCarta(casuale.getNum(), casuale.getSeme());
        comprate[j++] = comprata;
      } catch (CartaNonDisponibileException e) {
      }
    }

    synchronized (System.out) {
      System.out.print("Giocatore " + num + " ha comprato: {");
      for (int i = 0; i < NUM_COMPRATE; i++) {
        System.out.print(comprate[i]);
        if (i < NUM_COMPRATE - 1) {
          System.out.print(", ");
        }
      }
      System.out.println("}");
    }

    try {
      boolean risultato = partita.attendiEstrazione(comprate);

      if (risultato) {
        System.out.println("Giocatore " + num + " ha vinto!");
      }
    } catch (InterruptedException e) {
      throw new RuntimeException("Giocatore " + num + " interrotto");
    }
  }
}
