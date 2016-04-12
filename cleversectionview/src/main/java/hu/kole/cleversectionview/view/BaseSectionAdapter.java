package hu.kole.cleversectionview.view;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hu.kole.cleversectionview.model.BaseSectionItemModel;
import hu.kole.cleversectionview.model.BaseSectionModel;

/**
 * Created by koleszargergo on 4/7/16.
 */
public abstract class BaseSectionAdapter<TModel extends BaseSectionModel, TModelItem extends  BaseSectionItemModel, TItemViewHolder extends RecyclerView.ViewHolder, THeaderViewHolder extends RecyclerView.ViewHolder, TFooterViewHolder extends RecyclerView.ViewHolder>  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected List<TModel> sections = new ArrayList<>();
    private List<TModelItem> allRowItems = new ArrayList<>();

    protected RecyclerView mConnectedRecyclerView;

    public BaseSectionAdapter(List<TModel> sectionList) {
        this.sections.clear();
        this.sections.addAll(sectionList);

        this.allRowItems.clear();
        this.allRowItems.addAll(copySectionsIntoRealObjects());
    }

    protected List<TModelItem> copySectionsIntoRealObjects() {
        List<TModel> temp = new ArrayList<>(this.sections);
        List<TModelItem> sectionItems = new ArrayList<>();

        for (TModel section : temp) {
            List<TModelItem> sectionItemModels = section.getSectionItems();

            if (sectionItemModels != null) {
                if (section.isHeaderVisible() && sectionItemModels.get(0).getViewType() != BaseSectionItemModel.TYPE_SECTION_HEADER) {
                    TModelItem header = (TModelItem) BaseSectionItemModel.createHeaderItem(section.getId());
                    header.title = section.getTitle();
                    sectionItemModels.add(0, header);
                }

                if (section.isFooterVisible() && sectionItemModels.get(sectionItemModels.size() -1).getViewType() != BaseSectionItemModel.TYPE_SECTION_FOOTER) {
                    sectionItemModels.add((TModelItem) BaseSectionItemModel.createFooterItem(section.getId()));
                }

                sectionItems.addAll(sectionItemModels);
            }
        }

        return sectionItems;
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
        return allRowItems.get(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == BaseSectionItemModel.TYPE_SECTION_HEADER) {
            return onCreateHeaderViewHolder(parent,viewType);
        } else if (viewType == BaseSectionItemModel.TYPE_SECTION_FOOTER) {
            return onCreateFooterViewHolder(parent,viewType);
        } else {
            return onCreateItemViewHolder(parent,viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = allRowItems.get(position).getViewType();

        if (viewType == BaseSectionItemModel.TYPE_SECTION_HEADER) {
            onBindHeaderViewHolder((THeaderViewHolder) holder,position, viewType);
        } else if (viewType == BaseSectionItemModel.TYPE_SECTION_FOOTER) {
            onBindFooterViewHolder((TFooterViewHolder) holder,position, viewType);
        } else {
            onBindItemViewHolder((TItemViewHolder) holder,position, viewType);
        }
    }

    public abstract THeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType);
    public abstract TItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);
    public abstract TFooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindHeaderViewHolder(THeaderViewHolder holder, int position, int viewType);
    public abstract void onBindItemViewHolder(TItemViewHolder holder, int position, int viewType);
    public abstract void onBindFooterViewHolder(TFooterViewHolder holder, int position, int viewType);
}
