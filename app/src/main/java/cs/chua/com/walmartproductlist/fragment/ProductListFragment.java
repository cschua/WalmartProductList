package cs.chua.com.walmartproductlist.fragment;

import android.support.v7.widget.LinearLayoutManager;

import cs.chua.com.walmartproductlist.adapter.ProductListAdapter;

/**
 * Created by christopherchua on 10/14/17.
 */

public class ProductListFragment extends ProductBaseFragment {
    public ProductListFragment(){}

    @Override
    public LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public ProductListAdapter getAdapter(boolean isLoadingAdded) {
        return new ProductListAdapter(getContext(), isLoadingAdded);
    }
}
