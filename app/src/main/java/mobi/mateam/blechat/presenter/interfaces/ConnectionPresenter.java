package mobi.mateam.blechat.presenter.interfaces;

import android.bluetooth.le.ScanResult;
import mobi.mateam.blechat.view.interfaces.ConnectionView;

public interface ConnectionPresenter extends BasePresenter<ConnectionView> {

  void onScanClick();

  void onDeviceClick(ScanResult bleScanResult);

  void onCancelClick();
}
