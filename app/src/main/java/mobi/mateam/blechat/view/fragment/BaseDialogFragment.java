package mobi.mateam.blechat.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;
import mobi.mateam.blechat.App;
import mobi.mateam.blechat.di.component.AppComponent;

public class BaseDialogFragment extends DialogFragment {
  public Context getAppContext() {
    return getActivity().getApplicationContext();
  }

  public Activity getActivityContext() {
    return getActivity();
  }

  public void showError(Throwable error) {
    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
  }

  public void showMessage(String text) {
    Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
  }

  public AppComponent getAppComponent() {
    return ((App) getActivity().getApplication()).getAppComponent();
  }
}
