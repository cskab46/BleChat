package mobi.mateam.blechat.bus.event;

public class Event {

  public static final int ON_DEVICE_CLICK = 100;
  public static final int NEW_MESSAGE = 200;
  public static final int CHAT_STATUS_CHANGED = 300;

  public int id = -1;

  Event(int id) {
    this.id = id;
  }








}