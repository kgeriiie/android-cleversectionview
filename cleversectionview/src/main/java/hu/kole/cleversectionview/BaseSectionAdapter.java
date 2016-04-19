package hu.kole.cleversectionview;

import android.content.res.Resources;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hu.kole.cleversectionview.R;
import hu.kole.cleversectionview.draganddrop.DragInfo;
import hu.kole.cleversectionview.draganddrop.DragManager;
import hu.kole.cleversectionview.listeners.EndlessScrollListener;
import hu.kole.cleversectionview.model.BaseSectionItemModel;
import hu.kole.cleversectionview.model.BaseSectionModel;
import hu.kole.cleversectionview.model.LoaderSectionItem;
import hu.kole.cleversectionview.viewholder.BaseDragAndDropViewHolder;
import hu.kole.cleversectionview.viewholder.LoaderItemViewHolder;

/**
 * Created by koleszargergo on 4/7/16.
 */
public abstract class BaseSectionAdapter<TModel extends BaseSectionModel, TModelItem extends  BaseSectionItemModel, TItemViewHolder extends BaseDragAndDropViewHolder, THeaderViewHolder extends RecyclerView.ViewHolder, TFooterViewHolder extends RecyclerView.ViewHolder>  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final static int CORRECTION = 1;
    private final static double FILLED_SECTION_IN_PERCENT = 0.8;

    protected List<TModel> sections = new ArrayList<>();
    private List<TModelItem> allRowItems = new ArrayList<>();

    protected RecyclerView mConnectedRecyclerView;

    protected OnItemClickListener<TModelItem> mOnSectionItemClickListener;
    protected EndlessScrollListener mEndlessScrollListener;

    private boolean isEndlessScrollListenerAdded = false;
    private boolean isLoadMoreProgressVisible = false;

    private final int SCROLL_AMOUNT = (int) (2 * Resources.getSystem().getDisplayMetrics().density);

    private DragManager dragManager;
    private int scrollState = RecyclerView.SCROLL_STATE_IDLE;
    private final PointF lastTouchPoint = new PointF(); // used to create ShadowBuilder

    public BaseSectionAdapter(List<TModel> sectionList) {

        setHasStableIds(true);

        isLoadMoreProgressVisible = false;

        this.sections.clear();
        this.sections.addAll(sectionList);

        this.allRowItems.clear();
        this.allRowItems.addAll(copySectionsIntoRealObjects());

        addEndlessScrollListener();
    }

    protected final List<TModelItem> copySectionsIntoRealObjects() {
        List<TModel> temp = cloneSectionList(this.sections);
        List<TModelItem> sectionItems = new ArrayList<>();

        for (TModel section : temp) {
            List<TModelItem> sectionItemModels = section.getSectionItems();

            if (sectionItemModels != null) {
                if (section.isHeaderVisible() && !section.isHeaderItemAtFirstPosition()) {
                    TModelItem header = BaseSectionItemModel.createHeaderItem(section.getId());
                    header.setParentId(section.getId());
                    sectionItemModels.add(0, header);
                }

                if (section.isFooterVisible() && !section.isFooterItemAtLastPosition()) {
                    TModelItem footer = BaseSectionItemModel.createFooterItem(section.getId());
                    footer.setParentId(section.getId());
                    sectionItemModels.add(footer);
                }

                for (TModelItem item : sectionItemModels) {
                    item.setParentId(section.getId());
                }

                sectionItems.addAll(sectionItemModels);
            }
        }

        return sectionItems;
    }

    public void updateDataSet(List<TModel> data) {

        isLoadMoreProgressVisible = false;

        sections.clear();
        allRowItems.clear();

        this.sections.addAll(data);
        this.allRowItems.addAll(copySectionsIntoRealObjects());

        addEndlessScrollListener();

        this.notifyDataSetChanged();
    }

    public final void addShowMoreProgress() {
        if (!isLoadMoreProgressVisible && this.sections.size() > 0) {
            TModel lastSection = this.sections.get(this.sections.size() -1);

            if (lastSection != null
                    && (lastSection.getSectionItems().size() == 0 || lastSection.getSectionItems().get(0).getViewType() != BaseSectionItemModel.VIEW_TYPE.TYPE_LOADER)) {

                TModel loader = (TModel) new LoaderSectionItem();

                this.sections.add(loader);

                this.allRowItems.clear();
                this.allRowItems.addAll(copySectionsIntoRealObjects());

                this.notifyDataSetChanged();

                isLoadMoreProgressVisible = true;
            }
        }
    }

    private final void removeShowMoreProgress() {
        if (isLoadMoreProgressVisible && this.sections.size() > 0) {
            TModel lastSection = this.sections.get(this.sections.size() -1);

            if (lastSection != null && lastSection.getSectionItems().size() > 0 && lastSection.getSectionItems().get(0).getViewType() == BaseSectionItemModel.VIEW_TYPE.TYPE_LOADER) {

                this.sections.remove(this.sections.size() - 1);

                this.allRowItems.clear();
                this.allRowItems.addAll(copySectionsIntoRealObjects());

                this.notifyDataSetChanged();

                isLoadMoreProgressVisible = false;
            }
        }
    }

    public TModelItem getItemAtPosition(int position) {
        return this.allRowItems.get(position);
    }

    public final TModel getSectionOfItem(TModelItem item) {
        for (TModel section : sections) {
            if (section.getId().equalsIgnoreCase(item.getParentId())) {
                return section;
            }
        }

        return null;
    }

    private final List<TModel> cloneSectionList(List<TModel> list) {
        List<TModel> temp = new ArrayList<>();

        for (TModel model : this.sections) {
            temp.add((TModel) model.clone());
        }

        return temp;
    }

    private void initDragAndDrop(RecyclerView recyclerView) {
        mConnectedRecyclerView = recyclerView;

        dragManager = new DragManager(mConnectedRecyclerView, this);
        mConnectedRecyclerView.setOnDragListener(dragManager);

        mConnectedRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                lastTouchPoint.set(e.getX(), e.getY());
                return false;
            }

            @Override public void onRequestDisallowInterceptTouchEvent(boolean b) { }

            @Override public void onTouchEvent(RecyclerView rv, MotionEvent e) { }
        });

        mConnectedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        handleScroll(recyclerView);
                    }
                });
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                scrollState = newState;
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        handleScroll(recyclerView);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return getItemAtPosition(position).getId().hashCode();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        initDragAndDrop(recyclerView);
    }

    @Override
    public int getItemCount() {
        return allRowItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        TModelItem item = getItemAtPosition(position);

        if (item.getLayoutType() == item.getViewType()) {
            //In default case we return with view type.
            return item.getViewType() + CORRECTION;
        } else {
            //In other case we return with sum of view type and layout type. We need to use correction to handle layout type with 0 value.
            return Math.abs(item.getViewType()) + Math.abs(item.getLayoutType()) + CORRECTION;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int type = viewType - CORRECTION;

        if (type == BaseSectionItemModel.VIEW_TYPE.TYPE_SECTION_HEADER) {
            return onCreateHeaderViewHolder(parent,convertToLayoutType(viewType));

        } else if (type == BaseSectionItemModel.VIEW_TYPE.TYPE_SECTION_FOOTER) {
            return onCreateFooterViewHolder(parent,convertToLayoutType(viewType));

        } else if (type == BaseSectionItemModel.VIEW_TYPE.TYPE_LOADER) {
            return onCreateLoaderViewHolder(parent,convertToLayoutType(viewType));

        } else {
            return onCreateItemViewHolder(parent,convertToLayoutType(viewType));

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        TModelItem item = getItemAtPosition(position);
        TModel section = getSectionOfItem(item);
        int viewType = item.getViewType();

        if (viewType == BaseSectionItemModel.VIEW_TYPE.TYPE_SECTION_HEADER) {
            onBindHeaderViewHolder((THeaderViewHolder) holder,section, item.getLayoutType());

        } else if (viewType == BaseSectionItemModel.VIEW_TYPE.TYPE_SECTION_FOOTER) {
            onBindFooterViewHolder((TFooterViewHolder) holder,section, item.getLayoutType());

        } else if (viewType == BaseSectionItemModel.VIEW_TYPE.TYPE_LOADER) {
            onBindLoaderViewHolder((LoaderItemViewHolder) holder, section, item.getLayoutType());

        } else {
            onBindItemViewHolder((TItemViewHolder) holder,item, item.getLayoutType());

            holder.itemView.setOnClickListener(handleOnItemClick());

            if (isDraggingEnabledAtItemPosition(section, item)) {
                holder.itemView.setOnLongClickListener((TItemViewHolder) holder);
                holder.itemView.setVisibility(getDraggingId() == item.getId().hashCode() ? View.INVISIBLE : View.VISIBLE);
                holder.itemView.postInvalidate();
            }
        }
    }

    private final int convertToLayoutType(int rawViewType) {
        int type  = (rawViewType - CORRECTION);

        if (type % TModelItem.TYPE_MASK == 0) {
            return type;
        } else {
            return type % TModelItem.TYPE_MASK;
        }
    }

    //ViewHolder section.

    public abstract THeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int layoutType);
    public abstract TItemViewHolder onCreateItemViewHolder(ViewGroup parent, int layoutType);
    public abstract TFooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int layoutType);

    public abstract void onBindHeaderViewHolder(THeaderViewHolder holder, TModel section, int layoutType);
    public abstract void onBindItemViewHolder(TItemViewHolder holder, TModelItem item, int layoutType);
    public abstract void onBindFooterViewHolder(TFooterViewHolder holder, TModel section, int layoutType);

    public LoaderItemViewHolder onCreateLoaderViewHolder(ViewGroup parent, int layoutType) {
        View view = LayoutInflater.from(mConnectedRecyclerView.getContext()).inflate(R.layout.view_progress,parent,false);
        return new LoaderItemViewHolder(view);
    }

    public void onBindLoaderViewHolder(LoaderItemViewHolder holder,TModel section, int layoutType) {
        //Not used yet.
    }

    public final void setOnSectionItemClickListener(OnItemClickListener listener) {
        this.mOnSectionItemClickListener = listener;
    }

    public final void setOnEndlessScrollListener(EndlessScrollListener listener) {
        this.mEndlessScrollListener = listener;
    }

    public final void setLoadMoreProgressEnabled(boolean isEnabled) {
        this.mEndlessScrollListener.setEndlessScrollEnabled(isEnabled);
    }

    public final void hideLoadMoreProgress() {
        removeShowMoreProgress();
    }

    private final void addEndlessScrollListener() {
        if (!isEndlessScrollListenerAdded && this.mEndlessScrollListener != null) {
            int countOfFilledSection = 0;

            for (TModel section : this.sections) {
                if (section.getSectionItems().size() > 0) {
                    countOfFilledSection ++;
                }
            }

            if ((this.sections.size() * FILLED_SECTION_IN_PERCENT) < countOfFilledSection) {
                this.mConnectedRecyclerView.addOnScrollListener(this.mEndlessScrollListener);
                this.isEndlessScrollListenerAdded = true;
            }
        }
    }

    private View.OnClickListener handleOnItemClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSectionItemClickListener != null) {
                    int absolutePos = mConnectedRecyclerView.getChildViewHolder(v).getAdapterPosition();
                    mOnSectionItemClickListener.onItemClick(allRowItems.get(absolutePos));
                }
            }
        };
    }

    public interface OnItemClickListener<TModelItem extends BaseSectionItemModel> {
        void onItemClick(TModelItem item);
    }

    public boolean isDraggingEnabledAtItemPosition(TModel section, TModelItem item) {
        return true;
    }

    /**
     * Drag & Drop section.
     *
     */

    public void onDrop() {
        //Not used yet.
    }

    /**
     * This should be reasonably performant as it gets called a lot on the UI thread.
     *
     * @return position of the item with the given id
     */
    public int getPositionForId(long id) {
        int pos = 0;

        for (TModelItem item : this.allRowItems) {
            if (item.getId().hashCode() == id) {
                return pos;
            }
            pos++;
        }

        return -1;
    }

    public boolean move(int fromPosition, int toPosition) {
        TModelItem itemAtFromPosition = getItemAtPosition(fromPosition);
        TModelItem itemAtToPosition = getItemAtPosition(toPosition);

        TModel fromSection = getSectionOfItem(itemAtFromPosition);
        TModel toSection = getSectionOfItem(itemAtToPosition);

        if (fromSection.equals(toSection)
                && isDraggingEnabledAtItemPosition(toSection,itemAtToPosition)
                && itemAtFromPosition.getViewType() == BaseSectionItemModel.VIEW_TYPE.TYPE_ITEM
                && itemAtToPosition.getViewType() == BaseSectionItemModel.VIEW_TYPE.TYPE_ITEM) {

            //We need modify item order in both list.
            toSection.getSectionItems().add(toSection.getItemIndexInSection(itemAtToPosition),toSection.getSectionItems().remove(toSection.getItemIndexInSection(itemAtFromPosition)));
            allRowItems.add(toPosition,allRowItems.remove(fromPosition));

            return true;
        }

        return false;
    }

    public long getDraggingId() {
        return dragManager.getDraggingId();
    }

    public PointF getLastTouchPoint() {
        return new PointF(lastTouchPoint.x, lastTouchPoint.y);
    }

    public void handleDragScroll(RecyclerView rv, DragInfo dragInfo) {
        if (rv.getLayoutManager().canScrollHorizontally()) {
            if (rv.canScrollHorizontally(-1) && dragInfo.shouldScrollLeft()) {
                rv.scrollBy(-SCROLL_AMOUNT, 0);
                dragManager.clearNextMove();
            } else if (rv.canScrollHorizontally(1) && dragInfo.shouldScrollRight(rv.getWidth())) {
                rv.scrollBy(SCROLL_AMOUNT, 0);
                dragManager.clearNextMove();
            }
        } else if (rv.getLayoutManager().canScrollVertically()) {
            if (rv.canScrollVertically(-1) && dragInfo.shouldScrollUp()) {
                rv.scrollBy(0, -SCROLL_AMOUNT);
                dragManager.clearNextMove();
            } else if (rv.canScrollVertically(1) && dragInfo.shouldScrollDown(rv.getHeight())) {
                rv.scrollBy(0, SCROLL_AMOUNT);
                dragManager.clearNextMove();
            }
        }
    }

    private void handleScroll(RecyclerView recyclerView) {
        if (scrollState != RecyclerView.SCROLL_STATE_IDLE) {
            return;
        }

        DragInfo lastDragInfo = dragManager.getLastDragInfo();
        if (lastDragInfo != null) {
            handleDragScroll(recyclerView, lastDragInfo);
        }
    }
}
