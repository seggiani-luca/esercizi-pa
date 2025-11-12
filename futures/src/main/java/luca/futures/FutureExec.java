package luca.futures;

import java.util.ArrayList;
import java.util.List;

public class FutureExec {

  static class TaskWrapper {

    private final SortingTask task;
    private long timeLeft;
    private TaskWrapper next;

    public TaskWrapper(SortingTask task, long timeLeft) {
      this.task = task;
      this.timeLeft = timeLeft;
    }
  }

  class ExecThread extends Thread {

    public void run() {
      while (true) {
        try {
          sleep(TIME_QUANTUM);
        } catch (InterruptedException e) {
          throw new RuntimeException("FutureExec thread was interrupted");
        }

        if (root == null) {
          continue;
        }

        root.timeLeft -= TIME_QUANTUM;
        if (root.timeLeft < 0) {
          root.task.execute();
          root = root.next;
        }
      }
    }
  }

  static final long TIME_QUANTUM = 100;

  private TaskWrapper root = null;

  public FutureExec() {
    (new ExecThread()).start();
  }

  public synchronized void add(SortingTask task) {
    long timeLeft = task.getScheduledTime() - System.currentTimeMillis();

    TaskWrapper back = null;
    TaskWrapper fwd = root;

    while (fwd != null && fwd.timeLeft < timeLeft) {
      back = fwd;
      fwd = fwd.next;
      timeLeft -= back.timeLeft;
    }

    TaskWrapper wrap = new TaskWrapper(task, timeLeft);
    if (back == null) {
      root = wrap;
    } else {
      back.next = wrap;
    }

    wrap.next = fwd;

    if (fwd != null) {
      fwd.timeLeft -= timeLeft;
    }
  }

  public void print() {
    int i = 0;
    long totLeft = 0;
    TaskWrapper temp = root;

    while (temp != null) {
      System.out.println("-- Task " + i + " --");
      temp.task.print();

      totLeft += temp.timeLeft;
      System.out.println("\tTime left: " + temp.timeLeft + " (cumulative: " + totLeft + ")");

      temp = temp.next;
      i++;
    }
  }
}
