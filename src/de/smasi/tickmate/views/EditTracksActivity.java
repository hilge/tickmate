package de.smasi.tickmate.views;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.smasi.tickmate.R;
import de.smasi.tickmate.database.TracksDataSource;
import de.smasi.tickmate.models.Track;

public class EditTracksActivity extends ListActivity {

	ArrayAdapter<Track> tracksAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_edit_tracks);
		// Show the Up button in the action bar.
		setupActionBar();
				
		loadTracks();
		registerForContextMenu(this.getListView());
	}
	
	protected void loadTracks()  {
		TracksDataSource ds = new TracksDataSource(this);
		Track[] ms = new Track[0];
		ds.open();
		ms = ds.getMyTracks().toArray(ms);
		ds.close();
		tracksAdapter = new TrackListAdapter(this, ms);
		this.getListView().setAdapter(tracksAdapter);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_tracks, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_choose_track:
		case R.id.action_choose_track_menu:
			Intent intent = new Intent(this, ChooseTrackActivity.class);
		    startActivityForResult(intent, 1);			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, ShowTrackActivity.class);
		intent.putExtra("track_id", ((Track)tracksAdapter.getItem(position)).getId());
	    startActivityForResult(intent, 1);				
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v("Tickmate", "tracks sub activity returned." + resultCode);
		loadTracks();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    
		switch (item.getItemId()) {
		
		case R.id.edit_tracks_edit:
			Track t = (Track)tracksAdapter.getItem((int)info.id);
			Intent intent = new Intent(this, EditTrackActivity.class);
			intent.putExtra("track_id", t.getId());
			startActivityForResult(intent, 1);	
			return true;
			/*
		case R.id.edit_tracks_deactivate:
			Track t2 = (Track)tracksAdapter.getItem((int)info.id);
			TracksDataSource ds = new TracksDataSource(this);
			ds.open();
			ds.deleteTrack(t2);
			ds.close();
			loadTracks();
			return true;
			*/
			
		case R.id.edit_tracks_delete:
			Track t3 = (Track)tracksAdapter.getItem((int)info.id);
			TracksDataSource ds2 = new TracksDataSource(this);
			ds2.open();
			ds2.deleteTrack(t3);
			ds2.close();
			loadTracks();
			return true;
		}		
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.edit_tracks_context_menu, menu);
	}

}
