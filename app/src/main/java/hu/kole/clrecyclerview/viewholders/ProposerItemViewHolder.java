package hu.kole.clrecyclerview.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import hu.kole.cleversectionview.viewholder.BaseDragAndDropViewHolder;
import hu.kole.clrecyclerview.R;
import hu.kole.clrecyclerview.test.TestAdapter;

/**
 * Created by koleszargergo on 4/12/16.
 */
public class ProposerItemViewHolder extends BaseDragAndDropViewHolder {

    public TextView titleTv;

    public ProposerItemViewHolder(View itemView, TestAdapter adapter) {
        super(itemView, adapter);

        titleTv = (TextView) itemView.findViewById(R.id.list_item_titleTv);
    }
}
