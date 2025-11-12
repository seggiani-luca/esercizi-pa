package luca.futures;

import java.util.ArrayList;
import java.util.List;

public class Futures {

  final static int LIST_SIZE = 10_000;
  final static int LIST_AMP = 1_000;

  private static List<Integer> creaLista() {
    List<Integer> l = new ArrayList<Integer>();
    for (int i = 0; i < LIST_SIZE; i++) {
      l.add((int) (Math.random() * LIST_AMP));
    }
    return l;
  }

  public static void main(String[] args) throws Exception {
    final long TIME_0 = 3_000;
    final int DELAY_0 = 1_000;
    final long TIME_1 = 2_000;
    final int DELAY_1 = 2_000;
    final long TIME_2 = 1_000;
    final int DELAY_2 = 3_000;

    FutureExec ef = new FutureExec();
    SortingTask c0 = new SortingTask(creaLista(), System.currentTimeMillis() + TIME_0, DELAY_0);
    ef.add(c0);
    SortingTask c1 = new SortingTask(creaLista(), System.currentTimeMillis() + TIME_1, DELAY_1);
    ef.add(c1);
    SortingTask c2 = new SortingTask(creaLista(), System.currentTimeMillis() + TIME_2, DELAY_2);
    ef.add(c2);

    try {
      String r0 = c0.getResult();
      System.out.println("File path: " + r0);
      String r1 = c1.getResult();
      System.out.println("File path: " + r1);
      String r2 = c2.getResult();
      System.out.println("File path: " + r2);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
  }
}
