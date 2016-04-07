package hu.kole.cleversectionview.view;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import hu.kole.cleversectionview.model.BaseModel;

/**
 * Created by koleszargergo on 4/7/16.
 */
public class BaseSectionRecyclerView<TModel extends BaseModel, TItemViewHolder extends RecyclerView.ViewHolder, THeaderViewHolder extends RecyclerView.ViewHolder, TFooterViewHolder extends RecyclerView.ViewHolder>  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected List<TModel> sections;

    public BaseSectionRecyclerView(List<TModel> sectionList) {
        this.sections = sectionList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
