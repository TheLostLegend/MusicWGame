package com.serwylo.beatgame;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import games.spooky.gdx.nativefilechooser.android.AndroidFileChooser;

public class AndroidLauncher extends AndroidApplication {
	private static final int WRITE_REQUEST_CODE = 69420;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},WRITE_REQUEST_CODE);
			}
		}
		initialize(new BeatFeetGame(new AndroidPlatformListener(this), true, new AndroidFileChooser(this)), config);
	}
}
