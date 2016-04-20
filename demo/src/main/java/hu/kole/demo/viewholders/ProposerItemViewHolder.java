package hu.kole.demo.viewholders;

import android.view.View;
import android.widget.TextView;

import hu.kole.cleversectionviewadapter.viewholder.BaseDragAndDropViewHolder;
import hu.kole.demo.R;
import hu.kole.demo.adapter.ProposerAdapter;

/**
 * Created by koleszargergo on 4/12/16.
 */
public class ProposerItemViewHolder extends BaseDragAndDropViewHolder {

    public TextView titleTv;

    public ProposerItemViewHolder(View itemView, ProposerAdapter adapter) {
        super(itemView, adapter);

        titleTv = (TextView) itemView.findViewById(R.id.list_item_titleTv);
    }
}
