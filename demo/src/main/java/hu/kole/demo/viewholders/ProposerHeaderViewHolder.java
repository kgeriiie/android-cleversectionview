package hu.kole.demo.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import hu.kole.demo.R;

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
