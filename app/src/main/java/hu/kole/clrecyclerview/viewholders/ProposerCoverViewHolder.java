package hu.kole.clrecyclerview.viewholders;

import android.view.View;
import android.widget.TextView;

import hu.kole.cleversectionview.BaseCleverSectionAdapter;
import hu.kole.cleversectionview.viewholder.BaseDragAndDropViewHolder;
import hu.kole.clrecyclerview.R;

/**
 * Created by koleszargergo on 4/19/16.
 */
public class ProposerCoverViewHolder extends BaseDragAndDropViewHolder {

    public TextView titleTv;

    public ProposerCoverViewHolder(View itemView, BaseCleverSectionAdapter adapter) {
        super(itemView, adapter);

        titleTv = (TextView) itemView.findViewById(R.id.list_item_titleTv);
    }
}
