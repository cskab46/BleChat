package mobi.mateam.blechat.view.interfaces;

import com.polidea.rxandroidble.RxBleScanResult;

public interface ConnectionView extends MvpView {

  void showConnections(RxBleScanResult rxBleScanResult);

  void showEmptyState();

  void showLoading();

  void hideLoading();

  void showCancelButton();

  void showScanButton();

  void showConnectionState();
}
