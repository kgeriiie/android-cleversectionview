package hu.kole.clrecyclerview.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import hu.kole.clrecyclerview.R;

/**
 * Created by koleszargergo on 4/12/16.
 */
public class ProposerHeaderViewHolder extends RecyclerView.ViewHolder{

    public TextView titleTv;

    public ProposerHeaderViewHolder(View itemView) {
        super(itemView);

        titleTv = (TextView) itemView.findViewById(R.id.view_item_headerTv);
    }
}
