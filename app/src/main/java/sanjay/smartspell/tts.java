package sanjay.smartspell;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class tts extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private EditText editText;
    private Button buttonSpeak;
    private Locale locale;
    private String language;
    private String Input;
    private TextView textView;
    private Animation animation;
    private int duration;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_audio);
        tts = new TextToSpeech(this, this);
        buttonSpeak=(Button)findViewById(R.id.play);
        Bundle bundle;
        bundle=getIntent().getExtras();
        //   textView=(TextView)findViewById(R.id.textView);
        language=bundle.getString("language");
        Input=bundle.getString("_input");
        //   textView.setText(Input);
        if(language=="English")
        {
            locale=Locale.ENGLISH;
        }
        if(language=="Chinese")
        {
            locale=Locale.CHINESE;
        }
        buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                speakOut();
            }
        });
    }

    @Override
    public void onDestroy() {
// Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                buttonSpeak.setEnabled(true);
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut() {
        String text = Input;
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        animation= AnimationUtils.loadAnimation(this,R.anim.animation_slide);
        //   textView.setText(Input);
        duration=Input.length()*1000+16000;
        animation.setDuration(duration);
        // textView.startAnimation(animation);
    }


    //  @Override
    //  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.activity_main, menu);
    //  return true;
}