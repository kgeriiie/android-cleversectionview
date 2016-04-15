package hu.kole.clrecyclerview.test;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

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
    public ProposerHeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int layoutType) {
        View view = getInflater().inflate(R.layout.view_item_header,parent,false);

        return new ProposerHeaderViewHolder(view);
    }

    @Override
    public ProposerItemViewHolder onCreateItemViewHolder(ViewGroup parent, int layoutType) {
        View view = getInflater().inflate(R.layout.view_item,parent,false);

        return new ProposerItemViewHolder(view);
    }

    @Override
    public ProposerFooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int layoutType) {
        View view = getInflater().inflate(R.layout.view_item_footer,parent,false);

        return new ProposerFooterViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(ProposerHeaderViewHolder holder,Proposer section, int layoutType) {

        holder.titleTv.setText(section.title);
    }

    @Override
    public void onBindItemViewHolder(ProposerItemViewHolder holder,ProposerItem item, int layoutType) {

        if (layoutType == ProposerItem.LAYOUT_TYPE.GREEN_LAYOUT) {
            holder.titleTv.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_green_dark));
        } else {
            holder.titleTv.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_red_dark));
        }

        holder.titleTv.setText(item.title);
    }

    @Override
    public void onBindFooterViewHolder(ProposerFooterViewHolder holder,Proposer section, int layoutType) {

        holder.titleTv.setText(section.title);
    }
}
