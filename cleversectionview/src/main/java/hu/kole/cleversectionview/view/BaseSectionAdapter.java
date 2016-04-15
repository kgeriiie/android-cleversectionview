package hu.kole.cleversectionview.view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hu.kole.cleversectionview.model.BaseSectionItemModel;
import hu.kole.cleversectionview.model.BaseSectionModel;

/**
 * Created by koleszargergo on 4/7/16.
 */
public abstract class BaseSectionAdapter<TModel extends BaseSectionModel, TModelItem extends  BaseSectionItemModel, TItemViewHolder extends RecyclerView.ViewHolder, THeaderViewHolder extends RecyclerView.ViewHolder, TFooterViewHolder extends RecyclerView.ViewHolder>  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final static int CORRECTION = 1;

    protected List<TModel> sections = new ArrayList<>();
    private List<TModelItem> allRowItems = new ArrayList<>();

    protected RecyclerView mConnectedRecyclerView;

    public BaseSectionAdapter(List<TModel> sectionList) {

        setHasStableIds(true);

        this.sections.clear();
        this.sections.addAll(sectionList);

        this.allRowItems.clear();
        this.allRowItems.addAll(copySectionsIntoRealObjects());
    }

    protected final List<TModelItem> copySectionsIntoRealObjects() {
        List<TModel> temp = new ArrayList<>(this.sections);
        List<TModelItem> sectionItems = new ArrayList<>();

        for (TModel section : temp) {
            List<TModelItem> sectionItemModels = section.getSectionItems();

            if (sectionItemModels != null) {
                if (section.isHeaderVisible() && !section.isHeaderItemAtFirstPosition()) {
                    TModelItem header = BaseSectionItemModel.createHeaderItem(section.getId());
                    header.title = section.getTitle();
                    header.setParentId(section.getId());
                    sectionItemModels.add(0, header);
                }

                if (section.isFooterVisible() && !section.isFooterItemAtLastPosition()) {
                    TModelItem footer = BaseSectionItemModel.createFooterItem(section.getId());
                    footer.title = section.getTitle();
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
        sections.clear();
        allRowItems.clear();

        this.sections.addAll(data);
        this.allRowItems.addAll(copySectionsIntoRealObjects());

        this.notifyDataSetChanged();
    }

    public TModelItem getItemAtPosition(int position) {
        return this.allRowItems.get(position);
    }

    private final TModel getSectionOfItem(TModelItem item) {
        for (TModel section : sections) {
            if (section.getId().equalsIgnoreCase(item.getParentId())) {
                return section;
            }
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return getItemAtPosition(position).getId().hashCode();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mConnectedRecyclerView = recyclerView;
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

        if (type == BaseSectionItemModel.TYPE_SECTION_HEADER) {
            return onCreateHeaderViewHolder(parent,convertToLayoutType(viewType));

        } else if (type == BaseSectionItemModel.TYPE_SECTION_FOOTER) {
            return onCreateFooterViewHolder(parent,convertToLayoutType(viewType));

        } else {
            return onCreateItemViewHolder(parent,convertToLayoutType(viewType));

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        TModelItem item = getItemAtPosition(position);
        TModel section = getSectionOfItem(item);
        int viewType = item.getViewType();

        if (viewType == BaseSectionItemModel.TYPE_SECTION_HEADER) {
            onBindHeaderViewHolder((THeaderViewHolder) holder,section, item.getLayoutType());

        } else if (viewType == BaseSectionItemModel.TYPE_SECTION_FOOTER) {
            onBindFooterViewHolder((TFooterViewHolder) holder,section, item.getLayoutType());

        } else {
            onBindItemViewHolder((TItemViewHolder) holder,item, item.getLayoutType());

        }
    }

    private final int convertToLayoutType(int rawType) {
        int type  = (rawType - CORRECTION);

        if (type % TModelItem.TYPE_SECTION_HEADER == 0 || type % TModelItem.TYPE_SECTION_FOOTER == 0  || type % TModelItem.TYPE_ITEM == 0) {
            return type;
        } else {
            return type % 100000;
        }
    }

    public abstract THeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int layoutType);
    public abstract TItemViewHolder onCreateItemViewHolder(ViewGroup parent, int layoutType);
    public abstract TFooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int layoutType);

    public abstract void onBindHeaderViewHolder(THeaderViewHolder holder, TModel section, int layoutType);
    public abstract void onBindItemViewHolder(TItemViewHolder holder, TModelItem item, int layoutType);
    public abstract void onBindFooterViewHolder(TFooterViewHolder holder, TModel section, int layoutType);

}
