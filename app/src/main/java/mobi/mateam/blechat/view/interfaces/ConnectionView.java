package mobi.mateam.blechat.view.interfaces;

import android.bluetooth.le.ScanResult;

public interface ConnectionView extends MvpView {

  void showConnection(ScanResult rxBleScanResult);

  void showEmptyState();

  void showLoading();

  void hideLoading();

  void showCancelButton();

  void showScanButton();

  void showConnectionState();
}
