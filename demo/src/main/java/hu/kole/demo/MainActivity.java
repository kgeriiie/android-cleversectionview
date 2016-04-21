package hu.kole.demo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hu.kole.cleversectionviewadapter.CleverSectionSpanSizeLookup;
import hu.kole.cleversectionviewadapter.listeners.EndlessScrollListener;
import hu.kole.cleversectionviewadapter.BaseCleverSectionAdapter;
import hu.kole.demo.models.Proposer;
import hu.kole.demo.models.ProposerItem;
import hu.kole.demo.logic.ProposerManager;
import hu.kole.demo.adapter.ProposerAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();

    GridLayoutManager mLayoutManager;
    RecyclerView listRv;
    ProposerAdapter adapter;
    List<Proposer> proposers = new ArrayList<>();

    //For simulate data downloading delay.
    private int[] delay = {1000,1500,2000,3000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listRv = (RecyclerView) findViewById(R.id.testRl);
        mLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);

        proposers.addAll(ProposerManager.getInstance().generateProposers(3));

        adapter = new ProposerAdapter(this,proposers);
        adapter.setDragAndDropEnabled(false);

        adapter.setOnEndlessScrollListener(new EndlessScrollListener(mLayoutManager,adapter,true) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMore();
            }
        });

        mLayoutManager.setSpanSizeLookup(new CleverSectionSpanSizeLookup(adapter,mLayoutManager));
        listRv.setItemAnimator(new DefaultItemAnimator());
        listRv.setLayoutManager(mLayoutManager);
        listRv.setAdapter(adapter);

        fillProposers(proposers);

        adapter.setOnSectionItemClickListener(new BaseCleverSectionAdapter.OnItemClickListener<ProposerItem>() {
            @Override
            public void onItemClick(View view, ProposerItem item) {
                Log.d(TAG,"Clicked on item named: " + item.title);
            }

        });
    }

    private void fillProposers(final List<Proposer> proposers) {
        Random random = new Random();

        for (final Proposer proposer : proposers) {

//            proposer.setSectionItems(ProposerManager.getInstance().generateProposerItemsMixed(6,proposer.getId()));
//            adapter.updateDataSet(MainActivity.this.proposers);

            int index = random.nextInt(4);
            final int itemCount = random.nextInt(6) + 1;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (itemCount < 4) {
                        proposer.setSectionItems(ProposerManager.getInstance().generateProposerItemsTypeOne(itemCount,proposer.getId()));
                    } else {
                        proposer.setSectionItems(ProposerManager.getInstance().generateProposerItemsTypeTwo(itemCount,proposer.getId()));
                    }

                    adapter.updateDataSet(MainActivity.this.proposers);
                }
            },delay[index]);
        }
    }

    private void loadMore() {

        if (proposers.size() < 30) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<Proposer> prop = ProposerManager.getInstance().generateProposers(5);
                    proposers.addAll(prop);
                    adapter.updateDataSet(proposers);

                    fillProposers(prop);
                }
            },1500);

            return;
        }

        adapter.updateDataSet(proposers);
    }
}
