package mobi.mateam.blechat.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScanResultsAdapter extends RecyclerView.Adapter<ScanResultsAdapter.ViewHolder> {

  private static final Comparator<ScanResult> SORTING_COMPARATOR = (lhs, rhs)
      -> lhs.getDevice().getAddress().compareTo(rhs.getDevice().getAddress());
  private final List<ScanResult> data = new ArrayList<>();
  private OnAdapterItemClickListener onAdapterItemClickListener;
  private final View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override public void onClick(View v) {

      if (onAdapterItemClickListener != null) {
        onAdapterItemClickListener.onAdapterViewClick(v);
      }
    }
  };

  public void addScanResult(ScanResult bleScanResult) {
    // Not the best way to ensure distinct devices, just for sake on the demo.
    for (int i = 0; i < data.size(); i++) {

      if (data.get(i).getDevice().equals(bleScanResult.getDevice())) {
        data.set(i, bleScanResult);
        notifyItemChanged(i);
        return;
      }
    }

    data.add(bleScanResult);
    Collections.sort(data, SORTING_COMPARATOR);
    notifyDataSetChanged();
  }

  public void clearScanResults() {
    data.clear();
    notifyDataSetChanged();
  }

  public ScanResult getItemAtPosition(int childAdapterPosition) {
    return data.get(childAdapterPosition);
  }

  @Override public int getItemCount() {
    return data.size();
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    final ScanResult rxBleScanResult = data.get(position);
    final BluetoothDevice bleDevice = rxBleScanResult.getDevice();
    holder.line1.setText(String.format("%s (%s)", bleDevice.getAddress(), bleDevice.getName()));
    holder.line2.setText(String.format("RSSI: %d", rxBleScanResult.getRssi()));
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.two_line_list_item, parent, false);
    itemView.setOnClickListener(onClickListener);
    return new ViewHolder(itemView);
  }

  public void setOnAdapterItemClickListener(OnAdapterItemClickListener onAdapterItemClickListener) {
    this.onAdapterItemClickListener = onAdapterItemClickListener;
  }

  public interface OnAdapterItemClickListener {

    void onAdapterViewClick(View view);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(android.R.id.text1) public TextView line1;
    @BindView(android.R.id.text2) public TextView line2;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
