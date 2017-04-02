package mobi.mateam.blechat.presenter.impl;

import android.bluetooth.BluetoothDevice;
import android.os.Build;
import java.util.Calendar;
import java.util.UUID;
import mobi.mateam.blechat.ble.ChatProvider;
import mobi.mateam.blechat.bus.EventBus;
import mobi.mateam.blechat.bus.event.ChatStatusChange;
import mobi.mateam.blechat.bus.event.Event;
import mobi.mateam.blechat.bus.event.NewMessage;
import mobi.mateam.blechat.model.pojo.Message;
import mobi.mateam.blechat.model.pojo.MessageBuilder;
import mobi.mateam.blechat.model.repository.ChatRepository;
import mobi.mateam.blechat.presenter.interfaces.ChatPresenter;
import mobi.mateam.blechat.view.interfaces.ChatView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static mobi.mateam.blechat.bus.event.ChatStatusChange.DEVICE_CONNECTED;
import static mobi.mateam.blechat.bus.event.ChatStatusChange.DEVICE_DISCONNECTED;
import static mobi.mateam.blechat.bus.event.ChatStatusChange.SERVICE_STARTED;
import static mobi.mateam.blechat.bus.event.ChatStatusChange.SERVICE_STOPED;

public class ChatPresenterImpl extends BasePresenterImpl<ChatView> implements ChatPresenter {

  private final EventBus eventBus;
  private final ChatRepository chatRepository;
  private ChatProvider chatProvider;
  private Subscriber<Event> eventSubscriber;
  private boolean isConnected;

  @Override public void attachView(ChatView chatView) {
    super.attachView(chatView);
    subscribeToEventBas();

  }



  private void subscribeToEventBas() {
    eventSubscriber = new Subscriber<Event>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {
        Timber.e(e);
      }

      @Override public void onNext(Event event) {
        switch (event.id) {
          case Event.NEW_MESSAGE:
            Message message = ((NewMessage) event).message;
            onNewIncomingMessage(message);
            break;
          case Event.CHAT_STATUS_CHANGED:
            ChatStatusChange chatStatusChange = (ChatStatusChange) event;
            onChatStatusChanged(chatStatusChange);
        }
      }
    };
    eventBus.observeEvents(Event.class).observeOn(AndroidSchedulers.mainThread()).subscribe(eventSubscriber);
  }

  private void onChatStatusChanged(ChatStatusChange chatStatusChange) {
    switch (chatStatusChange.statusId){
      case SERVICE_STARTED:
        isConnected = true;
        break;
      case  SERVICE_STOPED:
        isConnected = false;
        break;
      case DEVICE_CONNECTED:
        isConnected = true;

        break;
      case DEVICE_DISCONNECTED:
        isConnected = false;
        break;

    }
    updateUI();
  }

  public ChatPresenterImpl(EventBus eventBus, ChatProvider chatProvider, ChatRepository chatRepository) {
    this.eventBus = eventBus;
    this.chatProvider = chatProvider;
    this.chatRepository = chatRepository;
  }

  @Override public void connectToDevice(BluetoothDevice bluetoothDevice) {
    chatProvider.connectToChatOnDevice(bluetoothDevice);
    restoreChatHistory("Mock chat id");
  }

  private void restoreChatHistory(String chatID) {
    chatRepository.getMessagesByChatId("mock chat ID").observeOn(AndroidSchedulers.mainThread()).subscribe(messages -> {
      if (isViewAttached()){
        getView().showChatMessages(messages);
      }
    });
  }


  @Override public void startChatService() {
    restoreChatHistory("Mock chat id");
    try {
      chatProvider.startChatRoom();
    } catch (IllegalStateException error){
      if (isViewAttached()){
        getView().showError(error);
      }
    }

  }

  @Override public void sendMessage(String text) {
    Message message =
        new MessageBuilder()
            .setId(UUID.randomUUID().toString())
            .setIsIncoming(false)
            .setSenderName(Build.DEVICE)
            .setTime(Calendar.getInstance().getTimeInMillis())
            .setText(text).createMessage();

    chatProvider.sendMessage(message);
    chatRepository.saveMessage(message);
    if(isViewAttached()){
      getView().addChatMessage(message);
    }
  }


  @Override public void onNewIncomingMessage(Message message) {
    if (isViewAttached()) {
      getView().addChatMessage(message);
    }
    chatRepository.saveMessage(message);
  }

  private void updateUI() {
    if (isViewAttached()) {
      getView().setIsSendButtonEnabled(isConnected());
    }
  }

  @Override public void detachView() {
    super.detachView();
    chatProvider.disconnect();
    eventSubscriber.unsubscribe();
  }

  private boolean isConnected() {
    return isConnected;
  }
}
