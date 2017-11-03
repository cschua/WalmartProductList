package cs.chua.com.walmartproductlist.networkapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by christopherchua on 10/5/17.
 */

public class RetrofitClient {
    Retrofit retrofit = null;

    private RetrofitClient() {
    }

    private static class SingletonHelper {
        private static final RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            final Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(baseUrl);

            final Gson gson = new GsonBuilder().create();
            builder.addConverterFactory(GsonConverterFactory.create(gson));
            retrofit = builder.build();
        }
        return retrofit;
    }
}
