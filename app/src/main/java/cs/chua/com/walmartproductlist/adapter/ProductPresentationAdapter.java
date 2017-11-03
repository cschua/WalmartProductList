package cs.chua.com.walmartproductlist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.presenter.ProductItemPresenter;
import cs.chua.com.walmartproductlist.view.holder.BaseRecyclerViewHolder;

/**
 * Created by christopherchua on 10/13/17.
 */

public class ProductPresentationAdapter extends ProductBaseAdapter {

    public ProductPresentationAdapter(final Context context, final boolean isLoadingAdded) {
        super(context, new ProductItemPresenter(ProductItemPresenter.MODE_PRESENTATION), isLoadingAdded);
    }

    @Override
    public View getInflatedView(final LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.product_detailed_layout, parent, false);
    }

    @Override
    public BaseRecyclerViewHolder.ItemClickListener getViewClickListener() {
        // we dont have a click listener for this adapter
        return null;
    }
}
