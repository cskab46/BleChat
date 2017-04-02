package mobi.mateam.blechat.di.module;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import mobi.mateam.blechat.ble.BleAdvertiser;
import mobi.mateam.blechat.ble.BleScanner;
import mobi.mateam.blechat.bus.EventBus;

@Module public class BleModule {

  @Provides BluetoothManager provideBluetoothManager(Context context) {
    return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
  }

  @Provides @Singleton BleAdvertiser provideBleAdvertiser(Context context, BluetoothManager bluetoothManager, EventBus eventBus) {
    return new BleAdvertiser(context, bluetoothManager, eventBus);
  }

  @Provides BleScanner provideBleScanner(Context context, BluetoothManager bluetoothManager, EventBus eventBus) {
    return new BleScanner(context, bluetoothManager, eventBus);
  }
}
