package mobi.mateam.blechat.ble;

import android.bluetooth.BluetoothDevice;
import mobi.mateam.blechat.model.pojo.Message;

public class ChatProviderImpl implements ChatProvider {

  private final BleScanner bleScanner;
  private final BleAdvertiser bleAdvertiser;
  private boolean isServer;

  public ChatProviderImpl(BleScanner bleScanner, BleAdvertiser bleAdvertiser) {
    this.bleScanner = bleScanner;
    this.bleAdvertiser = bleAdvertiser;
  }

  @Override public void startChatRoom() throws IllegalStateException {
    isServer = true;
    bleAdvertiser.startAdvertising();
  }

  @Override public void sendMessage(Message message) {
    if (isServer) {
      bleAdvertiser.sendMessage(message.text);
    } else {
      bleScanner.sendMessage(message.text);
    }
  }

  @Override public void connectToChatOnDevice(BluetoothDevice bluetoothDevice) {
    isServer = false;
    bleScanner.connectToGattServer(bluetoothDevice);
  }

  @Override public void scanDevices(BleScanner.OnScanResultListener onScanResultListener) {
    bleScanner.startScanning(onScanResultListener);
  }

  @Override public void stopScanning() {
    bleScanner.stopScanning();
  }

  @Override public void disconnect() {
    if (isServer) {
      bleAdvertiser.destroy();
    } else {
      bleScanner.destroy();
    }
  }
}
