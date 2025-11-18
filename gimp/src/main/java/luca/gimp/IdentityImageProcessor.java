package luca.gimp;

public class IdentityImageProcessor extends ImageProcessor {

  public IdentityImageProcessor(String f) {
    super(f);
  }

  @Override
  protected int[] doProcess(int[] in) {
    return in;
  }
}
