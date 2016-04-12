package hu.kole.clrecyclerview.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.zip.Inflater;

import hu.kole.cleversectionview.view.BaseSectionAdapter;
import hu.kole.clrecyclerview.R;
import hu.kole.clrecyclerview.viewholders.ProposerFooterViewHolder;
import hu.kole.clrecyclerview.viewholders.ProposerHeaderViewHolder;
import hu.kole.clrecyclerview.viewholders.ProposerItemViewHolder;

/**
 * Created by koleszargergo on 4/12/16.
 */

public class TestAdapter extends BaseSectionAdapter<Proposer,ProposerItem,ProposerItemViewHolder,ProposerHeaderViewHolder,ProposerFooterViewHolder> {

    private Context mContext;

    public TestAdapter(Context context, List<Proposer> sectionList) {
        super(sectionList);
        this.mContext = context;
    }

    private LayoutInflater getInflater() {
        return LayoutInflater.from(mContext);
    }

    @Override
    public ProposerHeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = getInflater().inflate(R.layout.view_item_header,parent,false);

        return new ProposerHeaderViewHolder(view);
    }

    @Override
    public ProposerItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getInflater().inflate(R.layout.view_item,parent,false);

        return new ProposerItemViewHolder(view);
    }

    @Override
    public ProposerFooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View view = getInflater().inflate(R.layout.view_item_footer,parent,false);

        return new ProposerFooterViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(ProposerHeaderViewHolder holder, int position, int viewType) {

    }

    @Override
    public void onBindItemViewHolder(ProposerItemViewHolder holder, int position, int viewType) {

    }

    @Override
    public void onBindFooterViewHolder(ProposerFooterViewHolder holder, int position, int viewType) {

    }
}
