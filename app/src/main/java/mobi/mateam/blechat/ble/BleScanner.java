package mobi.mateam.blechat.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import mobi.mateam.blechat.bus.EventBus;
import mobi.mateam.blechat.bus.event.ChatStatusChange;
import mobi.mateam.blechat.bus.event.NewMessage;
import mobi.mateam.blechat.model.pojo.Message;
import mobi.mateam.blechat.model.pojo.MessageBuilder;
import mobi.mateam.blechat.util.StringCompressor;
import timber.log.Timber;

/**
 * BleScanner scans, connects to Gatt server and sends messages to devices that provide our chat service
 */
public class BleScanner {

  //region Class fields
  public static final int MTU = 512;
  private BluetoothManager mBluetoothManager;
  private BluetoothLeScanner mScanner;
  private OnScanResultListener onScanResultListener;
  private Context mContext;
  private EventBus eventBus;
  private BluetoothGatt mGatt;
  private BluetoothGattCharacteristic mCharacteristic;
  private ScanCallback mScanCallback = new ScanCallback() {
    @Override public void onScanResult(int callbackType, ScanResult result) {
      super.onScanResult(callbackType, result);
      if (result.getDevice() != null) {
        if (onScanResultListener != null) {
          onScanResultListener.onResult(result);
        }
        Timber.d("Discovered device: " + result.getDevice());
      }
    }
  };
  //endregion

  public BleScanner(Context context, BluetoothManager bluetoothManager, EventBus eventBus) {
    mBluetoothManager = bluetoothManager;
    mContext = context;
    this.eventBus = eventBus;
  }

  /**
   * startScanning will search for devices that provide our BLE chat service and connect to
   * gatt server on them. Scanning won't start if Bluetooth is disabled.
   */
  void startScanning(OnScanResultListener onScanResultListener) throws IllegalStateException {
    if (mBluetoothManager.getAdapter().isEnabled()) {
      this.onScanResultListener = onScanResultListener;
      List<ScanFilter> filters = getScanFiltersByChatService(); // Just devices with running Chat service
      ScanSettings settings = getScanSettings();
      mScanner = mBluetoothManager.getAdapter().getBluetoothLeScanner();
      mScanner.startScan(filters, settings, mScanCallback);
    } else {
      Timber.d("Bluetooth is disabled!");
      throw new IllegalStateException("Bluetooth is disabled!");
    }
  }

  private ScanSettings getScanSettings() {
    ScanSettings.Builder settingsBuilder = new ScanSettings.Builder();
    return settingsBuilder.build();
  }

  @NonNull private List<ScanFilter> getScanFiltersByChatService() {
    ScanFilter.Builder filterBuilder = new ScanFilter.Builder()
        .setServiceUuid(new ParcelUuid(UUID.fromString(ChatProvider.CHAT_SERVICE_UUID)));
    List<ScanFilter> filters = new ArrayList<>();
    filters.add(filterBuilder.build());
    return filters;
  }

  void connectToGattServer(BluetoothDevice device) {
    device.connectGatt(mContext, false, getBluetoothGattCallback(device));
  }

  @NonNull private BluetoothGattCallback getBluetoothGattCallback(final BluetoothDevice device) {
    return new BluetoothGattCallback() {
      @Override public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        mGatt = gatt;
        // Search chat characteristic  in connected device
        for (BluetoothGattService gattService : gatt.getServices()) {
          Timber.d("Service discovered: " + gattService.getUuid());
          if (gattService.getUuid().equals(UUID.fromString(ChatProvider.CHAT_SERVICE_UUID))) {
            for (BluetoothGattCharacteristic characteristic : gattService.getCharacteristics()) {
              mCharacteristic = characteristic;
              Timber.d("Characteristic discovered: " + characteristic.getUuid());
              gatt.setCharacteristicNotification(characteristic, true);
              sendStatusMessageWithDelay(500, ChatProvider.ONLINE_STATUS);
            }
          }
        }
      }

      @Override public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
      }

      @Override public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        mGatt = gatt;
        if (newState == BluetoothProfile.STATE_CONNECTED) {
          mGatt.requestMtu(MTU);  // To change 20 bytes limit TODO: Add check input
          Timber.d("Connected to Gatt Server");
          eventBus.post(new ChatStatusChange(ChatStatusChange.DEVICE_CONNECTED, gatt.getDevice()));
          gatt.discoverServices();
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
          eventBus.post(new ChatStatusChange(ChatStatusChange.DEVICE_DISCONNECTED, gatt.getDevice()));
          Timber.d("Disconnected from Gatt Server");
          eventBus.post(new NewMessage(buildIncommingMessage(ChatProvider.OFFLINE_STATUS, gatt.getDevice())));
        }
      }

      @Override public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        if (characteristic.getUuid().equals(UUID.fromString(ChatProvider.CHAT_CHARACTERISTIC_UUID))) {
          String msg;
          byte[] value = characteristic.getValue();
          if (value != null) {
            msg = StringCompressor.decompress(value);
            Message message = buildIncommingMessage(msg, device);
            eventBus.post(new NewMessage(message));
          }
          Timber.d("Characteristic changed value: ");
        }
      }
    };
  }

  private void sendStatusMessageWithDelay(int milliseconds, final String onlineStatus) {
    new Timer().schedule(new TimerTask() {
      @Override public void run() {

        sendMessage(onlineStatus);
      }
    }, milliseconds);
  }

  private Message buildIncommingMessage(String msg, BluetoothDevice device) {
    return new MessageBuilder().setId(UUID.randomUUID().toString())
        .setText(msg)
        .setIsIncoming(true)
        .setTime(Calendar.getInstance().getTimeInMillis())
        .setSenderName(!TextUtils.isEmpty(device.getName()) ? device.getName() : device.getAddress())
        .createMessage();
  }

  /**
   * sendMessage will send a message over BLE to the Gatt server if we have connected to it
   * and discovered our chat characteristic.
   *
   * @param msg message to be sent
   */
  void sendMessage(String msg) {
    if (mCharacteristic != null) {
      mCharacteristic.setValue(StringCompressor.compress(msg));
      mGatt.writeCharacteristic(mCharacteristic);
    }
  }

  void stopScanning() {
    if (mScanner != null) {
      mScanner.stopScan(mScanCallback);
    }
  }

  /**
   * destroy should be called as soon as we are done with BLE communication, to disconnect
   * from the Gatt server and clear resources.
   */
  void destroy() {
    if (mGatt != null) {
      mGatt.disconnect();
      mGatt.close();
    }
  }

  public interface OnScanResultListener {
    void onResult(ScanResult scanResult);
  }
}
