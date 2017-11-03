package cs.chua.com.walmartproductlist.presenter;

import java.util.List;

import cs.chua.com.walmartproductlist.model.local.ApplicationModel;
import cs.chua.com.walmartproductlist.model.remote.Product;
import cs.chua.com.walmartproductlist.model.remote.Products;
import cs.chua.com.walmartproductlist.networkapi.RetrofitService;
import cs.chua.com.walmartproductlist.networkapi.ServerAPIUtil;
import cs.chua.com.walmartproductlist.view.ProductScreenView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by christopherchua on 11/2/17.
 */

public class ProductScreenPresenter {

    // used for server call
    private RetrofitService retrofitService;
    private Callback<Products> serverCallback;
    private Call<Products> retrofitCall;

    private ProductScreenView productScreenView;

    public ProductScreenPresenter(final ProductScreenView productScreenView) {
        this.productScreenView = productScreenView;
        retrofitService = ServerAPIUtil.getProductList();
    }

    public void refreshProductList(final int page, final int count) {
        productScreenView.addLoadingView();
        // server command for getting the list of items
        serverCallback = new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                productScreenView.removeLoadingView();
                if (call.isCanceled()) {
                    return;
                }
                if (response.isSuccessful()) {
                    final Products products = response.body();
                    final List<Product> productList = products.getProducts();
                    ApplicationModel.getInstance().addToProductList(productList);
                    ApplicationModel.getInstance().updatePaginationCount(products.getTotalProducts());
                    productScreenView.updateProductListView(productList);
                } else {
                    // TODO handle error
                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                productScreenView.removeLoadingView();
                if (call.isCanceled()) {
                    return;
                }
            }
        };

        final String url = String.format(ServerAPIUtil.PRODUCT_LIST, ServerAPIUtil.API_KEY, page, count);
        retrofitCall = retrofitService.getProducts(ServerAPIUtil.BASE_URL+url);
        retrofitCall.enqueue(serverCallback);
    }

    public void onDestroy() {
        if (retrofitCall != null) {
            retrofitCall.cancel();
        }
        serverCallback = null;
        retrofitService = null;
        productScreenView = null;
    }
}
