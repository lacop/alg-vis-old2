package algvis.ds.cacheoblivious.orderedfile;

import algvis.ds.cacheoblivious.statictree.StaticTree;
import algvis.ds.dictionaries.bst.BST;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderedFile extends BST {

    public static String adtName = "cacheoblivious";
    public static String dsName = "orderedfile";

    OrderedFileNode root = null;

    // Linking with vEB tree
    StaticTree vEBtree = null;

    @Override
    public void draw(View V) {
        if (root != null) {
            root.drawTree(V);

            // Draw threshold values
            // Get X bounds from vEB tree if available as it's wider
            // TODO do in rebox?
            if (vEBtree != null && vEBtree.getRoot() != null) {
                root.drawThresholds(V, vEBtree.getRoot());
            } else {
                root.drawThresholds(V, root);
            }
        }
    }

    // TODO virtualize in BST or higher
    @Override
    public String getName() {
        return dsName;
    }

    @Override
    public BSTNode getRoot() {
        return root;
    }

    public OrderedFile(VisPanel panel) {
        this(panel, null);
    }

    public OrderedFile(VisPanel panel, StaticTree vEBtree) {
        super(panel);
        this.vEBtree = vEBtree;

        //initialize(new ArrayList(Arrays.asList(new Integer[]{1, 2, 3, 4})));
    }

    // TODO private + getters
    public int leafSize;
    public ArrayList<OrderedFileNode> leaves;

    public void initialize(List<Integer> elements) {
        // TODO check this math, optimize?

        // Find parameters - leaf size, leaf count, ...

        // At least two elements in leaf
        int leafSize = 2;
        // Compute base-2 log of number of elements (no float rounding errors)
        for(int e = elements.size(); e > 4; leafSize++, e /= 2);

        // Round up leaf size to power of two to work with vEB tree
        int powTwo = 2;
        for(; powTwo < leafSize; powTwo *= 2);
        leafSize = powTwo;

        // TODO optimal starting value?
        final int oneOverDensity = 2; // Density will be at most 1/2 at leafs
        // Leaf count is smallest power of two for which the elements
        // can be stored with the desired density
        int leafCount = 2;
        while (leafCount*leafSize < elements.size()*oneOverDensity) {
            leafCount *= 2;
        }

        // Generate empty leaf nodes
        ArrayList<OrderedFileNode> nodes = new ArrayList<OrderedFileNode>();
        for (int i = 0; i < leafCount; i++) {
            OrderedFileNode leaf = new OrderedFileNode(this, leafSize);
            leaf.setLeafOffset(nodes.size());
            nodes.add(leaf);
        }

        this.leafSize = leafSize;
        leaves = nodes;

        // Merge into tree
        while (nodes.size() > 1) {
            ArrayList<OrderedFileNode> merged = new ArrayList<OrderedFileNode>();
            for (int i = 0; i < nodes.size(); i += 2) {
                OrderedFileNode parent = new OrderedFileNode(this, leafSize);
                parent.linkLeft(nodes.get(i));
                parent.linkRight(nodes.get(i + 1));
                merged.add(parent);
            }
            nodes = merged;
        }

        root = nodes.get(0);

        // Calculate node heights (for thresholds)
        root.calcTree();

        // Insert elements evenly
        root.insertEvenly(elements);

        // Update layout
        reposition();
    }

    public double thresholdSparse(int height) {
        int depth = root.height - height;

        // 1/2 at root, 1/4 at leaves
        return 0.5 - 0.25*((double)depth / (root.height - 1));
    }
    public double thresholdDense(int height) {
        int depth = root.height - height;

        // 3/4 at root, 1 at leaves
        return 0.75 + 0.25*((double)depth / (root.height - 1));
    }

    public void insert(int pos, int value) {
        start(new OrderedFileInsert(this, pos, value));
    }
}
