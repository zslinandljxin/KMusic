package zsl.zhaoqing.com.kmusic.fragments;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zsl.zhaoqing.com.kmusic.R;
import zsl.zhaoqing.com.kmusic.controller.MusicPlayController;

/**
 * Created by Administrator on 2016/2/3.
 */
public class ConfigFragment extends Fragment {

    private final static String MUSIC_CONFIG_FILE = "music_config";
    private final static String PRESET_TAG = "presetreverb";
    private final static String BASS_TAG = "bassBoost";

    private LinearLayout layout;
    private MediaPlayer player;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Equalizer equalizer;
    private short brands;
    private BassBoost bassBoost;
    private PresetReverb presetReverb;

    private List<Short> reverbName = new ArrayList<>();
    private List<String> reverbValue = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        player = MusicPlayController.getInstance(getActivity()).getPlayer();
        preferences = getActivity().getSharedPreferences(MUSIC_CONFIG_FILE, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.config_fragment,null,false);
        setEqualizer();
        setBassBoost();
        setPresetReverb();
        return layout;
    }

    private void setEqualizer(){
        equalizer = new Equalizer(0,player.getAudioSessionId());
        equalizer.setEnabled(true);
        TextView eqText = new TextView(getActivity());
        eqText.setText("均衡器：");
        layout.addView(eqText);
        final short minEQLevel = equalizer.getBandLevelRange()[0];
        short maxEQlevel = equalizer.getBandLevelRange()[1];
        brands = equalizer.getNumberOfBands();
        for (short i = 0; i < brands; i++){
            TextView eqTextView = new TextView(getActivity());
            eqTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            eqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            final String tag = String.valueOf(equalizer.getCenterFreq(i) / 1000);
            eqTextView.setText( tag + " Hz");
            layout.addView(eqTextView);
            LinearLayout tmpLinear = new LinearLayout(getActivity());
            tmpLinear.setOrientation(LinearLayout.HORIZONTAL);
            TextView minDbTextView = new TextView(getActivity());
            minDbTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            minDbTextView.setText((minEQLevel / 100) + " dB");
            TextView maxDbTextView = new TextView(getActivity());
            maxDbTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            maxDbTextView.setText((maxEQlevel / 100) + " dB");
            SeekBar seekBar = new SeekBar(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            seekBar.setLayoutParams(params);
            seekBar.setMax(maxEQlevel - minEQLevel);
            seekBar.setProgress(preferences.getInt(tag,0));
            equalizer.setBandLevel(i, (short) (preferences.getInt(tag,0) + minEQLevel));
            final short brand = i;
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    equalizer.setBandLevel(brand, (short) (progress + minEQLevel));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    editor.putInt(String.valueOf(equalizer.getCenterFreq(brand) / 1000),seekBar.getProgress());
                    editor.commit();
                }
            });
            tmpLinear.addView(minDbTextView);
            tmpLinear.addView(seekBar);
            tmpLinear.addView(maxDbTextView);
            layout.addView(tmpLinear);
        }

    }

    private void setBassBoost(){
        bassBoost = new BassBoost(0,player.getAudioSessionId());
        bassBoost.setEnabled(true);
        TextView bbTextView = new TextView(getActivity());
        bbTextView.setText("重低音：");
        layout.addView(bbTextView);
        SeekBar seekBar = new SeekBar(getActivity());
        seekBar.setMax(1000);
        seekBar.setProgress(preferences.getInt(BASS_TAG,0));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bassBoost.setStrength((short) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt(BASS_TAG,seekBar.getProgress());
                editor.commit();
            }
        });
        layout.addView(seekBar);
    }

    private void setPresetReverb(){
        presetReverb = new PresetReverb(0,player.getAudioSessionId());
        presetReverb.setEnabled(true);
        TextView preTextView = new TextView(getActivity());
        preTextView.setText("音场：");
        layout.addView(preTextView);
        for (short i = 0; i < equalizer.getNumberOfPresets(); i++){
            reverbName.add(i);
            reverbValue.add(equalizer.getPresetName(i));
        }
        Spinner spinner = new Spinner(getActivity());
        spinner.setDropDownVerticalOffset(30);
        spinner.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,reverbValue));
        spinner.setSelection(preferences.getInt(PRESET_TAG,0));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presetReverb.setPreset(reverbName.get(position));
                editor.putInt(PRESET_TAG,position);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        layout.addView(spinner);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity().isFinishing() && player != null){
            equalizer.release();
            bassBoost.release();
            presetReverb.release();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
