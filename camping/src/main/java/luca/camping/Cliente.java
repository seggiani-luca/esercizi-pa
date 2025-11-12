package luca.camping;

public class Cliente extends Thread {

  private Campeggio camp;
  private final int quante;
  private final long tempo;

  public Cliente(Campeggio camp, int quante, long tempo) {
    this.camp = camp;
    this.quante = quante;
    this.tempo = tempo;
  }

  @Override
  public void run() {
    int p;

    try {
      p = camp.prenota(quante, tempo);
    } catch (PrenotazioneFallitaException e) {
      System.err.println("Cliente non è riuscito a prenotare per timeout");
      return;
    }

    if (Math.random() > 0.5) {
      camp.disdici(p);
    }
  }
}
