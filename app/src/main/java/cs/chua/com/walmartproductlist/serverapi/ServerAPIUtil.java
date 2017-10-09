package cs.chua.com.walmartproductlist.serverapi;

/**
 * Created by christopherchua on 10/5/17.
 */

public class ServerAPIUtil {
    public final static String BASE_URL = "https://walmartlabs-test.appspot.com/_ah/api/walmart/v1/";
    //{apiKey}/{pageNumber}/{pageSize}
    public final static String PRODUCT_LIST = "walmartproducts/%1$s/%2$d/%3$d";

    public static RetrofitService getProductList() {
        return RetrofitClient.getInstance().getClient(BASE_URL).create(RetrofitService.class);
    }
}
