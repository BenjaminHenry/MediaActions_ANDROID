package mediaaction.android.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils {

	public static String getLocation(Context context, ExifInterface exif) {

		Geocoder geocoder;
		List<Address> addresses;
		geocoder = new Geocoder(context, Locale.getDefault());

		double[] LatLng = getLatLong(exif);

		if (LatLng == null)
			return "";

		try {
			addresses = geocoder.getFromLocation(LatLng[0], LatLng[1], 1);
			return addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	static double[] getLatLong(ExifInterface exif) {
		double Latitude = 0;
		double Longitude = 0;

		String attrLATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
		String attrLATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
		String attrLONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
		String attrLONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

		if (attrLATITUDE == null || attrLONGITUDE == null)
			return null;

		if (attrLATITUDE_REF.equals("N")) {
			Latitude = convertToDegree(attrLATITUDE);
		} else {
			Latitude = 0 - convertToDegree(attrLATITUDE);
		}

		if (attrLONGITUDE_REF.equals("E")) {
			Longitude = convertToDegree(attrLONGITUDE);
		} else {
			Longitude = 0 - convertToDegree(attrLONGITUDE);
		}

		return new double[]{Latitude, Longitude};
	}

	static Double convertToDegree(String stringDMS) {
		Double result = null;
		String[] DMS = stringDMS.split(",", 3);

		String[] stringD = DMS[0].split("/", 2);
		Double D0 = Double.valueOf(stringD[0]);
		Double D1 = Double.valueOf(stringD[1]);
		Double FloatD = D0 / D1;

		String[] stringM = DMS[1].split("/", 2);
		Double M0 = Double.valueOf(stringM[0]);
		Double M1 = Double.valueOf(stringM[1]);
		Double FloatM = M0 / M1;

		String[] stringS = DMS[2].split("/", 2);
		Double S0 = Double.valueOf(stringS[0]);
		Double S1 = Double.valueOf(stringS[1]);

		Double FloatS = S0 / S1;

		result = (FloatD + (FloatM / 60) + (FloatS / 3600));

		return result;

	}
}
