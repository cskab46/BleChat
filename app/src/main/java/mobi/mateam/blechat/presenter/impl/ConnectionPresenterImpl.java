package mobi.mateam.blechat.presenter.impl;

import android.Manifest;
import android.bluetooth.le.ScanResult;
import com.tbruyelle.rxpermissions.RxPermissions;
import mobi.mateam.blechat.R;
import mobi.mateam.blechat.ble.ChatProvider;
import mobi.mateam.blechat.bus.EventBus;
import mobi.mateam.blechat.bus.event.OnDeviceClickEvent;
import mobi.mateam.blechat.presenter.interfaces.ConnectionPresenter;
import mobi.mateam.blechat.view.interfaces.ConnectionView;
import timber.log.Timber;

public class ConnectionPresenterImpl extends BasePresenterImpl<ConnectionView> implements ConnectionPresenter {
  private final EventBus eventBus;
  private final ChatProvider chatProvider;
  private RxPermissions rxPermissions;
  private boolean isScanning;

  public ConnectionPresenterImpl(EventBus eventBus, ChatProvider chatProvider) {
    this.chatProvider = chatProvider;
    this.eventBus = eventBus;
  }

  @Override public void attachView(ConnectionView connectionView) {
    super.attachView(connectionView);
    rxPermissions = new RxPermissions(getView().getActivityContext());
    updateUIState();
  }

  @Override public void onScanClick() {
    rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(granted -> {
      if (granted) { // Always true pre-M
        Timber.d("ACCESS_FINE_LOCATION permission is granted");
        isScanning = true;
        scanDevices();
        if (isViewAttached()){
          getView().showCancelButton();
        }
      } else {
        if (isViewAttached()) {
          getView().showMessage(getView().getAppContext().getString(R.string.location_required_message));
        }
      }
    });
  }

  private void scanDevices() {
    try {
      getView().showLoading();
      isScanning = true;
      chatProvider.scanDevices(scanResult -> {
        if (isViewAttached()) {
          getView().showConnection(scanResult);
        }
        updateUIState();
      });
    } catch (Throwable e) {
      onScanFailure(e);
    }
  }

  private void updateUIState() {
    if (isViewAttached()) {
      if (isScanning) {
        getView().showConnectionState();
        getView().showCancelButton();
        getView().showLoading();
      } else {
        getView().showEmptyState();
        getView().showScanButton();
        getView().hideLoading();
      }
    }
  }

  private void onScanFailure(Throwable throwable) {
    Timber.e(throwable);
    isScanning = false;
    updateUIState();

    if (isViewAttached()) {
      getView().showEmptyState();
      getView().showError(throwable);
    }
  }

  private void stopScanning() {
    isScanning = false;
    chatProvider.stopScanning();
    if (isViewAttached()) {
      getView().showConnection(null);
      getView().showEmptyState();
    }
  }

  @Override public void onDeviceClick(ScanResult bleScanResult) {
    stopScanning();
    eventBus.post(new OnDeviceClickEvent(bleScanResult));
  }

  @Override public void onCancelClick() {
    stopScanning();

    updateUIState();
  }
}
