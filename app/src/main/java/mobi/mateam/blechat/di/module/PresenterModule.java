package mobi.mateam.blechat.di.module;

import com.polidea.rxandroidble.RxBleClient;
import dagger.Module;
import dagger.Provides;
import mobi.mateam.blechat.eventBus.EventBus;
import mobi.mateam.blechat.model.repository.ChatRepository;
import mobi.mateam.blechat.presenter.impl.ChatPresenterImpl;
import mobi.mateam.blechat.presenter.impl.ConnectionPresenterImpl;
import mobi.mateam.blechat.presenter.impl.MainPresenterImpl;
import mobi.mateam.blechat.presenter.interfaces.ChatPresenter;
import mobi.mateam.blechat.presenter.interfaces.ConnectionPresenter;
import mobi.mateam.blechat.presenter.interfaces.MainPresenter;

@Module public class PresenterModule {

  @Provides MainPresenter provideMainPresenter(EventBus eventBus, RxBleClient rxBleClient){
      return new MainPresenterImpl(eventBus, rxBleClient);
  }

  @Provides ConnectionPresenter provideConnectionPresenter(EventBus eventBus, RxBleClient rxBleClient){
    return new ConnectionPresenterImpl(eventBus, rxBleClient);
  }

  @Provides ChatPresenter provideChatPresenter(EventBus eventBus, RxBleClient rxBleClient, ChatRepository chatRepository){
    return new ChatPresenterImpl(eventBus, rxBleClient, chatRepository);
  }
}
