package hu.kole.clrecyclerview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.kole.clrecyclerview.test.Proposer;
import hu.kole.clrecyclerview.test.ProposerItem;
import hu.kole.clrecyclerview.test.TestAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView listRv;
    TestAdapter adapter;
    List<Proposer> proposers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listRv = (RecyclerView) findViewById(R.id.testRl);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        proposers.addAll(createHeaderAndFooterLessProposer());
        proposers.addAll(createHeaderLessProposer());
        proposers.addAll(createFooterLessProposer());
        proposers.addAll(createGeneralProposer());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                schedule();
            }
        },1000);

        adapter = new TestAdapter(this,proposers);

        listRv.setItemAnimator(new DefaultItemAnimator());
        listRv.setLayoutManager(llm);
        listRv.setAdapter(adapter);
    }

    private void schedule() {
        Handler h = new Handler();

//        h.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                proposers.get(0).proposerItems = createGenerailItems0();
//                adapter.updateDataSet(proposers);
//            }
//        },3500);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                proposers.get(1).proposerItems = createGenerailItems1();
                adapter.updateDataSet(proposers);
            }
        },3000);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                proposers.get(2).proposerItems = createGenerailItems2();
                adapter.updateDataSet(proposers);
            }
        },2000);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                proposers.get(3).proposerItems = createGenerailItems3();
                adapter.updateDataSet(proposers);
            }
        },1000);

    }

    private List<Proposer> createHeaderAndFooterLessProposer() {
        Proposer p1 = new Proposer();
        p1.id = "PID_PROP_0";
        p1.title = "minden nélkül";
        p1.setHeaderVisibility(false);
        p1.setFooterVisibility(false);

        return Arrays.asList(p1);
    }

    private List<Proposer> createHeaderLessProposer() {
        Proposer p1 = new Proposer();
        p1.id = "PID_PROP_1";
        p1.title = "Ez csak egyedül egy footer";
        p1.setHeaderVisibility(false);

        return Arrays.asList(p1);
    }

    private List<Proposer> createFooterLessProposer() {
        Proposer p1 = new Proposer();
        p1.id = "PID_PROP_2";
        p1.setFooterVisibility(false);
        p1.title = "Ez egy header egyedül";

        return Arrays.asList(p1);
    }

    private List<Proposer> createGeneralProposer() {
        Proposer p1 = new Proposer();
        p1.id = "PID_PROP_3";
        p1.title = "Header és Footer is együtt";

        return Arrays.asList(p1);
    }

    private List<ProposerItem> createGenerailItems0() {
        List<ProposerItem> items = new ArrayList<>();

        ProposerItem p1 = new ProposerItem();
        p1.id = "PID_ITEM_01";
        p1.title = "Banner 1";
        items.add(p1);

        ProposerItem p2 = new ProposerItem();
        p2.id = "PID_ITEM_02";
        p2.title = "Banner 2";
        items.add(p2);

        return items;
    }

    private List<ProposerItem> createGenerailItems1() {
        List<ProposerItem> items = new ArrayList<>();

        ProposerItem p1 = new ProposerItem();
        p1.id = "PID_ITEM_11";
        p1.title = "Egyes Elem 1";
        items.add(p1);

        ProposerItem p2 = new ProposerItem();
        p2.id = "PID_ITEM_12";
        p2.title = "Egyes Elem 2";
        items.add(p2);

        ProposerItem p3 = new ProposerItem();
        p3.id = "PID_ITEM_13";
        p3.title = "Egyes Elem 3";
        items.add(p3);

        ProposerItem p4 = new ProposerItem();
        p4.id = "PID_ITEM_14";
        p4.title = "Egyes Elem 4";
        items.add(p4);

        return items;
    }

    private List<ProposerItem> createGenerailItems2() {
        List<ProposerItem> items = new ArrayList<>();

        ProposerItem p1 = new ProposerItem();
        p1.id = "PID_ITEM_21";
        p1.title = "Kettes Elem 1";
        items.add(p1);

        ProposerItem p2 = new ProposerItem();
        p2.id = "PID_ITEM_22";
        p2.title = "Kettes Elem 2";
        p2.layoutType = ProposerItem.LAYOUT_TYPE.GREEN_LAYOUT;
        items.add(p2);

        ProposerItem p3 = new ProposerItem();
        p3.id = "PID_ITEM_23";
        p3.title = "Kettes Elem 3";
        items.add(p3);

        ProposerItem p4 = new ProposerItem();
        p4.id = "PID_ITEM_24";
        p4.title = "Kettes Elem 4";
        p4.layoutType = ProposerItem.LAYOUT_TYPE.GREEN_LAYOUT;
        items.add(p4);

        return items;
    }

    private List<ProposerItem> createGenerailItems3() {
        List<ProposerItem> items = new ArrayList<>();

        ProposerItem p1 = new ProposerItem();
        p1.id = "PID_ITEM_31";
        p1.title = "Harmas Elem 1";
        items.add(p1);

        ProposerItem p2 = new ProposerItem();
        p2.id = "PID_ITEM_32";
        p2.title = "Harmas Elem 2";
        p2.layoutType = ProposerItem.LAYOUT_TYPE.GREEN_LAYOUT;
        items.add(p2);

        ProposerItem p3 = new ProposerItem();
        p3.id = "PID_ITEM_33";
        p3.title = "Harmas Elem 3";
        items.add(p3);

        ProposerItem p4 = new ProposerItem();
        p4.id = "PID_ITEM_34";
        p4.title = "Harmas Elem 4";
        items.add(p4);

        return items;
    }

}
