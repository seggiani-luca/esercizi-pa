package luca.yfactor;

public class Cantante extends Thread {

  /**
   * Array di nomi dei cantanti.
   */
  public static final String[] NOMI = {"BimboMarmitta", "CuboEBasta", "Franco252", "IlQuattro"};

  /**
   * Contatore usato per indicizzare l'array di nomi.
   */
  private static int counter = 0;

  /**
   * Tempo massimo per cui un cantante può aspettare in camerino o sul palco.
   */
  public static final long MAX_ATTESA = 1000;

  /**
   * Probabilità che il cantante fumi in camerino.
   */
  public static final float CHANCE_FUMO = 0.1f;

  /**
   * Studio dove questo cantante si esibisce (cioè i cui camerini e il cui palco questo cantante
   * usa).
   */
  private final Studio studio;

  /**
   * Segnala se questo cantante è un fumatore.
   */
  private final boolean fumatore;

  /**
   * Crea un nuovo cantante in un dato studio.
   *
   * @param studio lo studio dove il cantante si esibirà
   * @param fumatore questo cantante è un fumatore?
   */
  public Cantante(Studio studio, boolean fumatore) {
    this.studio = studio;
    this.fumatore = fumatore;

    this.setName(NOMI[counter++]);
  }

  /**
   * Helper per calcolare il tempo di attesa in camerino o sul palco.
   *
   * @return il tempo di attesa
   */
  private long getAttesa() {
    return (long) (Math.random() * MAX_ATTESA);
  }

  /**
   * Flusso di esecuzione del cantante, dove questo prova ad entrare in un camerino, salire sul
   * palco (che automaticamente lo rimuove dal camerino), e scendere dal palco. Nel caso suoni
   * l'allarme antincendio, il cantante ricomincia tale flusso da capo, finché non riesce a salire
   * sul palco ed esibirsi.
   */
  @Override
  public void run() {
    while (true) {
      // descrittore del camerino
      int cd = -1;

      // entra nel camerino
      try {
        cd = studio.entraCamerino();
      } catch (InterruptedException e) {
        throw new RuntimeException("Cantante " + getName() + " è stato interrotto prima di entrare in camerino");
      }

      // se ne ha voglia, fuma
      if (fumatore && Math.random() < CHANCE_FUMO) {
        studio.fuma(cd);
      }

      // aspetta in camerino, deve gestire l'allarme
      try {
        long attesa = getAttesa();
        sleep(attesa);
        System.out.println("Cantante " + getName() + " si prepara nel camerino " + cd + " per " + attesa + " ms");
      } catch (InterruptedException e) {
        // interrotto significa allarme antincendio
        System.out.println("Cantante " + getName() + " ricomincia da capo");

        // chi viene interrotto aveva ottenuto per forza un camerino, quindi deve uscire
        studio.esciCamerino(cd);

        // ricomincia
        continue;
      }

      // sale sul palco
      try {
        studio.saliSulPalco(cd);
      } catch (InterruptedException e) {
        throw new RuntimeException("Cantante " + getName() + " è stato interrotto prima di salire sul palco");
      }

      // aspetta sul palco
      try {
        long attesa = getAttesa();
        sleep(attesa);
        System.out.println("Cantante " + getName() + " canta per " + attesa + " ms");
      } catch (InterruptedException e) {
        throw new RuntimeException("Cantante " + getName() + " è stato interrotto mentre era sul palco");
      }

      // scende dal palco
      studio.scendiDalPalco(cd);

      // se il cantante arriva qui, ha terminato
      return;
    }
  }
}
