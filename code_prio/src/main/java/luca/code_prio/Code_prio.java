package luca.code_prio;

import java.util.Scanner;

class Produttore extends Thread {

  static int messIdx;

  Deposito dep;
  DepositoVecchio depV;
  double probUrg;

  public Produttore(Deposito dep, DepositoVecchio depV, double probUrg) {
    this.dep = dep;
    this.depV = depV;
    this.probUrg = probUrg;
  }

  public void run() {
    while (true) {
      try {
        sleep((long) (Math.random() * 1000));
      } catch (InterruptedException e) {
        throw new RuntimeException("Produttore svegliato");
      }

      Messaggio mess = new Messaggio("Messaggio " + messIdx++, Math.random() > probUrg);
      dep.inserisci(mess);

      try {
        sleep((long) 10);
      } catch (InterruptedException e) {
        throw new RuntimeException("run() produttore svegliata");
      }

      try {
        depV.inserisci(mess);
      } catch (InterruptedException e) {
        throw new RuntimeException("DepositoVecchio.inserisci() svegliato");
      }
    }
  }
}

class Consumatore extends Thread {

  Deposito dep;
  DepositoVecchio depV;
  int quanti;

  public Consumatore(Deposito dep, DepositoVecchio depV, int quanti) {
    this.dep = dep;
    this.depV = depV;
    this.quanti = quanti;
  }

  public void run() {
    while (true) {
      try {
        sleep((long) (Math.random() * 1000));
      } catch (InterruptedException e) {
        throw new RuntimeException("Produttore svegliato");
      }

      try {
        sleep((long) 10);
      } catch (InterruptedException e) {
        throw new RuntimeException("run() consumatore svegliata");
      }

      dep.estrai(quanti);
      try {
        depV.estrai(quanti);
      } catch (InterruptedException e) {
        throw new RuntimeException("DepositoVecchio.inserisci() svegliato");
      }
    }
  }
}

public class Code_prio {

  public static void main(String[] args) {
    Deposito dep = new Deposito();
    DepositoVecchio depV = new DepositoVecchio();

    for (int i = 0; i < 3; i++) {
      (new Produttore(dep, depV, 0.5)).start();
    }
    for (int i = 0; i < 2; i++) {
      (new Consumatore(dep, depV, 5)).start();
    }

//    Scanner scan = new Scanner(System.in);
//    while (true) {
//      String str = scan.nextLine().strip();
//      String tokens[] = str.split("\\s+");
//
//      if (tokens.length < 1) {
//        System.err.println("Formato: cons <quanti> | prod <messaggio> <priorità>");
//        continue;
//      }
//
//      switch (tokens[0]) {
//        case "c":
//        case "cons": {
//          if (tokens.length < 2) {
//            System.err.println("Formato: cons <quanti> ");
//            continue;
//          }
//
//          int quanti = Integer.parseInt(tokens[1]);
//          dep.estrai(quanti);
//
//          break;
//        }
//        case "p":
//        case "prod": {
//          if (tokens.length < 3) {
//            System.err.println("Formato: prod <messaggio> <priorità> ");
//            continue;
//          }
//
//          String messString = tokens[1];
//          boolean prio = tokens[2].equals("urg");
//
//          Messaggio mess = new Messaggio(messString, prio);
//          dep.inserisci(mess);
//
//          break;
//        }
//        default:
//          System.err.println("Token non valido: " + tokens[0]);
//      }
//    }
  }
}
