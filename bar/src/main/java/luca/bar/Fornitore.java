package luca.bar;

public class Fornitore extends Thread {

  public static final long ATTESA = 3000;

  private final Bancone banco;

  public Fornitore(Bancone banco) {
    this.banco = banco;
  }

  @Override
  public void run() {
    while (true) {
      int idx = (int) (Math.random() * Ingradiente.values().length);
      Ingradiente nuovoIngr = Ingradiente.values()[idx];
      banco.setIngradiente(nuovoIngr);

      try {
        Ingradiente ingr = banco.aspetta(ATTESA);

        while (ingr != null) {
          banco.setIngradiente(Ingradiente.altro(ingr));

          ingr = banco.aspetta(ATTESA);
        }
      } catch (InterruptedException e) {
        throw new RuntimeException("Fornitore è stato interrotto...");
      }
    }
  }
}
