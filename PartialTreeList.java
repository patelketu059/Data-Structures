package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Arc;
import structures.Graph;
import structures.PartialTree;
import structures.Vertex;

/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
	
		/* COMPLETE THIS METHOD */
		PartialTreeList finalResult = new PartialTreeList();
		
		for(int c = 0; c < graph.vertices.length; c++)
		{
			
			PartialTree tree = new PartialTree(graph.vertices[c]);
			Vertex.Neighbor neigh = graph.vertices[c].neighbors;
			
			while(neigh != null)
			{
				Arc arc = new Arc(graph.vertices[c], neigh.vertex, neigh.weight);
				tree.getArcs().insert(arc);
				neigh = neigh.next;
			}
			
			finalResult.append(tree);
			
		}
		return finalResult;
	}
	
	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<Arc> execute(PartialTreeList ptlist) {
		
		/* COMPLETE THIS METHOD */

		ArrayList<Arc> finalResult = new ArrayList<Arc>();
		
		while(ptlist.size() != 1)
		{
			
			PartialTree firstTree = ptlist.remove();
			Arc tree = firstTree.getArcs().deleteMin();
			
			if(tree.getv1().getRoot().parent == tree.getv2().getRoot().parent)
			{
				boolean tf = true;
				while(tf)
				{
					if(tree.getv1().getRoot().parent == tree.getv2().getRoot().parent)
					{
						tree = firstTree.getArcs().deleteMin();
					}
					else
					{
						tf = false;
					}
				}
				
			}
			
			
			finalResult.add(tree);
			PartialTree secondTree = ptlist.removeTreeContaining(tree.getv2().getRoot().parent);
			firstTree.merge(secondTree);
			ptlist.append(firstTree);
		}
		
		return finalResult;
	}
	
    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    			
    	if (rear == null) {
    		throw new NoSuchElementException("list is empty");
    	}
    	PartialTree ret = rear.next.tree;
    	if (rear.next == rear) {
    		rear = null;
    	} else {
    		rear.next = rear.next.next;
    	}
    	size--;
    	return ret;
    		
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    throws NoSuchElementException {
    		/* COMPLETE THIS METHOD */
    		Node curr = rear;
    		Node nextOne = rear.next;
    		while(nextOne != rear)
    		{
    			if(nextOne.tree.getRoot() == vertex)
    			{
    				PartialTree out = nextOne.tree;
    				curr.next = nextOne.next;
    				nextOne = nextOne.next;
    				size--;
    				return out;
    			}
    			curr = curr.next;
    			nextOne = nextOne.next;
    		}
    		
    		if(nextOne.tree.getRoot() == vertex)
    		{
    			
    			PartialTree out = nextOne.tree;
    			
    			curr.next = nextOne.next;
    			rear = curr;
    			
    			nextOne = nextOne.next;
    			size--;
    			
    			
    			return out;
    			
    		}
    		
    		
    		
    		throw new NoSuchElementException();
     }
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}


