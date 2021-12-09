package id.refactory.users.clients;

import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import id.refactory.users.BuildConfig;
import id.refactory.users.services.UserService;
import id.refactory.users.utils.ConstantUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserClient {
    public static UserService userService() {
        OkHttpClient.Builder okHttp = new OkHttpClient().newBuilder();
        okHttp.retryOnConnectionFailure(true);
        okHttp.connectTimeout(60, TimeUnit.SECONDS);
        okHttp.writeTimeout(60, TimeUnit.SECONDS);
        okHttp.readTimeout(60, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(s -> Log.e("API-LOG", s));
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        if (BuildConfig.DEBUG) okHttp.addInterceptor(interceptor);

        okHttp.addInterceptor(chain -> {
            Request request = chain.request();
            Request newReq = request.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept-Encoding", "identity")
                    .addHeader("Connection", "close")
                    .addHeader("Transfer-Encoding", "chunked")
                    .build();
            return chain.proceed(newReq);
        });

        OkHttpClient client = okHttp.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantUtil.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        return retrofit.create(UserService.class);
    }
}
