package per.parks.greg.guid;

import java.util.UUID;
import java.math.BigInteger;


class JsonFmtUUID {
  public long msbs;
  public long lsbs;
}

public class MyUUID {

    protected UUID uuid;
    protected BigInteger biTotal;

    protected MyUUID(UUID u) {
    	this.uuid = u;
    	// calculate the BigInt value by concatenating the hex strings
    	//     of the two long integer components ...
    	this.biTotal = new BigInteger(Long.toHexString(uuid.getMostSignificantBits()) +
    				      Long.toHexString(uuid.getLeastSignificantBits()), 16);
    }

    public static MyUUID Random() {
    	UUID r = UUID.randomUUID();

    	return new MyUUID(r);
    }


    public boolean ck_sum(BigInteger ck) {
      return 0 == ck.compareTo(biTotal);
    }

    // unused!
    public JsonFmtUUID jsonObj() {
    	JsonFmtUUID retval = new JsonFmtUUID();
    	retval.msbs = uuid.getMostSignificantBits();
    	retval.lsbs = uuid.getLeastSignificantBits();
    	return retval;
    }
}

// https://howtodoinjava.com/learningpaths/gson/

// class SerializeUUID implements JsonSerializer<MyUUID> {
//   public JsonElement serialize(MyUUID src, Type typeOfSrc,
//                                JsonSerializationContext context) {
//      JsonObject retval = new JsonObect();
//      JsonFmtUUID uuid = src.jsonObj();
//      retval.add("msbs", uuid.msbs);
//      retval.add("lsbs", uuid.lsbs);
//      return retval;
//   }
// }
