package hu.kole.cleversectionview.model;

import java.util.List;

/**
 * Created by koleszargergo on 4/7/16.
 */
public abstract class BaseSectionModel implements Cloneable{

    protected boolean isHeaderVisible = true;
    protected boolean isFooterVisible = true;

    public boolean isHeaderVisible() {
        return isHeaderVisible;
    }

    public boolean isFooterVisible() {
        return isFooterVisible;
    }

    public abstract String getId();
    public abstract String getTitle();
    public abstract <T extends BaseSectionItemModel> List<T> getSectionItems();

    public BaseSectionModel() {
    }

    private BaseSectionModel(BaseSectionModel model) {
        this.isHeaderVisible = model.isHeaderVisible;
        this.isFooterVisible = model.isFooterVisible;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new BaseSectionModel(this) {
            @Override
            public String getId() {
                return this.getId();
            }

            @Override
            public String getTitle() {
                return this.getTitle();
            }

            @Override
            public <T extends BaseSectionItemModel> List<T> getSectionItems() {
                return getSectionItems();
            }
        };
    }
}
