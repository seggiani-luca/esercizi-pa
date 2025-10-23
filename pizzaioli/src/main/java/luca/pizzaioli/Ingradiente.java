package luca.pizzaioli;

public enum Ingradiente {
  POMODORO,
  MOZZARELLA,
  PASTA;

  public static int numero() {
    return values().length;
  }
}
