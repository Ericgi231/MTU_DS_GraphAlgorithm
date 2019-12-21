package cs2321;

import java.util.Comparator;

public class QuickSort<E extends Comparable<E>> implements Sorter<E> {

	public void sort(E[] array) {
		//this function is used to call sortWithIndexes, so it has the same time complexity
		
		//assign p and q to the start and end of the array, then call sortWithIndexes to do the sorting
		int p = 0;
		int q = array.length-1;
		sortWithIndexes(array, p, q);
	}
	
	public void sortWithIndexes(E[] a, int p, int q) {
		//this function recursively calls itself at most n times though is likely to call itself log n times
		//since this function splits such that the last element is in between all elements greater or less than
		//in the worst case partition will split the array into an array of length 0 and an array of length n-1
		//if this worst case it hit every loop, the total loops will be n
		//leaving you with a loop of n calling a function with O(n)
		//giving you O(n^2)
		//Though this situation is very rare and you are more likely to get closer to n log n
		
		//if the start is before the end
		if(p < q) {
			//partition the array
			//putting the q value in the middle and arranging the array so that all values before q are smaller and all values after are larger
			//r is the middle index
			int r = partition(a, p, q);
			
			//take each half of the partitioned array and call this function
			sortWithIndexes(a, p, r-1);
			sortWithIndexes(a, r+1, q);
		}
	}
	
	public int partition(E[] a, int p, int q) {
		//loops through an array from left to right and right to left, at worst, it stops when these values cross
		//meaning at most it can loop through all n values in the array, O(n)
		
		//set i to the start of the array and j to the end
		Comparator<E> c = new DefaultComparator<E>();
		E pv = a[q];
		int i = p;
		int j = q-1;
		
		//loop as long as the start and end have not crossed
		while(i <= j) {
			//if the value at index i is less than the pivot value, continue to move index i to the right
			while(i <= j && c.compare(a[i], pv) <= 0) {
				i++;
			}
			
			//if the value at index j is greater than the pivot value, continue to move index j to the left
			while(i <= j && c.compare(pv, a[j]) <= 0) {
				j--;
			}
			
			//if i and j have not crossed
			if(i < j) {
				//swap i and j then move both indexes by one
				E temp = a[i];
				a[i] = a[j];
				a[j] = temp;
				i++;
				j--;
			}
		}
		
		//lastly, swap the pivot value with the middle value, leaving all values left of the pivot smaller and all values to the right larger
		E temp = a[i];
		a[i] = a[q];
		a[q] = temp;
		return i;
	}
}
