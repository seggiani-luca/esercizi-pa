package luca.gimp;

import java.util.Arrays;

public abstract class ImageProcessor {

  private class ImageWorker extends Thread {

    private final int[] set;

    public ImageWorker(int[] set) {
      this.set = set;

      System.out.println("=> Creato worker su insieme: " + Arrays.toString(set));
    }

    @Override
    public void run() {
      for (int row : set) {
        for (int c = 0; c < image.getCols(); c++) {
          int r = image.getChannel(Image.Channel.Red)[row][c];
          int g = image.getChannel(Image.Channel.Green)[row][c];
          int b = image.getChannel(Image.Channel.Blue)[row][c];
          int[] in = {r, g, b};

          int[] out = doProcess(in);

          image.getChannel(Image.Channel.Red)[row][c] = out[Image.Channel.Red];
          image.getChannel(Image.Channel.Green)[row][c] = out[Image.Channel.Green];
          image.getChannel(Image.Channel.Blue)[row][c] = out[Image.Channel.Blue];
        }
      }
    }
  }

  private Image image;

  public ImageProcessor(String f) {
    image = new Image(f);
  }

  protected abstract int[] doProcess(int[] in);

  public synchronized void process(int n) throws InterruptedException {
    ImageWorker[] workers = new ImageWorker[n];
    int interval = image.getRows() / n;

    // prima gli n - 1
    for (int i = 0; i < n - 1; i++) {
      int[] set = new int[interval];
      for (int j = 0; j < interval; j++) {
        set[j] = i * interval + j;
      }

      workers[i] = new ImageWorker(set);
    }

    // poi l'ultimo (prende il resto)
    int left = image.getRows() - interval * (n - 1);
    int[] set = new int[left];
    for (int j = 0; j < left; j++) {
      set[j] = (n - 1) * interval + j;
    }

    workers[n - 1] = new ImageWorker(set);

    for (int i = 0; i < n; i++) {
      workers[i].start();
    }

    for (int i = 0; i < n; i++) {
      workers[i].join();
    }
  }

  public synchronized Image getImage() {
    return image;
  }
}
