package hu.kole.demo.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hu.kole.cleversectionviewadapter.BaseCleverSectionAdapter;
import hu.kole.cleversectionviewadapter.model.BaseSectionItemModel;
import hu.kole.cleversectionviewadapter.viewholder.BaseDragAndDropViewHolder;
import hu.kole.demo.R;
import hu.kole.demo.models.Proposer;
import hu.kole.demo.models.ProposerItem;
import hu.kole.demo.viewholders.ProposerCoverViewHolder;
import hu.kole.demo.viewholders.ProposerFooterViewHolder;
import hu.kole.demo.viewholders.ProposerHeaderViewHolder;
import hu.kole.demo.viewholders.ProposerItemViewHolder;

/**
 * Created by koleszargergo on 4/12/16.
 */

public class ProposerAdapter extends BaseCleverSectionAdapter<Proposer,ProposerItem,BaseDragAndDropViewHolder,ProposerHeaderViewHolder,ProposerFooterViewHolder> {

    private Context mContext;

    public ProposerAdapter(Context context, List<Proposer> sectionList) {
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
        } else if (layoutType == ProposerItem.LAYOUT_TYPE.CARD_LAYOUT_GREEN) {
            ProposerItemViewHolder cHolder = (ProposerItemViewHolder) holder;
            cHolder.titleTv.setText(item.title);
            cHolder.titleTv.setBackgroundColor(ContextCompat.getColor(mContext,android.R.color.holo_green_dark));
        } else if (layoutType == ProposerItem.LAYOUT_TYPE.CARD_LAYOUT_RED) {
            ProposerItemViewHolder cHolder = (ProposerItemViewHolder) holder;
            cHolder.titleTv.setText(item.title);
            cHolder.titleTv.setBackgroundColor(ContextCompat.getColor(mContext,android.R.color.holo_red_dark));
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
    public boolean isDraggingEnabledAtItemPosition(Proposer section,ProposerItem fromItem, ProposerItem toItem) {

//        if ((fromItem.getSpanType() == BaseSectionItemModel.SPAN_TYPE.GRID_TYPE && section.getItemIndexInSection(fromItem) == 0) ||
//                (toItem.getSpanType() == BaseSectionItemModel.SPAN_TYPE.GRID_TYPE && section.getItemIndexInSection(toItem) == 0)) {
//            return false;
//        }

        return super.isDraggingEnabledAtItemPosition(section,fromItem, toItem);
    }
}
