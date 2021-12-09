package id.refactory.users.services;

import id.refactory.users.models.BaseModel;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("api")
    Observable<BaseModel> getUsers(@Query("results") int result);
}
