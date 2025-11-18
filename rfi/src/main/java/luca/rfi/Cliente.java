package luca.rfi;

public class Cliente extends Thread {

  public static final String[] NOMI = {"Anna", "Biagio", "Carmine", "Diego", "Edoardo"};
  private static int counter = 0;

  public static final int MIN_POSTI = 3;
  public static final int MAX_POSTI = 20;
  public static final float CHANCE_DISDICI = 0.5f;
  public static final int TIMEOUT = 1000;

  private final Treno treno;

  public Cliente(Treno treno) {
    this.setName(NOMI[counter++]);
    this.treno = treno;
  }

  @Override
  public void run() {
    int posti = (int) (Math.random() * (MAX_POSTI - MIN_POSTI) + MIN_POSTI);
    int pren;

    try {
      pren = treno.prenota(posti, TIMEOUT);
    } catch (InterruptedException | PrenotazioneFallitaException e) {
      throw new RuntimeException("Prenotazione fallita." + e.getMessage());
    }

    if (Math.random() < CHANCE_DISDICI) {
      treno.disdici(pren);
    }
  }
}
