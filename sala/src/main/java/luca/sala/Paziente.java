package luca.sala;

public class Paziente extends Thread {

  public static String[] nomi = {"Alice", "Biago", "Corrado", "Diego", "Ercole", "Fausto"};
  public static int idx = 0;

  private Sala sala;
  private int prio;

  public Paziente(Sala sala, int prio) {
    this.sala = sala;
    this.prio = prio;

    this.setName(nomi[idx++]);
  }

  @Override
  public void run() {
    try {
      sala.entrata(prio);

      Thread.sleep((long) (1000 + Math.random() * 1000));

      sala.uscita();
    } catch (InterruptedException e) {
      throw new RuntimeException("Cliente è stato interrotto");
    }
  }
}
