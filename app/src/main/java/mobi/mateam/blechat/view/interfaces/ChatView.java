package mobi.mateam.blechat.view.interfaces;

import java.util.ArrayList;
import mobi.mateam.blechat.model.pojo.Message;

public interface ChatView extends MvpView {

  void showChatMessages(ArrayList<Message> messages);

  void addChatMessage(Message message);

  void setIsSendButtonEnabled(boolean connected);
}
