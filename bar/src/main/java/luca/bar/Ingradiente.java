package luca.bar;

public enum Ingradiente {
  Latte,
  Caffe;

  public static Ingradiente altro(Ingradiente ingr) {
    int idx = (ingr.ordinal() + 1) % values().length;
    return values()[idx];
  }
}
