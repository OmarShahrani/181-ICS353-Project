package strassen;

/**
 *
 * @author omarm
 */
public class MatrixOperations {

    public Matrix subtract(Matrix A, Matrix B) {
        int size;
        size = A.size;
        Matrix C = new Matrix(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                C.push(A.get(row, col) - B.get(row, col), row, col);
            }
        }
        return C;
    }

    public Matrix add(Matrix A, Matrix B) {
        int size;
        size = A.size;
        Matrix C = new Matrix(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                C.push(A.get(row, col) + B.get(row, col), row, col);
            }
        }
        return C;
    }

    public Matrix mult(Matrix A, Matrix B, int n, String algorithm) {
        return mult(A, B, n, algorithm, 0);
    }

    public Matrix mult(Matrix A, Matrix B, int n, String algorithm, int base) {
        if (n < base) {
            throw new RuntimeException("Error: the base (" + base + ") is less than n (" + n + ")");
        }
        int size;
        size = (int) Math.pow(2, n);
        Matrix Product;
        Product = new Matrix(size);
        String switchStr = (base > 1) ? algorithm + "_G" : algorithm + "_" + base;
        switch (switchStr) {
            case "CLASSICAL_ITER_0":
                Product = classicalMultIter(A, B);
                break;
            case "CLASSICAL_RECURSIVE_0":
                Product = classicalMultRecursive(A, B, n);
                break;
            case "STRASSEN_0":
                Product = strassenMult0(A, B, n);
                break;
            case "STRASSEN_1":
                Product = strassenMult1(A, B, n);
                break;
            case "STRASSEN_G":
                Product = strassenMultG(A, B, n, base);
                break;
        }
        return Product;
    }

    private Matrix classicalMultIter(Matrix A, Matrix B) {
        int size = A.size;
        long sum;
        Matrix Product = new Matrix(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                sum = 0;
                for (int i = 0; i < size; i++) {
                    sum += A.get(row, i) * B.get(i, col);
                }
                Product.push(sum, row, col);
            }
        }
        return Product;
    }

    private Matrix classicalMultRecursive(Matrix A, Matrix B, int n) {
        if (n == 0) {
            Matrix Product;
            Product = new Matrix(1);
            long c11;
            c11 = A.get(0, 0) * B.get(0, 0);
            Product.push(c11, 0, 0);
            return Product;
        } else {
            Matrix B1, B2, B3, B4, B5, B6, B7, B8;
            B1 = getBlock("B1", A, B, n - 1);
            B2 = getBlock("B2", A, B, n - 1);
            B3 = getBlock("B3", A, B, n - 1);
            B4 = getBlock("B4", A, B, n - 1);
            B5 = getBlock("B5", A, B, n - 1);
            B6 = getBlock("B6", A, B, n - 1);
            B7 = getBlock("B7", A, B, n - 1);
            B8 = getBlock("B8", A, B, n - 1);

            return collectProduct(n, B1, B2, B3, B4, B5, B6, B7, B8);
        }
    }

    private Matrix strassenMult0(Matrix A, Matrix B, int n) {
        if (n == 0) {
            Matrix Product;
            Product = new Matrix(1);
            long c11;
            c11 = A.get(0, 0) * B.get(0, 0);
            Product.push(c11, 0, 0);
            return Product;
        } else {
            Matrix D1, D2, D3, D4, D5, D6, D7;
            D1 = getBlock("0D1", A, B, n - 1);
            D2 = getBlock("0D2", A, B, n - 1);
            D3 = getBlock("0D3", A, B, n - 1);
            D4 = getBlock("0D4", A, B, n - 1);
            D5 = getBlock("0D5", A, B, n - 1);
            D6 = getBlock("0D6", A, B, n - 1);
            D7 = getBlock("0D7", A, B, n - 1);

            return collectProduct(n, D1, D2, D3, D4, D5, D6, D7);
        }
    }

    private Matrix strassenMult1(Matrix A, Matrix B, int n) {
        if (n == 1) {
            Matrix Product;
            Product = new Matrix(2);
            long d1, d2, d3, d4, d5, d6, d7;
            d1 = (A.get(0, 0) + A.get(1, 1)) * (B.get(0, 0) + B.get(1, 1));
            d2 = (A.get(1, 0) + A.get(1, 1)) * B.get(0, 0);
            d3 = A.get(0, 0) * (B.get(0, 1) - B.get(1, 1));
            d4 = A.get(1, 1) * (B.get(1, 0) - B.get(0, 0));
            d5 = (A.get(0, 0) + A.get(0, 1)) * B.get(1, 1);
            d6 = (A.get(1, 0) - A.get(0, 0)) * (B.get(0, 0) + B.get(0, 1));
            d7 = (A.get(0, 1) - A.get(1, 1)) * (B.get(1, 0) + B.get(1, 1));

            Product.push(d1 + d4 - d5 + d7, 0, 0);
            Product.push(d3 + d5, 0, 1);
            Product.push(d2 + d4, 1, 0);
            Product.push(d1 - d2 + d3 + d6, 1, 1);
            return Product;
        } else {
            Matrix D1, D2, D3, D4, D5, D6, D7;
            D1 = getBlock("1D1", A, B, n - 1);
            D2 = getBlock("1D2", A, B, n - 1);
            D3 = getBlock("1D3", A, B, n - 1);
            D4 = getBlock("1D4", A, B, n - 1);
            D5 = getBlock("1D5", A, B, n - 1);
            D6 = getBlock("1D6", A, B, n - 1);
            D7 = getBlock("1D7", A, B, n - 1);

            return collectProduct(n, D1, D2, D3, D4, D5, D6, D7);
        }
    }

    private Matrix strassenMultG(Matrix A, Matrix B, int n, int base) {
        if (n == base) {
            return classicalMultIter(A, B);
        } else {
            Matrix D1, D2, D3, D4, D5, D6, D7;
            D1 = getBlock("D1", A, B, n - 1, base);
            D2 = getBlock("D2", A, B, n - 1, base);
            D3 = getBlock("D3", A, B, n - 1, base);
            D4 = getBlock("D4", A, B, n - 1, base);
            D5 = getBlock("D5", A, B, n - 1, base);
            D6 = getBlock("D6", A, B, n - 1, base);
            D7 = getBlock("D7", A, B, n - 1, base);

            return collectProduct(n, D1, D2, D3, D4, D5, D6, D7);
        }
    }

    private Matrix getBlock(String block, Matrix A, Matrix B, int n, int base) {
        Matrix Temp = null;
        int halfSize = (int) Math.pow(2, n);
        switch (block) {
            case "D1":
                Temp = strassenMultG(add(A.getRange(0, 0, halfSize), A.getRange(halfSize, halfSize, halfSize)), add(B.getRange(0, 0, halfSize), B.getRange(halfSize, halfSize, halfSize)), n, base);
                break;
            case "D2":
                Temp = strassenMultG(add(A.getRange(halfSize, 0, halfSize), A.getRange(halfSize, halfSize, halfSize)), B.getRange(0, 0, halfSize), n, base);
                break;
            case "D3":
                Temp = strassenMultG(A.getRange(0, 0, halfSize), subtract(B.getRange(0, halfSize, halfSize), B.getRange(halfSize, halfSize, halfSize)), n, base);
                break;
            case "D4":
                Temp = strassenMultG(A.getRange(halfSize, halfSize, halfSize), subtract(B.getRange(halfSize, 0, halfSize), B.getRange(0, 0, halfSize)), n, base);
                break;
            case "D5":
                Temp = strassenMultG(add(A.getRange(0, 0, halfSize), A.getRange(0, halfSize, halfSize)), B.getRange(halfSize, halfSize, halfSize), n, base);
                break;
            case "D6":
                Temp = strassenMultG(subtract(A.getRange(halfSize, 0, halfSize), A.getRange(0, 0, halfSize)), add(B.getRange(0, 0, halfSize), B.getRange(0, halfSize, halfSize)), n, base);
                break;

            case "D7":
                Temp = strassenMultG(subtract(A.getRange(0, halfSize, halfSize), A.getRange(halfSize, halfSize, halfSize)), add(B.getRange(halfSize, 0, halfSize), B.getRange(halfSize, halfSize, halfSize)), n, base);
                break;

        }
        return Temp;
    }

    private Matrix getBlock(String block, Matrix A, Matrix B, int n) {
        Matrix Temp = null;
        int halfSize = (int) Math.pow(2, n);
        switch (block) {
            case "B1":
                Temp = classicalMultRecursive(A.getRange(0, 0, halfSize), B.getRange(0, 0, halfSize), n);
                break;
            case "B2":
                Temp = classicalMultRecursive(A.getRange(0, halfSize, halfSize), B.getRange(halfSize, 0, halfSize), n);
                break;
            case "B3":
                Temp = classicalMultRecursive(A.getRange(0, 0, halfSize), B.getRange(0, halfSize, halfSize), n);
                break;
            case "B4":
                Temp = classicalMultRecursive(A.getRange(0, halfSize, halfSize), B.getRange(halfSize, halfSize, halfSize), n);
                break;
            case "B5":
                Temp = classicalMultRecursive(A.getRange(halfSize, 0, halfSize), B.getRange(0, 0, halfSize), n);
                break;
            case "B6":
                Temp = classicalMultRecursive(A.getRange(halfSize, halfSize, halfSize), B.getRange(halfSize, 0, halfSize), n);
                break;
            case "B7":
                Temp = classicalMultRecursive(A.getRange(halfSize, 0, halfSize), B.getRange(0, halfSize, halfSize), n);
                break;
            case "B8":
                Temp = classicalMultRecursive(A.getRange(halfSize, halfSize, halfSize), B.getRange(halfSize, halfSize, halfSize), n);
                break;

            case "0D1":
                Temp = strassenMult0(add(A.getRange(0, 0, halfSize), A.getRange(halfSize, halfSize, halfSize)), add(B.getRange(0, 0, halfSize), B.getRange(halfSize, halfSize, halfSize)), n);
                break;
            case "0D2":
                Temp = strassenMult0(add(A.getRange(halfSize, 0, halfSize), A.getRange(halfSize, halfSize, halfSize)), B.getRange(0, 0, halfSize), n);
                break;
            case "0D3":
                Temp = strassenMult0(A.getRange(0, 0, halfSize), subtract(B.getRange(0, halfSize, halfSize), B.getRange(halfSize, halfSize, halfSize)), n);
                break;
            case "0D4":
                Temp = strassenMult0(A.getRange(halfSize, halfSize, halfSize), subtract(B.getRange(halfSize, 0, halfSize), B.getRange(0, 0, halfSize)), n);
                break;
            case "0D5":
                Temp = strassenMult0(add(A.getRange(0, 0, halfSize), A.getRange(0, halfSize, halfSize)), B.getRange(halfSize, halfSize, halfSize), n);
                break;
            case "0D6":
                Temp = strassenMult0(subtract(A.getRange(halfSize, 0, halfSize), A.getRange(0, 0, halfSize)), add(B.getRange(0, 0, halfSize), B.getRange(0, halfSize, halfSize)), n);
                break;

            case "0D7":
                Temp = strassenMult0(subtract(A.getRange(0, halfSize, halfSize), A.getRange(halfSize, halfSize, halfSize)), add(B.getRange(halfSize, 0, halfSize), B.getRange(halfSize, halfSize, halfSize)), n);
                break;

            case "1D1":
                Temp = strassenMult1(add(A.getRange(0, 0, halfSize), A.getRange(halfSize, halfSize, halfSize)), add(B.getRange(0, 0, halfSize), B.getRange(halfSize, halfSize, halfSize)), n);
                break;
            case "1D2":
                Temp = strassenMult1(add(A.getRange(halfSize, 0, halfSize), A.getRange(halfSize, halfSize, halfSize)), B.getRange(0, 0, halfSize), n);
                break;
            case "1D3":
                Temp = strassenMult1(A.getRange(0, 0, halfSize), subtract(B.getRange(0, halfSize, halfSize), B.getRange(halfSize, halfSize, halfSize)), n);
                break;
            case "1D4":
                Temp = strassenMult1(A.getRange(halfSize, halfSize, halfSize), subtract(B.getRange(halfSize, 0, halfSize), B.getRange(0, 0, halfSize)), n);
                break;
            case "1D5":
                Temp = strassenMult1(add(A.getRange(0, 0, halfSize), A.getRange(0, halfSize, halfSize)), B.getRange(halfSize, halfSize, halfSize), n);
                break;
            case "1D6":
                Temp = strassenMult1(subtract(A.getRange(halfSize, 0, halfSize), A.getRange(0, 0, halfSize)), add(B.getRange(0, 0, halfSize), B.getRange(0, halfSize, halfSize)), n);
                break;

            case "1D7":
                Temp = strassenMult1(subtract(A.getRange(0, halfSize, halfSize), A.getRange(halfSize, halfSize, halfSize)), add(B.getRange(halfSize, 0, halfSize), B.getRange(halfSize, halfSize, halfSize)), n);
                break;

        }
        return Temp;
    }

    private Matrix collectProduct(int n, Matrix D1, Matrix D2, Matrix D3, Matrix D4, Matrix D5, Matrix D6, Matrix D7) {
        int blockLen = (int) Math.pow(2, n - 1);
        int size = (int) Math.pow(2, n);
        Matrix Product, C11, C12, C21, C22;
        Product = new Matrix(size);
        C11 = subtract(add(add(D1, D4), D7), D5);
        C12 = add(D3, D5);
        C21 = add(D2, D4);
        C22 = add(add(subtract(D1, D2), D3), D6);
        Product.put(C11, 0, 0);
        Product.put(C12, 0, blockLen);
        Product.put(C21, blockLen, 0);
        Product.put(C22, blockLen, blockLen);
        return Product;
    }
    
    private Matrix collectProduct(int n, Matrix B1, Matrix B2, Matrix B3, Matrix B4, Matrix B5, Matrix B6, Matrix B7, Matrix B8) {
        int blockLen = (int) Math.pow(2, n - 1);
        int size = (int) Math.pow(2, n);
        Matrix Product, C11, C12, C21, C22;
        Product = new Matrix(size);
        C11 = add(B1,B2);
        C12 = add(B3, B4);
        C21 = add(B5, B6);
        C22 = add(B7, B8);
        Product.put(C11, 0, 0);
        Product.put(C12, 0, blockLen);
        Product.put(C21, blockLen, 0);
        Product.put(C22, blockLen, blockLen);
        return Product;
    }
}
