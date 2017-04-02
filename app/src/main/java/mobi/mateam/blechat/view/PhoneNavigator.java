package mobi.mateam.blechat.view;

import android.bluetooth.BluetoothDevice;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import mobi.mateam.blechat.R;
import mobi.mateam.blechat.view.activity.MainActivity;
import mobi.mateam.blechat.view.fragment.BaseFragment;
import mobi.mateam.blechat.view.fragment.ChatFragment;
import mobi.mateam.blechat.view.fragment.StartFragment;
import mobi.mateam.blechat.view.interfaces.Navigator;

public class PhoneNavigator implements Navigator {

  private MainActivity activity;

  @Override public void onAttach(MainActivity mainActivity) {
    this.activity = mainActivity;
    initState();
  }

  private void initState() {
    Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.container);
    if (f == null){
      showStartView();
    }
  }

  private void showStartView() {
    StartFragment fragment = StartFragment.newInstance();
    replaceFragment(fragment);
  }

  @Override public void showChatView(BluetoothDevice device) {
    ChatFragment chatFragment = ChatFragment.newInstance(device);
    replaceFragment(chatFragment);
  }

  @Override public void onBackPressed() {
    Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.container);
    if (f instanceof StartFragment) {
      activity.finish();
    } else if (f instanceof ChatFragment) {
      showStartView();
    }
  }

  private void replaceFragment(BaseFragment fragment) {
    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.container, fragment);
    transaction.commit();
  }

  @Override public void onDestroy() {
    activity = null;
  }
}
