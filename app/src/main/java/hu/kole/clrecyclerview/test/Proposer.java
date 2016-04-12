package hu.kole.clrecyclerview.test;

import java.util.List;

import hu.kole.cleversectionview.model.BaseSectionItemModel;
import hu.kole.cleversectionview.model.BaseSectionModel;

/**
 * Created by koleszargergo on 4/12/16.
 */

public class Proposer extends BaseSectionModel {

    public String id;
    public String title;
    public List<ProposerItem> proposerItems;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public <T extends BaseSectionItemModel> List<T> getSectionItems() {
        return (List<T>) proposerItems;
    }

    public void setFooterVisibility(boolean isVisible) {
        this.isFooterVisible = isVisible;
    }

    public void setHeaderVisibility(boolean isVisible) {
        this.isHeaderVisible = isVisible;
    }
}
