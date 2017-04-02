package mobi.mateam.blechat.bus.event;

import android.bluetooth.BluetoothDevice;

public class ChatStatusChange extends Event {
 public static final int SERVICE_STARTED = 100;
 public static final int SERVICE_STOPED = 101;
 public static final int DEVICE_CONNECTED = 200;
 public static final int DEVICE_DISCONNECTED = 202;
  public int statusId;
  public BluetoothDevice device;

  public ChatStatusChange(int id, BluetoothDevice device) {
    super(Event.CHAT_STATUS_CHANGED);
    statusId = id;
    this.device = device;
  }
}
