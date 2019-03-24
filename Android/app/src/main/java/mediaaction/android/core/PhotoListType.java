package mediaaction.android.core;

import java.io.Serializable;


/**
 *  PhotoListType enulerates the different states of a picture
 *  These can be SOLD, UPLOADED or EMPTY
 */
public enum PhotoListType implements Serializable {
	SOLD,
	UPLOADED,
	EMPTY
}