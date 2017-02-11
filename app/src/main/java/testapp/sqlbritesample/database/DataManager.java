package testapp.sqlbritesample.database;

import android.content.Context;

import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import rx.Observable;
import rx.Scheduler;
import testapp.sqlbritesample.model.ContentModel;

public class DataManager {

    String TAG = "DataManager";
    private DatabaseHelper mDatabaseHelper;
    private Scheduler mScheduler;

    public DataManager(Context context, Scheduler scheduler) {
        mDatabaseHelper = new DatabaseHelper(context);
        mScheduler = scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        mScheduler = scheduler;
    }

    public Scheduler getScheduler() {
        return mScheduler;
    }

    public Observable<ContentModel> saveContent(ContentModel content) {
        return mDatabaseHelper.saveContent(content);
    }

    public int saveContentList(List<ContentModel> contentList) {
        return mDatabaseHelper.saveContentList(contentList);
    }

    public Observable<List<ContentModel>> getContent(int category_id) {
        return mDatabaseHelper.getContent(category_id);
    }

    public Observable<SqlBrite.Query> getContentQuery(int category_id) {
        return mDatabaseHelper.getContentQuery(category_id);
    }
}
