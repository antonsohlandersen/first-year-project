package bfst22.vector.Parsing;

import java.util.regex.Pattern;

public class RegexParser {

    public String street, houseNumber, floor, side, postcode, city;

    String input;
    private final static String REGEX = "^(?<street>[A-ZÆØÅa-zæøå ./]+)([ ]?)?(?<houseNumber>[0-9]+[a-zA-Zæøå]?)?[, ]?(?<postcode>[0-9]+)?[, ]?(?<city>[A-Za-zæøå ]+)??$";
    private final static Pattern PATTERN = Pattern.compile(REGEX);

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    public static String[] getAll(String prefix){
        var matcher = PATTERN.matcher(prefix);
        String street = "";
        String houseNumber = "";
        String postcode = "";
        String city = "";
        if (matcher.matches()) {
            street = matcher.group("street");
            houseNumber = matcher.group("houseNumber");
            postcode = matcher.group("postcode");
            city = matcher.group("city");
        }
        if (street != null){
            street = street.trim().toLowerCase();
        }
        else{
            street = "";
        }
        if (houseNumber == null){
            houseNumber = "";
        }
        if (postcode == null){
            postcode = "";
        }
        if (city == null){
            city = "";
        }
        String[] parsedAddress = new String[]{street, houseNumber, postcode, city};
        return parsedAddress;
    }
}