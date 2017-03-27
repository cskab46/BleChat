package mobi.mateam.blechat.di.module;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import mobi.mateam.blechat.eventBus.EventBus;

@Module public class AppModule {
  Application application;

  public AppModule(Application application) {
    this.application = application;
  }

  @Provides @Singleton Context provideApplicationContext() {
    return application;
  }

  @Singleton @Provides EventBus provideEventBus() {
    return new EventBus();
  }
}