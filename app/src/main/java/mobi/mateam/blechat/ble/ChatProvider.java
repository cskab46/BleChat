package mobi.mateam.blechat.ble;

import android.bluetooth.BluetoothDevice;
import mobi.mateam.blechat.model.pojo.Message;

public interface ChatProvider {
  String CHAT_SERVICE_UUID = "d68db8ac-9caf-4b52-aa66-1998a90cf0cb";
//  String CHATB_SERVICE_UUID = "0000b81d-0000-1000-8000-00805f9b34fb";
  String CHAT_CHARACTERISTIC_UUID = "bed5e61f-f04c-472e-acf4-50e4649c62cd";
  String ONLINE_STATUS = "user online";
  String OFFLINE_STATUS = "user offline";

  /**
   * Advertise new BLE Chat Service
   *
   * @throws IllegalStateException if device doesn't support Advertise mode or bluetooth is disabled
   */
  void startChatRoom() throws IllegalStateException;

  /**
   * Send new message (The provider will send message as server or client - depend on running mode)
   *
   * @param message - message to send
   */
  void sendMessage(Message message);

  /**
   * Connect to running chat service  on another device as client
   *
   * @param bluetoothDevice - device with BLE Chat service
   */
  void connectToChatOnDevice(BluetoothDevice bluetoothDevice);

  /**
   * Scan BLE devices
   *
   * @param onScanResultListener - just devices with running BLE Chat Service
   */
  void scanDevices(BleScanner.OnScanResultListener onScanResultListener);

  /**
   * Stop scanning devices. Have to be called after {@code scanDevices}
   */
  void stopScanning();

  /**
   * Disconnect from all connection (Server and Client mode)
   */
  void disconnect();
}
