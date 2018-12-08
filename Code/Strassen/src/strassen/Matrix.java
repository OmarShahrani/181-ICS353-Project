package strassen;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 *
 * @author omarm
 */
public class Matrix {

    long[][] matrix;
    int size;

    public Matrix(int size) {
        this.size = size;
        this.matrix = new long[size][size];

    }

    public void push(long value, int row, int col) {
        this.matrix[row][col] = value;
    }

    public long get(int row, int col) {
        return this.matrix[row][col];
    }

    public Matrix getRange(int rowIndex, int colIndex, int size) {
        Matrix newCopy = new Matrix(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                newCopy.push(this.matrix[row + rowIndex][col + colIndex], row, col);
            }
        }
        return newCopy;
    }

    public void put(Matrix B, int rowIndex, int colIndex) {
        long value;
        for (int row = 0; row < B.size; row++) {
            for (int col = 0; col < B.size; col++) {
                value = this.get(row+rowIndex, col+colIndex) + B.get(row, col);
                this.push(value, row+rowIndex, col+colIndex);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Arrays.deepHashCode(this.matrix);
        hash = 97 * hash + this.size;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Matrix other = (Matrix) obj;
        if (this.size != other.size) {
            return false;
        }
        if (!Arrays.deepEquals(this.matrix, other.matrix)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String str;
        DecimalFormat df = new DecimalFormat("#.00");
        str = "";
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                str += String.format("%5s ", this.matrix[row][col]);
            }
            str += "\n";
        }
        return "Matrix {\n" + str + "}";
    }

}
