package com.example.aleksandar.skynet;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/** Class {@link PlayerActivity} displays video
 * and enables controls: play(video playing),
 * pause(pauses video), stop(closes activity)
 * and skip(skipping to second in video)
 *
 * @author SkyNet team
 */

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView playerView;
    private Button pauseButton;
    private Button playButton;
    private Button stopButton;
    private Button skipButton;
    private EditText textSkip;

    /**
     * @param bundle contains data from calling activity
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_player);

        playerView = (YouTubePlayerView) findViewById(R.id.player_view);
        playerView.initialize(YoutubeConnector.KEY, this);
        pauseButton = (Button) findViewById(R.id.button_pause);
        playButton = (Button) findViewById(R.id.button_play);
        stopButton = (Button) findViewById(R.id.button_stop);
        skipButton = (Button) findViewById(R.id.button_skip);
        textSkip = (EditText) findViewById(R.id.text_skip);
    }

    /**
     * Method shows a message in case initialization fails
     * @param provider
     * @param result
     */
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {
        Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show();
    }

    /**
     * Method implements controls play, pause, stop and skip
     * On stop, current activity closes
     * @param provider
     * @param player
     * @param restored
     */
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player,
                                        boolean restored) {
        if (!restored) {
            player.cueVideo(getIntent().getStringExtra("VIDEO_ID"));
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
            player.loadVideo(getIntent().getStringExtra("VIDEO_ID"));

            textSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int a = player.getDurationMillis();
                        if (a <= 0) {
                            new AlertDialog.Builder(v.getContext()).setMessage("Video is loading, please wait...").show();
                        } else {
                            new AlertDialog.Builder(v.getContext()).setMessage("Video duration: " + player.getDurationMillis() / 1000 + " s").show();
                        }
                    } catch (Exception e) {
                        new AlertDialog.Builder(v.getContext()).setMessage("Video is closed, press back").show();
                    }
                }
            });

            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        player.pause();
                    } catch (Exception e) {
                        new AlertDialog.Builder(v.getContext()).setMessage("Video is closed, press back").show();
                    }
                }
            });

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        player.play();
                    } catch (Exception e) {
                        new AlertDialog.Builder(v.getContext()).setMessage("Video is closed, press back").show();
                    }
                }
            });

            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //player.release();
                    PlayerActivity.this.finish();
                }
            });

            skipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String input = textSkip.getText().toString().trim();
                    int skip;
                    try {
                        skip = Integer.parseInt(input) * 1000;
                        if (skip > player.getDurationMillis())
                            throw new Exception("Wrong duration");
                        else
                            player.seekToMillis(skip);
                    } catch (Exception e) {
                        if (e.getClass().equals(NumberFormatException.class))
                            new AlertDialog.Builder(v.getContext()).setMessage("Please enter a number").show();
                        else if( e.getMessage() == "Wrong duration")
                            new AlertDialog.Builder(v.getContext()).setMessage("Please enter a number smaller than video duration").show();
                        else
                            new AlertDialog.Builder(v.getContext()).setMessage("Video is closed, press back").show();
                    }
                }
            });
        }
    }
}
