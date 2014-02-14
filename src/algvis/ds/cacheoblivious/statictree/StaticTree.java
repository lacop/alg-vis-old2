package algvis.ds.cacheoblivious.statictree;

import algvis.core.DataStructure;
import algvis.core.visual.ZDepth;
import algvis.ds.cacheoblivious.Cache;
import algvis.ds.cacheoblivious.SingleBlockCache;
import algvis.ds.dictionaries.bst.BST;
import algvis.ds.dictionaries.bst.BSTInsert;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;

public class StaticTree extends BST {

    public static String adtName = "cacheoblivious";
    public static String dsName = "statictree";

    // TODO abstract to CacheObliviousDS, selectable from panel
    public Cache cache = new SingleBlockCache(4, false);

    protected StaticTree(VisPanel panel) {
        super(panel);
    }

    @Override
    public void insert(int x) {
        start(new StaticTreeInsert(this, new StaticTreeNode(this, x, ZDepth.ACTIONNODE)));
    }

    public void setOrder() {
        start(new StaticTreeSetOrder(this));
    }

    // TODO update while running
    @Override
    public String stats() {
        StringBuilder sb = new StringBuilder();
        sb.append("BS: " + cache.getBlockSize() + "  ");
        sb.append("AC: " + cache.getAccessCount() + "  ");
        sb.append("RC: " + cache.getReadCount() + " ; ");

        return sb.toString() + super.stats();
    }
}