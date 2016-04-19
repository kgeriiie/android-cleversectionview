package hu.kole.clrecyclerview.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hu.kole.cleversectionview.BaseCleverSectionAdapter;
import hu.kole.cleversectionview.model.BaseSectionItemModel;
import hu.kole.cleversectionview.viewholder.BaseDragAndDropViewHolder;
import hu.kole.clrecyclerview.R;
import hu.kole.clrecyclerview.viewholders.ProposerCoverViewHolder;
import hu.kole.clrecyclerview.viewholders.ProposerFooterViewHolder;
import hu.kole.clrecyclerview.viewholders.ProposerHeaderViewHolder;
import hu.kole.clrecyclerview.viewholders.ProposerItemViewHolder;

/**
 * Created by koleszargergo on 4/12/16.
 */

public class TestAdapter extends BaseCleverSectionAdapter<Proposer,ProposerItem,BaseDragAndDropViewHolder,ProposerHeaderViewHolder,ProposerFooterViewHolder> {

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
    public BaseDragAndDropViewHolder onCreateItemViewHolder(ViewGroup parent, int layoutType) {

        if (layoutType == ProposerItem.LAYOUT_TYPE.COVER_LAYOUT) {
            View view = getInflater().inflate(R.layout.view_item_cover,parent,false);
            return new ProposerCoverViewHolder(view, this);
        } else {
            View view = getInflater().inflate(R.layout.view_item,parent,false);
            return new ProposerItemViewHolder(view, this);
        }
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
    public void onBindItemViewHolder(BaseDragAndDropViewHolder holder,ProposerItem item, int layoutType) {

        if (layoutType == ProposerItem.LAYOUT_TYPE.COVER_LAYOUT) {
            ProposerCoverViewHolder cHolder = (ProposerCoverViewHolder) holder;
            cHolder.titleTv.setText(item.title);
        } else {
            ProposerItemViewHolder cHolder = (ProposerItemViewHolder) holder;
            cHolder.titleTv.setText(item.title);
        }
    }

    @Override
    public void onBindFooterViewHolder(ProposerFooterViewHolder holder,Proposer section, int layoutType) {

        holder.titleTv.setText(section.title);
    }

    @Override
    public boolean isDraggingEnabledAtItemPosition(Proposer section, ProposerItem proposerItem) {
        if (proposerItem.getSpanType() == BaseSectionItemModel.SPAN_TYPE.GRID_TYPE && section.getItemIndexInSection(proposerItem) == 0) {
            return false;
        }

        return super.isDraggingEnabledAtItemPosition(section, proposerItem);
    }
}
