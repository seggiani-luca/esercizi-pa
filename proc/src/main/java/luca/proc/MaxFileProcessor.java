package luca.proc;

import java.io.IOException;

public class MaxFileProcessor extends FileProcessor {

  public MaxFileProcessor(String fileName) throws IOException {
    super(fileName);
  }

  @Override
  protected byte doProcessBlock(byte[] block) {
    byte max = block[0];

    for (int i = 1; i < block.length; i++) {
      if (block[i] > max) {
        max = block[i];
      }
    }

    return max;
  }
}
