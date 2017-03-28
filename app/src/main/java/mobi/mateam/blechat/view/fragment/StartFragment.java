package mobi.mateam.blechat.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mobi.mateam.blechat.R;

public class StartFragment extends BaseFragment {

  private Unbinder unbinder;

  public StartFragment() {
    // Required empty public constructor
  }

  public static StartFragment newInstance() {
    return new StartFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_start, container, false);
    unbinder = ButterKnife.bind(this, view);

    return view;
  }

  @OnClick(R.id.btn_start_chats) public void onChatsClick(){

  }

  @OnClick(R.id.btn_start_connection) public void onConnectionClick(){
      ConnectionDialog.newInstance().show(getFragmentManager(), "ConnectionDialog");
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }


}
