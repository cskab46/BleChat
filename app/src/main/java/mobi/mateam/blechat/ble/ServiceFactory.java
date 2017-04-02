package mobi.mateam.blechat.ble;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import java.util.UUID;

import static android.bluetooth.BluetoothGattService.SERVICE_TYPE_PRIMARY;

/**
 * ServiceFactory generates chat service with chat characteristic
 */
public class ServiceFactory {

  /**
   * generateChatService will instantiate a BluetoothGattService object with our chat service UUID and add
   * our char Characteristic with appropriate permissions and properties.
   *
   * @return chat service with chat characteristic
   */
  public static BluetoothGattService generateChatService() {
    BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(UUID.fromString(ChatProvider.CHAT_CHARACTERISTIC_UUID),
        BluetoothGattCharacteristic.PROPERTY_READ
            | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE
            | BluetoothGattCharacteristic.PROPERTY_BROADCAST
            | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
        BluetoothGattCharacteristic.PERMISSION_READ | BluetoothGattCharacteristic.PERMISSION_WRITE);
    BluetoothGattService service = new BluetoothGattService(UUID.fromString(ChatProvider.CHAT_SERVICE_UUID), SERVICE_TYPE_PRIMARY);

    BluetoothGattDescriptor gD = new BluetoothGattDescriptor(UUID.fromString(ChatProvider.CHAT_CHARACTERISTIC_UUID),
        BluetoothGattDescriptor.PERMISSION_WRITE | BluetoothGattDescriptor.PERMISSION_READ);

    characteristic.addDescriptor(gD);
    service.addCharacteristic(characteristic);
    return service;
  }


}
