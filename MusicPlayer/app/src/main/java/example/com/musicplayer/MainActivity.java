package example.com.musicplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Song> songList;
    private ListView songListView;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                return;
            }
        }

        mDrawerLayout = findViewById(R.id.MusicMainActivity);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        songList = new ArrayList<>();
        songListView = findViewById(R.id.song_list);
        songListView.setOnItemClickListener(songPicked);

        getSongList();
        sortList();

        SongAdapter adapter = new SongAdapter(MainActivity.this, R.layout.song_item, songList);
        songListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* switch (item.getItemId()) {
            case R.id.action_shuffle:
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicSrv = null;
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);*/

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicSrv = binder.getService();
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    AdapterView.OnItemClickListener songPicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView adapterView, View view, int i, long l) {
            musicSrv.setSong(i);
            musicSrv.playSong();
        }
    };


    public void sortList() {
        Collections.sort(songList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });
    }

    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor != null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);

            long thisId;
            String thisTitle;
            String thisArtist;

            //add songs to list
            do {
                thisId = musicCursor.getLong(idColumn);
                thisTitle = musicCursor.getString(titleColumn);
                thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            } while (musicCursor.moveToNext());
        }
    }


}
