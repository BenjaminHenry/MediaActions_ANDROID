package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import mediaaction.android.R;

public class PhotoListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<Bitmap> imageList;

	PhotoListAdapter(Context _context, List<Bitmap> _imageList) {
		imageList = _imageList;
		context = _context;
	}

	@Override
	public int getCount() {
		return imageList.size();
	}

	@Override
	public Object getItem(int i) {
		return imageList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {

		if (inflater == null)
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (view == null) {
			assert inflater != null;
			view = inflater.inflate(R.layout.image_item, null);
		}

		ImageView img = view.findViewById(R.id.imageItem);
		img.setImageBitmap(imageList.get(i));

		return view;
	}
}
