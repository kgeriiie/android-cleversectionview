package hu.kole.cleversectionviewadapter;

import android.support.v7.widget.GridLayoutManager;

import hu.kole.cleversectionviewadapter.model.BaseSectionItemModel;

/**
 * Created by koleszargergo on 4/19/16.
 */
public class CleverSectionSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private BaseCleverSectionAdapter<?,?,?,?,?> mAdapter = null;
    private GridLayoutManager mLayoutManager = null;

    public CleverSectionSpanSizeLookup(BaseCleverSectionAdapter<?,?,?,?,?> adapter, GridLayoutManager layoutManager) {
        this.mAdapter = adapter;
        this.mLayoutManager = layoutManager;
    }

    @Override
    public int getSpanSize(int position) {
        if (this.mAdapter.getItemAtPosition(position).getSpanType() == BaseSectionItemModel.SPAN_TYPE.GRID_TYPE) {
            return 1;
        } else {
            return mLayoutManager.getSpanCount();
        }
    }
}
