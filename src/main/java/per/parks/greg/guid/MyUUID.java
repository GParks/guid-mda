package per.parks.greg.guid;

import java.util.UUID;
import java.math.BigInteger;
import java.lang.Integer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// unused class
class JsonFmtUUID {
  public long msbs;
  public long lsbs;
}

public class MyUUID
    implements Comparable<MyUUID> {

  public int compareTo(MyUUID y) {
    // TO DO: maybe later, compare md5 hash if the (cached) value has been calculated...
    return biTotal.compareTo(y.biTotal);
  }
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

  private final boolean bDebug = false;

  protected MessageDigest md;
  private final BigInteger EIGHT_BITS = new BigInteger("255");

  private void calc_hash(BigInteger val, int byt) {
      if (byt >= 0) {
        // recurse first, then digest the 'next' byte
        if (val.compareTo(EIGHT_BITS) > 0) {
          // System.err.println("\t    MyUUID:calc_hash - recursing, shifting right and byt = " + Integer.toString(byt-1));
          calc_hash(val.shiftRight(8), byt-1);
        }  // else {
        //   System.err.println("\t    MyUUID:calc_hash - recursing with zero");
        //   calc_hash(BigInteger.ZERO, byt-1);
        // }

        if (bDebug) {
            byte[] ba = val.and(EIGHT_BITS).toByteArray();
            byte bVal = ba[ba.length-1];
            String addl = "";
            // if (ba.length > 1) {
            //   addl = String.format("(2nd byte = 0x%02x; length = %d, ba.length)", ba[1], ba.length);
            // }
            System.err.format("\t    calc_hash updating 0x%02x %s\n", bVal, addl);
            md.update(bVal);
        } else {
          byte[] ba = val.and(EIGHT_BITS).toByteArray();
          md.update(ba[ba.length-1]);
        }
      }
  }

  public BigInteger delta(MyUUID x) {
    return biTotal.subtract(x.biTotal);

  }
  
  protected BigInteger h = null;

  // TO DO: cache the result, so I don't have to re-calculate
  public BigInteger hash() {
    BigInteger retval;
    if (null == h) {
      try {
        md = MessageDigest.getInstance("MD5");

        // 128 bits = 16 bytes
        calc_hash(biTotal, 15);

        // I may like this better as a 32 digit hex [string] value...
        h = retval = new BigInteger(1, md.digest());
      } catch (NoSuchAlgorithmException nsa) {
        System.err.println("\tMyUUID::hash couldn't get an 'MD5' algorithm instance!");
        retval = BigInteger.ZERO;
      }
    } else {
      retval = h;
    }
    return retval;
  }

  public BigInteger asBigInt() {
    return this.biTotal;
  }

  public String toString() {
    return this.uuid.toString();
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
