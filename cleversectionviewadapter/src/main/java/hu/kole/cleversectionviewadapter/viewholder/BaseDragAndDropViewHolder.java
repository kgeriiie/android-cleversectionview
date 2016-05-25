package hu.kole.cleversectionviewadapter.viewholder;

import android.graphics.Point;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import hu.kole.cleversectionviewadapter.draganddrop.DragInfo;
import hu.kole.cleversectionviewadapter.draganddrop.NoForegroundShadowBuilder;
import hu.kole.cleversectionviewadapter.BaseCleverSectionAdapter;
import hu.kole.cleversectionviewadapter.model.BaseSectionItemModel;

/**
 * Created by koleszargergo on 4/19/16.
 */
public class BaseDragAndDropViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

    private final BaseCleverSectionAdapter<?,?,?,?,?> mAdapter;
    private BaseCleverSectionAdapter.OnItemClickListener<BaseSectionItemModel> mOnSectionItemClickListener;

    public BaseDragAndDropViewHolder(View itemView, BaseCleverSectionAdapter adapter) {
        super(itemView);
        mAdapter = adapter;
        itemView.setOnClickListener(this);
    }

    public void setOnSectionItemClickListener(BaseCleverSectionAdapter.OnItemClickListener<BaseSectionItemModel> listener) {
        mOnSectionItemClickListener = listener;
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

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();

        if (mOnSectionItemClickListener != null && position > -1) {
            mOnSectionItemClickListener.onItemClick(v,mAdapter.getItemAtPosition(position));
        }
    }
}
