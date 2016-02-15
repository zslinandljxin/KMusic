package zsl.zhaoqing.com.kmusic.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import zsl.zhaoqing.com.kmusic.R;
import zsl.zhaoqing.com.kmusic.interfaces.MusicCallback;
import zsl.zhaoqing.com.kmusic.model.MusicInfo;
import zsl.zhaoqing.com.kmusic.model.MyButton;

/**
 * Created by Administrator on 2016/2/9.
 */
public class SearchListAdapter extends ArrayAdapter<MusicInfo> {

    private  Context context;
    private List<MusicInfo> dataSource;

    public SearchListAdapter(Context context, List<MusicInfo> objects) {
        super(context, 0, objects);
        this.context = context;
        dataSource = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        String text = dataSource.get(position).getSongName();
        if (text == null || text.equals("")){
            return super.getView(position, convertView, parent);
        }
        if (convertView == null){
            view = ((Activity)context).getLayoutInflater().inflate(R.layout.search_item_view,null,false);
        }else {
            view = convertView;
        }
        TextView musicInfo = (TextView) view.findViewById(R.id.music_info_text);
        musicInfo.setText(text);
        MyButton downloadButton = (MyButton) view.findViewById(R.id.download_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MusicCallback)context).downloadMusic(dataSource.get(position));
            }
        });
        return view;
    }
}
