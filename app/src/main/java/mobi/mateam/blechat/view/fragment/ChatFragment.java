package mobi.mateam.blechat.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import java.util.ArrayList;
import mobi.mateam.blechat.R;
import mobi.mateam.blechat.model.pojo.Message;
import mobi.mateam.blechat.presenter.interfaces.ChatPresenter;
import mobi.mateam.blechat.view.interfaces.ChatView;

public class ChatFragment extends BaseFragment implements ChatView {

  private Unbinder unbinder;
  private static ChatPresenter presenter;
  public ChatFragment() {
    // Required empty public constructor
  }

  public static ChatFragment newInstance() {
    return new ChatFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_chat, container, false);
    unbinder = ButterKnife.bind(this, view);
    setPresenter();

    return view;
  }

  private void setPresenter() {
    if (presenter == null) {
      presenter = getAppComponent().getChatPresenter();
    }
    presenter.attachView(this);
  }

  @OnClick(R.id.btn_start_chats) public void onChatsClick(){

  }

  @OnClick(R.id.btn_start_connection) public void onConnectionClick(){
      ConnectionDialog.newInstance().show(getFragmentManager(), "ConnectionDialog");
  }

  @Override public void showChatMessages(ArrayList<Message> messages) {

  }

  @Override public void addChatMessage(Message message) {

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
