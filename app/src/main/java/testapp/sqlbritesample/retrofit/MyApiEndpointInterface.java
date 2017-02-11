package testapp.sqlbritesample.retrofit;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;
import testapp.sqlbritesample.model.ContentModel;

public interface MyApiEndpointInterface {

    @GET("joginder89/repos")
    Observable<List<ContentModel>> getRepos();
}
