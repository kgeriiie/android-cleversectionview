package hu.kole.clrecyclerview.test;

import hu.kole.cleversectionview.model.BaseSectionItemModel;

/**
 * Created by koleszargergo on 4/12/16.
 */
public class ProposerItem extends BaseSectionItemModel {

    public static class LAYOUT_TYPE {
        public static final int GREEN_LAYOUT = 0;
        public static final int RED_LAYOUT = 1;
    }

    public String id;
    public String title;
    public int layoutType = LAYOUT_TYPE.RED_LAYOUT;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getLayoutType() {
        return layoutType;
    }
}
