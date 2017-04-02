package mobi.mateam.blechat.view.interfaces;

import android.app.Activity;
import android.content.Context;
import mobi.mateam.blechat.di.component.AppComponent;

public interface MvpView {

  Context getAppContext();

  Activity getActivityContext();

  void showError(Throwable error);

  void showMessage(String text);

  AppComponent getAppComponent();
}
