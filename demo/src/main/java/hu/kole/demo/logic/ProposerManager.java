package hu.kole.demo.logic;

import java.util.ArrayList;
import java.util.List;

import hu.kole.demo.models.Proposer;
import hu.kole.demo.models.ProposerItem;

/**
 * Created by koleszargergo on 4/19/16.
 */
public class ProposerManager {

    private static ProposerManager mInstance = null;

    private int proposerId = 0;
    private int proposerItemId = 0;

    private static final String PROPOSER_ID = "PRO_";
    private static final String ITEM_ID = "ITEM_";

    public static ProposerManager getInstance() {
        if (mInstance == null) {
            mInstance = new ProposerManager();
        }

        return mInstance;
    }

    private ProposerManager() {
        proposerId = 0;
        proposerItemId = 0;
    }

    public void bindItemsToProposer(List<Proposer> proposers, List<ProposerItem> items) {
        for (Proposer p : proposers) {
            if (p.getId().equalsIgnoreCase(items.get(0).getParentId())) {
                p.setSectionItems(items);
                return;
            }
        }
    }

    public Proposer generateProposerBanner() {
        Proposer p = createProposer(proposerId, "BANNER_BANNER", false, false);
        proposerId ++;
        return p;
    }

    public List<Proposer> generateProposers(int size) {
        List<Proposer> proposers = new ArrayList<>();

        for (int i = 0;i < size; i++) {
            int index = proposerId + i;
            proposers.add(createProposer(index,"SECTION_" + index));
        }

        proposerId += size;

        return proposers;
    }

    public List<ProposerItem> generateBanner(String proposerParentId) {
        List<ProposerItem> proposerItems = new ArrayList<>();

        int index = proposerItemId;
        ProposerItem proposerItem = createProposerItem(index,"BANNER_" + index);
        proposerItem.setParentId(proposerParentId);
        proposerItems.add(proposerItem);

        proposerItemId ++;

        return proposerItems;
    }

    public List<ProposerItem> generateProposerItemsTypeOne(int size, String proposerParentId) {
        List<ProposerItem> proposerItems = new ArrayList<>();

        for (int i = 0;i < size; i++) {
            int index = proposerItemId + i;
            ProposerItem proposerItem = createProposerItem(index,"ITEM_" + index);
            proposerItem.setParentId(proposerParentId);
            proposerItems.add(proposerItem);
        }

        proposerItemId += size;

        return proposerItems;
    }

    public List<ProposerItem> generateProposerItemsMixed(int size, String proposerParentId) {
        List<ProposerItem> proposerItems = new ArrayList<>();

        for (int i = 0;i < size; i++) {
            int index = proposerItemId + i;
            ProposerItem proposerItem = createProposerItem(index,"ITEM_" + index);
            proposerItem.setParentId(proposerParentId);
            proposerItem.layoutType = i % 2 == 0 ? ProposerItem.LAYOUT_TYPE.CARD_LAYOUT_GREEN : ProposerItem.LAYOUT_TYPE.CARD_LAYOUT_RED;
            proposerItems.add(proposerItem);
        }

        proposerItemId += size;

        return proposerItems;
    }

    public List<ProposerItem> generateProposerItemsTypeTwo(int size, String proposerParentId) {
        List<ProposerItem> proposerItems = new ArrayList<>();

        for (int i = 0;i < size; i++) {
            int index = proposerItemId + i;
            ProposerItem proposerItem = createProposerItem(index,"ITEM_" + index);
            proposerItem.setParentId(proposerParentId);
            proposerItem.layoutType = ProposerItem.LAYOUT_TYPE.COVER_LAYOUT;
            proposerItems.add(proposerItem);
        }

        proposerItemId += size;

        return proposerItems;
    }

    private synchronized Proposer createProposer(int index, String title) {
        Proposer proposer = new Proposer();
        proposer.id = PROPOSER_ID + index;
        proposer.title = title;

        return proposer;
    }

    private Proposer createProposer(int index, String title,boolean isHeaderVisible, boolean isFooterVisible) {
        Proposer proposer = new Proposer();
        proposer.id = PROPOSER_ID + index;
        proposer.title = title;
        proposer.setHeaderVisibility(isHeaderVisible);
        proposer.setFooterVisibility(isFooterVisible);

        return proposer;
    }

    private ProposerItem createProposerItem(int index, String title) {
        ProposerItem item = new ProposerItem();
        item.id = ITEM_ID + index;
        item.title = title;

        return item;
    }
}
