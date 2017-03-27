package mobi.mateam.blechat.di.component;

import android.content.Context;
import dagger.Component;
import javax.inject.Singleton;
import mobi.mateam.blechat.di.module.AppModule;
import mobi.mateam.blechat.di.module.BleModule;

@Singleton @Component(modules = { AppModule.class, BleModule.class})
public interface AppComponent {

  Context context();
}
