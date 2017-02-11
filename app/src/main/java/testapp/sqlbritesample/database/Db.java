package testapp.sqlbritesample.database;

import android.content.ContentValues;
import android.database.Cursor;

import testapp.sqlbritesample.model.ContentModel;


public class Db {

    public Db() { }

    public static abstract class ContentTable {
        static final String TABLE_CONTENT = "content";
        static final String COLUMN_ID = "id";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_LANGUAGE = "language";

        static final String CREATE_CONTENT_TABLE = "CREATE TABLE " + TABLE_CONTENT + " ("
                + COLUMN_ID +" INTEGER,"
                + COLUMN_NAME + " TEXT NOT NULL,"
                + COLUMN_LANGUAGE + " TEXT NOT NULL"
                + " );";

        static ContentValues toContentValues(ContentModel contentModel) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, contentModel.getId());
            values.put(COLUMN_NAME, contentModel.getName());
            values.put(COLUMN_LANGUAGE, contentModel.getLanguage());
            return values;
        }

        public static ContentModel parseCursor(Cursor cursor) {
            ContentModel contentModel = new ContentModel();
            contentModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            contentModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            contentModel.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LANGUAGE)));
            return contentModel;
        }
    }
}
