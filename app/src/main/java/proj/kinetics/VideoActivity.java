package proj.kinetics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {


    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        this.setFinishOnTouchOutside(false);
        url = getIntent().getStringExtra("url");

        Uri uri = Uri.parse(url);
        final ProgressDialog pDialog = new ProgressDialog(VideoActivity.this);

        final VideoView videoView = (VideoView) findViewById(R.id.previewvideo);
        final ImageView iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        MediaController mediacontroller = new MediaController(VideoActivity.this);

        pDialog.setTitle("Please wait");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.show();
        mediacontroller.setAnchorView(videoView);
        videoView.setMediaController(mediacontroller);

        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoView.start();
            }
        });

    }
}
