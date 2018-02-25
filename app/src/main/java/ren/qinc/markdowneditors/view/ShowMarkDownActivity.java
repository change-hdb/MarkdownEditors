package ren.qinc.markdowneditors.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import ren.qinc.markdowneditors.R;
import ren.qinc.markdowneditors.utils.FileUtils;
import ren.qinc.markdowneditors.utils.SystemUtils;
import ren.qinc.markdowneditors.widget.MarkdownPreviewView;

public class ShowMarkDownActivity extends AppCompatActivity implements MarkdownPreviewView.OnLoadingFinishListener{

    private static final String TAG = "SMDA + hdbp";

    private MarkdownPreviewView mdView;

    private String currentFilePath;
    private static final String SCHEME_FILE = "file";

    String content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_mark_down);

        mdView = (MarkdownPreviewView) findViewById(R.id.hdb_mdview);

        mdView.setOnLoadingFinishListener(this);


        getIntentData();

//        startNewThread();


    }





    private void getIntentData() {
        Intent intent = this.getIntent();
        int flags = intent.getFlags();
        if ((flags & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0) {
            if (intent.getAction() != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
                if (SCHEME_FILE.equals(intent.getScheme())) {
                    //文件
                    String type = getIntent().getType();
                    // mImportingUri=file:///storage/emulated/0/Vlog.xml
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri uri = intent.getData();

                    if (uri != null && SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
                        //这是一个文件
                        currentFilePath = FileUtils.uri2FilePath(getBaseContext(), uri);

                    }
                }
            }
        }
    }



    public void getFileName(){

    }


    @Override
    public void onLoadingFinish() {

        startNewThread();
    }


    public void startNewThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileInputStream inputStream = null;
                try {
                    try {
                        inputStream = new FileInputStream(currentFilePath);
                        content = new String(SystemUtils.readInputStream(inputStream));
                        String title = "";
                        mdView.post(new Runnable() {
                            @Override
                            public void run() {
                                mdView.parseMarkdown(content, true);
//                                CommonMarkdownActivity.startMarkdownActivity(ShowMarkDownActivity.this,"", content);
                            }
                        });
                    } finally {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "run: ", e);
                }

            }
        }).start();
    }




}
