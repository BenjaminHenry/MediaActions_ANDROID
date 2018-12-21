package mediaaction.android.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Inspired from <a href="http://stackoverflow.com/a/33384551/815737">http://stackoverflow.com/a/33384551/815737</a>
 */
public class ProgressRequestBody extends RequestBody {
	private static final int SEGMENT_SIZE = 8192; // okio.Segment.SIZE
	private static final UploadCallbacks NO_OP_UPLOAD_CALLBACK = (uploaded, total) -> {
		// Nothing to do
	};

	private final MediaType mediaType;
	private final byte[] bytes;
	private final File file;
	private final UploadCallbacks listener;

	public ProgressRequestBody(@Nullable final MediaType mediaType, @NonNull final byte[] bytes, @Nullable final UploadCallbacks listener) {
		this.mediaType = mediaType;
		this.bytes = bytes;
		this.file = null;
		this.listener = listener != null ? listener : NO_OP_UPLOAD_CALLBACK;
	}

	public ProgressRequestBody(@Nullable final MediaType mediaType, @NonNull final File file, @Nullable final UploadCallbacks listener) {
		this.mediaType = mediaType;
		this.bytes = null;
		this.file = file;
		this.listener = listener != null ? listener : NO_OP_UPLOAD_CALLBACK;
	}

	@Override
	public MediaType contentType() {
		return mediaType;
	}

	@Override
	public long contentLength() throws IOException {
		return file != null ? file.length() : bytes.length;
	}

	@Override
	public void writeTo(BufferedSink sink) throws IOException {
		Source source = file != null ? Okio.source(file) : Okio.source(new ByteArrayInputStream(bytes));
		try {
			long uploaded = 0;
			long read;

			while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
				uploaded += read;
				sink.flush();
				listener.onProgressUpdate(uploaded, contentLength());
			}
		} finally {
			IOUtils.closeQuietly(source);
		}
	}

	public interface UploadCallbacks {
		void onProgressUpdate(long uploaded, long total);
	}

}