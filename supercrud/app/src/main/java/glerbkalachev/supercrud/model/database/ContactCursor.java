package glerbkalachev.supercrud.model.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import glerbkalachev.supercrud.model.Contact;
import glerbkalachev.supercrud.model.database.ContactDbSchema.ContactsTable;

//Класс позволяющий преобразовывать строку таблицы в объект контакта
public class ContactCursor extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ContactCursor(Cursor cursor) {
        super(cursor);
    }

    public Contact getContact() {
        String uuidString = getString(getColumnIndex(ContactsTable.Cols.UUID));
        String fio = getString(getColumnIndex(ContactsTable.Cols.FIO));
        String phone = getString(getColumnIndex(ContactsTable.Cols.PHONE));
        String email = getString(getColumnIndex(ContactsTable.Cols.EMAIL));

        Contact c = new Contact(UUID.fromString(uuidString));
        c.mFio = fio;
        c.mPhone = phone;
        c.mEmail = email;

        return c;
    }
}
