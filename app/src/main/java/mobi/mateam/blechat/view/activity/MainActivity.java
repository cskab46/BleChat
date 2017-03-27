package mobi.mateam.blechat.view.activity;

import android.app.ActionBar;
import android.os.Bundle;
import mobi.mateam.blechat.R;
import mobi.mateam.blechat.presenter.interfaces.MainPresenter;
import mobi.mateam.blechat.view.interfaces.MainView;
import mobi.mateam.blechat.view.interfaces.Navigator;

public class MainActivity extends BaseMvpActivity implements MainView {

  public static final int LAYOUT = R.layout.activity_main;
  private static MainPresenter presenter;
  private Navigator navigator;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(LAYOUT);
    setPresenter();
    setNavigator();
  }

  private void setPresenter() {
    if (presenter == null) {
      presenter = getAppComponent().getMainPresenter();
    }
    presenter.attachView(this);
  }

  private void setNavigator() {
    navigator = presenter.getNavigator();
    navigator.onCreate(MainActivity.this);
  }

  @Override public void showStartView() {

  }

  @Override public void showConnectionDialog() {

  }

  @Override public void showConnectionStatus(boolean isConnected) {
    int statusStringRes = isConnected? R.string.status_connected:R.string.status_disconected;
    ActionBar actionBar = getActionBar();
    if (actionBar != null) {
      actionBar.setTitle(statusStringRes);
    }
  }

  @Override public void showLoading() {

  }

  @Override public void hideLoading() {

  }

  @Override protected void onDestroy() {
    super.onDestroy();
    presenter.detachView();
    navigator.onDestroy();
    if (isFinishing()) {
      presenter = null;
    }
  }
}
