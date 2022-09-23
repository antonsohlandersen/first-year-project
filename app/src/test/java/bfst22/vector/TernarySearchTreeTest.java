package bfst22.vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bfst22.vector.Data.Street;
import bfst22.vector.Data.TSTree.TernarySearchTree;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TernarySearchTreeTest {

    Street street1;
    Street street2;
    Street street3;
    Street street4;
    Street street5;
    Street street6;
    TernarySearchTree tstOneStreet;
    TernarySearchTree tstTwoStreets;
    TernarySearchTree tstSamePrefix;
    TernarySearchTree tstSameStreet;
    TernarySearchTree multipleChildren;

    TernarySearchTree emptyTree;
    TernarySearchTree tst3;
    String[] parsedAddress1;
    String[] parsedAddress2;
    String[] parsedAddress3;
    String[] parsedAddress4;
    String[] parsedAddress5;
    String[] parsedAddress6;
    String[] parsedAddress7;

    TernarySearchTree tstPrefix;
    Street prefixStreet1;
    Street prefixStreet2;

    @BeforeEach
    public void setup(){
        street1 = new Street("Skovduestien", "2400", "København");
        street1.addAddress("17", new float[]{1, 2});
        tstOneStreet = new TernarySearchTree();
        tstOneStreet.addStreet(street1);


        street2 = new Street("Skovduestien", "2500", "København");
        street2.addAddress("13", new float[]{3, 4});

        street3 = new Street("Skovduestien", "2600", "København");
        street3.addAddress("13", new float[]{5, 6});
        tstTwoStreets = new TernarySearchTree();
        tstTwoStreets.addStreet(street2);
        tstTwoStreets.addStreet(street3);



        street4 = new Street("Sagavej", "3000", "København");
        street4.addAddress("10", new float[]{7, 8});

        street5 = new Street("Sagavej", "3000", "København");
        street5.addAddress("5", new float[]{9, 10});

        tstSameStreet = new TernarySearchTree();
        tstSameStreet.addStreet(street4);
        tstSameStreet.addStreet(street5);


        parsedAddress1 = new String[]{"Skovduestien", "17", "2400", ""};
        parsedAddress2 = new String[]{"Skovduestien", "13", "2500", ""};
        parsedAddress3 = new String[]{"Skovduestien", "13", "2600", ""};
        parsedAddress4 = new String[]{"Sagavej", "10", "3000", ""};
        parsedAddress5 = new String[]{"Sagavej", "5", "3000", ""};
        parsedAddress6 = new String[]{"Nørrebrogade", "30", "2200", ""};
        parsedAddress7 = new String[]{"Nørrebro", "30", "2200", ""};

        //tst = new TernarySearchTree();
        emptyTree = new TernarySearchTree();

        tstPrefix = new TernarySearchTree();
        prefixStreet1 = new Street("Nørrebrogade", "2200", "København");
        prefixStreet1.addAddress("30", new float[]{11, 12});
        prefixStreet2 = new Street("Nørrebro", "2200", "København");
        prefixStreet2.addAddress("30", new float[]{13, 14});
        tstPrefix.addStreet(prefixStreet1);
        tstPrefix.addStreet(prefixStreet2);
        multipleChildren = new TernarySearchTree();
        multipleChildren.addStreet(street1);
        multipleChildren.addStreet(street2);
        multipleChildren.addStreet(street3);
        multipleChildren.addStreet(prefixStreet1);
        multipleChildren.addStreet(prefixStreet2);
        street6 = new Street("Tetersgade", "1000", "København");
        street6.addAddress("27", new float[]{13, 14});
        multipleChildren.addStreet(street6);

        street4 = new Street("Sagagade", "5000", "København");
        street4.addAddress("10", new float[]{18, 19});

        street5 = new Street("Sagagade", "5000", "København");
        street5.addAddress("5", new float[]{20, 21});
        multipleChildren.addStreet(street4);
        multipleChildren.addStreet(street5);

    }

    @Test
    void testMultipleChildren(){
        int actStreets = multipleChildren.getStreetCounter();
        int expStreets = 7;
        assertEquals(expStreets, actStreets);

        int actUniqueAddresses = multipleChildren.getUniqueAddresses();
        int expUniqueAddresses = 8;
        assertEquals(expUniqueAddresses, actUniqueAddresses);

        int actNodes = multipleChildren.getNodeCounter();
        int expNodes = 41;
        assertEquals(expNodes, actNodes);

        multipleChildren.compressTree();
        multipleChildren.updateInfoToConsoleViaTraversal();

        actStreets = multipleChildren.getStreetCounter();
        expStreets = 7;
        assertEquals(expStreets, actStreets);

        actUniqueAddresses = multipleChildren.getUniqueAddresses();
        expUniqueAddresses = 8;
        assertEquals(expUniqueAddresses, actUniqueAddresses);

        actNodes = multipleChildren.getNodeCounter();
        expNodes = 7;

        assertEquals(expNodes, actNodes);

    }

    @Test
    void emptyTest(){
        boolean exp = true;
        boolean act = emptyTree.isEmpty();
        assertEquals(exp, act);

        exp = false;
        act = tstOneStreet.isEmpty();
        assertEquals(exp, act);
    }

    @Test
    void testOneStreetGetCoordinatesPreAndPostCompression(){
        float[] act = tstOneStreet.getAddressCoordinates(parsedAddress1);
        float[] exp = new float[]{1, 2};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);

        tstOneStreet.compressTree();
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);
    }

    @Test
    void testTwoStreetsGetCoordinatesPreAndPostCompression(){
        float[] act = tstTwoStreets.getAddressCoordinates(parsedAddress2);
        float[] exp = new float[]{3, 4};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);

        act = tstTwoStreets.getAddressCoordinates(parsedAddress3);
        exp = new float[]{5, 6};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);

        tstTwoStreets.compressTree();

        act = tstTwoStreets.getAddressCoordinates(parsedAddress2);
        exp = new float[]{3, 4};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);

        act = tstTwoStreets.getAddressCoordinates(parsedAddress3);
        exp = new float[]{5, 6};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);
    }

    @Test
    void testTwoStreetsSameNameDataPreAndPostCompression(){
        int actStreets = tstTwoStreets.getStreetCounter();
        int expStreets = 2;
        assertEquals(expStreets, actStreets);

        int actUniqueAddresses = tstTwoStreets.getUniqueAddresses();
        int expUniqueAddresses = 2;
        assertEquals(expUniqueAddresses, actUniqueAddresses);

        int actNodes = tstTwoStreets.getNodeCounter();
        int expNodes = 12;


        assertEquals(expNodes, actNodes);

        tstTwoStreets.compressTree();
        tstTwoStreets.updateInfoToConsoleViaTraversal();

        actStreets = tstTwoStreets.getStreetCounter();
        expStreets = 2;
        assertEquals(expStreets, actStreets);

        actUniqueAddresses = tstTwoStreets.getUniqueAddresses();
        expUniqueAddresses = 2;
        assertEquals(expUniqueAddresses, actUniqueAddresses);

        actNodes = tstTwoStreets.getNodeCounter();
        expNodes = 1;

        assertEquals(expNodes, actNodes);
    }


    @Test
    void testOneStreetDataPreAndPostCompression(){
        int actStreets = tstOneStreet.getStreetCounter();
        int expStreets = 1;
        assertEquals(expStreets, actStreets);

        int actUniqueAddresses = tstOneStreet.getUniqueAddresses();
        int expUniqueAddresses = 1;
        assertEquals(expUniqueAddresses, actUniqueAddresses);

        int actNodes = tstOneStreet.getNodeCounter();
        int expNodes = 12;

        assertEquals(expNodes, actNodes);

        tstOneStreet.compressTree();
        tstOneStreet.updateInfoToConsoleViaTraversal();

        actStreets = tstOneStreet.getStreetCounter();
        expStreets = 1;
        assertEquals(expStreets, actStreets);
        actUniqueAddresses = tstOneStreet.getUniqueAddresses();
        expUniqueAddresses = 1;

        assertEquals(expUniqueAddresses, actUniqueAddresses);

        actNodes = tstOneStreet.getNodeCounter();
        expNodes = 1;

        assertEquals(expNodes, actNodes);
    }



    @Test
    void sameStreetDifferentAddressDataPreAndPostCompression() {
        int actStreets = tstSameStreet.getStreetCounter();
        int expStreets = 1;
        assertEquals(expStreets, actStreets);

        int actUniqueAddresses = tstSameStreet.getUniqueAddresses();
        int expUniqueAddresses = 2;
        assertEquals(expUniqueAddresses, actUniqueAddresses);

        int actNodes = tstSameStreet.getNodeCounter();
        int expNodes = 7;

        assertEquals(expNodes, actNodes);

        tstSameStreet.compressTree();
        tstSameStreet.updateInfoToConsoleViaTraversal();
        actStreets = tstSameStreet.getStreetCounter();
        expStreets = 1;
        assertEquals(expStreets, actStreets);

        actUniqueAddresses = tstSameStreet.getUniqueAddresses();
        expUniqueAddresses = 2;
        assertEquals(expUniqueAddresses, actUniqueAddresses);

        actNodes = tstSameStreet.getNodeCounter();
        expNodes = 1;

        assertEquals(expNodes, actNodes);

    }

    @Test
    void sameStreetDifferentAddressGetCoordinatesPreAndPostCompression(){
        float[] act = tstSameStreet.getAddressCoordinates(parsedAddress4);
        float[] exp = new float[]{7, 8};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[0], act[0]);

        act = tstSameStreet.getAddressCoordinates(parsedAddress5);
        exp = new float[]{9, 10};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);

        tstSameStreet.compressTree();

        act = tstSameStreet.getAddressCoordinates(parsedAddress4);
        exp = new float[]{7, 8};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);

        act = tstSameStreet.getAddressCoordinates(parsedAddress5);
        exp = new float[]{9, 10};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);
    }

    @Test
    void samePrefixDifferentStreetsGetCoordinatesPrePostCompression(){
        float[] act = tstPrefix.getAddressCoordinates(parsedAddress6);
        float[] exp = new float[]{11, 12};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);

        act = tstPrefix.getAddressCoordinates(parsedAddress7);
        exp = new float[]{13, 14};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);

        tstPrefix.compressTree();

        act = tstPrefix.getAddressCoordinates(parsedAddress6);
        exp = new float[]{11, 12};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);

        act = tstPrefix.getAddressCoordinates(parsedAddress7);
        exp = new float[]{13, 14};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);
    }

    @Test
    void samePrefixDifferentStreetsDataPrePostCompression(){

        int actStreets = tstPrefix.getStreetCounter();
        int expStreets = 2;
        assertEquals(expStreets, actStreets);

        int actUniqueAddresses = tstPrefix.getUniqueAddresses();
        int expUniqueAddresses = 2;
        assertEquals(expUniqueAddresses, actUniqueAddresses);

        int actNodes = tstPrefix.getNodeCounter();
        int expNodes = 12;

        assertEquals(expNodes, actNodes);

        tstPrefix.compressTree();
        tstPrefix.updateInfoToConsoleViaTraversal();
        actStreets = tstPrefix.getStreetCounter();
        expStreets = 2;
        assertEquals(expStreets, actStreets);

        actUniqueAddresses = tstPrefix.getUniqueAddresses();
        expUniqueAddresses = 2;
        assertEquals(expUniqueAddresses, actUniqueAddresses);

        actNodes = tstPrefix.getNodeCounter();
        expNodes = 2;

        assertEquals(expNodes, actNodes);

    }

    @Test
    void searchPostCompressionTest(){
        multipleChildren.compressTree();
        float[] act = multipleChildren.getAddressCoordinates(parsedAddress6);
        float[] exp = new float[]{11, 12};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[0], act[0]);

        act = multipleChildren.getAddressCoordinates(new String[]{"hejsa"});
        exp = null;
        assertEquals(exp, act);

        act = multipleChildren.getAddressCoordinates(new String[]{"nørrebrogada"});
        exp = null;
        assertEquals(exp, act);

        act = multipleChildren.getAddressCoordinates(new String[]{"nørrebrogadq"});
        exp = null;
        assertEquals(exp, act);

        act = multipleChildren.getAddressCoordinates(new String[]{"n"});
        exp = null;
        assertEquals(exp, act);

        act = multipleChildren.getAddressCoordinates(new String[]{""});
        exp = null;
        assertEquals(exp, act);

        act = multipleChildren.getAddressCoordinates(new String[]{"nørr"});
        exp = null;
        assertEquals(exp, act);
    }

    @Test
    void SearchSuggestionsPostCompression(){
        multipleChildren.compressTree();
        multipleChildren.getSuggestions().clear();
        multipleChildren.searchSuggestions(new String[]{"sa", "", "", ""});
        int exp = 2;
        int act = multipleChildren.getSuggestions().size();
        assertEquals(exp, act);
        String actFirstSuggestion = multipleChildren.getSuggestions().get(0);
        String expFirstSuggestion = "Sagagade 10 5000 København";

        assertEquals(expFirstSuggestion, actFirstSuggestion);

        String actLastSuggestion = multipleChildren.getSuggestions().get(multipleChildren.getSuggestions().size()-1);
        String expLastSuggestion = "Sagagade 5 5000 København";

        assertEquals(expLastSuggestion, actLastSuggestion);

        multipleChildren.getSuggestions().clear();
        multipleChildren.searchSuggestions(new String[]{"q", "", "", ""});
        exp = 0;
        act = multipleChildren.getSuggestions().size();
        assertEquals(exp, act);

        multipleChildren.getSuggestions().clear();
        multipleChildren.searchSuggestions(new String[]{"skovduestien", "", "", ""});
        exp = 3;
        act = multipleChildren.getSuggestions().size();
        assertEquals(exp, act);
        actFirstSuggestion = multipleChildren.getSuggestions().get(0);
        expFirstSuggestion = "Skovduestien 17 2400 København";

        assertEquals(expFirstSuggestion, actFirstSuggestion);

        actLastSuggestion = multipleChildren.getSuggestions().get(multipleChildren.getSuggestions().size()-1);
        expLastSuggestion = "Skovduestien 13 2600 København";

        assertEquals(expLastSuggestion, actLastSuggestion);


        multipleChildren.getSuggestions().clear();

        multipleChildren.searchSuggestions(new String[]{"skov", "", "", ""});
        exp = 3;
        act = multipleChildren.getSuggestions().size();
        assertEquals(exp, act);

        actFirstSuggestion = multipleChildren.getSuggestions().get(0);
        expFirstSuggestion = "Skovduestien 17 2400 København";

        assertEquals(expFirstSuggestion, actFirstSuggestion);

        actLastSuggestion = multipleChildren.getSuggestions().get(multipleChildren.getSuggestions().size()-1);
        expLastSuggestion = "Skovduestien 13 2600 København";

        assertEquals(expLastSuggestion, actLastSuggestion);
    }

    @Test
    void compressTreeEdgeCase(){
        TernarySearchTree edgecase = new TernarySearchTree();
        Street edge1 = new Street("Sagavej", "2100", "København");
        edge1.addAddress("10", new float[]{1, 2});
        Street edge2 = new Street("Sagavejs", "2450", "København");
        edge2.addAddress("11", new float[]{1, 2});
        Street edge3 = new Street("Sagavejse", "2900", "København");
        edge2.addAddress("12", new float[]{1, 2});

        edgecase.addStreet(edge1);
        edgecase.addStreet(edge2);
        edgecase.addStreet(edge3);

        edgecase.compressTree();
        edgecase.updateInfoToConsoleViaTraversal();
        int expStreets = 3;
        int actStreets = edgecase.getStreetCounter();
        assertEquals(expStreets, actStreets);

        int expAddresses = 3;
        int actAddresses = edgecase.getUniqueAddresses();
        assertEquals(expAddresses, actAddresses);

        int expNodes = 3;
        int actNodes = edgecase.getNodeCounter();
        assertEquals(expNodes, actNodes);
    }

    @Test
    void compressTreeEdgeCase2(){
        TernarySearchTree edgecase = new TernarySearchTree();
        Street edge1 = new Street("Nørrebrogade", "2100", "København");
        edge1.addAddress("10", new float[]{1, 2});
        Street edge2 = new Street("Nørrebrovej", "2450", "København");
        edge2.addAddress("11", new float[]{1, 2});
        Street edge3 = new Street("Nørrebrostræde", "2900","København");
        edge3.addAddress("12", new float[]{1, 2});
        Street edge4 = new Street("Nørrebrobanen", "2850", "København");
        edge4.addAddress("12", new float[]{1, 2});
        Street edge5 = new Street("Nørre Bodega", "2851", "København");
        edge5.addAddress("12", new float[]{1, 2});
        Street edge6 = new Street("Nørre Stræde", "2852", "København");
        edge6.addAddress("12", new float[]{1, 2});

        edgecase.addStreet(edge1);
        edgecase.addStreet(edge2);
        edgecase.addStreet(edge3);
        edgecase.addStreet(edge4);
        edgecase.addStreet(edge5);
        edgecase.addStreet(edge6);

        edgecase.compressTree();
        edgecase.searchSuggestions(new String[]{"nørreb", "", "", ""});
        int exp = 4;
        int act = edgecase.getSuggestions().size();
        assertEquals(exp, act);
        String actFirstSuggestion = edgecase.getSuggestions().get(0);
        String expFirstSuggestion = "Nørrebrobanen 2850 København";

        assertEquals(expFirstSuggestion, actFirstSuggestion);

        String actLastSuggestion = edgecase.getSuggestions().get(edgecase.getSuggestions().size()-1);
        String expLastSuggestion = "Nørrebrovej 2450 København";

        assertEquals(expLastSuggestion, actLastSuggestion);

        edgecase.getSuggestions().clear();

        edgecase.searchSuggestions(new String[]{"nørrebros", "", "", ""});
        exp = 1;
        act = edgecase.getSuggestions().size();
        assertEquals(exp, act);

        edgecase.getSuggestions().clear();

        edgecase.searchSuggestions(new String[]{"n", "", "", ""});
        exp = 5;
        act = edgecase.getSuggestions().size();
        assertEquals(exp, act);
        actFirstSuggestion = edgecase.getSuggestions().get(0);
        expFirstSuggestion = "Nørre Bodega 2851 København";

        assertEquals(expFirstSuggestion, actFirstSuggestion);

        actLastSuggestion = edgecase.getSuggestions().get(edgecase.getSuggestions().size()-1);
        expLastSuggestion = "Nørrebrostræde 2900 København";

        assertEquals(expLastSuggestion, actLastSuggestion);
    }

    @Test
    void findFiveGrandChildrenTest(){
        TernarySearchTree edgecase = new TernarySearchTree();
        Street edge1 = new Street("Nørregade", "2900", "København");
        edge1.addAddress("10", new float[]{1, 2});
        Street edge2 = new Street("Nørregade", "2900", "København");
        edge2.addAddress("11", new float[]{1, 2});
        Street edge3 = new Street("Nørregade", "2900", "København");
        edge3.addAddress("12", new float[]{1, 2});
        Street edge4 = new Street("Nørregade", "2900", "København");
        edge4.addAddress("12", new float[]{1, 2});
        Street edge5 = new Street("Nørregade", "2900", "København");
        edge5.addAddress("12", new float[]{1, 2});
        Street edge6 = new Street("Nørregade", "2900", "København");
        edge6.addAddress("12", new float[]{1, 2});

        edgecase.addStreet(edge1);
        edgecase.addStreet(edge2);
        edgecase.addStreet(edge3);
        edgecase.addStreet(edge4);
        edgecase.addStreet(edge5);
        edgecase.addStreet(edge6);
    }




    @AfterEach
    void tearDown(){
        street1 = null;
         street2 = null;
         street3 = null;
         tstOneStreet = null;
         tstTwoStreets = null;
         tstSamePrefix = null;
         tstSameStreet = null;

         emptyTree = null;
         tst3 = null;
         parsedAddress1 = null;
         parsedAddress2 = null;
         parsedAddress3 = null;

    }



}
