package mobi.mateam.blechat.presenter.interfaces;

import mobi.mateam.blechat.view.interfaces.MvpView;

public interface BasePresenter<T extends MvpView> {

  void attachView(T t);

  void detachView();

  T getView();

  boolean isViewAttached();
}
