package mobi.mateam.blechat.view.interfaces;

import android.bluetooth.BluetoothDevice;
import mobi.mateam.blechat.view.activity.MainActivity;

public interface Navigator {
  void onAttach(MainActivity mainActivity);

  void onDestroy();

  void showChatView(BluetoothDevice device);

  void onBackPressed();
}
