package luca.mercante;

public class CartaNonDisponibileException extends Exception {

  public CartaNonDisponibileException() {
    super();
  }

  public CartaNonDisponibileException(String mess) {
    super(mess);
  }
}
