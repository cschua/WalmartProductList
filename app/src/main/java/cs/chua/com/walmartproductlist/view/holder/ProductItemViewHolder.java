package cs.chua.com.walmartproductlist.view.holder;

import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.util.FormatUtil;
import cs.chua.com.walmartproductlist.util.GlideApp;
import cs.chua.com.walmartproductlist.view.ProductItemView;

/**
 * Created by christopherchua on 10/5/17.
 */

public class ProductItemViewHolder extends BaseRecyclerViewHolder implements ProductItemView {
    private final ImageView imageView;
    private final TextView priceTextView;
    private final TextView nameTextView;
    private final RatingBar ratingStars;
    private final TextView ratingCountTextView;
    private final TextView outOfStockTextView;
    private final TextView shortDescriptionTextView;
    private final TextView longDescriptionTextView;

    public ProductItemViewHolder(final View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        priceTextView = itemView.findViewById(R.id.price_textview);
        nameTextView = itemView.findViewById(R.id.name_textview);
        ratingStars = itemView.findViewById(R.id.rating_stars);
        ratingCountTextView = itemView.findViewById(R.id.rating_count);
        outOfStockTextView = itemView.findViewById(R.id.outofstock_textview);
        shortDescriptionTextView = itemView.findViewById(R.id.short_description_textview);
        longDescriptionTextView = itemView.findViewById(R.id.long_description_textview);
    }

    @Override
    public void setImageView(final String url) {
        GlideApp.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void setPriceTextView(final String price) {
        priceTextView.setText(price);
    }

    @Override
    public void setNameTextView(final String name) {
        nameTextView.setText(name);
    }

    @Override
    public void setRatingStarBar(final float rating) {
        ratingStars.setRating(rating);
    }

    @Override
    public void setRatingCountTextView(final String ratingCount) {
        ratingCountTextView.setText(ratingCount);
    }

    @Override
    public void setOutOfStockVisibility(final boolean isVisible) {
        outOfStockTextView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setShortDescriptionTextView(final String shortDescription) {
        final Spanned spanned = FormatUtil.fromHtml(shortDescription);
        shortDescriptionTextView.setText(spanned);
    }

    @Override
    public void setShortDescriptionVisibility(final boolean isVisible) {
        shortDescriptionTextView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setLongDescriptionTextView(final String longDescription) {
        final Spanned spanned = FormatUtil.fromHtml(longDescription);
        longDescriptionTextView.setText(spanned);
    }

    @Override
    public void setLongDescriptionVisibility(final boolean isVisible) {
        longDescriptionTextView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
