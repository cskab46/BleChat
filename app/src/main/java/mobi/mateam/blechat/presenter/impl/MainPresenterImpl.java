package mobi.mateam.blechat.presenter.impl;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import mobi.mateam.blechat.bus.EventBus;
import mobi.mateam.blechat.bus.event.ChatStatusChange;
import mobi.mateam.blechat.bus.event.Event;
import mobi.mateam.blechat.bus.event.OnDeviceClickEvent;
import mobi.mateam.blechat.presenter.interfaces.MainPresenter;
import mobi.mateam.blechat.view.PhoneNavigator;
import mobi.mateam.blechat.view.interfaces.MainView;
import mobi.mateam.blechat.view.interfaces.Navigator;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static mobi.mateam.blechat.bus.event.ChatStatusChange.DEVICE_CONNECTED;
import static mobi.mateam.blechat.bus.event.ChatStatusChange.DEVICE_DISCONNECTED;
import static mobi.mateam.blechat.bus.event.ChatStatusChange.SERVICE_STARTED;
import static mobi.mateam.blechat.bus.event.ChatStatusChange.SERVICE_STOPED;

public class MainPresenterImpl extends BasePresenterImpl<MainView> implements MainPresenter {

  private final EventBus eventBus;

  private Navigator navigator;
  private Subscriber<Event> eventSubscriber;

  public MainPresenterImpl(EventBus eventBus) {

    this.eventBus = eventBus;
  }

  @Override public void attachView(MainView mainView) {
    super.attachView(mainView);
    subscribeToEventBas();
  }

  @SuppressWarnings("unchecked") private void subscribeToEventBas() {
    eventSubscriber = new Subscriber<Event>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {
        Timber.e(e);
      }

      @Override public void onNext(Event event) {
        switch (event.id) {
          case Event.ON_DEVICE_CLICK:
            startChatView((OnDeviceClickEvent) event);
            break;
          case Event.CHAT_STATUS_CHANGED:
             onConnectionChange((ChatStatusChange) event);
        }
      }
    };
    eventBus.observeEvents(Event.class).observeOn(AndroidSchedulers.mainThread()).subscribe(eventSubscriber);
  }

  private void onConnectionChange(ChatStatusChange event) {
    String title = "";
    String subTitle = "";
    switch (event.statusId){
      case DEVICE_CONNECTED:
          title = "Connected";
          subTitle = getSubTitleString(event);
        break;
      case DEVICE_DISCONNECTED:
        title = "Disconnected";
        subTitle = getSubTitleString(event);
        break;
      case  SERVICE_STARTED:
        title = "Server/Start";
        break;
      case SERVICE_STOPED:
        title = "Server/Stop";
    }
    getView().showTitle(title);
    getView().showSubTitle(subTitle);
  }

  private String getSubTitleString(ChatStatusChange event) {
    return event.device!= null ? TextUtils.isEmpty(event.device.getName()) ? event.device.getAddress(): event.device.getName():"";
  }

  /**
   * Start new ChatFragment
   * @param event if not null - chat will be started in Client Mode, if event is null - in Server Mode
   */
  private void startChatView(OnDeviceClickEvent event) {
    BluetoothDevice device =null;
    if (event.bleScanResult != null) {
      device =event.bleScanResult.getDevice();
    }
    navigator.showChatView(device);
  }

  @Override public Navigator getNavigator() {
    if (navigator == null) {
      navigator = new PhoneNavigator();
    }
    return navigator;
  }

  @Override public void detachView() {
    super.detachView();
    eventSubscriber.unsubscribe();
  }
}
