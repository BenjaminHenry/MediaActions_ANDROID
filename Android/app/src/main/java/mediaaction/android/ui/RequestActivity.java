package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mediaaction.android.R;
import mediaaction.android.logic.Request.RequestDTO;

public class RequestActivity extends AppCompatActivity {

	public static final String EXTRA_REQUEST_DATA = "RequestActivity.EXTRA_REQUEST_DATA";

	public static Intent prepare(Context context, RequestDTO requestData) {
		return new Intent(context, RequestActivity.class)
				.putExtra(EXTRA_REQUEST_DATA, requestData);
	}

	private static RequestDTO extractUserData(Intent intent) {
		return (RequestDTO) intent.getSerializableExtra(EXTRA_REQUEST_DATA);
	}

	@BindView(R.id.RequestTitle)
	TextView requestTitle;
	@BindView(R.id.RequestDescription)
	TextView requestDescription;
	@BindView(R.id.RequestReward)
	TextView requestReward;

	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);
		ButterKnife.bind(this);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);
		setTitle("Request");

		RequestDTO requestData = extractUserData(getIntent());

		requestTitle.setText(requestData.title);
		requestDescription.setText(requestData.description);
		requestReward.setText("Reward : " + requestData.reward.toString() + " €");
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
}
