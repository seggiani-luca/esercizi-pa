package luca.qc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Deposito {

  public static final float Q_THRESH = 0.2f;

  public final List<Pezzo> pezzi = new ArrayList<>();
  public final int dim;

  public Deposito(int dim) {
    this.dim = dim;
  }

  private void stampa() {
    System.out.print("[");
    for (int i = 0; i < pezzi.size(); i++) {
      System.out.print(pezzi.get(i));
      if (i != pezzi.size() - 1) {
        System.out.print(", ");
      }
    }
    System.out.println("]");
  }

  private boolean validaInserimento(Pezzo.Tipo tipo) {
    // veramente pieno
    if (pezzi.size() == dim) {
      return false;
    }

    // pieno - 1, tutti pezzi uguali
    if (pezzi.size() == dim - 1) {
      for (Pezzo p : pezzi) {
        if (p.getTipo() != tipo) {
          // almeno uno diverso
          return true;
        }
      }

      return false;
    }

    return true;
  }

  private int[] validaEstrazione() {
    // traccia indici prezzi trovati
    int[] indici = new int[Pezzo.Tipo.values().length];
    for (int i = 0; i < indici.length; i++) {
      indici[i] = -1;
    }

    // controlla ogni pezzo
    for (int i = 0; i < pezzi.size(); i++) {
      Pezzo p = pezzi.get(i);

      // prendi i primi trovati, cioè salta i successivi
      if (indici[p.getTipo().ordinal()] != -1) {
        continue;
      }

      // trovato
      indici[p.getTipo().ordinal()] = i;

      // controlla se trovati tutti
      boolean passato = true;
      for (int idx : indici) {
        if (idx == -1) {
          passato = false;
          break;
        }
      }

      if (passato) {
        return indici;
      }
    }

    return null;
  }

  public synchronized void inserisci(Pezzo p) throws InterruptedException {
    while (!validaInserimento(p.getTipo())) {
      wait();
    }

    System.out.println("--- Operazione di inserimento ---");
    System.out.print("Pre: ");
    stampa();
    System.out.println("Inserito pezzo: " + p);

    pezzi.add(p);

    System.out.print("Post: ");
    stampa();
    System.out.println();

    notifyAll();
  }

  public synchronized Pezzo[] estrai() throws InterruptedException {
    int[] indici;
    indici = validaEstrazione();

    while (indici == null) {
      wait();
      indici = validaEstrazione();
    }

    System.out.println("--- Operazione di estrazione ---");
    System.out.print("Pre: ");
    stampa();

    Pezzo[] pezziRes = new Pezzo[indici.length];

    // prima estrai
    for (int idx : indici) {
      pezziRes[idx] = pezzi.get(idx);
    }
    // poi rimuovi
    for (Pezzo p : pezziRes) {
      pezzi.remove(p);
    }

    System.out.print("Estratti pezzi: [");
    for (int i = 0; i < pezziRes.length; i++) {
      System.out.print(pezziRes[i]);
      if (i != pezziRes.length - 1) {
        System.out.print(", ");
      }
    }
    System.out.println("]");
    System.out.print("Post: ");
    stampa();
    System.out.println();

    notifyAll();
    return pezziRes;
  }

  public synchronized int controlla() {
    System.out.println("--- Operazione di controllo ---");
    System.out.print("Pre: ");
    stampa();

    List<Pezzo> daRimuovere = new ArrayList<>();

    // prima colleziona
    for (Pezzo p : pezzi) {
      if (p.getQualita() < Q_THRESH) {
        daRimuovere.add(p);
      }
    }

    // poi rimuovi
    for (Pezzo p : daRimuovere) {
      pezzi.remove(p);
    }

    System.out.println("Controllo effettuato, rimossi " + daRimuovere.size() + " pezzi");

    System.out.print("Post: ");
    stampa();
    System.out.println();

    notifyAll();
    return daRimuovere.size();
  }
}
