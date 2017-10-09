package cs.chua.com.walmartproductlist.controller;

import android.text.Spanned;
import android.view.View;

import cs.chua.com.walmartproductlist.model.remote.Product;
import cs.chua.com.walmartproductlist.util.FormatUtil;
import cs.chua.com.walmartproductlist.util.GlideApp;
import cs.chua.com.walmartproductlist.view.product.ProductDetailViews;
import cs.chua.com.walmartproductlist.view.product.ProductItemViews;

/**
 * Created by christopherchua on 10/6/17.
 */

public class ViewHolderPopulator {
    private final static int MAX_RATING_COUNT = 9999;

    public static void populateProductDetail(final Product product, final ProductDetailViews holder) {
        populateProductItem(product, holder);
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

    public static void populateProductItem(final Product product, final ProductItemViews holder) {
        GlideApp.with(holder.imageView.getContext())
                .load(product.getImage())
                .centerCrop()
                .into(holder.imageView);

        holder.priceTextView.setText(product.getPrice());
        holder.nameTextView.setText(product.getName());

        holder.ratingStars.setRating(product.getReviewRating().floatValue());

        final int reviewCount = product.getReviewCount();
        if (reviewCount > MAX_RATING_COUNT) {
            holder.ratingCountTextView.setText(Integer.toString(MAX_RATING_COUNT) + "+");
        } else {
            holder.ratingCountTextView.setText(Integer.toString(reviewCount));
        }

        if (product.getInStock()) {
            holder.outOfStockTextView.setVisibility(View.GONE);
        } else {
            holder.outOfStockTextView.setVisibility(View.VISIBLE);
        }
    }
}
