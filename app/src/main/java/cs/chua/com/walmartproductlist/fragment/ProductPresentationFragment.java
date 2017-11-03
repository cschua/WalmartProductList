package cs.chua.com.walmartproductlist.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs.chua.com.walmartproductlist.adapter.ProductPresentationAdapter;

/**
 * Created by christopherchua on 10/14/17.
 */

public class ProductPresentationFragment extends ProductBaseFragment {

    public ProductPresentationFragment(){}

    public static ProductPresentationFragment newInstance(final int defaultPosition, final int totalPagesLoaded) {
        final ProductPresentationFragment fragment = new ProductPresentationFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARGS_DEFAULT_POSITION, defaultPosition);
        bundle.putInt(ARGS_TOTAL_PAGE_LOADED, totalPagesLoaded);
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
    public ProductPresentationAdapter getAdapter(boolean isLoadingAdded) {
        return new ProductPresentationAdapter(getContext(), isLoadingAdded);
    }
}
