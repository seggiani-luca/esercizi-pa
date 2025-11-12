package luca.parking;

public class Operaio extends Thread {

  private final Parcheggio park;

  public Operaio(Parcheggio park) {
    this.park = park;
  }

  @Override
  public void run() {
    while (true) {
      try {
        for (int i = 0; i < 3; i++) {
          park.lava();
        }

        sleep(1000);
      } catch (InterruptedException e) {
        System.err.println("OPERAIO: sono stato interrotto");
      }
    }
  }
}
