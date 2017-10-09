package cs.chua.com.walmartproductlist.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by christopherchua on 10/8/17.
 */

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    protected abstract void loadMoreItems(final int currentPage);

    private static final int PAGE_START = 1;
    private final LinearLayoutManager layoutManager;
    private final int totalPagesToLoad;
    private int totalPagesLoaded = PAGE_START;
    private boolean isLoading = false;

    public PaginationScrollListener(final LinearLayoutManager layoutManager,
                                    final int totalPagesToLoad, final int totalPagesLoaded) {
        this.layoutManager = layoutManager;
        this.totalPagesToLoad = totalPagesToLoad;
        this.totalPagesLoaded = totalPagesLoaded;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (!isLoading && moreItemsToLoad()) {

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();


            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                isLoading = true;
                totalPagesLoaded += 1; //Increment page index to load the next one
                loadMoreItems(totalPagesLoaded);
            }
        }
    }

    public void updateIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean moreItemsToLoad() {
        return totalPagesLoaded < totalPagesToLoad;
    }

    public int getTotalPagesLoaded() {
        return totalPagesLoaded;
    }
}
