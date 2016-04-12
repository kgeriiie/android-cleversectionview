package hu.kole.clrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        proposers.addAll(createHeaderLessProposer());
        proposers.addAll(createFooterLessProposer());
        proposers.addAll(createGeneralProposer());

        adapter = new TestAdapter(this,proposers);

        listRv.setLayoutManager(llm);
        listRv.setAdapter(adapter);
    }

    private List<Proposer> createHeaderLessProposer() {
        Proposer p1 = new Proposer();
        p1.id = "PID_PROP_1";
        p1.proposerItems = createGenerailItems();
        p1.setHeaderVisibility(false);

        return Arrays.asList(p1);
    }

    private List<Proposer> createFooterLessProposer() {
        Proposer p1 = new Proposer();
        p1.id = "PID_PROP_2";
        p1.proposerItems = createGenerailItems();
        p1.setFooterVisibility(false);

        return Arrays.asList(p1);
    }

    private List<Proposer> createGeneralProposer() {
        Proposer p1 = new Proposer();
        p1.id = "PID_PROP_3";
        p1.proposerItems = createGenerailItems();

        return Arrays.asList(p1);
    }

    private List<ProposerItem> createGenerailItems() {
        List<ProposerItem> items = new ArrayList<>();

        ProposerItem p1 = new ProposerItem();
        p1.id = "PID_ITEM_01";
        p1.title = "Elem 1";
        items.add(p1);

        ProposerItem p2 = new ProposerItem();
        p2.id = "PID_ITEM_02";
        p2.title = "Elem 2";
        items.add(p2);

        ProposerItem p3 = new ProposerItem();
        p3.id = "PID_ITEM_03";
        p3.title = "Elem 3";
        items.add(p3);

        ProposerItem p4 = new ProposerItem();
        p4.id = "PID_ITEM_04";
        p4.title = "Elem 4";
        items.add(p4);

        return items;
    }

}
