package cs.chua.com.walmartproductlist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.presenter.ProductItemPresenter;
import cs.chua.com.walmartproductlist.view.holder.BaseRecyclerViewHolder;

/**
 * Created by christopherchua on 10/5/17.
 */

public class ProductListAdapter extends ProductBaseAdapter implements BaseRecyclerViewHolder.ItemClickListener {

    private OnProductItemListener productItemListener;

    public interface OnProductItemListener {
        void onProductListAdapterClick(final int position);
    }

    public ProductListAdapter(final Context context, final boolean isLoadingAdded) {
        super(context, new ProductItemPresenter(ProductItemPresenter.MODE_LIST), isLoadingAdded);
        if (context instanceof OnProductItemListener) {
            productItemListener = (OnProductItemListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet ProductListAdapter.OnProductItemListener");
        }
    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {
        productItemListener.onProductListAdapterClick(position);
    }

    @Override
    public View getInflatedView(final LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.product_list_item_layout, parent, false);
    }

    @Override
    public BaseRecyclerViewHolder.ItemClickListener getViewClickListener() {
        return this;
    }

    @Override
    public void onDestroy() {
        productItemListener = null;
    }
}
