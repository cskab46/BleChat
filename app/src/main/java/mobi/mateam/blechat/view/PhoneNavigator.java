package mobi.mateam.blechat.view;

import android.support.v4.app.FragmentTransaction;
import mobi.mateam.blechat.R;
import mobi.mateam.blechat.view.activity.MainActivity;
import mobi.mateam.blechat.view.fragment.BaseFragment;
import mobi.mateam.blechat.view.fragment.ChatFragment;
import mobi.mateam.blechat.view.fragment.StartFragment;
import mobi.mateam.blechat.view.interfaces.Navigator;

public class PhoneNavigator implements Navigator {

  private MainActivity activity;

  @Override public void onCreate(MainActivity mainActivity) {
    this.activity = mainActivity;
    showStartView();
  }

  private void showStartView() {
    StartFragment fragment = StartFragment.newInstance();
    replaceFragment(fragment);
  }

  @Override public void showChatView(String macAddress) {
    ChatFragment chatFragment = ChatFragment.newInstance(macAddress);
    replaceFragment(chatFragment);
  }

  private void replaceFragment(BaseFragment fragment) {
    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.container, fragment);
    transaction.commit();
  }

  @Override public void onDestroy() {

  }
}
