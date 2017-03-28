package mobi.mateam.blechat.presenter.impl;

import com.polidea.rxandroidble.RxBleClient;
import mobi.mateam.blechat.eventBus.Event;
import mobi.mateam.blechat.eventBus.EventBus;
import mobi.mateam.blechat.eventBus.OnDeviceClickEvent;
import mobi.mateam.blechat.presenter.interfaces.MainPresenter;
import mobi.mateam.blechat.view.PhoneNavigator;
import mobi.mateam.blechat.view.interfaces.MainView;
import mobi.mateam.blechat.view.interfaces.Navigator;
import rx.Subscriber;
import timber.log.Timber;

public class MainPresenterImpl extends BasePresenterImpl<MainView> implements MainPresenter {

  private final RxBleClient rxBleClient;
  private final EventBus eventBus;
  private Navigator navigator;
  private Subscriber<Event> eventSubscriber;

  public MainPresenterImpl(EventBus eventBus, RxBleClient rxBleClient) {
    this.eventBus = eventBus;
    this.rxBleClient = rxBleClient;
  }

  @Override public void attachView(MainView mainView) {
    super.attachView(mainView);
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
          case Event.ON_DEVICE_CLICK:
            startChatView((OnDeviceClickEvent) event);
            break;
        }
      }
    };
    eventBus.observeEvents(Event.class).subscribe(eventSubscriber);
  }

  private void startChatView(OnDeviceClickEvent event) {
    navigator.showChatView(event.bleScanResult.getBleDevice().getMacAddress());
  }

  @Override public void onConnectionsClick() {

  }

  @Override public void onChatClick() {

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
