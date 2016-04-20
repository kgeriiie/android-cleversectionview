package hu.kole.cleversectionviewadapter.model;

/**
 * Created by koleszargergo on 4/7/16.
 */
public abstract class BaseSectionItemModel implements Cloneable {

    public static final int TYPE_MASK = 10000;

    public static class VIEW_TYPE {
        public static final int TYPE_SECTION_HEADER =   -1 * TYPE_MASK;
        public static final int TYPE_SECTION_FOOTER =   -2 * TYPE_MASK;
        public static final int TYPE_LOADER =           -3 * TYPE_MASK;
        public static final int TYPE_ITEM =             -4 * TYPE_MASK;
    }

    public static class SPAN_TYPE {
        public static final int LINEAR_TYPE = 0;
        public static final int GRID_TYPE = 1;
    }

    private String parentId;
    private int _type = VIEW_TYPE.TYPE_ITEM;

    public BaseSectionItemModel() {}

    public abstract String getId();

    //Bind to parent section.
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getParentId() {
        return this.parentId;
    }

    public boolean equals(BaseSectionItemModel model) {
        return getId().equals(model.getId());
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }

    /**
     * Get view type of row item in base adapter. (It could be HEADER, ITEM, FOOTER OR LOADER).
     *
     * It can't customize by user.
     *
     * @return base view type.
     */
    public final int getViewType() {
        return _type;
    }

    /**
     * Get span type of row item in base adapter. (It could be GRID OR LINEAR).
     *
     * User can customize, witch will be choose.
     *
     * @return base view type.
     */
    public int getSpanType() {
        return SPAN_TYPE.LINEAR_TYPE;
    }

    /**
     * Get layout type of row item. It can be customize by user. In default mode it equals with getViewType.
     * @return customized view type.
     */
    public int getLayoutType() {
        return getViewType();
    }

    /**
     * Create a new header row type item.
     *
     * @param id    Parent section's id.
     * @param <T>
     * @return      Row item.
     */
    public static final <T extends BaseSectionItemModel> T createHeaderItem(final String id) {

        BaseSectionItemModel header =  new BaseSectionItemModel() {
            @Override
            public String getId() {
                return "HEADER_" + id;
            }
        };

        header._type = VIEW_TYPE.TYPE_SECTION_HEADER;

        return (T) header;
    }

    /**
     * Create a new footer row type item.
     *
     * @param id    Parent section's id.
     * @param <T>
     * @return      Row item.
     */
    public static final <T extends BaseSectionItemModel> T createFooterItem(final String id) {

        BaseSectionItemModel footer = new BaseSectionItemModel() {
            @Override
            public String getId() {
                return "FOOTER_" + id;
            }
        };

        footer._type = VIEW_TYPE.TYPE_SECTION_FOOTER;

        return (T) footer;
    }

    /**
     * Create a new loader row type item.
     *
     * @param id    Parent section's id.
     * @param <T>
     * @return      Row item.
     */
    public static final <T extends BaseSectionItemModel> T createLoaderItem(final String id) {

        BaseSectionItemModel loader = new BaseSectionItemModel() {
            @Override
            public String getId() {
                return "LOADER_" + id;
            }
        };

        loader._type = VIEW_TYPE.TYPE_LOADER;

        return (T) loader;
    }
}
