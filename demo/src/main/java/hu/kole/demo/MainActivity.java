package hu.kole.demo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

    boolean isDragEnabled = true;

    //For simulate data downloading delay.
    private int[] delay = {1000,1010,1020,1040};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDragEnabled = !isDragEnabled;

                Toast.makeText(getApplicationContext(), "Drag engedélyezve: " + isDragEnabled, Toast.LENGTH_SHORT).show();

                generateProposers();

                fillProposers(proposers);

                adapter.updateDataSet(proposers);
            }
        });

        listRv = (RecyclerView) findViewById(R.id.testRl);
        mLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);

        generateProposers();

        adapter = new ProposerAdapter(this,proposers);
        adapter.setDragAndDropEnabled(true);
        adapter.setDelayBetweenUpdates(500);

//        adapter.setOnEndlessScrollListener(new EndlessScrollListener(mLayoutManager,adapter,true) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                loadMore();
//            }
//        });

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

    private void generateProposers() {
        proposers = new ArrayList<>();

        proposers.add(ProposerManager.getInstance().generateProposerBanner());

        List<Proposer> pop = ProposerManager.getInstance().generateProposers(3);

        pop.get(0).isDraggable = isDragEnabled;

        proposers.addAll(pop);
    }

    private int in,i;
    private void fillProposers(final List<Proposer> proposers) {
        Random random = new Random();

        in = 0;
        i  = 0;

        for (final Proposer proposer : proposers) {

//            proposer.setSectionItems(ProposerManager.getInstance().generateProposerItemsMixed(6,proposer.getId()));
//            adapter.updateDataSet(MainActivity.this.proposers);

            final int index = random.nextInt(4);
            final int itemCount = random.nextInt(6) + 1;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (proposer.title.contains("BANN") && in < 1) {
                        proposer.setSectionItems(ProposerManager.getInstance().generateBanner(proposer.getId()));

                        in ++;
                    }  else {
                        proposer.setSectionItems(ProposerManager.getInstance().generateProposerItemsTypeTwo(itemCount,proposer.getId()));
                    }

                    adapter.updateDataSet(MainActivity.this.proposers);
                }
            },delay[i]);

            i ++;
        }
    }

    private void fill() {

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
