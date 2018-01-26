package mobi.mateam.blechat.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import mobi.mateam.blechat.bus.EventBus;
import mobi.mateam.blechat.bus.event.ChatStatusChange;
import mobi.mateam.blechat.bus.event.NewMessage;
import mobi.mateam.blechat.model.pojo.Message;
import mobi.mateam.blechat.model.pojo.MessageBuilder;
import mobi.mateam.blechat.util.Constants;
import mobi.mateam.blechat.util.StringCompressor;
import timber.log.Timber;

/**
 * BleAdvertiser starts a Gatt server and advertises it's services
 */
public class BleAdvertiser {

  //region Class fields
  private BluetoothManager bluetoothManager;
  private BluetoothGattServer gattServer;
  private BluetoothDevice connectedDevice;
  private Context context;
  private EventBus eventBus;
  private BluetoothDevice chatClientDevice;
  private AdvertiseCallback mAdvertiseCallback;
  //endregion


  public BleAdvertiser(Context context, BluetoothManager bluetoothManager, EventBus eventBus) {
    this.bluetoothManager = bluetoothManager;
    this.context = context;
    this.eventBus = eventBus;
    mAdvertiseCallback = getAdvertiseCallback();
  }

  /**
   * startAdvertising will open a Gatt server, add our chat services to it and start advertisement with default settings.
   * Advertising won't start if bluetooth is disabled or device doesn't support the Peripheral mode.
   */
  void startAdvertising() throws IllegalStateException {
    if (bluetoothManager.getAdapter().isEnabled()) {
      if (bluetoothManager.getAdapter().isMultipleAdvertisementSupported()) {

        BluetoothGattServerCallback callback = getBluetoothGattServerCallback();

        gattServer = bluetoothManager.openGattServer(context, callback);

        BluetoothGattService service = ServiceFactory.generateChatService();

        gattServer.addService(service);
      } else {
        Timber.d("Central mode not supported by the device!");
        throw new IllegalStateException("Server mode not supported by the device!");
      }
    } else {
      Timber.d("Bluetooth is disabled!");
      throw new IllegalStateException("Bluetooth is disabled!");
    }
  }

  /**
   * sendMessage will send a message represented as a String, over the BLE if server was successfully started and client has connected.
   *
   * @param msg message to be sent
   */
  void sendMessage(String msg) {
    if (connectedDevice != null) {
      BluetoothGattCharacteristic characteristic =
          ServiceFactory.generateChatService().getCharacteristic(UUID.fromString(ChatProvider.CHAT_CHARACTERISTIC_UUID));
      characteristic.setValue(StringCompressor.compress(msg));
      gattServer.notifyCharacteristicChanged(connectedDevice, characteristic, false);
    }
  }

  //region Callbacks getters
  @NonNull private AdvertiseCallback getAdvertiseCallback() {
    return new AdvertiseCallback() {

      @Override public void onStartSuccess(AdvertiseSettings settingsInEffect) {
        super.onStartSuccess(settingsInEffect);
        Timber.d("Advertising started successfully");
        eventBus.post(new ChatStatusChange(ChatStatusChange.SERVICE_STARTED, null));
      }

      @Override public void onStartFailure(int errorCode) {
        super.onStartFailure(errorCode);
        switch (errorCode) {
          case AdvertiseCallback.ADVERTISE_FAILED_ALREADY_STARTED:
            Timber.e("Advertise failed: already started");
            break;
          case AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE:
            Timber.e("Advertise failed: data too large");
            break;
          case AdvertiseCallback.ADVERTISE_FAILED_FEATURE_UNSUPPORTED:
            Timber.e("Advertise failed: feature unsupported");
            break;
          case AdvertiseCallback.ADVERTISE_FAILED_INTERNAL_ERROR:
            Timber.e("Advertise failed: internal error");
            break;
          case AdvertiseCallback.ADVERTISE_FAILED_TOO_MANY_ADVERTISERS:
            Timber.e("Advertise failed: too many advertisers");
            break;
        }
        eventBus.post(new ChatStatusChange(ChatStatusChange.SERVICE_STOPED, null));
      }
    };
  }
  @NonNull private BluetoothGattServerCallback getBluetoothGattServerCallback() {
    return new BluetoothGattServerCallback() {

      @Override
      public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
      }

      @Override public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite,
          boolean responseNeeded, int offset, byte[] value) {
        super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
        gattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value);
      }

      @Override public void onMtuChanged(BluetoothDevice device, int mtu) {
        super.onMtuChanged(device, mtu);
      }

      @Override public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
        super.onConnectionStateChange(device, status, newState);
        if (newState == BluetoothProfile.STATE_CONNECTED) {
          Timber.d("Client connected: " + device.getAddress());

          connectedDevice = device;

          sendStatusMessageWithDelay(500, ChatProvider.ONLINE_STATUS);
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
          Timber.d("Client disconnected: " + device.getAddress());
          connectedDevice = null;
          if (chatClientDevice != null && device.getAddress().equals(chatClientDevice.getAddress())) {
            eventBus.post(new ChatStatusChange(ChatStatusChange.DEVICE_DISCONNECTED, device));
            eventBus.post(new NewMessage(buildIncommingMessage(chatClientDevice, ChatProvider.OFFLINE_STATUS)));

            chatClientDevice = null;
          }
        }
      }

      private void sendStatusMessageWithDelay(int milliseconds, final String onlineStatus) {
        new Timer().schedule(new TimerTask() {
          @Override public void run() {
            if (connectedDevice != null) {
              sendMessage(onlineStatus);
            }
          }
        }, milliseconds);
      }

      @Override public void onServiceAdded(int status, BluetoothGattService service) {
        super.onServiceAdded(status, service);
        BleAdvertiser.this.onServiceAdded();
      }

      @Override public void onNotificationSent(BluetoothDevice device, int status) {
        super.onNotificationSent(device, status);
        Timber.d("onNotificationSent");
      }

      @Override public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic,
          boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
        super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
        if (isChatCharacteristic(characteristic)) {
          chatClientDevice = device;
          eventBus.post(new ChatStatusChange(ChatStatusChange.DEVICE_CONNECTED, device));
          String msg = "";
          if (value != null) {
            msg = StringCompressor.decompress(value);
            Message message = buildIncommingMessage(device, msg);
            eventBus.post(new NewMessage(message));
          }
          Timber.d("onCharacteristicWriteRequest: " + msg);
        }
      }
    };
  }

  //endregion

  private void onServiceAdded() {
    final BluetoothLeAdvertiser bluetoothLeAdvertiser = bluetoothManager.getAdapter().getBluetoothLeAdvertiser();
    AdvertiseData.Builder dataBuilder = getAdvertisingDataBuilder();

    AdvertiseSettings.Builder settingsBuilder = getAdvertisingSettingsBuilder();

    bluetoothLeAdvertiser.startAdvertising(settingsBuilder.build(), dataBuilder.build(), mAdvertiseCallback);
  }

  private boolean isChatCharacteristic(BluetoothGattCharacteristic characteristic) {
    return characteristic.getUuid().equals(UUID.fromString(ChatProvider.CHAT_CHARACTERISTIC_UUID));
  }

  private Message buildIncommingMessage(BluetoothDevice device, String msg) {
    return new MessageBuilder().setId(UUID.randomUUID().toString())
        .setText(msg)
        .setIsIncoming(true)
        .setTime(Calendar.getInstance().getTimeInMillis())
        .setSenderName(!TextUtils.isEmpty(device.getName()) ? device.getName() : device.getAddress())
        .createMessage();
  }

  //region Service Settings and Data Builders
  @NonNull private AdvertiseSettings.Builder getAdvertisingSettingsBuilder() {
    AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
    settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
    settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
    settingsBuilder.setConnectable(true);
    return settingsBuilder;
  }

  @NonNull private AdvertiseData.Builder getAdvertisingDataBuilder() {
    AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
//    dataBuilder.setIncludeTxPowerLevel(false); //necessity to fit in 31 byte advertisement
    dataBuilder.addServiceUuid(new ParcelUuid(UUID.fromString(ChatProvider.CHAT_SERVICE_UUID)));
//    dataBuilder.addServiceData(ParcelUuid.fromString(ChatProvider.CHAT_SERVICE_UUID), Build.DEVICE.getBytes());
    Constants.setDeviceName(bluetoothManager.getAdapter().getName());
    dataBuilder.addServiceData(ParcelUuid.fromString(ChatProvider.CHAT_SERVICE_UUID),Constants.getDeviceName().getBytes());

    return dataBuilder;
  }
  //endregion

  /**
   * destroy will close the gatt server if it was opened. Method should be called once we're done with the BLE communication, to clear up the
   * resources.
   */
  void destroy() {
    eventBus.post(new ChatStatusChange(ChatStatusChange.SERVICE_STOPED, chatClientDevice));
    final BluetoothLeAdvertiser bluetoothLeAdvertiser = bluetoothManager.getAdapter().getBluetoothLeAdvertiser();
    if (bluetoothLeAdvertiser != null && mAdvertiseCallback != null) {
      bluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
    }
    if (gattServer != null) {

      gattServer.clearServices();
      gattServer.close();
    }
  }
}
