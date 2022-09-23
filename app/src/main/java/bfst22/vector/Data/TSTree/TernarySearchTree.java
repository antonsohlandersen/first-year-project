package bfst22.vector.Data.TSTree;

import bfst22.vector.Data.NumLocation;
import bfst22.vector.Data.Street;
import bfst22.vector.exceptions.TreeStructureException;

import java.io.Serializable;
import java.util.ArrayList;

//TerniarySearchTree holds all addresses in Street objects which are kept in nodes in the tree.
//based on implementation from https://www.sanfoundry.com/java-program-ternary-search-tree/
public class TernarySearchTree implements Serializable {
    public static final long serialVersionUID = 9082414;
    private TSTNode root;
    private ArrayList<String> suggestions = new ArrayList<>();
    private int nodeCounter;
    private int streetCounter;
    private int uniqueAddresses;
    private ArrayList<Character> compressedChars = new ArrayList<>();

    public TernarySearchTree() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void insert(Street street) {
        root = insert(root, street, street.getStreetname().toLowerCase().toCharArray(), 0);
    }

    private void addCharsToCompressedChars(char[] data) {
        for (char c : data){
            compressedChars.add(c);
        }
    }
    public void compressTree() {
        compressTree(root, root);
    }

    //Compresses the tree recursively to save memory. Long branches of nodes that have only one child are
    //compressed into single nodes
    private void compressTree(TSTNode r, TSTNode compressionStart) {
        if (r == null || compressionStart == null){
            return;
        }
        TSTNode child = r.getLonelyChild();
        if (child != null && !r.hasData()){
            addCharsToCompressedChars(child.getChars());
            compressTree(child, compressionStart);
        }
        else{
            if (!compressedChars.isEmpty()){
                compressionStart.absorb(r);
                compressionStart.setData(compressedChars);
                compressedChars.clear();
                compressTree(compressionStart.getLeft(), compressionStart.getLeft());
                compressTree(compressionStart.getMiddle(), compressionStart.getMiddle());
                compressTree(compressionStart.getRight(), compressionStart.getRight());
            }
            else{
                compressTree(r.getLeft(), r.getLeft());
                compressTree(r.getMiddle(), r.getMiddle());
                compressTree(r.getRight(), r.getRight());
            }
        }
    }
    public void searchSuggestions(String[] parsedAddress){
        if (!parsedAddress[0].isBlank()) {
            char[] prefix = parsedAddress[0].toCharArray();
            searchSuggestions(root, parsedAddress, prefix, 0);
        }
    }

    //adds suggestions for addresses to searchSuggestions based on a parsed address or
    // a partially parsed address
    private void searchSuggestions(TSTNode r, String[] parsedAddress, char[] prefix, int ptr) {
        if (r == null) {
            return;
        }
        int iterations = prefix.length - ptr;
        if (iterations>r.getChars().length){
            iterations = r.getChars().length;
        }
        boolean match = true;
        int iteration_stop = -1;
        for (int i = 0; i < iterations; i++){
            if (prefix[ptr + i] != r.getChars()[i]){
                match = false;
                iteration_stop = i;
                break;
            }
        }
        if (match){
            if (prefix.length - ptr > r.getChars().length){
                searchSuggestions(r.getMiddle(), parsedAddress, prefix, ptr + r.getChars().length);
            }
            else if (iterations<r.getChars().length){
                if (!r.hasData()){
                    findFiveStreets(r);
                }
                else{
                    for (Street s : r.getStreets()){
                        for (NumLocation numLoc : s.getAddressMap()){
                            if (suggestions.size()>4){
                                break;
                            }
                            if (numLoc.getHousenumber().contains(parsedAddress[1])){
                                suggestions.add(s.fullAddress(numLoc.getHousenumber()));
                            }
                        }
                    }
                    return;
                }
            }
            else if (iterations==r.getChars().length) {
                if (!r.hasData()){
                    findFiveStreets(r.getMiddle());
                    if (suggestions.size() == 1){
                        suggestions.clear();
                        findFiveAddresses(r.getMiddle(), parsedAddress);
                    }
                    return;
                }
                else{
                    for (Street s : r.getStreets()){
                        for (NumLocation numLoc : s.getAddressMap()){
                            if (numLoc.getHousenumber().contains(parsedAddress[1])){
                                s.findFiveAddresses(suggestions, parsedAddress);
                            }
                        }
                    }
                    return;
                }
            }
        }
        else{
            if (iteration_stop < 0){
                return;
            }
            if (iteration_stop == r.getChars().length-1){
                if (prefix[ptr + iteration_stop] < r.getChars()[r.getChars().length-1]){
                    searchSuggestions(r.getLeft(), parsedAddress, prefix, ptr+r.getChars().length-1);
                }
                else if (prefix[ptr + iteration_stop] > r.getChars()[r.getChars().length-1]){
                    searchSuggestions(r.getRight(), parsedAddress, prefix, ptr + r.getChars().length-1);
                }
            }
            else if(iteration_stop < r.getChars().length-1){
                return;
            }
        }
    }

    //traverses the tree, searching for a address matching the parsed address
    private Street searchParsedAddress(TSTNode r, String[] parsedAddress, char[] prefix, int ptr) {
        if (r == null) {
            return null;
        }
        int iterations = prefix.length - ptr;
        if (iterations>r.getChars().length){
            iterations = r.getChars().length;
        }
        boolean match = true;
        int iteration_stop = -1;
        for (int i = 0; i < iterations; i++){
            if (prefix[ptr + i] != r.getChars()[i]){
                match = false;
                iteration_stop = i;
                break;
            }
        }
        if (match){
            if (prefix.length - ptr > r.getChars().length){
                return searchParsedAddress(r.getMiddle(), parsedAddress, prefix, ptr + r.getChars().length);
            }
            else if (iterations==r.getChars().length){
                if (!r.hasData()){
                    return null;
                }
                for (Street s : r.getStreets()) {
                    if (parsedAddress[2].equals(s.getPostNumber())) {
                        return s;
                    }
                }
            }
        }
        else{
            if (iteration_stop < 0){
                return null;
            }
            if (iteration_stop == r.getChars().length-1){
                if (prefix[ptr + iteration_stop] < r.getChars()[r.getChars().length-1]){
                    return searchParsedAddress(r.getLeft(), parsedAddress, prefix, ptr+r.getChars().length-1);
                }
                else if (prefix[ptr + iteration_stop] > r.getChars()[r.getChars().length-1]){
                    return searchParsedAddress(r.getRight(), parsedAddress, prefix, ptr + r.getChars().length-1);
                }
            }
            else if(iteration_stop < r.getChars().length-1){
                return null;
            }
        }
        return null;
    }

    //adds a street to the tree. If the street already exists, only the housenumber and location are added
    public void addStreet(Street street) {
        this.uniqueAddresses++;
        Street result = searchStreet(root, street, street.getStreetname().toLowerCase().toCharArray(), 0);
        if (result != null) {
            result.addAddress(street.getFirstAddress().getHousenumber(),
                    street.getFirstAddress().getLoc());
        } else {
            insert(street);
        }
    }

    //returns the coordinates of a parsed address if the address exists
    public float[] getAddressCoordinates(String[] parsedAddress) {
        if (parsedAddress[0].isBlank()) {
            return null;
        }
        float[] coordinates = null;
        Street result = searchParsedAddress(root, parsedAddress, parsedAddress[0].toLowerCase().toCharArray(), 0);
        if (result != null){
            coordinates = result.getCoordinates(parsedAddress[1]);
        }
        return coordinates;
    }

    //inserts the Street and leaves a trail of chars that are stored in the TSTNodes
    private TSTNode insert(TSTNode r, Street street, char[] prefix, int ptr) {
        if (r == null) {
            r = new TSTNode(new char[]{prefix[ptr]});
            nodeCounter++;
            if (ptr == prefix.length-1){
                r.setStreets(new ArrayList<>());
                r.getStreets().add(street);
                streetCounter++;
                return r;
            }
        }
        if (prefix[ptr] < r.getChars()[0])
            r.setLeft(insert(r.getLeft(), street, prefix, ptr));
        else if (prefix[ptr] > r.getChars()[0])
            r.setRight(insert(r.getRight(), street, prefix, ptr));
        else{
            if (ptr == prefix.length-1){
                if (prefix[ptr] == r.getChars()[0]){
                    if (!r.hasData()){
                        r.setStreets(new ArrayList<>());
                    }
                    r.getStreets().add(street);
                    streetCounter++;
                    return r;
                }
            }
            else if (ptr + 1 < prefix.length)
                r.setMiddle(insert(r.getMiddle(), street, prefix, ptr + 1));
        }
        return r;
    }

    private Street searchStreet(TSTNode r, Street street, char[] prefix, int ptr) {
        if (r == null)
            return null;
        if (prefix[ptr] < r.getChars()[0])
            return searchStreet(r.getLeft(), street, prefix, ptr);
        else if (prefix[ptr] > r.getChars()[0])
            return searchStreet(r.getRight(), street, prefix, ptr);
        else {
            if (ptr == prefix.length - 1) {
                if (!r.hasData()) {
                    return null;
                }
                for (Street s : r.getStreets()) {
                    if (s.getPostNumber().equals(street.getPostNumber())) {
                        return s;
                    }
                }
            }
            else if (ptr + 1 < prefix.length) {
                return searchStreet(r.getMiddle(), street, prefix, ptr + 1);
            }
        }
        return null;
    }

    //finds five addresses based a partially parsed address by traversing the tree from left to right
    private void findFiveAddresses(TSTNode r, String[] parsedAddress) {
        if (suggestions.size()>4){
            return;
        }
        if (r == null) {
            return;
        }
        if (r.hasData()){
            for (Street s : r.getStreets()){
                if (s != null){
                    s.findFiveAddresses(suggestions, parsedAddress);
                }
            }
        }
        findFiveAddresses(r.getLeft(), parsedAddress);
        findFiveAddresses(r.getMiddle(), parsedAddress);
        findFiveAddresses(r.getRight(), parsedAddress);
    }

    //finds five streets based a partially parsed address by traversing the tree from left to right
    private void findFiveStreets(TSTNode r) {
        if (suggestions.size() > 4) {
            return;
        }
        if (r == null) {
            return;
        }
        if (r.hasData()){
            for (Street s : r.getStreets()){
                if (suggestions.size() > 4) {
                    break;
                }
                if (s != null){
                    suggestions.add(s.fullAddress(""));
                }
            }
        }
        findFiveStreets(r.getLeft());
        findFiveStreets(r.getMiddle());
        findFiveStreets(r.getRight());
    }

    //updates information about the tree by traversing it
  public void updateInfoToConsoleViaTraversal() {
        this.nodeCounter = 0;
        this.streetCounter = 0;
        this.uniqueAddresses = 0;
        traverse(root);
    }

    //traverse the tree and update information
    private void traverse(TSTNode r) {
        if (r != null)
        {
            if (r.hasData()){
                for (Street s : r.getStreets()){
                    streetCounter++;
                    uniqueAddresses += s.getAddressMap().size();
                }
            }
            this.nodeCounter++;
            traverse(r.getLeft());
            traverse(r.getMiddle());
            traverse(r.getRight());
        }
    }


    public void checkTreeForErrors(){
        checkTreeForErrors(root);
    }

    private void checkTreeForErrors(TSTNode r){
        if (!r.hasData()){
            if (r.getLeft() == null && r.getMiddle() == null && r.getRight() == null){
                throw new TreeStructureException(r);
            }
        }
        if (r.getChars() == null){
            return;
        }
        if (r.getMiddle() != null){
            checkTreeForErrors(r.getMiddle());
        }
        if (r.getLeft() != null){
            if (r.getLeft().getChars()[0]>= r.getChars()[r.getChars().length-1]){
                throw new TreeStructureException(r.getLeft().getChars()[r.getLeft().getChars().length-1], r.getChars()[r.getChars().length-1], "less");
            }
            checkTreeForErrors(r.getLeft());
        }
        if (r.getRight() != null){
            if (r.getRight().getChars()[0]<= r.getChars()[r.getChars().length-1]){
                throw new TreeStructureException(r.getRight().getChars()[r.getRight().getChars().length-1], r.getChars()[r.getChars().length-1], "moore");
            }
            checkTreeForErrors(r.getRight());
        }
    }

    public void clearSuggestions() {
        suggestions.clear();
    }

    public void getInfoToConsole(){
        System.out.println("Number of nodes: " + this.nodeCounter);
        System.out.println("Number of streets: " + this.streetCounter);
        System.out.println("Number of unique addresses: " + this.uniqueAddresses);
        System.out.println("\n");
    }

    public int getStreetCounter() {
        return streetCounter;
    }

    public int getNodeCounter() {
        return nodeCounter;
    }

    public int getUniqueAddresses() {
        return uniqueAddresses;
    }

    public ArrayList<String> getSuggestions() {
        return suggestions;
    }
}
