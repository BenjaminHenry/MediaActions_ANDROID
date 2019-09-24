package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mediaaction.android.R;
import mediaaction.android.logic.User.UserManager;

public class ForgetPasswordActivity extends AppCompatActivity {

	private UserManager userManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		ButterKnife.bind(this);

		userManager = new UserManager();
	}

	@SuppressLint("CheckResult")
	@OnClick(R.id.forgetPasswordValidateButton)
	public void forgetPasswordValidate() {
	}
}
