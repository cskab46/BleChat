package mobi.mateam.blechat.di.module;

import android.content.Context;
import com.polidea.rxandroidble.RxBleClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class BleModule {

  @Provides @Singleton RxBleClient provideRxBleClient(Context context) {
    return RxBleClient.create(context);
  }
}
