package luca.yfactor;

public class Studio {

  /**
   * Array di thread rappresentante i cantanti nei camerini. Un indice all'interno di questa array
   * rappresenta un descrittore di camerino.
   */
  private final Thread[] camerini;

  /**
   * Intero che rappresenta il camerino precedentemente occupato (cioè il descrittore di camerino)
   * del cantante correntemente sul palco. Vale -1 se il palco è vuoto.
   */
  private int palco = -1;

  /**
   * Crea un nuovo studio con un dato numero di camerini.
   *
   * @param numCamerini numero di camerini dello studio
   */
  public Studio(int numCamerini) {
    camerini = new Thread[numCamerini];
  }

  /**
   * Helper che scansiona l'array di camerini per trovarne uno libero. Restituisce -1 se non ce ne
   * sono.
   *
   * @return indice camerino libero, o -1 se non trovato
   */
  private int getCamerinoLibero() {
    for (int i = 0; i < camerini.length; i++) {
      if (camerini[i] == null) {
        return i;
      }
    }

    return -1;
  }

  /**
   * Entra in un camerino, restituendo il descrittore del camerino. Blocca finché non c'è un
   * camerino libero.
   *
   * @return il descrittore di camerino ottenuto
   * @throws InterruptedException in caso di interruzioni
   */
  public synchronized int entraCamerino() throws InterruptedException {
    int cd = getCamerinoLibero();
    while (cd == -1) {
      wait();
      cd = getCamerinoLibero();
    }

    System.out.println("Cantante " + Thread.currentThread().getName() + " entra nel camerino " + cd);

    camerini[cd] = Thread.currentThread();
    return cd;
  }

  /**
   * Esce dal camerino indicato dal descrittore fornito.
   *
   * @param cd descrittore del camerino da liberare
   */
  public synchronized void esciCamerino(int cd) {
    if (cd < 0 || cd >= camerini.length) {
      throw new ArrayIndexOutOfBoundsException("Cantante " + Thread.currentThread().getName() + " vuole uscire da un camerino inesistente");
    }
    if (camerini[cd] != Thread.currentThread()) {
      throw new IllegalArgumentException("Cantante " + Thread.currentThread().getName() + " vuole uscire da un camerino dove non è entrato");
    }

    System.out.println("Cantante " + Thread.currentThread().getName() + " esce dal camerino " + cd);

    camerini[cd] = null;
    notifyAll();
  }

  /**
   * Sale sul palco, prendendo in argomento il descrittore di camerino del cantante. Blocca finché
   * il palco non è libero. Prima di bloccare, esce dal camerino (il chiamante resta in attesa di
   * poter salire sul palco dopo essere uscito).
   *
   * @param cd descrittore del camerino del cantante che sale sul palco
   * @throws InterruptedException in caso di interruzioni
   */
  public synchronized void saliSulPalco(int cd) throws InterruptedException {
    if (cd < 0 || cd >= camerini.length) {
      throw new ArrayIndexOutOfBoundsException("Cantante " + Thread.currentThread().getName() + " vuole salire sul palco da un camerino inesistente");
    }
    if (camerini[cd] != Thread.currentThread()) {
      throw new IllegalArgumentException("Cantante " + Thread.currentThread().getName() + " vuole salire sul palco da un camerino dove non è entrato");
    }

    // la chiamata a saliSulPalco() provoca automaticamente la chiamata di esciCamerino()
    esciCamerino(cd);

    while (palco != -1) {
      wait();
    }

    System.out.println("Cantante " + Thread.currentThread().getName() + " sale sul palco");

    palco = cd;
    notifyAll();
  }

  /**
   * Scende dal palco, prendendo in argomento il descrittore di camerino del cantante.
   *
   * @param cd descrittore del camerino del cantante che scende dal palco
   */
  public synchronized void scendiDalPalco(int cd) {
    if (palco != cd) {
      throw new IllegalArgumentException("Cantante " + Thread.currentThread().getName() + " vuole scendere da un palco su cui non è mai salito");
    }

    System.out.println("Cantante " + Thread.currentThread().getName() + " scende dal palco");

    palco = -1;
    notifyAll();
  }

  /**
   * Fuma nel backstage, facendo scattare l'allarme antincendio. Questo provoca l'interruzione di
   * tutti i cantanti presenti nei camerini.
   *
   * @param cd descrittore del camerino del cantante che fuma
   */
  public synchronized void fuma(int cd) {
    if (cd < 0 || cd >= camerini.length) {
      throw new ArrayIndexOutOfBoundsException("Cantante " + Thread.currentThread().getName() + " vuole fumare in un camerino inesistente");
    }
    if (camerini[cd] != Thread.currentThread()) {
      throw new IllegalArgumentException("Cantante " + Thread.currentThread().getName() + " vuole fumare in un camerino dove non è entrato");
    }

    System.out.println("Cantante " + Thread.currentThread().getName() + " fuma in camerino, scattato allarme");

    for (Thread t : camerini) {
      // non interrompere camerini vuoti
      if (t == null) {
        continue;
      }

      t.interrupt();
    }
  }
}
