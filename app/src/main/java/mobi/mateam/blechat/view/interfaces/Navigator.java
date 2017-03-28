package mobi.mateam.blechat.view.interfaces;

import mobi.mateam.blechat.view.activity.MainActivity;

public interface Navigator {
  void onCreate(MainActivity mainActivity);

  void onDestroy();

  void showChatView(String macAddress);
}
