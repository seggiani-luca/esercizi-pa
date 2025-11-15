package luca.mercante;

public class Carta {

  public static final int NUM = 10;

  private final int num;

  public int getNum() {
    return num;
  }

  private final Seme seme;

  public Seme getSeme() {
    return seme;
  }

  public static int indice(int num, Seme seme) {
    return num - 1 + seme.ordinal() * 10;
  }

  public static boolean valida(int num, Seme seme) {
    return seme != null && num >= 1 && num <= NUM;
  }

  public static Carta casuale() {
    int num = (int) (Math.random() * NUM + 1);
    Seme seme = Seme.values()[(int) (Math.random() * Seme.values().length)];

    return new Carta(num, seme);
  }

  public boolean equals(Carta other) {
    return other.seme == this.seme && other.num == this.num;
  }

  public Carta(int num, Seme seme) throws IllegalArgumentException {
    if (!valida(num, seme)) {
      throw new IllegalArgumentException("Impossibile creare carta");
    }

    this.seme = seme;
    this.num = num;
  }

  @Override
  public String toString() {
    return num + " di " + seme;
  }
}
