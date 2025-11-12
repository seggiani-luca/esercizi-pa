package luca.parking;

public class ParcheggioPienoException extends Exception {

  public ParcheggioPienoException() {
    super();
  }

  public ParcheggioPienoException(String mess) {
    super(mess);
  }
}
