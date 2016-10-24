
public class cacheTest {
	
	/* The following program tests Cache.java, an application that simulates a computer cache (using Least Recently
	 * Used policy) by feeding in a number of addresses, which will either be a hit or miss.
	 * Cache size can be altered by the constraints L K N (bytes per cache line, number of directories and number of sets)
	 * 
	 */
	
	public static void main(String args[]) {
		Cache cache = new Cache (16, 4, 2);
		String addresses[] = {"0000", "0004", "000c", "2200", "00d0", "00e0", "1130", "0028", "113c", "2204", "0010", "0020", "0004", "0040",
				"2208", "0008", 
				"00a0", "0004", "1104", "0028", "000c", "0084", "000c", "3390", "00b0", "1100", "0028", "0064", "0070", "00d0", "0008", "3394"};
		cache.getSetAndTag(addresses);
		//System.out.println(cache.HitOrMiss(add));
	}

}
