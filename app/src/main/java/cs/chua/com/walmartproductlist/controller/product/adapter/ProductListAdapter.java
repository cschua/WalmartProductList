package cs.chua.com.walmartproductlist.controller.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.view.BaseRecyclerViewHolder;
import cs.chua.com.walmartproductlist.view.ProductViewHolder;

/**
 * Created by christopherchua on 10/5/17.
 */

public class ProductListAdapter extends ProductBaseAdapter {

    private BaseRecyclerViewHolder.ItemClickListener itemViewClickListener;
    private OnProductItemListener productItemListener;

    public ProductListAdapter(final Context context, final boolean isLoadingAdded) {
        super(context, isLoadingAdded);
        if (context instanceof OnProductItemListener) {
            productItemListener = (OnProductItemListener) context;
            itemViewClickListener = new BaseRecyclerViewHolder.ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    productItemListener.onProductListAdapterClick(position);
                }
            };
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet ProductListAdapter.OnProductItemListener");
        }
    }

    @Override
    public View getInflatedView(final LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.product_list_item_layout, parent, false);
    }

    @Override
    public void setViewClickListener(final ProductViewHolder holder) {
        holder.setItemClickListener(itemViewClickListener);
    }

    @Override
    public void onDestroy() {
        itemViewClickListener = null;
        productItemListener = null;
    }
}
