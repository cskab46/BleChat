package mobi.mateam.blechat.bus.event;

import android.bluetooth.BluetoothDevice;

public class ConnectionChange extends Event {
  public final BluetoothDevice device;
  public final int status;
  public final int newState;

  public ConnectionChange(BluetoothDevice device, int status, int newState) {
    super(Event.CHAT_STATUS_CHANGED);
    this.device = device;
    this.status = status;
    this.newState = newState;
  }
}
