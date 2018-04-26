package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.rmd2k.guitarstudio_android.DataModel.BarNoData;
import com.example.rmd2k.guitarstudio_android.DataModel.EditNoteInfo;
import com.example.rmd2k.guitarstudio_android.DataModel.Note;
import com.example.rmd2k.guitarstudio_android.DataModel.NoteNoData;
import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.NoteEditView;
import com.example.rmd2k.guitarstudio_android.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class GuitarNoteViewActivity extends AppCompatActivity {

    ListView lstGuitarNoteView;
    NoteEditView noteEditView;
    Context mContext;
    GuitarNoteViewAdapter adapter;
    NotesModel notesModel;
    private final MyHandler myHandler = new MyHandler(this);

    boolean editMode = false;
    Drawable oldSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guitar_note_view);

        mContext = getApplicationContext();
        notesModel = NotesModel.getInstance(mContext);
        notesModel.setGuitarNotesWithNotesTitle("天空之城");
        lstGuitarNoteView = findViewById(R.id.lstGuitarNoteView);
        noteEditView = findViewById(R.id.noteEditView);
        adapter = new GuitarNoteViewAdapter(mContext, myHandler);
        lstGuitarNoteView.setAdapter(adapter);

        // 保存ListView点击效果
        oldSelector = lstGuitarNoteView.getSelector();
    }

    public static class MyHandler extends Handler {
        private final WeakReference<GuitarNoteViewActivity> mActivity;

        private MyHandler(GuitarNoteViewActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            GuitarNoteViewActivity activity = mActivity.get();
            activity.adapter.refreshVisibleItem(activity.lstGuitarNoteView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guitar_note_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_edit:
                if (!editMode) {
                    editMode = true;
                    noteEditView.setMaxHeight(getWindow().getDecorView().getHeight());
                    lstGuitarNoteView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                } else {
                    editMode = false;
                    noteEditView.setMaxHeight(0);
                    lstGuitarNoteView.setSelector(oldSelector);
                }
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changeNoteType(View view) {
        String tag = view.getTag().toString();
        switch (tag) {
            case "2":
                System.out.println("note_minim");
                break;
            case "4":
                System.out.println("note_crotcheta");
                break;
            case "8":
                System.out.println("note_quaver");
                break;
            case "16":
                System.out.println("note_demiquaver");
                break;
        }
    }

    public void changeFretNo(View view) {
        int pushNum = Integer.parseInt(view.getTag().toString());
    }

    public void addBarNo(View view) {
        int barNo = notesModel.getCurrentEditNote().getBarNo();
        BarNoData barNoData = new BarNoData();
        ArrayList<NoteNoData> noteNoDataArrayList = new ArrayList<>();
        barNoData.setNoteNoDataArray(noteNoDataArrayList);
        notesModel.addBarNoDataAtIndex(barNoData, barNo);
        addBlankNoteNo(barNo, 0);

        EditNoteInfo editNoteInfo = notesModel.getCurrentEditNote();
        editNoteInfo.setNoteNo(0);
        editNoteInfo.setStringNo(0);
        refreshGuitarNoteView();
    }

    public void deleteBarNo(View view) {

    }

    public void addNoteNo(View view) {
        EditNoteInfo editNoteInfo = notesModel.getCurrentEditNote();
        int barNo = editNoteInfo.getBarNo();
        int noteNo = editNoteInfo.getNoteNo();

        addBlankNoteNo(barNo, noteNo);

        refreshGuitarNoteView();
    }

    private void addBlankNoteNo(int barNo, int noteNo) {

        NoteNoData noteNoData = new NoteNoData();
        noteNoData.setNoteType("4");
        ArrayList<Note> noteArrayList = new ArrayList<>();
        Note note = new Note();
        note.setStringNo("-1");
        note.setFretNo("-1");
        note.setPlayType("Normal");
        noteArrayList.add(note);
        noteNoData.setNoteArray(noteArrayList);
        notesModel.addNoteNoDataAtIndex(noteNoData, barNo, noteNo);
    }

    public void deleteNoteNo(View view) {

    }

    public void finishEdit(View view) {

    }

    public void cancelEdit(View view) {

    }

    private void refreshGuitarNoteView() {
        notesModel.calNotesSize();
        //lstGuitarNoteView.invalidate();
        myHandler.sendEmptyMessage(0);
    }
}