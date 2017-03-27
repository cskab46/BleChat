package mobi.mateam.blechat.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import mobi.mateam.blechat.R;

public class StartFragment extends BaseFragment {


  public StartFragment() {
    // Required empty public constructor
  }


  public static StartFragment newInstance() {
    StartFragment fragment = new StartFragment();
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_start, container, false);
  }
}
