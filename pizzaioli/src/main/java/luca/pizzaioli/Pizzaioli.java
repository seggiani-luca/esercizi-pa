package luca.pizzaioli;

// Per fare una pizza sono necessari tre ingredienti: pomodoro, mozzarella e pasta. Intorno a un
// tavolo ci sono tre pizzaioli. Ogni pizzaiolo ha una riserva infinita di uno solo dei tre
// ingredienti: il primo di pomodoro, il secondo di mozzarella, il terzo di pasta. Al tavolo è
// presente anche un fornitore dotato di una riserva infinita di tutti e tre gli ingredienti. Il
// fornitore permette ai pizzaioli di fare le pizze mettendo sul tavolo due dei tre ingredienti. Più
// in dettaglio: il fornitore sceglie due ingredienti a caso; il pizzaiolo che ha il terzo
// ingrediente preleva quelli sul tavolo e fa una pizza. Il fornitore sceglie altri due ingredienti
// a caso e il ciclo si ripete.

public class Pizzaioli {

  public static void main(String[] args) {
    // inizializza tavolo e fornitore
    Tavolo tavolo = new Tavolo();
    (new Fornitore(tavolo)).start();

    // inizializza pizzaioli
    for (Ingradiente ingr : Ingradiente.values()) {
      (new Pizzaiolo(ingr, tavolo)).start();
    }
  }
}
