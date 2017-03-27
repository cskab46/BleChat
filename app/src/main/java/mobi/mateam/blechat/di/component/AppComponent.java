package mobi.mateam.blechat.di.component;

import android.content.Context;
import dagger.Component;
import javax.inject.Singleton;
import mobi.mateam.blechat.di.module.AppModule;
import mobi.mateam.blechat.di.module.BleModule;
import mobi.mateam.blechat.di.module.PresenterModule;
import mobi.mateam.blechat.presenter.interfaces.MainPresenter;

@Singleton @Component(modules = { AppModule.class, BleModule.class, PresenterModule.class }) public interface AppComponent {

  Context context();

  MainPresenter getMainPresenter();
}
