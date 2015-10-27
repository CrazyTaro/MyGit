package us.bestapp.henrytaro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import us.bestapp.henrytaro.player.model.Song;

/**
 * Created by xuhaolin on 15/10/20.
 */
public class TrackAdapter extends BaseAdapter {
    private List<Song> mList;
    private Context mContext;

    public TrackAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(List<Song> list) {
        this.mList = list;
//        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        Song song;
        if (convertView != null) {
            holder = (Holder) convertView.getTag();
        } else {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_item, null);
            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.artist = (TextView) convertView.findViewById(R.id.tv_artist);
            convertView.setTag(holder);
        }
        song = mList.get(position);
        holder.name.setText(song.getTitle());
        holder.artist.setText(song.getAlbum());
        return convertView;
    }

    private class Holder {
        public TextView name;
        public TextView artist;
    }
}
