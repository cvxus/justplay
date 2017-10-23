
package net.forklogic.justplay;

import java.io.File;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class SettingsActivity extends PreferenceActivity {
	private static final String TAG = "SettingsActivity";

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		setupSimplePreferencesScreen();
	}

	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() {

		addPreferencesFromResource(R.xml.pretty_good_preferences);
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
        if(id == android.R.id.home){
        	onBackPressed();
        	return true;
        }
		return super.onOptionsItemSelected(item);
	}

	@Override
	@Deprecated
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {

		Log.i(TAG, "User clicked " + preference.getTitle());
		if (preference.getKey().equals("choose_music_directory_prompt")) {
			final File path = Utils.getRootStorageDirectory();
			DirectoryPickerOnClickListener picker = new DirectoryPickerOnClickListener(
					this, path);
			picker.showDirectoryPicker();
			Log.i(TAG, "User selected " + picker.path);
			return true;
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private static class DirectoryPickerOnClickListener implements
			OnClickListener {
		private SettingsActivity activity;
		private File path;
		private List<File> files;
		
		private DirectoryPickerOnClickListener(SettingsActivity activity,
				File root) {
			this.path = root;
			files = Utils.getPotentialSubDirectories(root);
			this.activity = activity;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == 0) {
				dialog.dismiss();
		        SharedPreferences prefs = activity.getSharedPreferences("PrettyGoodMusicPlayer", MODE_PRIVATE);

				Log.i(TAG,
						"Preferences update success: "
								+ prefs.edit()
										.putString("ARTIST_DIRECTORY",
												path.getAbsolutePath())
										.commit());
				prefs.edit().putInt("ARTIST_LIST_TOP", Integer.MIN_VALUE)
						.putInt("ARTIST_LIST_INDEX", Integer.MIN_VALUE)
						.commit();
				return;
			}
			if (which == 1) {
				dialog.dismiss();
				if (path.getParentFile() != null) {
					path = path.getParentFile();
				}
				files = Utils.getPotentialSubDirectories(path);
				showDirectoryPicker();
				
			} else {
				dialog.dismiss();
				File f = files.get(which - 2);
				path = new File(path, f.getName());
				files = Utils.getPotentialSubDirectories(path);
				showDirectoryPicker();
			}

		}

		private void showDirectoryPicker() {
			final Item[] items = new Item[files.size() + 2];
			for(int i = 0;i<files.size();i++){
				items[i + 2] = new Item(files.get(i).getName(), R.drawable.ic_action_collection);
			}
			
			items[0] = new Item(path.getAbsolutePath(), R.drawable.ic_pgmp_launcher);
			items[1] = new Item(activity.getResources().getString(R.string.directorydialogup), android.R.drawable.ic_menu_upload);
			
			ListAdapter adapter = new ArrayAdapter<Item>(
				    activity,
				    android.R.layout.select_dialog_item,
				    android.R.id.text1, items){
				        public View getView(int position, View convertView, ViewGroup parent) {
				            View v = super.getView(position, convertView, parent);
				            TextView tv = (TextView)v.findViewById(android.R.id.text1);

				            tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

				            int dp5 = (int) (5 * activity.getResources().getDisplayMetrics().density + 0.5f);
				            tv.setCompoundDrawablePadding(dp5);

				            return v;
				        }
				    };
			new AlertDialog.Builder(activity).setTitle(activity.getResources().getString(R.string.directorydialogprompt))
					.setIcon(android.R.drawable.ic_menu_zoom)
					.setAdapter(adapter, this).show();
		}
		
	}
	
	
	public static class Item{
	    public final String text;
	    public final int icon;
	    public Item(String text, Integer icon) {
	        this.text = text;
	        this.icon = icon;
	    }

	    @Override
	    public String toString() {
	        return text;
	    }
	}
	
}
