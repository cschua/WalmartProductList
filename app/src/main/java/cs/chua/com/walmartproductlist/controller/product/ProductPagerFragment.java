package cs.chua.com.walmartproductlist.controller.product;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cs.chua.com.walmartproductlist.controller.product.adapter.ProductPagerAdapter;
import cs.chua.com.walmartproductlist.model.remote.Product;

/**
 * Created by christopherchua on 10/14/17.
 */

public class ProductPagerFragment extends ProductBaseFragment {
    public static final String ARG_DEFAULT_POSITION = "argdefaultindex";
    private int defaultPosition = 0;

    public ProductPagerFragment(){}

    public static ProductPagerFragment newInstance(final int defaultPosition) {
        final ProductPagerFragment fragment = new ProductPagerFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_DEFAULT_POSITION, defaultPosition);
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
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null) {
            defaultPosition = bundle.getInt(ARG_DEFAULT_POSITION);
        }
    }

    @Override
    public void updateList(final List<Product> productList) {
        productsRecyclerView.scrollToPosition(defaultPosition);
        super.updateList(productList);
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    public ProductPagerAdapter getAdapter(boolean isLoadingAdded) {
        return new ProductPagerAdapter(getContext(), isLoadingAdded);
    }

    public boolean onBackPressed() {
        final int currentIndex = layoutManager.findFirstCompletelyVisibleItemPosition();
        if (currentIndex == defaultPosition) {
            return false;
        }
        if (currentIndex > defaultPosition) {
            productsRecyclerView.scrollToPosition(currentIndex - 1);
        } else {
            productsRecyclerView.scrollToPosition(currentIndex + 1);
        }
        return true;
    }
}
