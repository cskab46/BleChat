package mobi.mateam.blechat.presenter;

import mobi.mateam.blechat.presenter.interfaces.BasePresenter;
import mobi.mateam.blechat.view.interfaces.MvpView;

public abstract class BasePresenterImpl<T extends MvpView> implements BasePresenter<T> {

  private T mView;

  public BasePresenterImpl() {

  }

  @Override public void attachView(T t) {
    mView = t;
  }

  @Override public void detachView() {
    mView = null;
  }

  @Override public T getView() {
    return mView;
  }

  @Override public boolean isViewAttached() {
    return mView != null;
  }
}