package mobi.mateam.blechat.model.pojo;

public class Message {

  public final String id;
  public String chatId;
  public String senderName;
  public String text;
  public long time;
  public boolean isIncoming;




  public Message(String id, String chatId, String senderName, String text, long time, boolean isIncoming) {
    this.id = id;
    this.chatId = chatId;
    this.senderName = senderName;
    this.text = text;
    this.time = time;
    this.isIncoming = isIncoming;
  }


}
