package luca.passwd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Cracker {

  public static String hash(String clear) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] digest = md.digest(clear.getBytes());

      // convert bytes to hex
      StringBuilder hexString = new StringBuilder();
      for (byte b : digest) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public static class CrackerWorker extends Thread {

    private Cracker crack;
    private String hash;
    private String[] dict;

    private boolean stop = false;

    public void earlyStop() {
      stop = true;
    }

    public CrackerWorker(Cracker crack, String hash, String[] dict, int idx) {
      this.crack = crack;
      this.hash = hash;
      this.dict = dict;

//      System.out.println("\t=> Lavoratore " + idx);
//      System.out.println("\t\tSotto-dizionario: " + Arrays.toString(dict));
    }

    @Override
    public void run() {
      for (String str : dict) {
        if (stop) {
          return;
        }

        if (Cracker.hash(str).equals(hash)) {
          crack.clear = str;
          crack.stopWorkers();
          return;
        }
      }
    }
  }

  private String hash;
  private String clear = null;

  CrackerWorker[] workerThreads;

  public Cracker(String hash) {
    this.hash = hash;
  }

  private void stopWorkers() {
    if (workerThreads == null) {
      throw new NullPointerException("Cannot stop uninitialized workers");
    }

    for (CrackerWorker workerThread : workerThreads) {
      if (workerThread != null) {
        workerThread.earlyStop();
      }
    }
  }

  public synchronized void check(String[] dict, int workers) throws InterruptedException {
//    System.out.println("--- Richiesta computazione ---");
//    System.out.println("\tDizionario: " + Arrays.toString(dict));
//    System.out.println("\tNumero thread: " + workers);
//    System.out.println("\tThread:");

    int interval = dict.length / workers;
    workerThreads = new CrackerWorker[workers];

    for (int i = 0; i < workers - 1; i++) {
      String[] subDict = Arrays.copyOfRange(dict, interval * i, interval * (i + 1));
      workerThreads[i] = new CrackerWorker(this, hash, subDict, i);
      workerThreads[i].start();
    }

    // ultimo prende il resto
    String[] subDict = Arrays.copyOfRange(dict, interval * (workers - 1), dict.length);
    workerThreads[workers - 1] = new CrackerWorker(this, hash, subDict, workers - 1);
    workerThreads[workers - 1].start();

    for (int i = 0; i < workers; i++) {
      workerThreads[i].join();
    }
  }

  public synchronized String getString() {
    return clear;
  }
}
