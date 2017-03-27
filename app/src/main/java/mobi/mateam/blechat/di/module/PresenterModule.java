package mobi.mateam.blechat.di.module;

import com.polidea.rxandroidble.RxBleClient;
import dagger.Module;
import dagger.Provides;
import mobi.mateam.blechat.eventBus.EventBus;
import mobi.mateam.blechat.presenter.MainPresenterImpl;
import mobi.mateam.blechat.presenter.interfaces.MainPresenter;

@Module public class PresenterModule {

  @Provides MainPresenter provideMainPresenter(EventBus eventBus, RxBleClient rxBleClient){
      return new MainPresenterImpl(eventBus, rxBleClient);
  }
}
