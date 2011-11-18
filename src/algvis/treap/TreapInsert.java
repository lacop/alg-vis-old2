package algvis.treap;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;

public class TreapInsert extends Algorithm {
	Treap T;
	BSTNode v;
	int K;

	public TreapInsert(Treap T, int x) {
		super(T);
		this.T = T;
		v = T.v = new TreapNode(T, K = x);
		setHeader("insertion");
	}

	@Override
	public void run() {
		if (T.root == null) {
			T.root = v;
			v.goToRoot();
			setText("newroot");
			mysuspend();
		} else {
			BSTNode w = T.root;
			v.goAboveRoot();
			setText("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					setText("alreadythere");
					v.goDown();
					return;
				} else if (w.key < K) {
					setText("bstinsertright", K, w.key);
					if (w.right != null) {
						w = w.right;
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					setText("bstinsertleft", K, w.key);
					if (w.left != null) {
						w = w.left;
					} else {
						w.linkLeft(v);
						break;
					}
				}
				v.goAbove(w);
				mysuspend();
			}
			T.reposition();
			mysuspend();
			// bubleme nahor
			setText("treapbubbleup");
			while (!v.isRoot() && ((TreapNode) v.parent).p < ((TreapNode) v).p) {
				T.rotate(v);
				mysuspend();
			}
		}
		setText("done");
		T.v = null;
	}
}
