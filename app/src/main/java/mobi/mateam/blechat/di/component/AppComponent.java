package mobi.mateam.blechat.di.component;

import android.content.Context;
import dagger.Component;
import javax.inject.Singleton;
import mobi.mateam.blechat.di.module.AppModule;
import mobi.mateam.blechat.di.module.BleModule;
import mobi.mateam.blechat.di.module.ChatModule;
import mobi.mateam.blechat.di.module.PresenterModule;
import mobi.mateam.blechat.presenter.interfaces.ChatPresenter;
import mobi.mateam.blechat.presenter.interfaces.ConnectionPresenter;
import mobi.mateam.blechat.presenter.interfaces.MainPresenter;

@Singleton @Component(modules = { AppModule.class, BleModule.class, PresenterModule.class, ChatModule.class }) public interface AppComponent {

  Context context();

  MainPresenter getMainPresenter();

  ConnectionPresenter getConnectionPresenter();

  ChatPresenter getChatPresenter();
}
