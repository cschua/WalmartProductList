package cs.chua.com.walmartproductlist.controller.product;

import android.app.Activity;
import android.os.Bundle;
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
import cs.chua.com.walmartproductlist.controller.product.adapter.ProductBaseAdapter;
import cs.chua.com.walmartproductlist.model.local.ApplicationModel;
import cs.chua.com.walmartproductlist.model.local.PaginationCount;
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

public abstract class ProductBaseFragment extends Fragment {

    public abstract LinearLayoutManager getLayoutManager();
    public abstract ProductBaseAdapter getAdapter(boolean isLoadingAdded);

    public final static String TAG = ProductBaseFragment.class.getSimpleName();
    public static final String ARGS_DEFAULT_POSITION = "argDefaultPosition";
    // used in pagination
    public final static String ARGS_TOTAL_PAGE_LOADED = "argCurrentState";
    // used in the adapter for adding pagination support
    public final static String ARGS_LOADING_ADDED = "argLoadingAdded";

    // used for server call
    private RetrofitService retrofitService;
    private Callback<Products> serverCallback;
    private Call<Products> retrofitCall;

    // recyclerView
    protected RecyclerView productsRecyclerView;
    protected ProductBaseAdapter productListAdapter;
    protected LinearLayoutManager layoutManager;
    private PaginationScrollListener paginationScrollListener;

    private int defaultPosition = 0;
    private int currentPage = 1;

    public ProductBaseFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.product_list_layout,
                container, false);
        productsRecyclerView = rootView.findViewById(R.id.products_recyclerview);
        layoutManager = getLayoutManager();
        productsRecyclerView.setLayoutManager(layoutManager);
        productsRecyclerView.setHasFixedSize(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        retrofitService = ServerAPIUtil.getProductList();

        final boolean isLoadingAdded;
        final List<Product> productList = ApplicationModel.getInstance().getProducts();
        if (savedInstanceState == null) {
            final Bundle bundle = getArguments();
            if (bundle != null) {
                defaultPosition = bundle.getInt(ARGS_DEFAULT_POSITION);
            }
            isLoadingAdded = false;
        } else {
            isLoadingAdded = savedInstanceState.getBoolean(ARGS_LOADING_ADDED, false);
            currentPage = savedInstanceState.getInt(ARGS_TOTAL_PAGE_LOADED, 1);
        }

        productListAdapter = getAdapter(isLoadingAdded);
        productsRecyclerView.setAdapter(productListAdapter);

        // only send a server call if we don't already have a list from the saveInstanceState
        if (productList == null || productList.size() == 0) {
            sendGetProductsCommand(currentPage, PaginationCount.TOTAL_ITEMS_PER_PAGE);
        } else {
            productsRecyclerView.scrollToPosition(defaultPosition);
            updateProductsAdapter(productList);
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState){
        if (productListAdapter != null) {
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

    public boolean onBackPressed() {
        final int currentIndex = layoutManager.findFirstCompletelyVisibleItemPosition();
        if (currentIndex == defaultPosition) {
            return false;
        }
        if (currentIndex > defaultPosition) {
            productsRecyclerView.smoothScrollToPosition(currentIndex - 1);
        } else {
            productsRecyclerView.smoothScrollToPosition(currentIndex + 1);
        }
        return true;
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
                    final List<Product> productList = products.getProducts();
                    ApplicationModel.getInstance().addToProductList(productList);
                    ApplicationModel.getInstance().updatePaginationCount(products.getTotalProducts());
                    updateProductsAdapter(productList);
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

    private void updateProductsAdapter(final List<Product> productList) {
        final Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        updatePagination();
        productListAdapter.updateList(productList);
        productListAdapter.notifyDataSetChanged();
    }

    private void updatePagination() {
        final PaginationCount productPageCount = ApplicationModel.getInstance().getPaginationCount();
        if (productPageCount == null) {
            return;
        }

        if (paginationScrollListener == null) {
            paginationScrollListener = new PaginationScrollListener(layoutManager,
                    productPageCount.getTotalPagination(), currentPage) {
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

    public List<Product> getProductList() {
        return new ArrayList<>(productListAdapter.getProductList());
    }
}
