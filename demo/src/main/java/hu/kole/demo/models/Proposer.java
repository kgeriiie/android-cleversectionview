package hu.kole.demo.models;

import java.util.ArrayList;
import java.util.List;

import hu.kole.cleversectionviewadapter.model.BaseSectionItemModel;
import hu.kole.cleversectionviewadapter.model.BaseSectionModel;

/**
 * Created by koleszargergo on 4/12/16.
 */

public class Proposer extends BaseSectionModel {

    public String id;
    public String title;
    public List<ProposerItem> proposerItems = new ArrayList<>();

    public boolean isDraggable = false;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public <T extends BaseSectionItemModel> List<T> getSectionItems() {
        return (List<T>) proposerItems;
    }

    @Override
    public <T extends BaseSectionItemModel> void setSectionItems(List<T> sectionItems) {
        proposerItems = (List<ProposerItem>) sectionItems;
    }

    public void setFooterVisibility(boolean isVisible) {
        this.isFooterVisible = isVisible;
    }

    public void setHeaderVisibility(boolean isVisible) {
        this.isHeaderVisible = isVisible;
    }

    @Override
    public int getSectionItemCount() {
        return getSectionItems().size();
    }
}
