package mobi.mateam.blechat.view.interfaces;

public interface MainView extends MvpView {

  void showStartView();

  void showConnectionDialog();

  void showConnectionStatus();

  void showLoading();

  void hideLoading();

}
