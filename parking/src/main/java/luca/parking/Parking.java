package luca.parking;

public class Parking {

  public static void main(String[] args) {
    Parcheggio park = new Parcheggio(10);
    (new Operaio(park)).start();

    for (int i = 0; i < 10; i++) {
      (new Auto(park)).start();
    }

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
    }

    while (true) {
      if (park.liberi() == 0) {
        System.exit(0);
      }
    }
  }
}
