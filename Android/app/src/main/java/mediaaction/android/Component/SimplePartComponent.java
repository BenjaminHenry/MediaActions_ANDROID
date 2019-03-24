package mediaaction.android.Component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import mediaaction.android.R;

<<<<<<< HEAD
=======
import org.junit.Assert.*;

>>>>>>> 8641d676c84e30c5edefd6ff59913f1fa8bbe668
/**
 * DEPRECATED
 */
public class SimplePartComponent extends ConstraintLayout {
	public SimplePartComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.simple_part_component, this, true);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SimplePartComponent, 0, 0);
		TextView simplePartTitleText = findViewById(R.id.simplePartTitle);
		simplePartTitleText.setText(a.getString(R.styleable.SimplePartComponent_simplePartTitle));
		a.recycle();
	}
}