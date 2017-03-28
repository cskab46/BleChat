package mobi.mateam.blechat.presenter.impl;

import com.polidea.rxandroidble.RxBleClient;
import mobi.mateam.blechat.eventBus.EventBus;
import mobi.mateam.blechat.model.pojo.Message;
import mobi.mateam.blechat.model.repository.ChatRepository;
import mobi.mateam.blechat.presenter.interfaces.ChatPresenter;
import mobi.mateam.blechat.view.interfaces.ChatView;

public class ChatPresenterImpl extends BasePresenterImpl<ChatView> implements ChatPresenter {
  private final EventBus eventBus;
  private final RxBleClient rxBleClient;
  private final ChatRepository chatRepository;

  public ChatPresenterImpl(EventBus eventBus, RxBleClient rxBleClient, ChatRepository chatRepository) {
    this.eventBus = eventBus;
    this.rxBleClient = rxBleClient;
    this.chatRepository = chatRepository;
  }

  @Override public void sendMessage(Message message) {

  }

  @Override public void onNewIncomingMessage(Message message) {

  }
}
