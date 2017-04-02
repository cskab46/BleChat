package mobi.mateam.blechat.model.pojo;

public class MessageBuilder {
  private String id;
  private String chatId;
  private String senderName;
  private String text;
  private long time;
  private boolean isIncoming;

  public MessageBuilder setId(String id) {
    this.id = id;
    return this;
  }

  public MessageBuilder setChatId(String chatId) {
    this.chatId = chatId;
    return this;
  }

  public MessageBuilder setSenderName(String senderName) {
    this.senderName = senderName;
    return this;
  }

  public MessageBuilder setText(String text) {
    this.text = text;
    return this;
  }

  public MessageBuilder setTime(long time) {
    this.time = time;
    return this;
  }

  public MessageBuilder setIsIncoming(boolean isIncoming) {
    this.isIncoming = isIncoming;
    return this;
  }

  public Message createMessage() {
    return new Message(id, chatId, senderName, text, time, isIncoming);
  }
}