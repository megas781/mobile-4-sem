package glerbkalachev.supercrud.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import glerbkalachev.supercrud.model.database.ContactDbSchema.ContactsTable;

//Класс, создающий базу данных, если её еще нет
public class ContactDatabaseBuilder extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "contactBase.db";

    public ContactDatabaseBuilder(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "create table %1$s (" +
                "_id integer primary key autoincrement," +
                "%2$s," + //uuid
                "%3$s," + //fio
                "%4$s," + //phone
                "%5$s" + //email
                ")";

        db.execSQL(String.format(createTableQuery,
                ContactsTable.NAME,
                ContactsTable.Cols.UUID,
                ContactsTable.Cols.FIO,
                ContactsTable.Cols.PHONE,
                ContactsTable.Cols.EMAIL));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
