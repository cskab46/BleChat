package mobi.mateam.blechat.model.pojo;

public class Message {

  public final String id;
  public String chatId;
  public String senderName;
  public String text;
  public long time;
  public boolean isIncoming;


  public Message(String id) {
    this.id = id;
  }

}
