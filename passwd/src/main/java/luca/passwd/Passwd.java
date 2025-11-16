package luca.passwd;

import java.util.Scanner;

public class Passwd {

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);

    while (true) {
      System.out.print("$ ");
      System.out.flush();
      String line = scan.nextLine().trim();
      args = line.split("\\s+");

      if (args.length < 1) {
        System.out.println("Uso: [hash <clear> | brute <hash> <numThread> <dict>...] | quit");
        continue;
      }

      switch (args[0]) {
        case "hash" -> {
          if (args.length < 2) {
            System.err.println("Uso: hash <clear>");
            continue;
          }

          String clear = args[1];

          if (args.length != 2) {
            System.err.println("Spazzatura dopo " + clear);
            continue;
          }

          String hash = Cracker.hash(clear);
          System.out.println(clear + " => " + hash);
        }
        case "brute" -> {
          if (args.length < 4) {
            System.err.println("Uso: brute <hash> <numThread> <dict>...]");
            continue;
          }

          String hash = args[1];
          int numThread;
          try {
            numThread = Integer.parseInt(args[2]);
          } catch (NumberFormatException e) {
            System.err.println("Numero thread invalido. " + e.getMessage());
            continue;
          }

          String[] dict = new String[args.length - 3];
          for (int i = 0; i < dict.length; i++) {
            dict[i] = args[i + 3];
          }

          Cracker crack = new Cracker(hash);
          try {
            crack.check(dict, numThread);
          } catch (InterruptedException e) {
            System.err.println("Processo interotto durante la computazione");
            continue;
          }
          String clear = crack.getString();

          if (clear == null) {
            System.out.println("Nessuna corrispondenza");
          } else {
            System.out.println(clear + " => " + hash);
          }
        }
        case "quit" -> {
          System.out.println("Ciao!");
          System.exit(0);
        }
        default -> {
          System.err.println("Opzione ignota \"" + args[0] + "\"");
        }
      }
    }
  }
}
