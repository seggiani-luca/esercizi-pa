package luca.gimp;

public class GrayscaleImageProcessor extends ImageProcessor {

  public GrayscaleImageProcessor(String f) {
    super(f);
  }

  @Override
  protected int[] doProcess(int[] in) {
    int avg = (in[Image.Channel.Red] + in[Image.Channel.Green] + in[Image.Channel.Blue]) / 3;
    int[] res = new int[3];
    res[Image.Channel.Red] = res[Image.Channel.Green] = res[Image.Channel.Blue] = avg;

    return res;
  }
}
