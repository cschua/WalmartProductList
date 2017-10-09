package cs.chua.com.walmartproductlist.controller.product;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.controller.ViewHolderPopulator;
import cs.chua.com.walmartproductlist.model.remote.Product;
import cs.chua.com.walmartproductlist.util.Pagination;
import cs.chua.com.walmartproductlist.util.PaginationScrollListener;
import cs.chua.com.walmartproductlist.view.BaseRecyclerViewHolder;
import cs.chua.com.walmartproductlist.view.product.ProductItemViewHolder;

/**
 * Created by christopherchua on 10/5/17.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductItemViewHolder> implements Pagination {
    private static final int TYPE_PRODUCT = 0;
    private static final int TYPE_PROGRESS_LOADING = 1;

    private final LayoutInflater inflater;
    private List<Product> productList;
    private BaseRecyclerViewHolder.ItemClickListener itemClickListener;
    private OnProductItemListener onProductItemListener;

    // used for pagination
    private PaginationScrollListener paginationScrollListener;
    private boolean isLoadingAdded = false;


    public interface OnProductItemListener {
        void onProductListAdapterClick(final int position);
    }

    public ProductListAdapter(final Context context, final boolean isLoadingAdded) {
        if (context instanceof OnProductItemListener) {
            this.isLoadingAdded = isLoadingAdded;
            onProductItemListener = (OnProductItemListener) context;
            productList = new ArrayList<>();
            inflater = LayoutInflater.from(context);
            itemClickListener = new BaseRecyclerViewHolder.ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    onProductItemListener.onProductListAdapterClick(position);
                }
            };
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet ProductListAdapter.OnProductItemListener");
        }
    }

    @Override
    public ProductItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView;
        // we're supporting pagination so check for viewType
        if (TYPE_PRODUCT == viewType) { // default behavior, it's a product
            itemView = inflater.inflate(R.layout.product_list_item_layout, parent, false);
        } else { // otherwise it's the progress item (should be the last item if available)
            itemView = inflater.inflate(R.layout.item_progress, parent, false);
        }
        return new ProductItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductItemViewHolder holder, int position) {
        // we're supporting pagination so we need to make sure to bind on product
        if (TYPE_PRODUCT == getItemViewType(position)) {
            final Product product = productList.get(position);
            ViewHolderPopulator.populateProductItem(product, holder.getProductViews());
            holder.setItemClickListener(itemClickListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // last one is progress item if there are more items to load
        return (position == productList.size() - 1 && isLoadingAdded) ? TYPE_PROGRESS_LOADING : TYPE_PRODUCT;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void onDestroy() {
        itemClickListener = null;
        onProductItemListener = null;
        paginationScrollListener = null;
    }

    // the following methods below are used for pagination
    public void updateList(final List<Product> items) {
        if (paginationScrollListener != null) {
            // reset the loading state of pagination
            paginationScrollListener.updateIsLoading(false);
        }
        if (getItemCount() > 0) {
            // we already have items in the list so we need to make sure to remove the progress
            removeLoadingView();
            productList.addAll(items);
        } else {
            productList = items == null ? new ArrayList<Product>() : items;
        }
        updateLoadingView();
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setPaginationScrollListener(final PaginationScrollListener onScrollListener) {
        this.paginationScrollListener = onScrollListener;
    }

    private void updateLoadingView() {
        // we only support pagination if a listener is provided and there are more items to load
        if (paginationScrollListener != null && paginationScrollListener.moreItemsToLoad()) {
            addLoadingView();
        } else {
            removeLoadingView();
        }
    }

    @Override
    public void addLoadingView() {
        // add a progress item at the end of the list
        if (!isLoadingAdded && productList != null) {
            isLoadingAdded = true;
            productList.add(new Product());
            notifyDataSetChanged();
        }
    }

    @Override
    public void removeLoadingView() {
        // remove the progress item (it's the last one in the list)
        if (isLoadingAdded) {
            isLoadingAdded = false;
            int position = getItemCount() - 1;
            if (position >= 0) {
                productList.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    @Override
    public boolean isLoadingAdded() {
        return isLoadingAdded;
    }
}
