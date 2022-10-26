package com.ringtones.compose.foundation.common.song_alarm_manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ringtones.compose.data.SongAction
import com.ringtones.compose.foundation.receiver.MediaPlayerReceiver
import javax.inject.Inject

class SongAlarmManager @Inject constructor(
	private val context: Context
) {
	
	private val intent = Intent(
		SongAction.STOP.ordinal.toString(),
		null,
		context,
		MediaPlayerReceiver::class.java
	)
	
	fun setTimer(durationInMs: Long) {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		
		alarmManager.setExactAndAllowWhileIdle(
			AlarmManager.RTC_WAKEUP,
			System.currentTimeMillis() + durationInMs,
			PendingIntent.getBroadcast(
				context,
				0,
				intent,
				PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
			)
		)
	}
	
	fun cancelTimer() {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		
		if (stopAudioPendingIntent() != null) {
			alarmManager.cancel(stopAudioPendingIntent())
			stopAudioPendingIntent()!!.cancel()
		}
	}
	
	fun isExists(): Boolean {
		return stopAudioPendingIntent() != null
	}
	
	private fun stopAudioPendingIntent(): PendingIntent? {
		return PendingIntent.getBroadcast(
			context,
			0,
			intent,
			PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
		)
	}

}