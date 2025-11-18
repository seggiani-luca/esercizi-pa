package luca.sala;

public class Prova {

  public static void main(String[] args) {
    Sala sala = new Sala(4, 2);

    for (int i = 0; i < 6; i++) {
      Thread p = new Paziente(sala, (int) (Math.random() * 5 + 1));
      p.start();
    }
  }
}
