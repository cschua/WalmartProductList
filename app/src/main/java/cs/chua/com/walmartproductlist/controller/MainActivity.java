package cs.chua.com.walmartproductlist.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.controller.product.ProductBaseFragment;
import cs.chua.com.walmartproductlist.controller.product.ProductListFragment;
import cs.chua.com.walmartproductlist.controller.product.adapter.ProductListAdapter;

public class MainActivity extends AppCompatActivity
        implements ProductListAdapter.OnProductItemListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProductListFragment productListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showProductListScreen();
    }

    @Override
    public void onProductListAdapterClick(int position) {
        // used in ProductListAdapter when items are clicked.  show the full detail screen
        Log.d(TAG, "onProductListAdapterClick " + position);
        if (productListFragment != null) {
            productListFragment.updateDefaultPosition(position);
            final Intent intent = new Intent(this, ProductSlideScreenActivity.class);
            intent.putExtra(ProductSlideScreenActivity.INTENT_EXTRA_DEFAULT_POSITION, position);
            intent.putExtra(ProductSlideScreenActivity.INTENT_EXTRA_TOTAL_PAGE_LOADED, productListFragment.getTotalPagesLoaded());
            startActivity(intent);
        }
    }

    private void showProductListScreen() {
        final FragmentManager fm = getSupportFragmentManager();
        final Fragment fragment = fm.findFragmentByTag(ProductBaseFragment.TAG);
        if (fragment == null) {
            Log.d(TAG, "create and show ProductBaseFragment");
            productListFragment = new ProductListFragment();
            final FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.product_list_framelayout, productListFragment, ProductBaseFragment.TAG);
            ft.commit();
        } else {
            // we don't refresh if fragment was previously already loaded
            // this is to deal with orientation changes or when the activity is recreated
            productListFragment = (ProductListFragment) fragment;
            if (productListFragment.isVisible()) {
                // TODO we can add logic to refresh products if data is old
                Log.d(TAG, "fragment is already showing");
            }
        }
    }
}