package mobi.mateam.blechat.view;

import android.support.v4.app.FragmentTransaction;
import mobi.mateam.blechat.R;
import mobi.mateam.blechat.view.activity.MainActivity;
import mobi.mateam.blechat.view.fragment.StartFragment;
import mobi.mateam.blechat.view.interfaces.Navigator;

public class PhoneNavigator implements Navigator {

  private MainActivity activity;

  @Override public void onCreate(MainActivity mainActivity) {
    this.activity = mainActivity;
    setStartView();
  }

  private void setStartView() {
    StartFragment fragment = StartFragment.newInstance();

    replaceFragment(fragment);
  }

  private void replaceFragment(StartFragment fragment) {
    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.container, fragment);
    transaction.commit();
  }

  @Override public void onDestroy() {

  }
}
