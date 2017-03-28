package mobi.mateam.blechat.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import java.util.ArrayList;
import mobi.mateam.blechat.R;
import mobi.mateam.blechat.model.pojo.Message;
import mobi.mateam.blechat.presenter.interfaces.ChatPresenter;
import mobi.mateam.blechat.view.interfaces.ChatView;

public class ChatFragment extends BaseFragment implements ChatView {

  public static final int LAYOUT = R.layout.fragment_chat;
  private static final String KEY_MAC_ADDRESS = "key_mac_address";
  private static ChatPresenter presenter;
  @BindView(R.id.rv_chat) RecyclerView rvChat;
  @BindView(R.id.et_message) EditText etMessage;
  @BindView(R.id.btn_send_message) Button btnSendMessage;
  private Unbinder unbinder;

  public ChatFragment() {
    // Required empty public constructor
  }

  public static ChatFragment newInstance(String macAddress) {
    ChatFragment chatFragment = new ChatFragment();
    Bundle args = new Bundle();
    args.putString(KEY_MAC_ADDRESS, macAddress);
    chatFragment.setArguments(args);
    return chatFragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(LAYOUT, container, false);
    unbinder = ButterKnife.bind(this, view);
    setPresenter();
    if (getArguments() != null) {
      String macAddress = getArguments().getString(KEY_MAC_ADDRESS);
      presenter.setMacAddress(macAddress);
    }

    return view;
  }

  private void setPresenter() {
    if (presenter == null) {
      presenter = getAppComponent().getChatPresenter();
    }
    presenter.attachView(this);
  }

  @Override public void showChatMessages(ArrayList<Message> messages) {

  }

  @Override public void addChatMessage(Message message) {

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
