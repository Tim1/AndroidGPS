package de.timweb.android;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formKey = "dC1VZzBtSzZzclJYdVFtQzhmYWptWGc6MQ")
public class BlueTrackApp extends Application {
	@Override
	public void onCreate() {
		ACRA.init(this);
		super.onCreate();
	}
}
