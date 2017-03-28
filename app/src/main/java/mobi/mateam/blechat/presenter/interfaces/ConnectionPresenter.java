package mobi.mateam.blechat.presenter.interfaces;

import com.polidea.rxandroidble.RxBleScanResult;
import mobi.mateam.blechat.view.interfaces.ConnectionView;

public interface ConnectionPresenter extends BasePresenter<ConnectionView> {

  void onScanClick();

  void onDeviceClick(RxBleScanResult bleScanResult);

  void onCancelClick();
}
