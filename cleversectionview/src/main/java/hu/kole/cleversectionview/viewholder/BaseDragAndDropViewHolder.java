package hu.kole.cleversectionview.viewholder;

import android.graphics.Point;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import hu.kole.cleversectionview.draganddrop.DragInfo;
import hu.kole.cleversectionview.draganddrop.NoForegroundShadowBuilder;
import hu.kole.cleversectionview.BaseCleverSectionAdapter;

/**
 * Created by koleszargergo on 4/19/16.
 */
public class BaseDragAndDropViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

    private final BaseCleverSectionAdapter<?,?,?,?,?> mAdapter;

    public BaseDragAndDropViewHolder(View itemView, BaseCleverSectionAdapter adapter) {
        super(itemView);
        mAdapter = adapter;
    }

    public View.DragShadowBuilder getShadowBuilder(View itemView, Point touchPoint) {
        //return new DragSortShadowBuilder(itemView, touchPoint);
        return new NoForegroundShadowBuilder(itemView,touchPoint);
    }

    public final void startDrag() {
        PointF touchPoint = mAdapter.getLastTouchPoint();
        int x = (int) (touchPoint.x - itemView.getX());
        int y = (int) (touchPoint.y - itemView.getY());

        startDrag(getShadowBuilder(itemView, new Point(x, y)));
    }


    public final void startDrag(View.DragShadowBuilder dragShadowBuilder) {
        Point shadowSize = new Point();
        Point shadowTouchPoint = new Point();
        dragShadowBuilder.onProvideShadowMetrics(shadowSize, shadowTouchPoint);

        itemView.startDrag(null, dragShadowBuilder,
                new DragInfo(getItemId(), shadowSize, shadowTouchPoint, mAdapter.getLastTouchPoint()), 0);

        mAdapter.notifyItemChanged(getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        startDrag();

        return true;
    }
}
