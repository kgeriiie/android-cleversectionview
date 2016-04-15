package hu.kole.cleversectionview.model;

/**
 * Created by koleszargergo on 4/7/16.
 */
public abstract class BaseSectionItemModel implements Cloneable {

    public static final int TYPE_SECTION_HEADER = -100000;
    public static final int TYPE_SECTION_FOOTER = -200000;
    public static final int TYPE_ITEM = -30000;

    private String parentId;
    public String title;
    private int _type = TYPE_ITEM;

    public BaseSectionItemModel() {}

    private BaseSectionItemModel(BaseSectionItemModel item) {
        this.title = item.title;
        this.parentId = item.parentId;
        this._type = item._type;
    }

    public abstract String getId();
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
    protected Object clone() throws CloneNotSupportedException {
        return new BaseSectionItemModel(this) {
            @Override
            public String getId() {
                return this.getId();
            }
        };
    }

    /**
     * Get view type of row item in base adapter. (It could be HEADER, ITEM OR FOOTER).
     *
     * It can't customize by user.
     *
     * @return base view type.
     */
    public final int getViewType() {
        return _type;
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

        header._type = TYPE_SECTION_HEADER;

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

        footer._type = TYPE_SECTION_FOOTER;

        return (T) footer;
    }
}
