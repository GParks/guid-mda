package per.parks.greg.guid;

// https://github.com/kohsuke/args4j/blob/master/args4j/examples/SampleMain.java
import static org.kohsuke.args4j.ExampleMode.ALL;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.io.IOException;
import java.util.UUID;

import java.math.BigInteger;
import java.io.FileWriter;
import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;


//
// to read: https://tools.ietf.org/html/rfc4122
//

class MyUUID_Deserializer implements JsonDeserializer<MyUUID> {
  public MyUUID deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
                            JsonDeserializationContext context)
       throws JsonParseException {
    JsonObject jsonMap = json.getAsJsonObject();
    String sUUID = jsonMap.get("uuid").getAsString();
    BigInteger bi = jsonMap.get("biTotal").getAsBigInteger();
    java.util.UUID n = java.util.UUID.fromString(sUUID);
    MyUUID r = new MyUUID(n);
    if (!r.ck_sum(bi)) {
      // throw some exception here
    }
    return r;
  }
}


public class First
{
  @Option(name="-c", usage="count of GUIDs to generate")
  protected int count = 0;  // this was long before, what has happened?

  @Option(name="-o", usage="output file for JSON of UUIDs")
  protected String out_file = "uuids.json";

  @Option(name="-f", usage="input file (of UUIDs in JSON)")
  protected String in_file = null;

  protected MyUUID uuids[];

  First() {
    System.out.println("First ctor");
  }

  //
  // create a number of GUIDs, count is a cmd. line arg
  //
  // 9/12/2019 23:36 CDT
  // the commented out code is vestiges, it was more than I could figure,
  // tonight, how to "add," or even bit-wise combine the two longs,
  // which may well be negative, since they are not unsigned, and --
  // never mind, I figured out how to create what was needed.
  // (biHi and biLo are no longer needed, either)

  // 3/18/2020 21:32 CDT
  // ooiiii ioiiio ioooii iooioi  iioioi iioooo ioiiii ioiiio  iooooi
  // ioiioi ioiooi iooioo ioiiio ioiooi iooiii ioiooo iioioo
  // iooioo iiooio iooioi iooooi iiooio iiiooi,
  // changing, now, generate to
  // create count UUID objects -- and save them to a file - JSON list
  public void generate()
  {
    if (count == 0) {
      System.out.println("\tcount was zero, defaulting to 5; specify -c cmd line arg.");
      count = 5;
    }
    uuids = new MyUUID[count];

    for(int i=0; i<count; i++) {
      // https://stackoverflow.com/questions/2982748/create-a-guid-in-java
      UUID uuid = UUID.randomUUID();

      uuids[i] = new MyUUID(uuid);

      long lo = uuid.getLeastSignificantBits(),
          hi = uuid.getMostSignificantBits();
      BigInteger biHi = BigInteger.valueOf(hi);
      // if (hi < 0) {
      //  biHi = biHi.negate().add(BigInteger.ONE);
      // }
      BigInteger biLo = BigInteger.valueOf(lo);
      // if (lo < 0) {
      //   biLo = biLo.negate();
      // }
      BigInteger biTotal = new BigInteger(Long.toHexString(hi) + Long.toHexString(lo), 16);


      System.out.println("\t" + i + ": " + uuid + ", \t" + lo + ", " + hi);
      System.out.println("\t    " + Long.toHexString(hi) + " "
			   + Long.toHexString(lo));
      System.out.println("\t   0x" + biTotal.toString(16));
      // System.out.println("    - OR - " + biTotal.not().toString(16) );
      // biHi.negate().add(BigInteger.ONE).shiftLeft(64).toString(16) + "\n");
      System.out.println("\t = " + biTotal + "\n");
    }
  }

  public void serialize() {
    Gson g = new Gson();
    String s = g.toJson(uuids);
    FileWriter fw = null;
    try {
    	fw = new FileWriter(out_file);
    	fw.write(s);
    	fw.close();
    } catch (IOException e) {
	     System.err.println("\tFirst::serialize threw:" + e.toString());
    }
  }

  public boolean deserialize() {
    boolean retval = true;

    GsonBuilder gb = new GsonBuilder();
    gb.registerTypeAdapter(MyUUID.class, new MyUUID_Deserializer());
    Gson g = gb.create();

    FileReader fr = null;
    StringBuilder sb = new StringBuilder();
    try {
      fr = new FileReader(in_file);
      BufferedReader br = new BufferedReader(fr);
      while (br.ready()) {
        String l = br.readLine();
        sb.append(l + "\n");
      }
      br.close();
    } catch (FileNotFoundException fnf) {
      System.err.println("\tFirst::deserialize caught a file not found exception: " + fnf.getLocalizedMessage());
      retval = false;
    } catch (IOException ioe) {
      System.err.println("\tFirst::deserialize caught an I/O exception (maybe from closing the BufferedReader?): "
                         + ioe.getLocalizedMessage());
      retval = false;
    }
    if (null != sb) {
      uuids = g.fromJson(sb.toString(), MyUUID[].class);
    }

    return retval;
  }

  public static void main(String[] args) throws IOException {
    First f = new First();
    CmdLineParser parser = new CmdLineParser(f);
    try {
      parser.parseArgument(args);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      System.err.println("java -jar guid-mda.jar [options...] arguments...");
      // print the list of available options
      parser.printUsage(System.err);
      System.err.println();
    }
    boolean bContinue = true;
    if (null != f.in_file) {
      bContinue = f.deserialize();
    } else {
      f.generate();
    }
    if (bContinue) {
      // do some calculation(s)

      // write out JSON using Gson
      // https://github.com/google/gson/blob/master/UserGuide.md#gson-user-guide
      f.serialize();
    }
  }
}
