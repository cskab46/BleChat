package mobi.mateam.blechat.view.activity;

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

  /**
   * Inject presenter. presenter won't be null just in case of orientation change
   */
  private void setPresenter() {
    if (presenter == null) {
      presenter = getAppComponent().getMainPresenter();
    }
    presenter.attachView(this);
  }

  /**
   * Navigator - the field of presenter and will restore state of activity;
   */
  private void setNavigator() {
    navigator = presenter.getNavigator();
    navigator.onAttach(MainActivity.this);
  }

  @Override public void showTitle(String title) {

    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(title);
    }
  }

  @Override public void showSubTitle(String subTitle) {
    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setSubtitle(subTitle);
    }
  }

  /**
   * Presenter won't be destroy on orientation change
   */
  @Override protected void onDestroy() {

    super.onDestroy();
    presenter.detachView();
    navigator.onDestroy();
    if (isFinishing()) {
      presenter = null;
    }
  }

  /**
   * Navigator is responsible of back press event;
   * Can by useful in case of different behaving with landscape orientation or tablets.
   */
  @Override public void onBackPressed() {
    navigator.onBackPressed();
  }
}
