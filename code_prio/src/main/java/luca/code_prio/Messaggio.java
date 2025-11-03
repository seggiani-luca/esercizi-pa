package luca.code_prio;

public class Messaggio {

  private String mess;
  private boolean urg;

  public String getMess() {
    return mess;
  }

  public boolean getUrg() {
    return urg;
  }

  public Messaggio(String mess, boolean urg) {
    this.mess = mess;
    this.urg = urg;
  }

  @Override
  public String toString() {
    return "{" + mess + ", " + (urg ? "Urgente" : "Normale") + "}";
  }
}
