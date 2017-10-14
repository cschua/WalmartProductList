package cs.chua.com.walmartproductlist.controller.product.adapter;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.model.remote.Product;
import cs.chua.com.walmartproductlist.util.FormatUtil;
import cs.chua.com.walmartproductlist.view.ProductViewHolder;

/**
 * Created by christopherchua on 10/13/17.
 */

public class ProductPagerAdapter extends ProductBaseAdapter {

    public ProductPagerAdapter(final Context context, final boolean isLoadingAdded) {
        super(context, isLoadingAdded);
    }

    @Override
    public View getInflatedView(final LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.product_detailed_layout, parent, false);
    }

    @Override
    public void populateProductItem(final Product product, final ProductViewHolder holder) {
        holder.itemView.scrollTo(0, 0); // reset scroll position
        super.populateProductItem(product, holder);
        final Spanned shortDescription = FormatUtil.fromHtml(product.getShortDescription());
        if (shortDescription == null) {
            holder.shortDescriptionTextView.setVisibility(View.GONE);
        } else {
            holder.shortDescriptionTextView.setText(shortDescription);
            holder.shortDescriptionTextView.setVisibility(View.VISIBLE);
        }

        final Spanned longDescription = FormatUtil.fromHtml(product.getLongDescription());
        if (longDescription == null) {
            holder.longDescriptionTextView.setVisibility(View.GONE);
        } else {
            holder.longDescriptionTextView.setText(longDescription);
            holder.longDescriptionTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setViewClickListener(final ProductViewHolder holder) {
        // we dont have a click listener for this adapter
    }
}
