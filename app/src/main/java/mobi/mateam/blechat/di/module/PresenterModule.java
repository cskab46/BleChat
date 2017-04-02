package mobi.mateam.blechat.di.module;

import dagger.Module;
import dagger.Provides;
import mobi.mateam.blechat.ble.ChatProvider;
import mobi.mateam.blechat.bus.EventBus;
import mobi.mateam.blechat.model.repository.ChatRepository;
import mobi.mateam.blechat.presenter.impl.ChatPresenterImpl;
import mobi.mateam.blechat.presenter.impl.ConnectionPresenterImpl;
import mobi.mateam.blechat.presenter.impl.MainPresenterImpl;
import mobi.mateam.blechat.presenter.interfaces.ChatPresenter;
import mobi.mateam.blechat.presenter.interfaces.ConnectionPresenter;
import mobi.mateam.blechat.presenter.interfaces.MainPresenter;

@Module public class PresenterModule {

  @Provides MainPresenter provideMainPresenter(EventBus eventBus) {
    return new MainPresenterImpl(eventBus);
  }

  @Provides ConnectionPresenter provideConnectionPresenter(EventBus eventBus, ChatProvider chatProvider) {
    return new ConnectionPresenterImpl(eventBus, chatProvider);
  }

  @Provides ChatPresenter provideChatPresenter(EventBus eventBus, ChatProvider chatProvider, ChatRepository chatRepository) {
    return new ChatPresenterImpl(eventBus, chatProvider, chatRepository);
  }
}
