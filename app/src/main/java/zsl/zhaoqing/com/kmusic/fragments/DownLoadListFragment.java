package zsl.zhaoqing.com.kmusic.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import zsl.zhaoqing.com.kmusic.R;
import zsl.zhaoqing.com.kmusic.adapters.DownloadListAdapter;
import zsl.zhaoqing.com.kmusic.constants.Contants;
import zsl.zhaoqing.com.kmusic.interfaces.MyOnTouchListener;
import zsl.zhaoqing.com.kmusic.utils.MusicDownload;

/**
 * Created by Administrator on 2016/1/8.
 */
public class DownLoadListFragment extends Fragment implements GestureDetector.OnGestureListener,
        MyOnTouchListener{

    private ImageButton backButton;
    private ListView downloadListView;
    private DownloadListAdapter adapter;

    private GestureDetector detector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detector = new GestureDetector(getActivity(),this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.download_list_fragment,container, false);
        backButton = (ImageButton) view.findViewById(R.id.back_button);
        downloadListView = (ListView) view.findViewById(R.id.download_music_list);
        adapter = new DownloadListAdapter(getActivity(),(List<MusicDownload>) getArguments().
                getSerializable(Contants.DOWN_TASK));
        downloadListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public void updateList(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean listener(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 50){
            getActivity().onBackPressed();
            return true;
        }
        return false;
    }
}
