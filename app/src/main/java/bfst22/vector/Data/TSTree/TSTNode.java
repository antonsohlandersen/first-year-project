package bfst22.vector.Data.TSTree;

import bfst22.vector.Data.Street;
import bfst22.vector.Interfaces.Nodeable;

import java.io.Serializable;
import java.util.ArrayList;

//TSTNodes are responsible for holding Street-objects, or refer to other TSTNodes when
//traversing the tree during address-searching
public class TSTNode implements Nodeable, Serializable {
    public static final long serialVersionUID = 9082428;
    private char[] chars;
    private ArrayList<Street> streets;
    private boolean isEnd;
    private TSTNode left, middle, right;

    public TSTNode(char[] chars) {
        this.chars = chars;
        this.isEnd = false;
        this.left = null;
        this.middle = null;
        this.right = null;
    }

    //appends the data of a TSTNode during compression of the Terniary Search Tree
    public void setData(ArrayList<Character> data){
        if (data.isEmpty()){
            return;
        }
        char[] tempdata = new char[this.chars.length + data.size()];

        for (int i = 0; i < this.chars.length; i++){
            tempdata[i] = this.chars[i];
        }

        for (int j = 0; j< data.size(); j++){
            tempdata[j + this.chars.length] = data.get(j);
        }
        this.chars = tempdata;
    }

    public void absorb(TSTNode r) {
        this.left = r.left;
        this.middle = r.middle;
        this.right = r.right;
        this.isEnd = r.isEnd;
        this.streets = r.streets;
    }

    public String dataAsString(){
        String printout = "[";
        for (char c : chars){
            printout += c;
        }
        return printout + "]";
    }

    //returns the only child of TSTNode if it exists
    public TSTNode getLonelyChild(){
        if (this.right != null && this.middle == null && this.left == null){
            return this.right;
        }
        if (this.right == null && this.middle != null && this.left == null){
            return this.middle;
        }
        if (this.right == null && this.middle == null && this.left != null){
            return this.left;
        }
        return null;
    }

    public ArrayList<Street> getStreets() {
        return streets;
    }

    public void setStreets(ArrayList<Street> streets) {
        this.streets = streets;
    }

    public char[] getChars() {
        return chars;
    }

    public TSTNode getRight(){
        return right;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public TSTNode getLeft() {
        return left;
    }

    public TSTNode getMiddle() {
        return middle;
    }

    public void setRight(TSTNode right){
        this.right = right;
    }

    public void setLeft(TSTNode left) {
        this.left = left;
    }

    public void setMiddle(TSTNode middle) {
        this.middle = middle;
    }

    @Override
    public boolean hasData() {
        return streets != null;
    }
}
