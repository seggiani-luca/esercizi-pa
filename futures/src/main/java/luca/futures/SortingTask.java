package luca.futures;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SortingTask {

  private final List<Integer> list;

  private final long scheduledTime;

  public long getScheduledTime() {
    return scheduledTime;
  }

  private final int allowedDelay;

  public int getAllowedDelay() {
    return allowedDelay;
  }

  private Exception exception = new Exception("Error performing SortingTask");

  public void setException(Exception e) {
    exception = e;
  }

  private String fileName = null;
  private Exception thrownException = null;

  public boolean isComplete() {
    return fileName != null;
  }

  public boolean isError() {
    return thrownException != null;
  }

  public SortingTask(List<Integer> list, long scheduledTime, int allowedDelay) {
    this.list = list;
    this.scheduledTime = scheduledTime;
    this.allowedDelay = allowedDelay;
  }

  public synchronized void execute() {
    System.out.println("Executing SortingTask");
    list.sort(null);

    fileName = "lists/sort_" + scheduledTime + "_" + allowedDelay + ".txt";
    File listFile = new File(fileName);

    try {
      if (!listFile.createNewFile()) {
        throw new IOException("Tried writing to already existing file");
      }

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(listFile))) {
        for (Integer i : list) {
          writer.write(i);
          writer.write(" ");
        }
      }
    } catch (IOException e) {
      thrownException = new RuntimeException("Couldn't write result to file. " + e.getMessage());
    }

    notify();
  }

  public synchronized String getResult() throws Exception {
    if (!isComplete()) {
      try {
        wait();
      } catch (InterruptedException e) {
        throw new RuntimeException("getResult() was interrupted");
      }
    }

    if (isError()) {
      throw thrownException;
    }

    boolean late = System.currentTimeMillis() > scheduledTime + allowedDelay;
    if (late) {
      throw new Exception("SortingTask was late");
    }

    return fileName;
  }

  public void print() {
    System.out.println("\tList: (" + list.size() + " elements)");
    System.out.println("\tScheduled time: " + scheduledTime);
    System.out.println("\tAllowed delay: " + allowedDelay);
    System.out.println("\tDone?: " + isComplete());
    System.out.println("\tError?: " + isError());
  }
}
