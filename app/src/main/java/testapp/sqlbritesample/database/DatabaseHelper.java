package testapp.sqlbritesample.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import testapp.sqlbritesample.model.ContentModel;

public class DatabaseHelper {

    private BriteDatabase db;

    public DatabaseHelper(Context context) {
        SqlBrite mSqlBrite = new SqlBrite.Builder().build();
        db = mSqlBrite.wrapDatabaseHelper(new DbOpenHelper(context), Schedulers.io());
    }

    public Observable<List<ContentModel>> getContent(int catId) {
        return db.createQuery(Db.ContentTable.TABLE_CONTENT, "SELECT * FROM " + Db.ContentTable.TABLE_CONTENT + " WHERE category_id=" + catId + " ORDER BY id DESC")
                .map(new Func1<SqlBrite.Query, List<ContentModel>>() {
                    @Override
                    public List<ContentModel> call(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        //Log.d("SIZE", cursor.getCount() + "");
                        List<ContentModel> result = new ArrayList<>(cursor.getCount());
                        while (cursor.moveToNext()) {
                            ContentModel content = Db.ContentTable.parseCursor(cursor);
                            result.add(content);
                        }
                        cursor.close();
                        return result;
                    }
                });
    }

    public Observable<SqlBrite.Query> getContentQuery(int catId) {
        Log.e("DatabaseHelper", "getContent1->" + catId);
        return db.createQuery(Db.ContentTable.TABLE_CONTENT, "SELECT * FROM " + Db.ContentTable.TABLE_CONTENT);
    }

    public Observable<ContentModel> saveContent(final ContentModel contentModel) {
        return Observable.create(new Observable.OnSubscribe<ContentModel>() {
            @Override
            public void call(Subscriber<? super ContentModel> subscriber) {
                long result = db.insert(Db.ContentTable.TABLE_CONTENT, Db.ContentTable.toContentValues(contentModel));

                if (result >= 0) subscriber.onNext(contentModel);
                subscriber.onCompleted();
            }
        });
    }

    int saveContentList(final List<ContentModel> contentList) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ContentModel contentModel : contentList) {
                try {
                    long isInserted = db.insert(Db.ContentTable.TABLE_CONTENT, Db.ContentTable.toContentValues(contentModel));
                    Log.e("DatabaseHelper", "Going to insert -> " + contentModel.getId() + " isInserted : " + isInserted);
                } catch (Exception e) {
                    Log.e("DatabaseHelper", e.getMessage());
                }
            }
            transaction.markSuccessful();
            return 1;
        } finally {
            transaction.end();
        }
    }
}
