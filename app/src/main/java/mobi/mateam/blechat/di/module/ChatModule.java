package mobi.mateam.blechat.di.module;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import mobi.mateam.blechat.ble.BleAdvertiser;
import mobi.mateam.blechat.ble.BleScanner;
import mobi.mateam.blechat.ble.ChatProvider;
import mobi.mateam.blechat.ble.ChatProviderImpl;
import mobi.mateam.blechat.model.db.DbHelper;
import mobi.mateam.blechat.model.db.MockDbHelper;
import mobi.mateam.blechat.model.repository.ChatRepository;
import mobi.mateam.blechat.model.repository.ChatRepositoryMockImpl;

@Module public class ChatModule {

  @Provides @Singleton DbHelper provideDbHelper(Context context) {
    return new MockDbHelper();
  }

  @Provides @Singleton ChatRepository provideChatRepository(DbHelper dbHelper) {
    return new ChatRepositoryMockImpl(dbHelper);
  }

  @Provides ChatProvider provideChatProvider(BleScanner bleScanner, BleAdvertiser bleAdvertiser) {
    return new ChatProviderImpl(bleScanner, bleAdvertiser);
  }
}
