package luca.gimp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Gimp {

  public static void main(String[] args) {
    String image = null;
    try {
      Path imagePath = Paths.get("image.txt");
      image = String.join("\n", Files.readAllLines(imagePath));
    } catch (IOException e) {
      System.err.println("Errore caricamento immagine. " + e.getMessage());
      System.exit(1);
    }

    ImageProcessor proc = new GrayscaleImageProcessor(image);
    try {
      proc.process(2);
    } catch (InterruptedException e) {
      System.err.println("Elaborazione immagine interrotta");
      System.exit(1);
    }
    System.out.println("\nElaborazione:\n" + proc.getImage());
  }
}
