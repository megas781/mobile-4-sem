package glerbkalachev.supercrud.model.database;
//Константы для определения схемы таблицы в SQLite'е
public class ContactDbSchema {
    public static final class ContactsTable {
        public static final String NAME = "contacts";
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String FIO = "fio";
            public static final String PHONE = "phone";
            public static final String EMAIL = "email";
        }
    }
}