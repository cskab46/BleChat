package mobi.mateam.blechat.presenter;

import com.polidea.rxandroidble.RxBleClient;
import mobi.mateam.blechat.eventBus.EventBus;
import mobi.mateam.blechat.presenter.interfaces.MainPresenter;
import mobi.mateam.blechat.view.interfaces.MainView;

public class MainPresenterImpl extends BasePresenterImpl<MainView> implements MainPresenter {

  private final RxBleClient rxBleClient;
  private final EventBus eventBus;

  public MainPresenterImpl(EventBus eventBus, RxBleClient rxBleClient) {
    this.eventBus = eventBus;
    this.rxBleClient = rxBleClient;
  }

  @Override public void onConnectionsClick() {

  }

  @Override public void onChatClick() {

  }
}
