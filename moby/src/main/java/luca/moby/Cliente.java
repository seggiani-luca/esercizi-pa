package luca.moby;

public class Cliente extends Thread {

  private Traghetto trag;

  private int idx;
  private static int counter = 0;

  public Cliente(Traghetto trag) {
    this.trag = trag;
    this.idx = counter++;
  }

  @Override
  public void run() {
    int piano = (int) (Math.random() * trag.getPiani());
    int posto = (int) (Math.random() * trag.getPosti());

    System.out.println("Cliente " + idx + " seleziona il posto " + trag.posto2Stringa(piano, posto));
    if (trag.seleziona(piano, posto)) {
      trag.paga(piano, posto);
      System.out.println("Cliente " + idx + " ha pagato " + trag.posto2Stringa(piano, posto));
    }
  }
}
