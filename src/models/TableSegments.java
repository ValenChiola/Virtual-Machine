package models;

public class TableSegments {

    public int getSize(int segment) {
        throw new UnsupportedOperationException("Unimplemented method 'getSize'");
    }

    public int getBase(int segment) {
        throw new UnsupportedOperationException("Unimplemented method 'getBase'");
    }

    public int getLimit(int segment) {
        return getBase(segment) + getSize(segment);
    }

}