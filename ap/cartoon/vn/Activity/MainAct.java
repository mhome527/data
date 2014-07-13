package cartoon.youtube.vn.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import cartoon.youtube.vn.R;


/**
 * Created by huynhtran on 7/4/14.
 */
public class MainAct extends BaseAct implements View.OnClickListener {

    private ImageButton imgConan;
    private ImageButton imgDoraemon;
    private ImageButton imgTomJarry;
    private ImageButton imgOther;
    @Override
    public int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    public void onAfterCreate(Bundle savedInstanceState) {
        imgConan = (ImageButton)findViewById(R.id.imgConan);
        imgDoraemon = (ImageButton)findViewById(R.id.imgDoraemon);
        imgTomJarry = (ImageButton)findViewById(R.id.imgTomJarry);
        imgOther = (ImageButton)findViewById(R.id.imgOther);

        imgConan.setOnClickListener(this);
        imgDoraemon.setOnClickListener(this);
        imgTomJarry.setOnClickListener(this);
        imgOther.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        
        switch(view.getId()){
            case R.id.imgConan:
            case R.id.imgDoraemon:
            case R.id.imgTomJarry:
            case R.id.imgOther:
                startActivityAct(ListDetailAct.class);
                break;
        }
    }
}
