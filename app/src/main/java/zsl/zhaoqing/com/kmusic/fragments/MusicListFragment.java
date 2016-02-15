package zsl.zhaoqing.com.kmusic.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zsl.zhaoqing.com.kmusic.R;
import zsl.zhaoqing.com.kmusic.activity.MainActivity;
import zsl.zhaoqing.com.kmusic.adapters.MusicListAdapter;
import zsl.zhaoqing.com.kmusic.controller.MSearchController;
import zsl.zhaoqing.com.kmusic.interfaces.MusicCallback;
import zsl.zhaoqing.com.kmusic.model.MusicInfo;
import zsl.zhaoqing.com.kmusic.utils.MyLog;

/**
 * Created by Administrator on 2016/1/4.
 */
public class MusicListFragment extends Fragment{

    private static final String TAG = "musicListFragment";

    private ListView musicListView;
    private Toolbar toolbar;
    private MusicListAdapter adapter;
    private SearchView searchView;
    private List<MusicInfo> musicInfoList;

    public MusicListFragment(){
        Log.d("musicFragment","---------> create");
        musicInfoList = new ArrayList<>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MyLog.d(TAG,"---------> onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MyLog.d(TAG,"---------> oncreate");
        super.onCreate(savedInstanceState);
        //使fragment支持菜单
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyLog.d(TAG,"---------> oncreateview");
        View view = inflater.inflate(R.layout.music_list_fragment, null, false);
        toolbar = (Toolbar) view.findViewById(R.id.play_list_bar);
        musicListView = (ListView) view.findViewById(R.id.music_list);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.logo_pic);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        adapter = new MusicListAdapter(getActivity(),musicInfoList);
        musicListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MyLog.d(TAG,"---------> onviewcreate");
        super.onViewCreated(view, savedInstanceState);
        ((MusicCallback)getActivity()).supportToolBar(toolbar);
        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MusicCallback)getActivity()).musicSelected(musicInfoList,position);
            }
        });
        musicListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                TextView msg = new TextView(getActivity());
                msg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                msg.setGravity(Gravity.CENTER);
                msg.setTextSize(20);
                msg.setText(getActivity().getResources().getString(R.string.delete_music));
                builder.setCustomTitle(msg);
                final AlertDialog dialog = builder.create();
                dialog.show();
                msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        ((MusicCallback)getActivity()).deleteMusinInfo(musicInfoList.get(position));
                    }
                });
                return true;
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MyLog.d(TAG,"---------> onCreateOptionsMenu");
        inflater.inflate(R.menu.toolbar_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ((MusicCallback)getActivity()).notifySearchMusic(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.music_list_menu:
                showPlayList();
                break;
            case R.id.search_music_menu:
                MSearchController searcher = new MSearchController(getActivity());
                searcher.getMusicSearchResult();
                break;
            case R.id.download_music_menu:
                ((MusicCallback)getActivity()).openDownFragment();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void showPlayList() {
        ListView listView = new ListView(getActivity());
        listView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        listView.setDividerHeight(1);
        listView.setHeaderDividersEnabled(true);
        listView.setFooterDividersEnabled(true);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,
                ((MusicCallback)getActivity()).getPlayList());
        listView.setAdapter(adapter);
        PopupWindow popupWindow = new PopupWindow(listView, ViewGroup.LayoutParams.
                MATCH_PARENT, 500);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(toolbar);
    }

    /**
     * 更新listview列表
     * @param musicInfos
     */
    public void updateListview(List<MusicInfo> musicInfos){
        MyLog.d(TAG,"---------> updateListview");
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
}
