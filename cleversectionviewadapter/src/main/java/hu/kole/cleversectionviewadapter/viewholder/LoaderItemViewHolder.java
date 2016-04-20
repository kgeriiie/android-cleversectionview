package hu.kole.cleversectionviewadapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import hu.kole.cleversectionviewadapter.R;

/**
 * Created by koleszargergo on 4/18/16.
 */
public class LoaderItemViewHolder extends RecyclerView.ViewHolder {

    public CircleProgressBar loaderProgress;

    public LoaderItemViewHolder(View itemView) {
        super(itemView);

        loaderProgress = (CircleProgressBar) itemView.findViewById(R.id.content_loading_progress);
    }
}
