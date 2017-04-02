package mobi.mateam.blechat.bus.event;

import android.bluetooth.le.ScanResult;

public class OnDeviceClickEvent extends Event {
  public final ScanResult bleScanResult;

  public OnDeviceClickEvent(ScanResult bleScanResult) {
    super(ON_DEVICE_CLICK);
    this.bleScanResult = bleScanResult;
  }
}
