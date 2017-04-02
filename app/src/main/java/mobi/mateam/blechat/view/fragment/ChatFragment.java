package mobi.mateam.blechat.view.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import java.util.List;
import mobi.mateam.blechat.R;
import mobi.mateam.blechat.model.pojo.Message;
import mobi.mateam.blechat.presenter.interfaces.ChatPresenter;
import mobi.mateam.blechat.view.adapter.ChatMessageAdapter;
import mobi.mateam.blechat.view.interfaces.ChatView;

public class ChatFragment extends BaseFragment implements ChatView {

  public static final int LAYOUT = R.layout.fragment_chat;
  private static final String KEY_DEVICE = "key_mac_address";
  private static ChatPresenter presenter;
  @BindView(R.id.rv_chat) RecyclerView rvChat;
  @BindView(R.id.et_message) EditText etMessage;
  @BindView(R.id.btn_send_message) Button btnSendMessage;
  private Unbinder unbinder;
  private ChatMessageAdapter chatMessageAdapter;

  public ChatFragment() {
    // Required empty public constructor
  }

  public static ChatFragment newInstance(BluetoothDevice bluetoothDevice) {
    ChatFragment chatFragment = new ChatFragment();
    if (bluetoothDevice != null) {
      Bundle args = new Bundle();
      args.putParcelable(KEY_DEVICE, bluetoothDevice);
      chatFragment.setArguments(args);
    }
    return chatFragment;
  }

  public static void hideSoftKeyboard(Activity activity) {
    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(LAYOUT, container, false);
    unbinder = ButterKnife.bind(this, view);
    initRvChat();
    setPresenter();
    if (getArguments() != null) {
      BluetoothDevice bluetoothDevice = getArguments().getParcelable(KEY_DEVICE);
      presenter.connectToDevice(bluetoothDevice);
    } else {
      presenter.startChatService();
    }

    return view;
  }

  private void initRvChat() {
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    mLayoutManager.setReverseLayout(true);
    mLayoutManager.setStackFromEnd(true);

    rvChat.setLayoutManager(mLayoutManager);
    chatMessageAdapter = new ChatMessageAdapter();
    rvChat.setAdapter(chatMessageAdapter);
  }

  private void setPresenter() {
    if (presenter == null) {
      presenter = getAppComponent().getChatPresenter();
    }
    presenter.attachView(this);
  }

  @OnClick(R.id.btn_send_message) public void sendMessage() {
    String text = etMessage.getText().toString();
    presenter.sendMessage(text);
    hideSoftKeyboard(getActivity());
    etMessage.setText("");
  }

  @Override public void showChatMessages(List<Message> messages) {
    chatMessageAdapter.addMessages(messages);
  }

  @Override public void addChatMessage(Message message) {
    chatMessageAdapter.addMessage(message);
  }

  @Override public void setIsSendButtonEnabled(boolean connected) {
    btnSendMessage.setEnabled(connected);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    presenter.detachView();
    unbinder.unbind();
    if (isRemoving()) {
      presenter = null;
    }
  }
}
