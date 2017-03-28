package mobi.mateam.blechat.eventBus;

import com.polidea.rxandroidble.RxBleScanResult;

public class OnDeviceClickEvent extends Event{
  public final RxBleScanResult bleScanResult;

  public OnDeviceClickEvent(RxBleScanResult bleScanResult) {
    super(ON_DEVICE_CLICK);
    this.bleScanResult = bleScanResult;
  }
}
