package luca.bar;

public class Barista extends Thread {

  private static final String[] nomi = {"Anacleto", "Biagio", "Clelia"};
  private static int counter = 0;

  private final Bancone banco;
  private final boolean[] presi = new boolean[Ingradiente.values().length];

  public Barista(Bancone banco) {
    this.setName(nomi[counter++]);
    this.banco = banco;
  }

  public static boolean pronto(boolean[] maschera) {
    for (boolean preso : maschera) {
      if (!preso) {
        return false;
      }
    }

    return true;
  }

  private boolean pronto() {
    for (boolean preso : presi) {
      if (!preso) {
        return false;
      }
    }

    return true;
  }

  private void reset() {
    for (int i = 0; i < presi.length; i++) {
      presi[i] = false;
    }
  }

  @Override
  public void run() {
    while (true) {
      while (!pronto()) {
        Ingradiente ingr;

        try {
          ingr = banco.getIngradiente(presi);
        } catch (InterruptedException e) {
          throw new RuntimeException("Barista è stato interrotto");
        }

        presi[ingr.ordinal()] = true;
      }

      reset();
    }
  }
}
