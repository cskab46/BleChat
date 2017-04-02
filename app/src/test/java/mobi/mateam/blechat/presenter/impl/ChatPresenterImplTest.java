package mobi.mateam.blechat.presenter.impl;

import android.bluetooth.BluetoothDevice;
import mobi.mateam.blechat.ble.ChatProvider;
import mobi.mateam.blechat.bus.EventBus;
import mobi.mateam.blechat.model.pojo.Message;
import mobi.mateam.blechat.presenter.interfaces.ChatPresenter;
import mobi.mateam.blechat.view.interfaces.ChatView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class) public class ChatPresenterImplTest {

  private ChatPresenter chatPresenter;

  private EventBus eventBus = new EventBus();

  @Mock ChatProvider mockChatProvider;

  @Mock ChatView mockChatView;

  @Before public void setUp() {
    chatPresenter = new ChatPresenterImpl(eventBus, mockChatProvider);
  }

  @Test public void attachView() throws Exception {
    assertFalse(chatPresenter.isViewAttached());

    chatPresenter.attachView(mockChatView);

    assertTrue(chatPresenter.isViewAttached());
  }

  @Test public void startChatService_startChatRoomCalled() throws IllegalStateException {
    chatPresenter.startChatService();
    doNothing().when(mockChatProvider).startChatRoom();
    verify(mockChatProvider, times(1)).startChatRoom();
  }

  @Test public void startChatService_startChatRoom_ExceptionThrow_View_Detached() throws IllegalStateException {
    doThrow(new IllegalStateException()).when(mockChatProvider).startChatRoom();
    chatPresenter.startChatService();
    verify(mockChatView, times(0)).showError(any());
  }

  @Test public void startChatService_startChatRoom_ExceptionThrow_ViewAttached() throws IllegalStateException {
    //Before
    doThrow(new IllegalStateException()).when(mockChatProvider).startChatRoom();

    //Action
    chatPresenter.attachView(mockChatView);
    chatPresenter.startChatService();

    //Assertion
    doNothing().when(mockChatView).showError(any());
    verify(mockChatView, times(1)).showError(any());
  }

  @Test public void connectToDevice() throws Exception {
    BluetoothDevice mockDevice = mock(BluetoothDevice.class);

    chatPresenter.connectToDevice(mockDevice);

    doNothing().when(mockChatProvider).connectToChatOnDevice(mockDevice);
    verify(mockChatProvider, times(1)).connectToChatOnDevice(mockDevice);
  }

  @Test public void sendMessage_ViewDetached() throws Exception {
    String test = "Test";
    chatPresenter.sendMessage(test);

    doNothing().when(mockChatProvider).sendMessage(any());
    verify(mockChatProvider, times(1)).sendMessage(any());
    verify(mockChatView, times(0)).addChatMessage(any());
  }

  @Test public void sendMessage_ViewAttached() throws Exception {
    chatPresenter.attachView(mockChatView);

    String test = "Test";
    chatPresenter.sendMessage(test);

    doNothing().when(mockChatProvider).sendMessage(any());
    verify(mockChatProvider, times(1)).sendMessage(any());

    doNothing().when(mockChatView).addChatMessage(any());
    verify(mockChatView, times(1)).addChatMessage(any());
  }

  @Test public void onNewIncomingMessage_ViewDetached() throws Exception {
    Message message = mock(Message.class);

    chatPresenter.onNewIncomingMessage(message);

    verify(mockChatView, times(0)).addChatMessage(any());
  }

  @Test public void detachView_disconnect_called() throws Exception {
    assertFalse(chatPresenter.isViewAttached());
    chatPresenter.attachView(mockChatView);
    assertTrue(chatPresenter.isViewAttached());

    chatPresenter.detachView();
    doNothing().when(mockChatProvider).disconnect();
    verify(mockChatProvider, times(1)).disconnect();

    assertFalse(chatPresenter.isViewAttached());
  }
}