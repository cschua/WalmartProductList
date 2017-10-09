package cs.chua.com.walmartproductlist.controller.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.model.local.PaginationPageCount;
import cs.chua.com.walmartproductlist.model.remote.Product;

/**
 * Created by christopherchua on 10/6/17.
 */

public class ProductSlideScreenActivity extends AppCompatActivity {
    private static final String TAG = ProductSlideScreenActivity.class.getSimpleName();
    public static final String ARGS_PRODUCTS = "argproducts";
    public static final String ARGS_DEFAULT_INDEX = "argdefaultindex";
    private ViewPager viewPager;
    private ProductSlideScreenAdapter pagerAdapter;
    private int defaultIndex;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_slide_screen_layout);
        final Intent intent = getIntent();
        List<Product> productList = intent.getParcelableArrayListExtra(ARGS_PRODUCTS);
        if (productList == null || productList.size() == 0) {
            // TODO refresh or show error?
            Log.d(TAG, "Empty product list");
            return;
        }

        viewPager = (ViewPager) findViewById(R.id.products_viewpager);
        pagerAdapter = new ProductSlideScreenAdapter(getSupportFragmentManager());

        // we will limit pageviews to 30 items starting from the middle of the list if possible
        defaultIndex = intent.getIntExtra(ARGS_DEFAULT_INDEX, 0);
        final int leftIndex = Math.max(defaultIndex - PaginationPageCount.TOTAL_ITEMS_PER_PAGE/2, 0);
        final int rightIndex = Math.min(leftIndex + PaginationPageCount.TOTAL_ITEMS_PER_PAGE, productList.size()-1);
        productList = productList.subList(leftIndex, rightIndex);
        defaultIndex = Math.min(defaultIndex, 15);

        //TODO add viewPager.setOnScrollChangeListener(new PaginationScrollListener(...){});
        //TODO retrieve next 30 items as you get closer to the end of left/right index
        pagerAdapter.setProductList(productList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(defaultIndex);
    }

    @Override
    public void onBackPressed() {
        final int currentIndex = viewPager.getCurrentItem();
        if (currentIndex == defaultIndex) {
            // If the user is currently looking at the first or default product, allow the system
            // to handle the Back button
            super.onBackPressed();
        } else {
            // Otherwise, select the previous product.
            if (currentIndex > defaultIndex) {
                viewPager.setCurrentItem(currentIndex - 1);
            } else {
                viewPager.setCurrentItem(currentIndex + 1);
            }
        }
    }
}
