package mobi.mateam.blechat.model.pojo;

import java.util.ArrayList;

public class Chat {

  public final String chatId;
  public String senderId;
  public String reciverId;
  public ArrayList<String> messageIds;

  public Chat(String id, String senderId, String reciverId){
    this.chatId = id;
    this.senderId = senderId;
    this.reciverId = reciverId;
  }
}
