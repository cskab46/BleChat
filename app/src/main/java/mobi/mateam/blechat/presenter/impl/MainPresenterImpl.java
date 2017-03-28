package mobi.mateam.blechat.presenter.impl;

import com.polidea.rxandroidble.RxBleClient;
import mobi.mateam.blechat.eventBus.EventBus;
import mobi.mateam.blechat.presenter.interfaces.MainPresenter;
import mobi.mateam.blechat.view.PhoneNavigator;
import mobi.mateam.blechat.view.interfaces.MainView;
import mobi.mateam.blechat.view.interfaces.Navigator;

public class MainPresenterImpl extends BasePresenterImpl<MainView> implements MainPresenter {

  private final RxBleClient rxBleClient;
  private final EventBus eventBus;
  private Navigator navigator;

  public MainPresenterImpl(EventBus eventBus, RxBleClient rxBleClient) {
    this.eventBus = eventBus;
    this.rxBleClient = rxBleClient;
  }

  @Override public void attachView(MainView mainView) {
    super.attachView(mainView);
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
}
