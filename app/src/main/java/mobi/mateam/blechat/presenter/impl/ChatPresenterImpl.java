package mobi.mateam.blechat.presenter.impl;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import mobi.mateam.blechat.eventBus.EventBus;
import mobi.mateam.blechat.model.pojo.Message;
import mobi.mateam.blechat.model.repository.ChatRepository;
import mobi.mateam.blechat.presenter.interfaces.ChatPresenter;
import mobi.mateam.blechat.view.interfaces.ChatView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static com.trello.rxlifecycle.android.ActivityEvent.DESTROY;
import static com.trello.rxlifecycle.android.ActivityEvent.PAUSE;

public class ChatPresenterImpl extends BasePresenterImpl<ChatView> implements ChatPresenter {
  private final EventBus eventBus;
  private final RxBleClient rxBleClient;
  private final ChatRepository chatRepository;
  private RxBleDevice bleDevice;
  private Subscription connectionSubscription;

  public ChatPresenterImpl(EventBus eventBus, RxBleClient rxBleClient, ChatRepository chatRepository) {
    this.eventBus = eventBus;
    this.rxBleClient = rxBleClient;
    this.chatRepository = chatRepository;
  }

  @Override public void sendMessage(Message message) {

  }

  @Override public void onNewIncomingMessage(Message message) {

  }

  @Override public void setMacAddress(String macAddress) {
    initConnection(macAddress);
    connect();
  }

  private void connect() {
    RxAppCompatActivity activityContext = (RxAppCompatActivity) getView().getActivityContext();

    connectionSubscription = bleDevice.establishConnection(getView().getAppContext(), false)
        .compose(activityContext.bindUntilEvent(PAUSE))
        .observeOn(AndroidSchedulers.mainThread())
        .doOnUnsubscribe(this::clearSubscription)
        .subscribe(this::onConnectionReceived, this::onConnectionFailure);
  }

  private void clearSubscription() {
    connectionSubscription = null;
    updateUI();
  }

  private void initConnection(String macAddress) {
   // setTitle(getString(R.string.mac_address, macAddress));
    bleDevice = rxBleClient.getBleDevice(macAddress);
    setConnectionListener();
  }

  private boolean isConnected() {
    return bleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED;
  }

  private void onConnectionFailure(Throwable throwable) {
    Timber.e(throwable);
    if (isViewAttached()){
      getView().showMessage("Connection error: " + throwable);
    }
  }

  private void onConnectionReceived(RxBleConnection connection) {
    Timber.d("Connection received");
    if (isViewAttached()){
      getView().showMessage("Connection received");
    }
  }


  private void setConnectionListener() {
    // How to listen for connection state changes
    RxAppCompatActivity activityContext = (RxAppCompatActivity) getView().getActivityContext();
    bleDevice.observeConnectionStateChanges()
        .compose(activityContext.bindUntilEvent(DESTROY))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onConnectionStateChange);
  }

  private void onConnectionStateChange(RxBleConnection.RxBleConnectionState newState){
   // connectionStateView.setText(newState.toString());
    updateUI();
  }

  private void updateUI() {
    if (isViewAttached()){
        getView().setIsSendButtonEnabled(isConnected());
    }
  }


}
