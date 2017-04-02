package mobi.mateam.blechat.presenter.interfaces;

import mobi.mateam.blechat.view.interfaces.MainView;
import mobi.mateam.blechat.view.interfaces.Navigator;

public interface MainPresenter extends BasePresenter<MainView> {

  Navigator getNavigator();
}
