package glerbkalachev.supercrud.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.UUID;

import glerbkalachev.supercrud.model.database.ContactCursor;
import glerbkalachev.supercrud.model.database.ContactDatabaseBuilder;
import glerbkalachev.supercrud.model.database.ContactDbSchema.ContactsTable;

public class ContactLab {

    private static ContactLab sInstance;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ContactLab(Context context) {
        //Достаем глобальный контекст приложения
        mContext = context.getApplicationContext();
        //Инициализируем базу данных
        mDatabase = new ContactDatabaseBuilder(mContext).getWritableDatabase();
    }

    //Публичные методы для работы с моделью Contact
    public static ContactLab get(Context context) {
        if (sInstance == null) {
            sInstance = new ContactLab(context);
        }
        return sInstance;
    }

    public ArrayList<Contact> getContactList() {

        ArrayList<Contact> contactList = new ArrayList<>();

        //два null обозначают "брать все!"
        ContactCursor contactCursor = queryContacts(null, null);
        try {
            contactCursor.moveToFirst();
            /* Свойство cursor.IsAfterLast() сообщает, что указатель вышел за пределы последней
             * строки и сейчас указывает на пустоту. В цикле while мы говорим, что когда это произойдет
             * цикл нужно завершить */
            while (!contactCursor.isAfterLast()) {
                /* Каждый раз, когда мы двигаемся вперед .moveToNext(), cursor начинает указывать
                 * на новую строку, и, следовательно, мы получим другой объект при вызове метода
                 * cursor.getContact() */
                contactList.add(contactCursor.getContact());
                contactCursor.moveToNext();
            }
        } finally {
            //Зачем-то вызывется cursor.close(). Наверное, это освобождает память
            contactCursor.close();
        }
        return contactList;
    }



    public Contact getContact(UUID uuid) {

        ContactCursor contactCursor = queryContacts(
                ContactsTable.Cols.UUID + " = ?",
                new String[]{uuid.toString()});

        try {
            //Если из базы данных ничего не найдено
            if (contactCursor.getCount() == 0) {
                return null;
            } else {
                //По идеи по uuid мы должны получить только одну строку, поэтому идем
                //К первому попавшемуся и возвращаем его
                contactCursor.moveToFirst();
                return contactCursor.getContact();
            }
        } finally {
            //Не забываем закрывать курсоры
            contactCursor.close();
        }
    }
    public void addContact(Contact c) {
        ContentValues values = getContentValues(c);
        mDatabase.insertOrThrow(ContactsTable.NAME,null,values);
    }
    public void deleteContact(UUID contactId) {
        mDatabase.delete(
                ContactsTable.NAME,
                ContactsTable.Cols.UUID + " = ?",
                new String[] {contactId.toString()});
    }
    public void updateContact(Contact contact) {
        /*
        Конструкция update принимает:
        1) Имя таблицы, чтобы определить, куда вставлять
        2) Объект класса ContentValues, чтобы определить, что вставлять
        3) логическое выражение, которое будет вставлено в запрос после ключегого "where"
        4) Массив параметров, которые будут вставляться вапрос вместо знаков вопроса (?)
        Аргументы не вставляются в логическое выражение напрямую, а передаются через массив строк
        во избежание SQL-инъекций
         */
        mDatabase.update(
                ContactsTable.NAME,
                getContentValues(contact),
                ContactsTable.Cols.UUID + " = ?",
                new String[] {contact.getId().toString()});

    }

    //Метод для преобразования объекта Java в строку таблицы
    private static ContentValues getContentValues(Contact contact) {

        ContentValues values = new ContentValues();
        values.put(ContactsTable.Cols.UUID,contact.getId().toString());
        values.put(ContactsTable.Cols.FIO, contact.mFio);
        values.put(ContactsTable.Cols.PHONE, contact.mPhone);
        values.put(ContactsTable.Cols.EMAIL, contact.mEmail);

        return values;
    }

    //Создание запроса данных
    private ContactCursor queryContacts(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ContactsTable.NAME,
                null,whereClause,
                whereArgs,
                null,
                null,
                null);

        return new ContactCursor(cursor);
    }

}
