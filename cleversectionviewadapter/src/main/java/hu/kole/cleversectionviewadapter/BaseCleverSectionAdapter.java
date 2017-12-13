package hu.kole.cleversectionviewadapter;

import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hu.kole.cleversectionviewadapter.draganddrop.DragInfo;
import hu.kole.cleversectionviewadapter.draganddrop.DragManager;
import hu.kole.cleversectionviewadapter.listeners.EndlessScrollListener;
import hu.kole.cleversectionviewadapter.model.BaseSectionItemModel;
import hu.kole.cleversectionviewadapter.model.BaseSectionModel;
import hu.kole.cleversectionviewadapter.model.LoaderSectionItem;
import hu.kole.cleversectionviewadapter.viewholder.BaseDragAndDropViewHolder;
import hu.kole.cleversectionviewadapter.viewholder.LoaderItemViewHolder;

/**
 * Created by koleszargergo on 4/7/16.
 */
public abstract class BaseCleverSectionAdapter<TSection extends BaseSectionModel, TSectionItem extends  BaseSectionItemModel, TItemViewHolder extends BaseDragAndDropViewHolder, THeaderViewHolder extends RecyclerView.ViewHolder, TFooterViewHolder extends RecyclerView.ViewHolder>  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final static int CORRECTION = 1;
    private final static double FILLED_SECTION_IN_PERCENT = 0.8;

    protected List<TSection> sections = new ArrayList<>();
    private List<TSectionItem> allRowItems = new ArrayList<>();

    protected RecyclerView mConnectedRecyclerView;

    protected OnItemClickListener<TSectionItem> mOnSectionItemClickListener;
    protected EndlessScrollListener mEndlessScrollListener;

    private boolean isEndlessScrollListenerAdded = false;
    private boolean isLoadMoreProgressVisible = false;
    private boolean isDragAndDropEnabled = true;

    private final int SCROLL_AMOUNT = (int) (2 * Resources.getSystem().getDisplayMetrics().density);

    private DragManager dragManager;
    private int scrollState = RecyclerView.SCROLL_STATE_IDLE;
    private final PointF lastTouchPoint = new PointF(); // used to create ShadowBuilder

    private Handler mDelayHandler = new Handler();
    private long mLastUpdateTime = -1;
    private long DELAY_BETWEEN_UPDATES = 0;

    public BaseCleverSectionAdapter(List<TSection> sectionList) {

        setHasStableIds(true);

        isLoadMoreProgressVisible = false;

        this.sections.clear();
        this.sections.addAll(sectionList);

        this.allRowItems.clear();
        this.allRowItems.addAll(copySectionsIntoRealObjects());

        addEndlessScrollListener();
    }

    /**
     *  Create a one dimension array from this.sections two dimensional array. BaseCleverSectionAdapter will use them to display row items in a simple recycler view.
     *
     * @return One dimensional array of sections.
     */
    protected final List<TSectionItem> copySectionsIntoRealObjects() {
        List<TSection> temp = cloneSectionList(this.sections);
        List<TSectionItem> sectionItems = new ArrayList<>();

        for (TSection section : temp) {

            List<TSectionItem> sectionItemModels = section.getSectionItems().size() > 0 ? section.<TSectionItem>getSectionItems().subList(0,section.getSectionItemCount()) : section.<TSectionItem>getSectionItems();

            if (sectionItemModels != null) {
                if (section.isHeaderVisible() && !section.isHeaderItemAtFirstPosition()) {
                    TSectionItem header = BaseSectionItemModel.createHeaderItem(section.getId());
                    header.setParentId(section.getId());
                    sectionItemModels.add(0, header);
                }

                if (section.isFooterVisible() && !section.isFooterItemAtLastPosition()) {
                    TSectionItem footer = BaseSectionItemModel.createFooterItem(section.getId());
                    footer.setParentId(section.getId());
                    sectionItemModels.add(footer);
                }

                for (TSectionItem item : sectionItemModels) {
                    item.setParentId(section.getId());
                }

                sectionItems.addAll(sectionItemModels);
            }
        }

        return sectionItems;
    }

    public void updateDataSet(final List<TSection> data) {
        mDelayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isLoadMoreProgressVisible = false;

                sections.clear();

                sections.addAll(data);
                updateDataSetAfterChanged();

                addEndlessScrollListener();
            }
        },calculateDelay());
    }

    public void updateDataSetAfterChanged() {

        this.allRowItems.clear();
        this.allRowItems.addAll(copySectionsIntoRealObjects());

        this.notifyDataSetChanged();
    }

    /**
     * Calculate delay between call of method updateDataSet. It's essential for schedule often successively again method calls.
     * @return
     */
    public synchronized long calculateDelay() {
        long currentTime = System.currentTimeMillis();

        if (mLastUpdateTime == -1 || currentTime > mLastUpdateTime) {
            mLastUpdateTime = currentTime + DELAY_BETWEEN_UPDATES;
            return 0;
        }

        mLastUpdateTime = mLastUpdateTime + DELAY_BETWEEN_UPDATES;

        return (mLastUpdateTime - currentTime) + DELAY_BETWEEN_UPDATES;
    }

    public void setDelayBetweenUpdates(long delay) {
        this.DELAY_BETWEEN_UPDATES = delay;
    }

    public long getDelayBetweenUpdates() {
        return this.DELAY_BETWEEN_UPDATES;
    }

    public List<TSection> getDataSet() {
        return this.sections;
    }

    /**
     * Add a loader item to the end of list, if it is not added.
     */
    public final void addShowMoreProgress() {
        if (!isLoadMoreProgressVisible && this.sections.size() > 0) {
            TSection lastSection = this.sections.get(this.sections.size() -1);

            if (lastSection != null
                    && (lastSection.getSectionItems().size() == 0 || lastSection.getSectionItems().get(0).getViewType() != BaseSectionItemModel.VIEW_TYPE.TYPE_LOADER)) {

                TSection loader = (TSection) new LoaderSectionItem();

                this.sections.add(loader);

                this.updateDataSetAfterChanged();

                isLoadMoreProgressVisible = true;
            }
        }
    }

    /**
     * Remove loader item from end of list, if it is added.
     */
    private final void removeShowMoreProgress() {
        if (isLoadMoreProgressVisible && this.sections.size() > 0) {
            TSection lastSection = this.sections.get(this.sections.size() -1);

            if (lastSection != null && lastSection.getSectionItems().size() > 0 && lastSection.getSectionItems().get(0).getViewType() == BaseSectionItemModel.VIEW_TYPE.TYPE_LOADER) {

                this.sections.remove(this.sections.size() - 1);

                this.updateDataSetAfterChanged();

                isLoadMoreProgressVisible = false;
            }
        }
    }

    public TSectionItem getItemAtPosition(int position) {
        if (allRowItems.size() == 0)
            updateDataSetAfterChanged();

        if (position < 0) {
            return null;
        }

        return this.allRowItems.get(position);
    }

    public synchronized void removeItem(TSectionItem content) {
        int index = -1;
        int section = -1;
        int j = 0;
        for (TSection proposer : getDataSet()) {
            for (int i = 0; i < proposer.getSectionItems().size(); i++) {
                if (content.equals(proposer.getSectionItems().get(i))) {
                    index = i;
                    section = j;
                }
            }
            j++;
        }

        if (section > -1 && index > -1)
            this.getDataSet().get(section).getSectionItems().remove(index);

        this.updateDataSetAfterChanged();
    }

    public synchronized void addItemsToSection(String sectionId, List<TSectionItem> items) {
        for (TSection section : sections) {
            if (section.getId().equalsIgnoreCase(sectionId)) {
                section.setSectionItems(items);
            }
        }

        this.updateDataSetAfterChanged();
    }

    public final TSection getSectionOfItem(TSectionItem item) {
        for (TSection section : sections) {
            if (section.getId().equalsIgnoreCase(item.getParentId())) {
                return section;
            }
        }

        return null;
    }

    private final List<TSection> cloneSectionList(List<TSection> list) {
        List<TSection> temp = new ArrayList<>();

        for (TSection model : this.sections) {
            temp.add((TSection) model.clone());
        }

        return temp;
    }

    /**
     * Set up drag and drop on recycler view.
     *
     * @param recyclerView  Connected recycler view.
     */
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
        TSectionItem item = getItemAtPosition(position);

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

        TSectionItem item = getItemAtPosition(position);
        TSection section = getSectionOfItem(item);
        int viewType = item.getViewType();

        if (viewType == BaseSectionItemModel.VIEW_TYPE.TYPE_SECTION_HEADER) {
            onBindHeaderViewHolder((THeaderViewHolder) holder,section, item.getLayoutType());

        } else if (viewType == BaseSectionItemModel.VIEW_TYPE.TYPE_SECTION_FOOTER) {
            onBindFooterViewHolder((TFooterViewHolder) holder,section, item.getLayoutType());

        } else if (viewType == BaseSectionItemModel.VIEW_TYPE.TYPE_LOADER) {
            onBindLoaderViewHolder((LoaderItemViewHolder) holder, section, item.getLayoutType());

        } else {
            onBindItemViewHolder((TItemViewHolder) holder,item, item.getLayoutType());

            ((TItemViewHolder) holder).setOnSectionItemClickListener((OnItemClickListener<BaseSectionItemModel>) mOnSectionItemClickListener);

            if (isDragAndDropEnabled && isDraggingEnabledAtItemPosition(section,item,item)) {
                holder.itemView.setOnLongClickListener((TItemViewHolder) holder);
                holder.itemView.setVisibility(getDraggingId() == item.getId().hashCode() ? View.INVISIBLE : View.VISIBLE);
                holder.itemView.postInvalidate();
            } else {
                holder.itemView.setOnLongClickListener(null);
            }
        }
    }

    /**
     * Convert layout type.
     *
     * @param rawViewType - raw viewType. (sum of viewType, layoutType and correction value 1)
     * @return base type of view or layout type of view if type has remainder value.
     */
    private final int convertToLayoutType(int rawViewType) {
        int type  = (rawViewType - CORRECTION);

        if (type < 0) {
            return type;
        } else {
            return type % TSectionItem.TYPE_MASK;
        }
    }

    //ViewHolder section.
    public abstract THeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int layoutType);
    public abstract TItemViewHolder onCreateItemViewHolder(ViewGroup parent, int layoutType);
    public abstract TFooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int layoutType);

    public abstract void onBindHeaderViewHolder(THeaderViewHolder holder, TSection section, int layoutType);
    public abstract void onBindItemViewHolder(TItemViewHolder holder, TSectionItem item, int layoutType);
    public abstract void onBindFooterViewHolder(TFooterViewHolder holder, TSection section, int layoutType);

    public LoaderItemViewHolder onCreateLoaderViewHolder(ViewGroup parent, int layoutType) {
        View view = LayoutInflater.from(mConnectedRecyclerView.getContext()).inflate(R.layout.view_progress,parent,false);
        return new LoaderItemViewHolder(view);
    }

    public void onBindLoaderViewHolder(LoaderItemViewHolder holder, TSection section, int layoutType) {
        //Not used yet.
    }

    public RecyclerView getConnectedRecyclerView() {
        return this.mConnectedRecyclerView;
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

    public final void setDragAndDropEnabled(boolean isEnabled) {
        this.isDragAndDropEnabled = isEnabled;
    }

    public final void hideLoadMoreProgress() {
        removeShowMoreProgress();
    }

    /**
     * Add endlessScrollListener to recycler view after more than value of FILLED_SECTION_IN_PERCENT section filled.
     *
     */
    private final void addEndlessScrollListener() {
        if (!isEndlessScrollListenerAdded && this.mEndlessScrollListener != null) {
            int countOfFilledSection = 0;

            for (TSection section : this.sections) {
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
                    mOnSectionItemClickListener.onItemClick(v, allRowItems.get(absolutePos));
                }
            }
        };
    }

    public interface OnItemClickListener<TModelItem extends BaseSectionItemModel> {
        void onItemClick(View view, TModelItem item);
    }

    /**
     * Enable or disable dragging options on items or sections.
     * @param section
     * @param fromItem
     * @param toItem
     * @return
     */
    public boolean isDraggingEnabledAtItemPosition(TSection section,TSectionItem fromItem, TSectionItem toItem) {
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

        for (TSectionItem item : this.allRowItems) {
            if (item.getId().hashCode() == id) {
                return pos;
            }
            pos++;
        }

        return -1;
    }

    public boolean move(int fromPosition, int toPosition) {
        TSectionItem itemAtFromPosition = getItemAtPosition(fromPosition);
        TSectionItem itemAtToPosition = getItemAtPosition(toPosition);

        if (itemAtFromPosition == null || itemAtToPosition == null) {
            return false;
        }

        TSection fromSection = getSectionOfItem(itemAtFromPosition);
        TSection toSection = getSectionOfItem(itemAtToPosition);

        if (fromSection.equals(toSection)
                && itemAtFromPosition.getViewType() == BaseSectionItemModel.VIEW_TYPE.TYPE_ITEM
                && itemAtToPosition.getViewType() == BaseSectionItemModel.VIEW_TYPE.TYPE_ITEM
                && isDraggingEnabledAtItemPosition(toSection,itemAtFromPosition,itemAtToPosition)) {

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
