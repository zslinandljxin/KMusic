package zsl.zhaoqing.com.kmusic.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zsl.zhaoqing.com.kmusic.R;
import zsl.zhaoqing.com.kmusic.adapters.SearchListAdapter;
import zsl.zhaoqing.com.kmusic.interfaces.MusicCallback;
import zsl.zhaoqing.com.kmusic.interfaces.MyOnTouchListener;
import zsl.zhaoqing.com.kmusic.model.MusicInfo;
import zsl.zhaoqing.com.kmusic.utils.MyLog;

/**
 * Created by Administrator on 2016/1/23.
 */
public class SearchListFragment extends Fragment implements GestureDetector.OnGestureListener,
        MyOnTouchListener{

    private final static String TAG = "searchListFragment";

    private ListView searchListView;
    private SearchView searchView;
    private ImageView backButton;
    private SearchListAdapter adapter;
    private GestureDetector detector;

    private List<MusicInfo> musicInfoList;

    @Override
    public void onAttach(Context context) {
        Log.d("searchListFragment","---------> onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MyLog.d(TAG,"---------> onCreate");
        super.onCreate(savedInstanceState);
        musicInfoList = new ArrayList<>();
        adapter = new SearchListAdapter(getActivity(),musicInfoList);
        detector = new GestureDetector(getActivity(),this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_list_fragment,null,false);
        searchListView = (ListView) view.findViewById(R.id.search_list);
        searchView = (SearchView) view.findViewById(R.id.search);
        backButton = (ImageButton) view.findViewById(R.id.back_button);
        searchListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setQueryHint(getActivity().getResources().getString(R.string.download_music));
                ((MusicCallback)getActivity()).searchFromServer(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MusicCallback)getActivity()).chooseMusicFromSearch(musicInfoList.get(position));
            }
        });
    }

    /**
     * 更新listview列表
     * @param musicInfos
     */
    public void notifyUpdateListview(List<MusicInfo> musicInfos){
        if (musicInfos == null || musicInfos.size() == 0){
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.search_no_music),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (musicInfoList != null){
            musicInfoList.clear();
        }
        musicInfoList.addAll(musicInfos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        MyLog.d(TAG,"---------> onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        MyLog.d(TAG,"---------> onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        MyLog.d(TAG,"---------> onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        MyLog.d(TAG,"---------> onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        MyLog.d(TAG,"---------> onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        MyLog.d(TAG,"---------> onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        MyLog.d(TAG,"---------> onDetach");
        super.onDetach();
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

    @Override
    public boolean listener(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

}
