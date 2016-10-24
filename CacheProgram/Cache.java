import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class Cache {
	
	private String cache[][];
	private int sets[];
	private String tags[];
	private String hitOrMiss[];
	private int L, K, N;
	private Queue<String> lastAccessed[]; //for calculating LRU
	private int hitCount=0;
	private int missCount=0;
	@SuppressWarnings("unchecked")
	public Cache(int L, int K, int N)
	{
		this.L = L;
		this.K = K;
		this.N = N;
		cache = new String[N][K];
		 for(int i=0; i< N; i++)
		 {
			 for(int j=0; j< K; j++)
			 {
				 cache[i][j] = null;
			 }
		 }
		 lastAccessed = new Queue[N]; //0th element= oldest (ie one to evict)
		 for(int i=0; i<N; i++)
		 {
			 lastAccessed[i] = new LinkedList<String>();
		 }
		
	}
	
	public void getSetAndTag(String addresses[])
	{
		sets = new int[addresses.length]; //2 columns one for set one for tag
		tags = new String[addresses.length];
		hitOrMiss = new String[addresses.length];
		String stringBinary = "";
		int num=0;
		int array[];
		for(int i=0; i<addresses.length; i++)
		{
			num = (Integer.parseInt(addresses[i], L));
			stringBinary = String.format("%16s", Integer.toBinaryString(num)).replace(' ', '0');
			
			if(N > 1) 
			{
				array = extractSet(stringBinary); 			
				sets[i] = array[0]; //store set number
				tags[i] = extractTag(stringBinary, array[1]); 
			}
			else //only 1 set
			{
				sets[i] = 0;
				tags[i] = extractTag(stringBinary, 11); 
			}
			checkCache(sets[i], tags[i], i);
			
		}
		System.out.println(print(addresses));
	}
	
	//takes in set number
	public int[] extractSet(String number)
	{
		char array[] = number.toCharArray();
		int indexSetBits = (int) (Math.log((double)N) / Math.log(2)); //3
		int index = 11 - (indexSetBits-1); //9
		String set = "";
		int setNumber =0;
		int limitIndex = index -1;
		for(int i=0; i<indexSetBits; i++)
		{
			set = set + "" + array[index];
			index++;
		}
		if(N > 1)
		{
			setNumber = Integer.parseInt(set, 2);
		}
		else
		{
			set = "" + array[index];
			setNumber = Integer.parseInt(set);
		}
		int returnArray[] = {setNumber, limitIndex};
		return returnArray;
		
	}
	
	public String extractTag(String number, int limitIndex)
	{
		String tagString = "";
		char array[] = number.toCharArray();
		for(int i=0; i<= limitIndex; i++)
		{
			tagString = tagString + array[i];
			
		}
		return tagString;
	}
	
	public void checkCache(int setNumber, String tag, int addressIndex)
	{
		//go through each directory to see where to put tag
		
		String toEvict = storeAccesses(setNumber, tag);
		for(int i=0; i<K; i++)
		{
			if(cache[setNumber][i] != null && cache[setNumber][i].equals(tag)) // if is in there
			{
				hitOrMiss[addressIndex] = "Hit";
				hitCount++;
				break;
			}
			else if(cache[setNumber][i] == null) //not in there and empty space //
			{
				cache[setNumber][i] = tag; 	//put into cache
				hitOrMiss[addressIndex] = "Miss";
				missCount++;
				break;
			}
			else //not in any occupied directory
			{
				if(i == (K-1)) //on last directory, calc LRU only when more than 1 directory
				{
					hitOrMiss[addressIndex] = "Miss";
					missCount++;
					//evict lru - peek
					for(int j=0; j<K; j++)
					{
						if(cache[setNumber][j].equals(toEvict))
						{
							cache[setNumber][j] = tag;
						}
					}
					
				}
				
			} //if there's only 1 directory then put it in there. else if more directories keep searching. 
		}
	}
	
	public String storeAccesses(int set, String tag)
	{
		String lru = "";
		if(lastAccessed[set].size() == K) //if all possible directories in cache are full
		{
			if((lastAccessed[set].contains(tag)))
			{
				lastAccessed[set].remove(tag);
				lastAccessed[set].add(tag);
				return lastAccessed[set].peek();
			}
			lru = lastAccessed[set].remove(); //remove oldest
			lastAccessed[set].add(tag); //add in the new incoming one
		}
		else
		{
			if((lastAccessed[set].contains(tag)))
			{
				lastAccessed[set].remove(tag);
				lastAccessed[set].add(tag);
				return lru = lastAccessed[set].peek();
			}
			else
			{
				lastAccessed[set].add(tag);
			}
			return lru = lastAccessed[set].peek();
		}

		return lru;
		
	}
	
	public String print(String addresses[])
	{
		String returnStr = "For cache of " + L + " bytes per line, " + K + " directories and " + N + " sets:";
		for(int i=0; i<addresses.length; i++)
		{
			returnStr = returnStr + "\n" + "Address: " + addresses[i] + " Set: " + sets[i] + " Tag: " + tags[i] + " Hit/Miss: " + hitOrMiss[i];
		}
		returnStr = returnStr + "\n Hit Count: " + hitCount  + " Miss Count: " + missCount;
		return returnStr;
	}
	
}
