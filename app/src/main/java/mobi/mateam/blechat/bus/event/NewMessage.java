package mobi.mateam.blechat.bus.event;

import mobi.mateam.blechat.model.pojo.Message;

public class NewMessage extends Event{
  public final Message message;

  public NewMessage(Message message) {
    super(Event.NEW_MESSAGE);
    this.message = message;
  }
}
