package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.logic.RxUtils;
import mediaaction.android.logic.User.UserManager;

public class ForgetPasswordActivity extends AppCompatActivity {

	@BindView(R.id.editTextUsername)
	EditText editTextUsername;

	@BindView(R.id.editTextEmail)
	EditText getEditTextEmail;

	private UserManager userManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		ButterKnife.bind(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		userManager = new UserManager();
	}

	@SuppressLint("CheckResult")
	@OnClick(R.id.forgetPasswordValidateButton)
	public void forgetPasswordValidate() {
		userManager.generatePasswordToken(editTextUsername.getText().toString(), getEditTextEmail.getText().toString())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
				.subscribe(x -> {
							Toast.makeText(this, "An email has been sent", Toast.LENGTH_LONG).show();
							finish();
						}
						, error ->
								Log.e("Error", "")
				);
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
}
