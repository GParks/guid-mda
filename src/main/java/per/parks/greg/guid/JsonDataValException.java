package per.parks.greg.guid;

public class JsonDataValException
    extends java.lang.RuntimeException {
    // by sub-classing RuntimeException, this is an un-checked exception
    // (I don't need to declare it in the func. defn.)
    //  I may want to re-think that later
    public JsonDataValException() {
      super("JSON data validation failed - the BigInteger of the parsed UUID does not match the associated biTotal value");
    }
}
