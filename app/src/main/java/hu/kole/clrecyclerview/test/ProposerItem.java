package hu.kole.clrecyclerview.test;

import hu.kole.cleversectionview.model.BaseSectionItemModel;

/**
 * Created by koleszargergo on 4/12/16.
 */
public class ProposerItem extends BaseSectionItemModel {

    public static class LAYOUT_TYPE {
        public static final int CARD_LAYOUT = 0;
        public static final int COVER_LAYOUT = 1;
    }

    public String id;
    public String title;
    public int layoutType = LAYOUT_TYPE.CARD_LAYOUT;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getLayoutType() {
        return layoutType;
    }

    @Override
    public int getSpanType() {
        if (getLayoutType() == LAYOUT_TYPE.CARD_LAYOUT) {
            return SPAN_TYPE.LINEAR_TYPE;
        } else {
            return SPAN_TYPE.GRID_TYPE;
        }
    }
}
