package luca.parking;

public class Auto extends Thread {

  private final Parcheggio park;

  public Auto(Parcheggio park) {
    this.park = park;
  }

  @Override
  public void run() {
    try {
      park.attendi();
    } catch (InterruptedException e) {
      System.err.println("AUTO: sono stata interrotta");
    } catch (ParcheggioPienoException e) {
      System.err.println("AUTO: il parcheggio è pieno");
    }
  }
}
