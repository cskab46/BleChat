package mobi.mateam.blechat;

import android.app.Application;
import mobi.mateam.blechat.di.component.AppComponent;
import mobi.mateam.blechat.di.component.DaggerAppComponent;
import mobi.mateam.blechat.di.module.AppModule;
import timber.log.Timber;

public class App extends Application {
  private AppComponent appComponent;

  @Override public void onCreate() {
    super.onCreate();
    Timber.plant(new Timber.DebugTree());
    this.initializeInjector();
  }

  private void initializeInjector() {
    this.appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
  }

  public AppComponent getAppComponent() {
    return this.appComponent;
  }
}
