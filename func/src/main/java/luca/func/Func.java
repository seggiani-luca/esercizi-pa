package luca.func;

import static java.lang.System.currentTimeMillis;

public class Func {

  public static final long N = 1_000_000_000;
  public static final int T = 4;

  public static long eval(long i) {
    return i * i + 3 * i + 5; // i^2 + 3i + 5
  }

  public static void main(String[] args) {
    // seq
    Sequential seq = new Sequential(N);

    long seqBeg = currentTimeMillis();
    seq.run();
    seq.attendiRisultato();
    long seqEnd = currentTimeMillis();

    System.out.println("seq.somma: " + seq.somma);
    long seqTime = seqEnd - seqBeg;
    System.out.println("seq.tempo: " + seqTime + " ms");

    // cur
    Concurrent cur = new Concurrent(N, T);

    long curBeg = currentTimeMillis();
    cur.run();
    cur.attendiRisultato();
    long curEnd = currentTimeMillis();

    System.out.println("cur.somma: " + cur.somma);
    long curTime = curEnd - curBeg;
    System.out.println("cur.tempo: " + curTime + " ms");

    System.out.println();

    double speedup = ((double) seqTime / curTime) * 100;
    System.out.println("Speedup: " + String.format("%.2f", speedup) + "%");
  }
}
