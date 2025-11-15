package luca.qc;

public class Pezzo {

  public static enum Tipo {
    X,
    Y
  }

  private final Tipo tipo;

  public Tipo getTipo() {
    return tipo;
  }

  private final float qualita;

  public float getQualita() {
    return qualita;
  }

  public Pezzo(Tipo tipo, float qualita) {
    if (qualita < 0 || qualita > 1) {
      throw new IllegalArgumentException("Valore qualità non valido, dev'essere compreso fra 0 e 1");
    }

    this.tipo = tipo;
    this.qualita = qualita;
  }

  @Override
  public String toString() {
    return "{" + tipo.toString() + ", " + String.format("%.4f", qualita) + "}";
  }
}
