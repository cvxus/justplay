
package net.forklogic.justplay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class AbstractMusicList extends Activity {
    private static final String TAG = "AbstractMusicList";

    protected BroadcastReceiver exitReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("net.forklogic.ACTION_EXIT");
        exitReceiver = new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "Received exit request, shutting down...");
                Intent msgIntent = new Intent(getBaseContext(), MusicPlaybackService.class);
                msgIntent.putExtra("Message", MusicPlaybackService.MSG_STOP_SERVICE);
                startService(msgIntent);
                finish();
            }

        };
        registerReceiver(exitReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(exitReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.abstract_music_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_now_playing) {
            if (MusicPlaybackService.isRunning()) {
                Intent intent = new Intent(AbstractMusicList.this, NowPlaying.class);
                intent.putExtra("From_Notification", true);
                startActivity(intent);
            } else {
                Toast.makeText(AbstractMusicList.this, R.string.nothing_playing, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(AbstractMusicList.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_exit) {
            Intent msgIntent = new Intent(getBaseContext(), MusicPlaybackService.class);
            msgIntent.putExtra("Message", MusicPlaybackService.MSG_STOP_SERVICE);
            startService(msgIntent);
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
