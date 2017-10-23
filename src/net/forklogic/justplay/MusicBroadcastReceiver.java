
package net.forklogic.justplay;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class MusicBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = "MusicBroadcastReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "got a thingy!");

		if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED
				.equals(intent.getAction())
				|| BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(intent
						.getAction())) {
			Log.i(TAG, "Got bluetooth disconnect action");
			Intent msgIntent = new Intent(context, MusicPlaybackService.class);
			msgIntent.putExtra("Message", MusicPlaybackService.MSG_PAUSE);
			context.startService(msgIntent);
		} else if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
			Log.i(TAG, "Media Button Receiver: received media button intent: "
					+ intent);

			KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(
					Intent.EXTRA_KEY_EVENT);
			Log.i(TAG, "Got a key event");
			if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
				Log.i(TAG, "Got a key up event");

				int keyCode = keyEvent.getKeyCode();
				Intent msgIntent = new Intent(context,
						MusicPlaybackService.class);

				switch (keyCode) {
				case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
					Log.i(TAG, "key pressed KEYCODE_MEDIA_FAST_FORWARD");
					break;
				case KeyEvent.KEYCODE_MEDIA_NEXT:
					Log.i(TAG, "key pressed KEYCODE_MEDIA_NEXT");
					msgIntent
							.putExtra("Message", MusicPlaybackService.MSG_NEXT);
					context.startService(msgIntent);
					break;
				case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
					Log.i(TAG, "key pressed KEYCODE_MEDIA_PLAY_PAUSE");
					msgIntent.putExtra("Message",
							MusicPlaybackService.MSG_PLAYPAUSE);
					context.startService(msgIntent);
					break;
				case KeyEvent.KEYCODE_MEDIA_PAUSE:
					Log.i(TAG, "key pressed KEYCODE_MEDIA_PAUSE");
					msgIntent.putExtra("Message",
							MusicPlaybackService.MSG_PLAYPAUSE);
					context.startService(msgIntent);
					break;
				case KeyEvent.KEYCODE_MEDIA_PLAY:
					Log.i(TAG, "key pressed KEYCODE_MEDIA_PLAY");
					msgIntent.putExtra("Message",
							MusicPlaybackService.MSG_PLAYPAUSE);
					context.startService(msgIntent);
					break;
				case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
					Log.i(TAG, "key pressed KEYCODE_MEDIA_PREVIOUS");
					msgIntent.putExtra("Message",
							MusicPlaybackService.MSG_PREVIOUS);
					context.startService(msgIntent);
					break;
				case KeyEvent.KEYCODE_MEDIA_REWIND:
					Log.i(TAG, "key pressed KEYCODE_MEDIA_REWIND");
					break;
				case KeyEvent.KEYCODE_MEDIA_STOP:
					Log.i(TAG, "key pressed KEYCODE_MEDIA_STOP");
					msgIntent.putExtra("Message",
							MusicPlaybackService.MSG_PLAYPAUSE);
					context.startService(msgIntent);
					break;
				default:
					Log.i(TAG, "key pressed " + keyCode);
					break;
				}

			}
		}

	}
}