package telran.util;

import java.util.Iterator;
import java.util.LinkedList;

public class HashSet<T> extends AbstractSet<T> {
	private static final int DEFAULT_ARRAY_LENGTH = 16;
	private static final float FACTOR = 0.75f;
	LinkedList<T> hashTable[];

	@SuppressWarnings("unchecked")
	public HashSet(int arrayLength) {
		hashTable = new LinkedList[arrayLength];
	}

	public HashSet() {
		this(DEFAULT_ARRAY_LENGTH);
	}

	@Override
	public boolean add(T obj) {
		boolean res = false;
		if (!contains(obj)) {
			res = true;
			size++;
			if (size > FACTOR * hashTable.length) {
				recreateHashTable();
			}
			int index = getHahTableIndex(obj);
			if (hashTable[index] == null) {
				hashTable[index] = new LinkedList<>();
			}
			hashTable[index].add(obj);
		}

		return res;
	}

	private int getHahTableIndex(T obj) {
		int hashCode = obj.hashCode();
		int res = Math.abs(hashCode) % hashTable.length;
		return res;
	}

	private void recreateHashTable() {
		HashSet<T> tmpSet = new HashSet<>(hashTable.length * 2);
		for (LinkedList<T> backet: hashTable) {
			if (backet != null) {
				for (T obj: backet) {
					tmpSet.add(obj);
				}
			}
		}
		hashTable = tmpSet.hashTable;
		tmpSet = null;

	}

	@Override
	public T remove(T pattern) {
		int index = getHahTableIndex(pattern);
		T res = null;
		if (hashTable[index] != null) {
			int indObj = hashTable[index].indexOf(pattern);
			if (indObj >= 0) {
				res = hashTable[index].remove(indObj);
				size--;
			}
		}
		
		return res;
	}

	@Override
	public boolean contains(T pattern) {
		boolean res = false;
		int htIndex = getHahTableIndex(pattern);
		if (hashTable[htIndex] != null) {
			res = hashTable[htIndex].contains(pattern);
		}
		return res;
	}

@Override
	public Iterator<T> iterator() {
		//TODO 
		return new HashSetIterator<T>();
	}
	private class HashSetIterator<T> implements Iterator<T> {
        //TODO iterator required fields
		    Iterator<T> backetIterators[];
		    int curentBacketIndex = -1;
		    int previosBacketIndex = -1;
		    
		    public HashSetIterator( ) {
				backetIterators = new Iterator [hashTable.length];
				for (int i=0; i<hashTable.length; i++) {
					if (hashTable[i]!=null) {
					backetIterators[i]= (Iterator<T>) hashTable[i].iterator();
					    if (curentBacketIndex <0) {
						    curentBacketIndex = i;
					    }
				    }
			    }
            }
@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
	        return curentBacketIndex<backetIterators.length && 
			backetIterators[curentBacketIndex].hasNext();		}

@Override
	    public T next() {
			// TODO Auto-generated method stub
			T res = backetIterators [curentBacketIndex].next();
			previosBacketIndex = curentBacketIndex ;
			curentBacketIndex = getNextCurentIndex(curentBacketIndex);
			return res;		
		}
		@Override
		public void remove() {
			//TODO
			backetIterators[previosBacketIndex].remove();
			size--;
		}
	private int getNextCurentIndex(int curentIndex) {
		if (!backetIterators [curentIndex].hasNext()) {
			while (++ curentIndex <backetIterators.length &&
			(BacketNotCreated(curentIndex)||BackedPassed(curentIndex))) ;	
			}
			return curentIndex;
		}
	private boolean BackedPassed(int index) {
		return !backetIterators[index].hasNext();
	}
	private boolean BacketNotCreated(int index) {
		return backetIterators[index] == null;
	}
	
}
}