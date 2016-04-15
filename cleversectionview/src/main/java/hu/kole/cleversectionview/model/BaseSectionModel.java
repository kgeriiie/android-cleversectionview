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

    /**
     * Is it a header item type at first place.
     * @param <T>
     * @return
     */
    public final <T extends BaseSectionItemModel> boolean isHeaderItemAtFirstPosition() {
        List<T> sectionItems = getSectionItems();

        if (sectionItems.size() > 0) {
            return sectionItems.get(0).getViewType() == BaseSectionItemModel.TYPE_SECTION_HEADER;
        } else {
            return false;
        }
    }

    /**
     * Is it a footer item type at last place.
     * @param <T>
     * @return
     */
    public final <T extends BaseSectionItemModel> boolean isFooterItemAtLastPosition() {
        List<T> sectionItems = getSectionItems();

        if (sectionItems.size() > 0) {
            int lastIndex = sectionItems.size() - 1;

            return sectionItems.get(lastIndex).getViewType() == BaseSectionItemModel.TYPE_SECTION_FOOTER;
        } else {
            return false;
        }
    }
}
