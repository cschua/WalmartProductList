package cs.chua.com.walmartproductlist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.fragment.ProductBaseFragment;
import cs.chua.com.walmartproductlist.fragment.ProductPresentationFragment;

/**
 * Created by christopherchua on 10/6/17.
 */

public class ProductSlideScreenActivity extends AppCompatActivity {
    private static final String TAG = ProductSlideScreenActivity.class.getSimpleName();
    public static final String INTENT_EXTRA_DEFAULT_POSITION = "extraDefaultPosition";
    public static final String INTENT_EXTRA_TOTAL_PAGE_LOADED = "extraTotalPageLoaded";
    private ProductPresentationFragment productListFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.product_slide_screen_layout);
        Log.d(TAG, "setContentView");
        final Intent intent = getIntent();
        final int defaultPosition = intent.getIntExtra(INTENT_EXTRA_DEFAULT_POSITION, 0);
        final int totalPageLoaded = intent.getIntExtra(INTENT_EXTRA_TOTAL_PAGE_LOADED, 1);
        showProductPagerScreen(defaultPosition, totalPageLoaded);
    }

    @Override
    public void onBackPressed() {
        if (!productListFragment.onBackPressed()) {
            // If the user is currently looking at the first or default product, allow the system
            // to handle the Back button. Otherwise, select the previous product.
            super.onBackPressed();
        }
    }

    private void showProductPagerScreen(final int defaultPosition, final int totalPageLoaded) {
        final FragmentManager fm = getSupportFragmentManager();
        final Fragment fragment = fm.findFragmentByTag(ProductBaseFragment.TAG);
        if (fragment == null) {
            Log.d(TAG, "create and show ProductBaseFragment");
            productListFragment = ProductPresentationFragment.newInstance(defaultPosition, totalPageLoaded);
            final FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.product_list_framelayout, productListFragment, ProductBaseFragment.TAG);
            ft.commit();
        } else {
            // we don't refresh if fragment was previously already loaded
            // this is to deal with orientation changes or when the activity is recreated
            productListFragment = (ProductPresentationFragment) fragment;
            if (productListFragment.isVisible()) {
                // TODO we can add logic to refresh products if data is old
                Log.d(TAG, "fragment is already showing");
            }
        }
    }
}
