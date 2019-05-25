package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mediaaction.android.R;
import mediaaction.android.logic.Request.RequestDTO;

public class RequestWallAdapter extends RecyclerView.Adapter<RequestWallAdapter.ViewHolder> {

	private static OnRequestClickListener listener;
	private List<RequestDTO> requestList;

	RequestWallAdapter(List<RequestDTO> _requestList, OnRequestClickListener _listener) {
		requestList = _requestList;
		listener = _listener;
	}

	@NonNull
	@Override
	public RequestWallAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull RequestWallAdapter.ViewHolder viewHolder, int i) {
		viewHolder.bind(requestList.get(i), listener);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public int getItemCount() {
		return requestList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		private TextView requestTitle;
		private TextView requestReward;
		private TextView requestDescription;

		ViewHolder(@NonNull View itemView) {
			super(itemView);
			requestTitle = itemView.findViewById(R.id.RequestItemTitle);
			requestReward = itemView.findViewById(R.id.RequestItemReward);
			requestDescription = itemView.findViewById(R.id.RequestItemDescription);
		}

		@SuppressLint("SetTextI18n")
		void bind(RequestDTO request, OnRequestClickListener listener) {
			requestTitle.setText(request.title);
			requestReward.setText(request.reward.toString() + " €");
			requestDescription.setText(request.description);
			itemView.setOnClickListener(view -> listener.OnRequestClicked(request));
		}
	}

	public interface OnRequestClickListener {
		void OnRequestClicked(RequestDTO request);
	}

}
