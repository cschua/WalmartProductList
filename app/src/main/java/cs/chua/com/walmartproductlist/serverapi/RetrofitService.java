package cs.chua.com.walmartproductlist.serverapi;

import cs.chua.com.walmartproductlist.model.remote.Products;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by christopherchua on 10/3/17.
 */

public interface RetrofitService {

    @GET
    Call<Products> getProducts(
            @Url String url
    );
}
