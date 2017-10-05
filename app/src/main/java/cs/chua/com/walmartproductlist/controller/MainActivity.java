package cs.chua.com.walmartproductlist.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.model.remote.Product;
import cs.chua.com.walmartproductlist.model.remote.Products;
import cs.chua.com.walmartproductlist.serverapi.RetrofitService;
import cs.chua.com.walmartproductlist.serverapi.ServerAPIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RetrofitService retrofitService;
    private RecyclerView productsRecyclerView;
    private ProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productsRecyclerView = (RecyclerView) findViewById(R.id.products_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        productsRecyclerView.setLayoutManager(layoutManager);
        productsRecyclerView.setHasFixedSize(true);
        productListAdapter = new ProductListAdapter(this);

        retrofitService = ServerAPIUtil.getProductList();
        loadProductList(getResources().getString(R.string.walmartlabs_api_key), 1, 10);
    }

    public void loadProductList(final String apiKey, final int pageNumber, final int pageSize) {
        final String url = String.format(ServerAPIUtil.PRODUCT_LIST, apiKey, pageNumber, pageSize);
        retrofitService.getProducts(url).enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                if(response.isSuccessful()) {
                    List<Product> productList = response.body().getProducts();
                    Log.d(TAG, "productList size: " + productList.size());
                    updateProducts(productList);
                } else {
                    // TODO handle error
                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                Log.w(TAG, t.getMessage());
                // TODO handle error
            }
        });
    }

    private void updateProducts(final List<Product> items) {
        if (!isFinishing()) {
            productListAdapter.setList(items);
            productsRecyclerView.setAdapter(productListAdapter);
        }
    }
}
