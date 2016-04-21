package hu.kole.cleversectionviewadapter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by koleszargergo on 4/7/16.
 */
public abstract class BaseSectionModel implements Cloneable {

    protected boolean isHeaderVisible = true;
    protected boolean isFooterVisible = true;

    public boolean isHeaderVisible() {
        return isHeaderVisible;
    }

    public boolean isFooterVisible() {
        return isFooterVisible;
    }

    public abstract String getId();
    public abstract <T extends BaseSectionItemModel> List<T> getSectionItems();
    public abstract <T extends BaseSectionItemModel> void setSectionItems(List<T> sectionItems);

    public BaseSectionModel() {
    }

    public int getSectionItemCount() {
        return getSectionItems().size();
    }

    public boolean equals(BaseSectionModel model) {
        return getId().equalsIgnoreCase(model.getId());
    }

    @Override
    public Object clone() {
        try {
            BaseSectionModel model = (BaseSectionModel) super.clone();
            model.setSectionItems(new ArrayList<>(model.getSectionItems()));
            return model;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }

    /**
     * Get item index in section.
     * @param <T>   row item.
     * @return      index of item in this section or -1 if it isn't in this section.
     */
    public <T extends BaseSectionItemModel>int getItemIndexInSection(T rowItem) {

        int pos = 0;

        for (BaseSectionItemModel item : getSectionItems()) {

            if (item.equals(rowItem)) {
                return pos;
            }

            pos ++;
        }

        return -1;
    }

    /**
     * Is it a header item type at first place.
     * @param <T>
     * @return
     */
    public final <T extends BaseSectionItemModel> boolean isHeaderItemAtFirstPosition() {
        List<T> sectionItems = getSectionItems();

        if (sectionItems.size() > 0) {
            return sectionItems.get(0).getViewType() == BaseSectionItemModel.VIEW_TYPE.TYPE_SECTION_HEADER;
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

            return sectionItems.get(lastIndex).getViewType() == BaseSectionItemModel.VIEW_TYPE.TYPE_SECTION_FOOTER;
        } else {
            return false;
        }
    }
}
