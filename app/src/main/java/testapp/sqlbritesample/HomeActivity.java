package testapp.sqlbritesample;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import testapp.sqlbritesample.adapter.CardAdapter;
import testapp.sqlbritesample.database.Db;
import testapp.sqlbritesample.model.ContentModel;
import testapp.sqlbritesample.retrofit.MyApiEndpointInterface;
import testapp.sqlbritesample.retrofit.RetrofitServiceGenerator;

public class HomeActivity extends AppCompatActivity implements CardAdapter.OnLoadMore{

    final String TAG = "HomeActivity";
    private ArrayList<ContentModel> contentList = new ArrayList<>();
    CardAdapter cardAdapter;
    MyApiEndpointInterface apiService;
    private boolean loadData = true;
    //    private DataManager mDataManager;

    @BindView(R.id.cardList) RecyclerView cardList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        cardAdapter = new CardAdapter(contentList, this);
        cardList.setAdapter(cardAdapter);
        cardAdapter.setOnLoadMore( this );
        //mDataManager = MyApplication.get().getDataManager();
        initLayout();
        initRetrofit();
        showContent(1);
        getRepos(4);
    }

    private void initLayout() {

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cardList.setLayoutManager(mLayoutManager);
    }

    private void updateAdapter(List<ContentModel> list) {
        if (list != null && list.size() > 0 && cardAdapter != null) {
            Log.e(TAG, "updateAdapter=>listSize-" + list.size());
            contentList.clear();
            contentList.addAll(list);
            cardAdapter.notifyDataSetChanged();
        } else {
            Log.e(TAG, "updateAdapter=>NotAbleToUpdateAdapter : " + cardAdapter);
        }
    }

    public void initRetrofit() {
        apiService = RetrofitServiceGenerator.createService(MyApiEndpointInterface.class);
    }

    private void getRepos(int catId) {

        if (loadData) {
            loadData = false;
            Observable<List<ContentModel>> contentListObservable =
                    apiService.getRepos();

            contentListObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<ContentModel>>() {
                        @Override
                        public void onCompleted() {
                            Log.e(TAG, "onCompleted Called;");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError Called : " + e.getMessage());
                        }

                        @Override
                        public void onNext(List<ContentModel> contentList) {
                            saveDataToDB(contentList);
                        }
                    });
        }
    }

    private void saveDataToDB(List<ContentModel> contentList) {
        Log.e(TAG, "Going to Save Data to Local DB contentList Size : " + contentList.size());
        if(contentList.size() > 0) {
            if(MyApplication.get().getDataManager().saveContentList(contentList) == 1) {
                loadData = true;
                showContent(2);
            }
        } else {
            Log.e(TAG, "No Data found on Server");
        }
    }

    public void showContent(int catId) {
        Log.e(TAG, "showContent=>Start");

        MyApplication.get().getDataManager().getContentQuery(catId)
                .subscribeOn(MyApplication.get().getDataManager().getScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SqlBrite.Query>() {
                    @Override public void call(SqlBrite.Query query) {
                        Log.e(TAG, "showContent=>query-" + query);
                        Cursor cursor = query.run();
                        Log.e(TAG, "showContent=>cursor.getCount()-" + cursor.getCount());
                        List<ContentModel> result = new ArrayList<>();
                        while (cursor.moveToNext()) {
                            ContentModel content = Db.ContentTable.parseCursor(cursor);
                            result.add(content);
                        }
                        cursor.close();
                        updateAdapter(result);
                    }
                });
    }

    @Override
    public void onLoadMoreCall(int id) {
        //getRepos(id);
    }

    public void getMore(View view) {
        Log.e(TAG, "getMore Called");
        getRepos(1);
    }
}
