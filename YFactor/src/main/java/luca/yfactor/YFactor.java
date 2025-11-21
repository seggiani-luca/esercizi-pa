package luca.yfactor;

public class YFactor {

  /**
   * Numero di camerini dello studio.
   */
  private static final int NUM_CAMERINI = 2;

  /**
   * Numero di cantanti che si esibiscono nello studio.
   */
  private static final int NUM_CANTANTI = 4;

  /**
   * Indica se i cantanti che si esibiscono nello studio sono o meno fumatori.
   */
  private static final boolean FUMATORI = true;

  /**
   * Flusso di esecuzione principale, crea uno studio e lo popola di cantanti, quindi inizia i
   * flussi di esecuzione dei cantanti e aspetta la loro terminazione. Infine termina stampando un
   * messaggio.
   *
   * @param args non significativo
   */
  public static void main(String[] args) {
    // crea studio e cantanti
    Studio studio = new Studio(NUM_CAMERINI);
    Cantante[] cantanti = new Cantante[NUM_CANTANTI];

    for (int i = 0; i < NUM_CANTANTI; i++) {
      cantanti[i] = new Cantante(studio, FUMATORI);
    }

    // inizia i flussi di esecuzione dei cantanti
    for (Cantante cantante : cantanti) {
      cantante.start();
    }

    // aspetta che tutti i cantanti si esibiscano
    try {
      for (Cantante cantante : cantanti) {
        cantante.join();
      }
    } catch (InterruptedException e) {
      throw new RuntimeException("main() è stato interrotto");
    }

    // i cantanti hanno finito di esibirsi, stampa un messaggio e termina
    System.out.println("Fine");
  }
}
