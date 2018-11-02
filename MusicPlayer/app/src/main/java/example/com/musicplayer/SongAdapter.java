package example.com.musicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song>{
    Context context;
    ViewHolder viewHolder = null;
    int resource = 0;

    public SongAdapter(Context context, int resource, List<Song> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Song song = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(this.resource, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.titleView = convertView.findViewById(R.id.song_title);
            viewHolder.artistView = convertView.findViewById(R.id.song_artist);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.titleView.setText(song.getTitle());
        viewHolder.artistView.setText(song.getArtist());

        return convertView;
    }


    public class ViewHolder{
        TextView titleView ;
        TextView artistView;
    }
}
