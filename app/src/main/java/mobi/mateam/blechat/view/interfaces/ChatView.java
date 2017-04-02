package mobi.mateam.blechat.view.interfaces;

import java.util.List;
import mobi.mateam.blechat.model.pojo.Message;

public interface ChatView extends MvpView {

  void showChatMessages(List<Message> messages);

  void addChatMessage(Message message);

  void setIsSendButtonEnabled(boolean connected);
}
