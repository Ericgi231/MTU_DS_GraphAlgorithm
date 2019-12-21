package cs2321;

import java.util.Comparator;

import net.datastructures.*;
/**
 * A Adaptable PriorityQueue based on an heap. 
 * 
 * Course: CS2321 Section ALL
 * Assignment: #3
 * @author
 */

public class HeapPQ<K,V> implements AdaptablePriorityQueue<K,V> {
	//nested classes
	//
	//implementation of entry, contains a key and value
	public class AdaptablePQEntry<K,V> implements Entry<K,V>{
		//vars
		//
		private K k;
		private V v;
		private int index;
		
		//constructors
		//
		public AdaptablePQEntry(K key, V value, int j) {
			k = key;
			v = value;
			index = j;
		}
		
		//functions
		@Override
		public K getKey() {
			return k;
		}

		@Override
		public V getValue() {
			return v;
		}
		
		public int getIndex() {
			return index;
		}
		
		public void setIndex(int j) {
			index = j;
		}
		
		protected void setKey(K key) {
			k=key;
		}
		protected void setValue(V value) {
			v=value;
		}
	}
	
	//vars
	//
	private Comparator<K> comp;
	protected ArrayList<Entry<K,V>> heap = new ArrayList<>();
	
	//constructors
	//
	//instantiate with default comparator
	public HeapPQ() {
		comp = new DefaultComparator<K>();
	}
	
	//instantiate with given comparator
	//c: comparator to use
	public HeapPQ(Comparator<K> c) {
		comp = c;
	}
	
	//functions
	//
	//restore order to heap by traveling up the heap from parent to parent
	//swapping entries when parent is larger than child
	//j: staring index
	
	public void upheap(int j){
		/* worst case this algorithm starts at the bottom of the tree and works it's way to the top only going through parents
		 * the height of a tree is equal to log of n, since this algorithm can only climb the height of a tree
		 * this algorithm is O(log n)
		 */
		//while not at top of heap
		while(j > 0) {
			//if parent is smaller than selected entry do nothing
			int p = parent(j);
			if(compare(heap.get(j), heap.get(p)) >= 0) {
				break;
			}
			
			//swap parent and child
			swap(j,p);
			j=p;
		}
	}
	
	//restore order to heap by traveling down the heap from child to child
	//swapping entries when child is smaller than parent
	//j: starting index
	
	public void downheap(int j){
		/* worst case this algorithm starts at the top of the tree and works it's way down to the bottom only going through children
		 * the height of a tree is equal to log of n, since this algorithm can only go the height of a tree
		 * this algorithm is O(log n)
		 */
		//while not at bottom of heap
		while(hasLeftChild(j)) {
			//take left child of current entry
			int leftChildIndex = leftChild(j);
			int smallChildIndex = leftChildIndex;
			
			//replace selected child with right child if right child is smaller
			if(hasRightChild(j)) {
				int rightChildIndex = rightChild(j);
				if(compare(heap.get(leftChildIndex), heap.get(rightChildIndex)) > 0) {
					smallChildIndex = rightChildIndex;
				}
			}
			
			//if selected child is larger than parent, do nothing
			if(compare(heap.get(smallChildIndex), heap.get(j)) >= 0) {
				break;
			}
			
			//swap parent and child
			swap(j, smallChildIndex);
			j = smallChildIndex;
		}
	}

	//return size of heap
	@Override
	
	public int size() {
		return heap.size();
	}

	
	//return true if heap is empty
	@Override
	
	public boolean isEmpty() {
		return size() == 0;
	}

	//insert new entry at end of heap, then restore order to the heap
	//key: key of new entry
	//value: value of new entry
	@Override
	
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
		/* This function calls upheap() which has an efficiency of O(log n)
		 * the rest of the function is all o(1)
		 * therefore this function is O(log n) 
		 */
		checkKey(key);
		Entry<K,V> newest = new AdaptablePQEntry<>(key, value, heap.size());
		heap.addLast(newest);
		upheap(heap.size() - 1);
		return newest;
	}

	//return smallest entry, do not remove
	@Override
	
	public Entry<K, V> min() {
		if(heap.isEmpty()) {
			return null;
		}
		return heap.get(0);
	}

	//return smallest entry and remove it
	//then restore order to the heap
	@Override
	
	public Entry<K, V> removeMin() {
		/* Unlike min, this function has to remove the value at the top of the tree
		 * in order to restore the heap it must call downheap() which is O(log n)
		 * the rest of the function is O(1)
		 * therefore this function is O(log n) 
		 */
		if(heap.isEmpty()) {
			return null;
		}
		
		Entry<K,V> answer = heap.get(0);
		swap(0, heap.size() - 1);
		heap.remove(heap.size() - 1);
		
		downheap(0);
		
		return answer;
	}

	//return and remove given entry from the heap
	//then restore order to the heap
	@Override
	
	public void remove(Entry<K, V> entry) throws IllegalArgumentException {
		/* Similar to removeMin this function must restore the order of the heap after removing a value
		 * The difference is that depending on where the removed values was, it will need to call either downheap or upheap, decided via the bubble() function
		 * bubble() is )(log n)
		 * therefore this function is O(log n) 
		 */
		AdaptablePQEntry<K,V> locator = validate(entry);
		int j = locator.getIndex();
		if(j == heap.size() - 1) {
			heap.remove(heap.size() - 1);
		} else {
			swap(j, heap.size() - 1);
			heap.remove(heap.size() - 1);
			bubble(j);
		}
		
	}

	//find the given entry then change it's key to the given key
	//finally, restore order to heap
	//entry: entry to be changed
	//key: new key
	@Override
	
	public void replaceKey(Entry<K, V> entry, K key) throws IllegalArgumentException {
		/* Upon replacing the key of an element the order must be restored since the new key might not fit in the same spot as the old one
		 * This function must call bubble to determine how to restore order
		 * bubble is O(log n)
		 * therefore this function is 0(log n)
		 */
		AdaptablePQEntry<K,V> locator = validate(entry);
		checkKey(key);
		locator.setKey(key);
		bubble(locator.getIndex());
	}

	//find the given entry then change it's value to the given value
	//entry: entry to be changed
	//value: new value
	@Override
	
	public void replaceValue(Entry<K, V> entry, V value) throws IllegalArgumentException {
		/* Unlike replacekey() this function only changes a value, which is not important when it comes to the order of the tree
		 * This means all that changes is a single know value, meaning this function is O(1)
		 */
		AdaptablePQEntry<K,V> locator = validate(entry);
		locator.setValue(value);
	}
	
	//utility
	//
	 
	//smaller math and checking utilities
	protected int parent(int j) {
		return (j-1)/2;
	}
	
	protected int leftChild(int j) {
		return 2*j+1;
	}
	
	protected int rightChild(int j) {
		return 2*j+2;
	}
	
	protected boolean hasLeftChild(int j) {
		return leftChild(j) < heap.size();
	}
	
	protected boolean hasRightChild(int j) {
		return rightChild(j) < heap.size();
	}
	
	//restore heap by moving entry either down or up heap
	//j: index of entry
	
	protected void bubble(int j) {
		/* This function either calls upheap or downheap, both of which are O(log n)
		 * therefore this function is O(log n)
		 */
		if(j > 0 && compare(heap.get(j), heap.get(parent(j))) < 0) {
			upheap(j);
		} else {
			downheap(j);
		}
	}
	
	//validates that an entry is adaptable
	//entry: entry to check
	
	protected AdaptablePQEntry<K,V> validate(Entry<K,V> entry) throws IllegalArgumentException{
		if(!(entry instanceof AdaptablePQEntry)) {
			throw new IllegalArgumentException("Invalid entry");
		}
		AdaptablePQEntry<K,V> locator = (AdaptablePQEntry<K, V>) entry;
		int j = locator.getIndex();
		if(j >= heap.size() || heap.get(j) != locator) {
			throw new IllegalArgumentException("Invalid entry");
		}
		return locator;
	}
	
	//swap values at two indexes
	//i: first index
	//j: second index
	
	protected void swap(int i, int j) {
		Entry<K,V> t = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, t);
		((AdaptablePQEntry<K,V>) heap.get(i)).setIndex(i);
		((AdaptablePQEntry<K,V>) heap.get(j)).setIndex(j);
	}
	
	//compare the keys of two entries using comparator
	//a: first entry in comparison
	//b: second entry in comparison
	
	protected int compare(Entry<K,V> a, Entry<K,V> b) {
		return comp.compare(a.getKey(), b.getKey());
	}
	
	//return true if key is valid
	//key: key to check validity of
	
	protected boolean checkKey(K key) throws IllegalArgumentException{
		try {
			return (comp.compare(key, key) == 0);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Incompatible key");
		}
	}
}
