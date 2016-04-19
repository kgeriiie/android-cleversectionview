package hu.kole.cleversectionview.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by koleszargergo on 4/18/16.
 */
public class LoaderSectionItem extends BaseSectionModel {

    public List<BaseSectionItemModel> items = new ArrayList<>();
    private final int id;

    public LoaderSectionItem() {
        Random random = new Random();
        id = random.nextInt(1000);

        isFooterVisible = false;
        isHeaderVisible = false;
        items = Arrays.asList(BaseSectionItemModel.createLoaderItem(String.valueOf(id)));
    }

    @Override
    public String getId() {
        return "LOADER_SECTION_" + id;
    }

    @Override
    public <T extends BaseSectionItemModel> List<T> getSectionItems() {
        return (List<T>) items;
    }

    @Override
    public <T extends BaseSectionItemModel> void setSectionItems(List<T> sectionItems) {
        this.items = (List<BaseSectionItemModel>) sectionItems;
    }
}
