package mobi.mateam.blechat.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.polidea.rxandroidble.RxBleScanResult;
import mobi.mateam.blechat.R;
import mobi.mateam.blechat.presenter.interfaces.ConnectionPresenter;
import mobi.mateam.blechat.view.adapter.ScanResultsAdapter;
import mobi.mateam.blechat.view.interfaces.ConnectionView;

public class ConnectionDialog extends BaseDialogFragment implements ConnectionView {
  public static final int LAYOUT = R.layout.fragment_dialog_connection;
  private static ConnectionPresenter presenter;
  @BindView(R.id.pb_loading) ProgressBar progressBar;
  @BindView(R.id.rv_connections) RecyclerView rvConnection;
  @BindView(R.id.tv_empty_state) TextView tvEmptyState;
  @BindView(R.id.btn_start_scan) Button btnAction;
  private Unbinder unbinder;
  private ScanResultsAdapter resultsAdapter;
  private boolean isBtnInScanMode = true;

  public static ConnectionDialog newInstance() {
    return new ConnectionDialog();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View v = inflater.inflate(LAYOUT, container, false);
    unbinder = ButterKnife.bind(this, v);
    initConnectionRecyclerView(rvConnection);
    setPresenter();
    return v;
  }

  private void setPresenter() {
    if (presenter == null) {
      presenter = getAppComponent().getConnectionPresenter();
    }
    presenter.attachView(this);
  }

  private void initConnectionRecyclerView(RecyclerView mRecyclerView) {
    mRecyclerView.setHasFixedSize(true);
    LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(recyclerLayoutManager);
    resultsAdapter = new ScanResultsAdapter();
    mRecyclerView.setAdapter(resultsAdapter);
    resultsAdapter.setOnAdapterItemClickListener(view -> {
      final int childAdapterPosition = mRecyclerView.getChildAdapterPosition(view);
      final RxBleScanResult itemAtPosition = resultsAdapter.getItemAtPosition(childAdapterPosition);
      onAdapterItemClick(itemAtPosition);
    });
  }

  private void onAdapterItemClick(RxBleScanResult bleScanResult) {
    presenter.onDeviceClick(bleScanResult);
    dismiss();
  }

  @OnClick(R.id.btn_start_scan) public void startScan() {
    if (isBtnInScanMode) {
      presenter.onScanClick();
    } else {
      presenter.onCancelClick();
    }
  }

  @Override public void onDetach() {
    super.onDetach();
  }

  @Override public void showConnections(RxBleScanResult rxBleScanResult) {
    if (rxBleScanResult == null) {
      resultsAdapter.clearScanResults();
    } else {
      resultsAdapter.addScanResult(rxBleScanResult);
    }
  }

  @Override public void showEmptyState() {
    rvConnection.setVisibility(View.INVISIBLE);
    progressBar.setVisibility(View.INVISIBLE);
    tvEmptyState.setVisibility(View.VISIBLE);
  }

  @Override public void showConnectionState() {
    rvConnection.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.INVISIBLE);
    tvEmptyState.setVisibility(View.INVISIBLE);
  }

  @Override public void showLoading() {
    tvEmptyState.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override public void hideLoading() {
    progressBar.setVisibility(View.GONE);
  }

  @Override public void showCancelButton() {
    isBtnInScanMode = false;
    btnAction.setText(R.string.btn_cancel_text);
  }

  @Override public void showScanButton() {
    isBtnInScanMode = true;
    btnAction.setText(R.string.btn_scan_text);
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

