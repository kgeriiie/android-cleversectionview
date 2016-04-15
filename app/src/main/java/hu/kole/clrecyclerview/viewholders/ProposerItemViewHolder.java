package hu.kole.clrecyclerview.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import hu.kole.clrecyclerview.R;

/**
 * Created by koleszargergo on 4/12/16.
 */
public class ProposerItemViewHolder extends RecyclerView.ViewHolder{

    public TextView titleTv;

    public ProposerItemViewHolder(View itemView) {
        super(itemView);

        titleTv = (TextView) itemView.findViewById(R.id.list_item_titleTv);
    }
}
