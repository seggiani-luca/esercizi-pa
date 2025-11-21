package luca.proc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public abstract class FileProcessor {

  private class FileProcessorWorker extends Thread {

    private boolean dir; // false: tail, true: head

    public FileProcessorWorker(boolean dir) {
      this.dir = dir;
    }

    private boolean processBlock(int i) {
      boolean isDone = testDone(i);
      if (isDone) {
        return false;
      }

      byte res = doProcessBlock(Arrays.copyOfRange(bytes, i * blockSize, (i + 1) * blockSize));
      blocks[i] = res;

      System.out.println("Worker " + Thread.currentThread().getName() + " ha processato blocco " + i);

      return true;
    }

    @Override
    public void run() {
      int len = done.length;
      if (dir) {
        for (int i = 0; i < len; i++) {
          if (!processBlock(i)) {
            return;
          }
        }
      } else {
        for (int i = len - 1; i >= 0; i--) {
          if (!processBlock(i)) {
            return;
          }
        }
      }
    }
  }

  private final byte[] bytes;

  private int blockSize;
  private byte[] blocks;
  private boolean[] done;

  private synchronized boolean testDone(int i) {
    if (!done[i]) {
      done[i] = true;
      return false;
    }

    return true;
  }

  public FileProcessor(String fileName) throws IOException {
    Path filePath = Path.of(fileName);
    bytes = Files.readAllBytes(filePath);
  }

  public byte[] process(int blockSize) throws InterruptedException {
    if (bytes.length % blockSize != 0) {
      throw new IllegalArgumentException("Impossibile elaborare file di dimensione " + bytes.length + " byte con dimensione di blocco di " + blockSize + " byte");
    }

    this.blockSize = blockSize;
    int len = bytes.length / blockSize;
    blocks = new byte[len];
    done = new boolean[len];

    FileProcessorWorker head = new FileProcessorWorker(true);
    FileProcessorWorker tail = new FileProcessorWorker(false);

    System.out.println("Avvio worker " + head.getName() + " e " + tail.getName() + " per lavorare su " + len + " blocchi");

    head.start();
    tail.start();

    head.join();
    tail.join();

    return blocks;
  }

  protected abstract byte doProcessBlock(byte[] block);
}
