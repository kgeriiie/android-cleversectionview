package hu.kole.demo.models;

import hu.kole.cleversectionviewadapter.model.BaseSectionItemModel;

/**
 * Created by koleszargergo on 4/12/16.
 */
public class ProposerItem extends BaseSectionItemModel {

    public static class LAYOUT_TYPE {
        public static final int CARD_LAYOUT = 0;
        public static final int CARD_LAYOUT_GREEN = 1;
        public static final int CARD_LAYOUT_RED = 2;
        public static final int COVER_LAYOUT = 3;
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
        if (getLayoutType() == LAYOUT_TYPE.CARD_LAYOUT || getLayoutType() == LAYOUT_TYPE.CARD_LAYOUT_GREEN || getLayoutType() == LAYOUT_TYPE.CARD_LAYOUT_RED) {
            return SPAN_TYPE.LINEAR_TYPE;
        } else {
            return SPAN_TYPE.GRID_TYPE;
        }
    }
}
