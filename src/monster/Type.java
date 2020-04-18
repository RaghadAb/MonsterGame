package monster;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Type {
	FIRE("FIRE", "WATER"),
	WATER("WATER", "GRASS"),
	ELECTRIC("ELECTRIC", "GRASS"),
	GRASS("FIRE", "GRASS");
	
	private Set<String> weaknesses;
	
	Type(String... weakStr) {
		weaknesses = Arrays.asList(weakStr).stream().collect(Collectors.toSet());
	}
	
	public boolean isWeakAgainst(Type type) {
		return weaknesses.contains(type.name());
	} //.contain() in sets returns a boolean value
}

//enum showing Mobile prices
//enum Mobile {
//   Samsung(400), Nokia(250),Motorola(325);
//
//   int price;
//   Mobile(int p) {
//      price = p;
//   }
//   int showPrice() {
//      return price;
//   }
//}

/* enums can be within a class or as a class , and it basically looks the same
without public
 */

/*
null values are not allowed in maps, copy elements into a map
Map mapA = new HashMap();
mapA.put("key1", "value1");
mapA.put("key2", "value2");

Map mapB = new HashMap();
mapB.putAll(mapA);
 */

