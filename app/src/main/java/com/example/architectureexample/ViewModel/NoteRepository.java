package com.example.architectureexample.ViewModel;

import android.app.Application;
import android.os.AsyncTask;

import com.example.architectureexample.Model.Note;
import com.example.architectureexample.Model.NoteDao;
import com.example.architectureexample.Model.NoteDataBase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class NoteRepository {
    private NoteDao noteDao;
    public LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        //here we make instance of db
        NoteDataBase dataBase = NoteDataBase.getInstance(application);

        // here we assigned the instance to dao so we can use methods of dao
        noteDao = dataBase.noteDao();

        allNotes = noteDao.GetAllNotes();

    }

    public void Insert(Note note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void Update(Note note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);

    }

    public void Delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);

    }

    public void DeleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDao).execute();

    }

    public LiveData<List<Note>> getAllNotes() {


        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.Insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.Update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.Delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        private DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.DeleteAllNotes();
            return null;
        }
    }


}
