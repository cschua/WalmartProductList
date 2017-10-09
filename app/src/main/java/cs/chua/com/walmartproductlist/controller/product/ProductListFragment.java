package cs.chua.com.walmartproductlist.controller.product;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.model.local.PaginationPageCount;
import cs.chua.com.walmartproductlist.model.remote.Product;
import cs.chua.com.walmartproductlist.model.remote.Products;
import cs.chua.com.walmartproductlist.serverapi.RetrofitService;
import cs.chua.com.walmartproductlist.serverapi.ServerAPIUtil;
import cs.chua.com.walmartproductlist.util.PaginationScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by christopherchua on 10/7/17.
 */

public class ProductListFragment extends Fragment {

    public final static String TAG = ProductListFragment.class.getSimpleName();
    public final static String ARGS_PRODUCTS = "productlist";
    // the server's total product count for this category
    public final static String ARGS_TOTAL_PRODUCTS = "totalproducts";
    // used in pagination
    public final static String ARGS_TOTAL_PAGE_LOADED = "currentstate";
    // used in the adapter for adding pagination support
    public final static String ARGS_LOADING_ADDED = "loadingadded";

    // used for server call
    private RetrofitService retrofitService;
    private Callback<Products> serverCallback;
    private Call<Products> retrofitCall;

    // recyclerView
    private RecyclerView productsRecyclerView;
    private ProductListAdapter productListAdapter;
    private LinearLayoutManager layoutManager;
    private PaginationScrollListener paginationScrollListener;

    // saved in and retrieved from saveInstanceState
    private int totalProducts = 0;
    private int currentPage = 1;

    public ProductListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.product_list_layout,
                container, false);
        productsRecyclerView = rootView.findViewById(R.id.products_recyclerview);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        productsRecyclerView.setLayoutManager(layoutManager);
        productsRecyclerView.setHasFixedSize(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        retrofitService = ServerAPIUtil.getProductList();

        final boolean isLoadingAdded;
        final List<Product> productList;
        if (savedInstanceState == null) {
            productList = new ArrayList<>();
            isLoadingAdded = false;
        } else {
            productList = savedInstanceState.getParcelableArrayList(ARGS_PRODUCTS);
            totalProducts = savedInstanceState.getInt(ARGS_TOTAL_PRODUCTS, 0);
            isLoadingAdded = savedInstanceState.getBoolean(ARGS_LOADING_ADDED, false);
            currentPage = savedInstanceState.getInt(ARGS_TOTAL_PAGE_LOADED, 1);
        }

        productListAdapter = new ProductListAdapter(getContext(), isLoadingAdded);
        productsRecyclerView.setAdapter(productListAdapter);

        // only send a server call if we don't already have a list from the saveInstanceState
        if (productList == null || totalProducts == 0) {
            sendGetProductsCommand(currentPage, PaginationPageCount.TOTAL_ITEMS_PER_PAGE);
        } else {
            updateProductsAdapter(totalProducts, productList);
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState){
        outState.putInt(ARGS_TOTAL_PRODUCTS, totalProducts);
        if (productListAdapter != null) {
            outState.putParcelableArrayList(ARGS_PRODUCTS,
                    new ArrayList<Parcelable>(productListAdapter.getProductList()));
            outState.putBoolean(ARGS_LOADING_ADDED, productListAdapter.isLoadingAdded());
        }
        if (paginationScrollListener != null) {
            outState.putInt(ARGS_TOTAL_PAGE_LOADED, paginationScrollListener.getTotalPagesLoaded());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (retrofitCall != null) {
            retrofitCall.cancel();
        }
        if (productListAdapter != null) {
            productListAdapter.onDestroy();
        }
        paginationScrollListener = null;
        serverCallback = null;
        retrofitService = null;
        super.onDestroy();
    }

    private void sendGetProductsCommand(final int page, final int count) {
        // show / hide loading, reuse pagination loading view
        productListAdapter.addLoadingView();
        // server command for getting the list of items
        serverCallback = new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                productListAdapter.removeLoadingView();
                if (call.isCanceled()) {
                    return;
                }
                if (response.isSuccessful()) {
                    final Products products = response.body();
                    updateProductsAdapter(products.getTotalProducts(), products.getProducts());
                } else {
                    // TODO handle error
                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                productListAdapter.removeLoadingView();
                if (call.isCanceled()) {
                    return;
                }
                Log.w(TAG, t.getMessage());
                // TODO handle error
            }
        };
        final String apiKey = getResources().getString(R.string.walmartlabs_api_key);
        final String url = String.format(ServerAPIUtil.PRODUCT_LIST, apiKey, page, count);
        retrofitCall = retrofitService.getProducts(url);
        retrofitCall.enqueue(serverCallback);
    }

    private void updateProductsAdapter(final int totalProducts,
                                       final List<Product> productList) {
        final Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        updatePagination(totalProducts);
        updateList(productList);
    }

    private void updatePagination(final int totalProducts) {
        this.totalProducts = totalProducts;

        final PaginationPageCount productPageCount = new PaginationPageCount(totalProducts);
        if (productPageCount == null) {
            return;
        }

        if (paginationScrollListener == null) {
            paginationScrollListener = new PaginationScrollListener(layoutManager,
                    productPageCount.getTotalPages(), currentPage) {
                @Override
                protected void loadMoreItems(final int currentPage) {
                    sendGetProductsCommand(currentPage,
                            productPageCount.getItemsCountForPage(currentPage));
                }
            };
        }
        // add the scrollListener to the recycler view to support pagination
        productsRecyclerView.addOnScrollListener(paginationScrollListener);
        // adapter needs scrollListner to update the progress view at the end of the list and to
        // update the pagination when the list of items are modified in size
        productListAdapter.setPaginationScrollListener(paginationScrollListener);
    }

    private void updateList(final List<Product> productList) {
        productListAdapter.updateList(productList);
        productListAdapter.notifyDataSetChanged();
    }

    public List<Product> getProductList() {
        final List<Product> productList = new ArrayList<>(productListAdapter.getProductList());
        // clean up last item, which is an item for the progress used in pagination
        final int index = productList.size()-1;
        if (productList.get(index).getId().length() == 0) {
            productList.remove(index);
        }
        return productList;
    }
}
