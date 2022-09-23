package bfst22.vector.Data;
import bfst22.vector.Collections.AddressMap;

import java.io.Serializable;
import java.util.ArrayList;

//Street represents a street, and all the addresses located on that street
public class Street implements Serializable {
    public static final long serialVersionUID = 9082415;
    String streetname;
    AddressMap addressMap;
    String postNumber;
    String city;

    public Street(String streetname, String postNumber, String city) {
        this.addressMap = new AddressMap();
        this.streetname = streetname.intern();
        this.postNumber = postNumber.intern();
        this.city = city.intern();
    }

    public void addAddress(String housenumber, float[] xy) {
        addressMap.add(new NumLocation(housenumber, xy[0], xy[1]));
    }

    public NumLocation getFirstAddress(){
        return addressMap.get(0);
    }

    public String getPostNumber() {
        return postNumber;
    }

    public String getStreetname() {
        return streetname;
    }

    public AddressMap getAddressMap() {
        return addressMap;
    }

    public void findFiveAddresses(ArrayList<String> searchSuggestions, String[] parsedAddress) {
        for (NumLocation numLoc : addressMap) {
            if (searchSuggestions.size() > 4) {
                return;
            }
            if (numLoc.getHousenumber().contains(parsedAddress[1])) {
                searchSuggestions.add(this.fullAddress(numLoc.getHousenumber()));
            }
        }
    }

    public String fullAddress(String housenumber) {
        if (housenumber.isBlank()){
            if (city.isBlank()){
                return streetname + " " + postNumber;
            }
            return streetname + " " + postNumber + " " + city;
        }
        return streetname + " " + housenumber + " " + postNumber + " " + city;
    }

    public float[] getCoordinates(String housenumber) {
        return addressMap.get(housenumber);
    }

}
