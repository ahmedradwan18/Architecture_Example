package com.example.architectureexample.Model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Note.class, version = 1)
public abstract class NoteDataBase extends RoomDatabase {


    // we used this var to turn this class into singleton , singleton means that we can't
    // create multiple instances from DB so we use the same instance every time
    private static NoteDataBase instance;

    // to access our DAO
    public abstract NoteDao noteDao();


    // we here create our only database instance , synchronized means set one thread can access
    // that method at time
    public static synchronized NoteDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDataBase.class
                    , "note_database").fallbackToDestructiveMigration().
                    addCallback(roomcallback).
                    build()
            ;
        }
        return instance;
    }


    private static RoomDatabase.Callback roomcallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateDBAsyncTask(instance).execute();
        }
    };

    private static class populateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        public populateDBAsyncTask(NoteDataBase noteDataBase) {
            noteDao = noteDataBase.noteDao();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.Insert(new Note("Title 1", "Description 1", 1));
            noteDao.Insert(new Note("Title 2", "Description 2", 2));
            noteDao.Insert(new Note("Title 3", "Description 3", 3));
            noteDao.Insert(new Note("Title 4", "Description 4", 4));

            return null;
        }
    }

}
