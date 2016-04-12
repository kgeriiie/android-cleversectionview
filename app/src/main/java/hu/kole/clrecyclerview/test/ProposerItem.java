package hu.kole.clrecyclerview.test;

import hu.kole.cleversectionview.model.BaseSectionItemModel;

/**
 * Created by koleszargergo on 4/12/16.
 */
public class ProposerItem extends BaseSectionItemModel {

    public String id;
    public String title;

    @Override
    public String getId() {
        return this.id;
    }
}
