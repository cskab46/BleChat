package mobi.mateam.blechat.presenter.impl;

import android.Manifest;
import android.support.annotation.StringRes;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleScanResult;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.tbruyelle.rxpermissions.RxPermissions;
import mobi.mateam.blechat.R;
import mobi.mateam.blechat.eventBus.EventBus;
import mobi.mateam.blechat.eventBus.OnDeviceClickEvent;
import mobi.mateam.blechat.presenter.interfaces.ConnectionPresenter;
import mobi.mateam.blechat.view.interfaces.ConnectionView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class ConnectionPresenterImpl extends BasePresenterImpl<ConnectionView> implements ConnectionPresenter {

  private final RxBleClient rxBleClient;
  private final EventBus eventBus;
  private Subscription scanSubscription;
  private RxPermissions rxPermissions;

  public ConnectionPresenterImpl(EventBus eventBus, RxBleClient rxBleClient) {
    this.rxBleClient = rxBleClient;
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
        scan();
      } else {
        if (isViewAttached()) {
          getView().showMessage("Location permission is required");
        }
      }
    });
  }

  private void scan() {
    getView().showLoading();
    scanSubscription = rxBleClient.scanBleDevices()
        .observeOn(AndroidSchedulers.mainThread())
        .doOnUnsubscribe(this::clearSubscription)
        .subscribe(this::addScanResult, this::onScanFailure);

    updateUIState();
  }

  private void updateUIState() {
    if (isViewAttached()) {
      if (isScanning()) {
        getView().showConnectionState();
        getView().showCancelButton();
      } else {
        getView().showEmptyState();
        getView().showScanButton();
      }
    }
  }

  private void onScanFailure(Throwable throwable) {
    Timber.e(throwable);
    updateUIState();

    if (isViewAttached()) {
      getView().showEmptyState();
      if (throwable instanceof BleScanException) {
        showBleScanException((BleScanException) throwable);
      }
    }
  }

  private void showBleScanException(BleScanException bleScanException) {

    @StringRes int messageRes;
    switch (bleScanException.getReason()) {
      case BleScanException.BLUETOOTH_NOT_AVAILABLE:
        messageRes = R.string.message_error_bluethooth_not_avalible;
        break;
      case BleScanException.BLUETOOTH_DISABLED:
        messageRes = R.string.message_error_bluethooth_disactivated;
        break;
      case BleScanException.LOCATION_PERMISSION_MISSING:
        messageRes = R.string.message_error_perrmision_missing;
        break;
      case BleScanException.LOCATION_SERVICES_DISABLED:
        messageRes = R.string.message_error_location_permission;
        break;
      case BleScanException.BLUETOOTH_CANNOT_START:
      default:
        messageRes = R.string.message_error_scaning_base;
        break;
    }

    String message = getView().getAppContext().getString(messageRes);
    getView().showMessage(message);
  }

  private void addScanResult(RxBleScanResult rxBleScanResult) {
    if (isViewAttached()) {
      getView().hideLoading();
      getView().showConnections(rxBleScanResult);
    }
  }

  private void clearSubscription() {
    scanSubscription.unsubscribe();
    if (isViewAttached()) {
      getView().showConnections(null);
      getView().showEmptyState();
    }
  }

  @Override public void onDeviceClick(RxBleScanResult bleScanResult) {
    eventBus.post(new OnDeviceClickEvent(bleScanResult));
  }

  @Override public void onCancelClick() {
    clearSubscription();
    scanSubscription = null;
    updateUIState();
  }

  private boolean isScanning() {
    return scanSubscription != null;
  }
}
