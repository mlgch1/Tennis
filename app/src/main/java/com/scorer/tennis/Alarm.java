
package com.scorer.tennis;

import android.app.DialogFragment;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import java.io.IOException;

// ***********************************

public class Alarm extends DialogFragment {

	private static MediaPlayer	myMediaPlayer;

	// ***********************************

	public static void soundAlarm(Context context) {
		if (myMediaPlayer == null) {
			myMediaPlayer = new MediaPlayer();
			try {
				myMediaPlayer.setDataSource(context, getAlarmUri());
				final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				
				audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM), AudioManager.FLAG_PLAY_SOUND);
				
				
				if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
					myMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
					myMediaPlayer.prepare();
					myMediaPlayer.start();
				}
			} catch (IOException e) {
				System.out.println("OOPS");
			}
		}
	}

	// ***********************************

	public static void stopAlarm() {
		if (myMediaPlayer != null) {
			myMediaPlayer.stop();
			myMediaPlayer = null;
		}
	}

	// ***********************************

	private static Uri getAlarmUri() {
		Uri alarm = null;
		if (alarm == null) {
			alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if (alarm == null) {
				alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			}
		}
		return alarm;
	}

	// ***********************************

	@Override
	public void onDestroy() {
		super.onDestroy();
		// We need to stop the MediaPlayer when the activity is finished.
		myMediaPlayer.stop();
		myMediaPlayer.release();
	}
}
