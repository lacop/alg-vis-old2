package algvis.ds.cacheoblivious.cobtree;

import algvis.ds.cacheoblivious.orderedfile.OrderedFile;
import algvis.ds.cacheoblivious.statictree.StaticTree;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

public class COBTree extends StaticTree {

    public static String adtName = "cacheoblivious";
    // TODO rename
    public static String dsName = "btree";

    private StaticTree vEBtree;
    private OrderedFile orderedFile;

    protected COBTree(VisPanel panel) {
        super(panel);

        vEBtree = new StaticTree(panel);
        orderedFile = new OrderedFile(panel, vEBtree);
        //orderedFile = new OrderedFile(panel);
    }

    @Override
    public void insert(int x) {
        vEBtree.insert(x);
    }

    @Override
    public void draw(View V) {
        super.draw(V);

        // OF draws cross-link edges, make it first so vEB leafs can overlap them
        orderedFile.draw(V);
        vEBtree.draw(V);
    }

    public void init() {
        vEBtree.fullInsert(15, 0);
    }

    @Override
    public void move() {
        super.move();

        vEBtree.move();
        orderedFile.move();
    }

    @Override
    public String stats() {
        // Needed to run calcTree and update node heights
        // which are used for thresholds
        orderedFile.stats();

        return "";
    }

    @Override
    public void reposition() {
        super.reposition();
        vEBtree.reposition();
        orderedFile.reposition();
    }
}
