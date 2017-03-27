package mobi.mateam.blechat.view.interfaces;

import android.content.Context;
import mobi.mateam.blechat.di.component.AppComponent;

public interface MvpView {

  public Context getAppContext();

  public Context getActivityContext();

  public void showError(Throwable error);

  public void showMessage(String text);

  AppComponent getAppComponent();
}
