package mobi.mateam.blechat.presenter.interfaces;

import mobi.mateam.blechat.model.pojo.Message;
import mobi.mateam.blechat.view.interfaces.ChatView;

public interface ChatPresenter extends BasePresenter<ChatView> {

  void sendMessage(Message message);

  void onNewIncomingMessage(Message message);
}
