package cs.chua.com.walmartproductlist.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.adapter.ProductBaseAdapter;
import cs.chua.com.walmartproductlist.model.local.ApplicationModel;
import cs.chua.com.walmartproductlist.model.local.PaginationCount;
import cs.chua.com.walmartproductlist.model.remote.Product;
import cs.chua.com.walmartproductlist.presenter.ProductScreenPresenter;
import cs.chua.com.walmartproductlist.util.PaginationScrollListener;
import cs.chua.com.walmartproductlist.view.ProductScreenView;

/**
 * Created by christopherchua on 10/7/17.
 */

public abstract class ProductBaseFragment extends Fragment implements ProductScreenView {
    public final static String TAG = ProductBaseFragment.class.getSimpleName();

    public abstract LinearLayoutManager getLayoutManager();
    public abstract ProductBaseAdapter getAdapter(boolean isLoadingAdded);

    public static final String ARGS_DEFAULT_POSITION = "argDefaultPosition";
    // used in pagination
    public final static String ARGS_TOTAL_PAGE_LOADED = "argCurrentState";
    // used in the adapter for adding pagination support
    public final static String ARGS_LOADING_ADDED = "argLoadingAdded";

    // views
    protected RecyclerView productsRecyclerView;
    protected ProductBaseAdapter productListAdapter;
    protected LinearLayoutManager layoutManager;
    private ProductScreenPresenter productScreenPresenter;
    private PaginationScrollListener paginationScrollListener;

    private int defaultPosition;
    private int totalPagesLoaded;

    public ProductBaseFragment() {
        defaultPosition = 0;
        totalPagesLoaded = 1;
    }

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

        productScreenPresenter = new ProductScreenPresenter(this);

        final boolean isLoadingAdded;
        if (savedInstanceState == null) {
            final Bundle bundle = getArguments();
            if (bundle != null) {
                defaultPosition = bundle.getInt(ARGS_DEFAULT_POSITION, 0);
                totalPagesLoaded = bundle.getInt(ARGS_TOTAL_PAGE_LOADED, 1);
            }
            isLoadingAdded = false;
        } else {
            defaultPosition = savedInstanceState.getInt(ARGS_DEFAULT_POSITION, 0);
            totalPagesLoaded = savedInstanceState.getInt(ARGS_TOTAL_PAGE_LOADED, 1);
            isLoadingAdded = savedInstanceState.getBoolean(ARGS_LOADING_ADDED, false);
        }

        productListAdapter = getAdapter(isLoadingAdded);
        productsRecyclerView.setAdapter(productListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        final List<Product> productList = ApplicationModel.getInstance().getProducts();
        // only send a server call if we don't already have a list from the saveInstanceState
        if (productList == null || productList.size() == 0) {
            productScreenPresenter.refreshProductList(totalPagesLoaded, PaginationCount.TOTAL_ITEMS_PER_PAGE);
        } else {
            productsRecyclerView.scrollToPosition(defaultPosition);
            updateProductListView(productList);
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
        outState.putInt(ARGS_DEFAULT_POSITION, defaultPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (productScreenPresenter != null) {
            productScreenPresenter.onDestroy();
        }
        if (productListAdapter != null) {
            productListAdapter.onDestroy();
        }
        paginationScrollListener = null;
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

    @Override
    public void updateProductListView(final List<Product> productList) {
        final Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        updatePagination();
        productListAdapter.updateList(productList);
        productListAdapter.notifyDataSetChanged();
    }

    @Override
    public void addLoadingView() {
        productListAdapter.addLoadingView();
    }

    @Override
    public void removeLoadingView() {
        productListAdapter.removeLoadingView();
    }

    @Override
    public void onError(final String error) {
        // TODO show user error?
        Log.w(TAG, error);
    }

    private void updatePagination() {
        final PaginationCount productPageCount = ApplicationModel.getInstance().getPaginationCount();
        if (productPageCount == null) {
            return;
        }

        if (paginationScrollListener == null) {
            paginationScrollListener = new PaginationScrollListener(layoutManager,
                    productPageCount.getTotalPages(), totalPagesLoaded) {
                @Override
                protected void loadMoreItems(final int currentPage) {
                    productScreenPresenter.refreshProductList(currentPage,
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

    public int getTotalPagesLoaded() {
        if (paginationScrollListener == null) {
            return 1;
        }
        return paginationScrollListener.getTotalPagesLoaded();
    }

    public void updateDefaultPosition(final int defaultPosition) {
        this.defaultPosition = defaultPosition;
    }
}
