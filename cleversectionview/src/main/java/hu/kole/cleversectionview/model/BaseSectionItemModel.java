package hu.kole.cleversectionview.model;

/**
 * Created by koleszargergo on 4/7/16.
 */
public abstract class BaseSectionItemModel implements Cloneable{

    public static final int TYPE_SECTION_HEADER = -1;
    public static final int TYPE_SECTION_FOOTER = -2;
    public static final int TYPE_ITEM = -3;

    public String title;

    private int _type = TYPE_ITEM;

    public abstract String getId();

    public static BaseSectionItemModel createHeaderItem(final String id) {

        BaseSectionItemModel header = new BaseSectionItemModel() {
            @Override
            public String getId() {
                return "HEADER_" + id;
            }
        };

        header._type = TYPE_SECTION_HEADER;

        return header;
    }

    public static BaseSectionItemModel createFooterItem(final String id) {

        BaseSectionItemModel footer = new BaseSectionItemModel() {
            @Override
            public String getId() {
                return "FOOTER_" + id;
            }
        };

        footer._type = TYPE_SECTION_FOOTER;

        return footer;
    }

    public BaseSectionItemModel() {}

    private BaseSectionItemModel(BaseSectionItemModel item) {
        this.title = item.title;
        this._type = item._type;
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

    public int getViewType() {
        return _type;
    }
}
