package mobi.mateam.blechat.view.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import mobi.mateam.blechat.App;
import mobi.mateam.blechat.di.component.AppComponent;
import mobi.mateam.blechat.view.interfaces.MvpView;

public class BaseFragment extends Fragment implements MvpView {
  public AppComponent getAppComponent() {
    return ((App) getActivity().getApplication()).getAppComponent();
  }

  @Override public Context getAppContext() {
    return getActivity().getApplicationContext();
  }

  @Override public Context getActivityContext() {
    return getActivity();
  }

  @Override public void showError(Throwable error) {
    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
  }

  @Override public void showMessage(String text) {
    Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();

  }
}
