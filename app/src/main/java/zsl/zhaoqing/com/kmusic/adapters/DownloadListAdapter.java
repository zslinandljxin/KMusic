package zsl.zhaoqing.com.kmusic.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import zsl.zhaoqing.com.kmusic.R;
import zsl.zhaoqing.com.kmusic.interfaces.MusicCallback;
import zsl.zhaoqing.com.kmusic.model.MyButton;
import zsl.zhaoqing.com.kmusic.utils.MusicDownload;

/**
 * Created by Administrator on 2016/2/12.
 */
public class DownloadListAdapter extends ArrayAdapter<MusicDownload> {

    private Context context;
    private List<MusicDownload> dataSource;

    public DownloadListAdapter(Context context,List<MusicDownload> objects) {
        super(context,0, objects);
        this.context = context;
        dataSource = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null){
            view = ((Activity)context).getLayoutInflater().inflate(R.layout.download_item_view,null,false);
        }else {
            view = convertView;
        }
        TextView songTitle = (TextView) view.findViewById(R.id.song_title);
        songTitle.setText(dataSource.get(position).getSongName());
        MyButton startDown = (MyButton) view.findViewById(R.id.start_down);
        MyButton pauseDown = (MyButton) view.findViewById(R.id.pause_down);
        MyButton deleteDown = (MyButton) view.findViewById(R.id.delete_down);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        dataSource.get(position).setProgressBar(progressBar);
        startDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.get(position).execute();
            }
        });
        pauseDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.get(position).cancel(true);
            }
        });
        deleteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.get(position).cancel(true);
                ((MusicCallback)context).deleteDownMusic(dataSource.get(position));
            }
        });
        return view;
    }
}
