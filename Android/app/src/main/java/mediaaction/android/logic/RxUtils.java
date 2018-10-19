package mediaaction.android.logic;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {

	private RxUtils() {
	}

	@NonNull
	public static <T> Maybe<T> justOrEmpty(@Nullable T item) {
		return (item == null ? Maybe.<T>empty() : Maybe.just(item))
				.subscribeOn(Schedulers.computation());
	}


	public static <T> SingleTransformer<T, T> displayCommonRestErrorDialogSingle(@NonNull Context context) {
		return tObservable -> tObservable.doOnError(displayCommonRestErrorDialogConsumer(context));
	}

	public static <T> FlowableTransformer<T, T> displayCommonRestErrorDialogFlowable(@NonNull Context context) {
		return tObservable -> tObservable.doOnError(displayCommonRestErrorDialogConsumer(context));
	}

	public static <T> MaybeTransformer<T, T> displayCommonRestErrorDialogMaybe(@NonNull Context context) {
		return tObservable -> tObservable.doOnError(displayCommonRestErrorDialogConsumer(context));
	}

	@NonNull
	private static Consumer<Throwable> displayCommonRestErrorDialogConsumer(@NonNull Context context) {
		return throwable -> {
			new AlertDialog.Builder(context)
					.setCancelable(false)
					.setMessage(throwable.getMessage())
					.setPositiveButton("ok", null)
					.create().show();
		};
	}
}