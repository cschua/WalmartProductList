package cs.chua.com.walmartproductlist.controller.product;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cs.chua.com.walmartproductlist.controller.product.adapter.ProductPagerAdapter;
import cs.chua.com.walmartproductlist.model.remote.Product;

/**
 * Created by christopherchua on 10/14/17.
 */

public class ProductPagerFragment extends ProductBaseFragment {

    public ProductPagerFragment(){}

    public static ProductPagerFragment newInstance(final int defaultPosition, final ArrayList<Product> productList) {
        final ProductPagerFragment fragment = new ProductPagerFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARGS_DEFAULT_POSITION, defaultPosition);
        bundle.putParcelableArrayList(ARGS_PRODUCTS, productList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = super.onCreateView(inflater, container, savedInstanceState);
        final SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(productsRecyclerView);
        return rootView;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    public ProductPagerAdapter getAdapter(boolean isLoadingAdded) {
        return new ProductPagerAdapter(getContext(), isLoadingAdded);
    }
}