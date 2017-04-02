package mobi.mateam.blechat.presenter.interfaces;

import android.bluetooth.BluetoothDevice;
import mobi.mateam.blechat.model.pojo.Message;
import mobi.mateam.blechat.view.interfaces.ChatView;

public interface ChatPresenter extends BasePresenter<ChatView> {

  void sendMessage(String message);

  void onNewIncomingMessage(Message message);

  void connectToDevice(BluetoothDevice bluetoothDevice);

  void startChatService();
}
