
import java.util.Scanner;

public class MatrixComplex {
	private ComplexNumber[][] value;
	private int row;
	private int col;
	Scanner scanner = new Scanner(System.in);

	MatrixComplex(int row, int col) {
		this.row = row > 0 ? row : 1;
		this.col = col > 0 ? col : 1;
		this.value = new ComplexNumber[row][col];
		this.initalize();
	}
	MatrixComplex(int row) {
		this.row = row > 0 ? row : 1;
		System.out.print("열 입력 > ");
		int c = scanner.nextInt();
		scanner.nextLine(); // nextInt() 후 버퍼에서 개행문자 지우기
		this.col = c > 0 ? c : 1;

		this.value = new ComplexNumber[this.row][this.col];
		this.initalize();
	}
	MatrixComplex() {
		System.out.print("행 입력 > ");
		int r = scanner.nextInt();
		scanner.nextLine(); // nextInt() 후 버퍼에서 개행문자 지우기
		this.row = r > 0 ? r : 1;
		System.out.print("열 입력 > ");
		int c = scanner.nextInt();
		scanner.nextLine(); // nextInt() 후 버퍼에서 개행문자 지우기
		this.col = c > 0 ? c : 1;

		this.value = new ComplexNumber[this.row][this.col];
		this.initalize();
	}
	MatrixComplex(ComplexNumber[][] mat) {
		this.row = mat.length;
		this.col = mat[0].length;
		this.value = mat;
	}
	MatrixComplex(ComplexNumber[] array, int col) { // c : 열의 갯수
		this((array.length / (col > 0 ? col : 1)) > 0 ? (array.length / (col > 0 ? col : 1)) : 1, col > 0 ? col : 1);
		if (array.length % col != 0) {
			System.out.println("값 손실이 있습니다.");
		}
		int count = 0;
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				this.value[i][j] = array[count];
				count++;
			}
		}
	}

	private void initalize() {
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				this.value[i][j] = new ComplexNumber(0, 0);
			}
		}
	}

	void nextMatrix() {
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				System.out.print(i + "행 " + j + "열의 값 > ");
				String complexNumber = scanner.nextLine();
				this.value[i][j] = new ComplexNumber(complexNumber);
			}
		}
	}

	MatrixComplex add(MatrixComplex mat) {
		if (this.row != mat.row || this.col != mat.col) {
			System.out.println("두 행렬의 행의 갯수와 열의 갯수가 다릅니다.");
			return this;
		}

		MatrixComplex result = new MatrixComplex(this.row, this.col);

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				result.value[i][j] = this.value[i][j].add(mat.value[i][j]);
			}
		}
		return result;
	}

	MatrixComplex substract(MatrixComplex mat) {
		if (this.row != mat.row || this.col != mat.col) {
			System.out.println("두 행렬의 행의 갯수와 열의 갯수가 다릅니다.");
			return this;
		}

		MatrixComplex result = new MatrixComplex(this.row, this.col);

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				result.value[i][j] = this.value[i][j].substract(mat.value[i][j]);
			}
		}
		return result;
	}

	MatrixComplex product(MatrixComplex mat) {
		if (this.col != mat.row) {
			System.out.println("곱이 불가능합니다.(행렬의 열의 갯수와 피연산 행렬의 행의 갯수가 같아야 함)");
			return this;
		}
		MatrixComplex result = new MatrixComplex(this.row, mat.col);
		for (int a = 0; a < this.row; a++) {
			for (int c = 0; c < mat.col; c++) {
				for (int b = 0; b < this.col; b++) {
					result.value[a][c] = result.value[a][c].add(this.value[a][b].product(mat.value[b][c]));
				}

			}
		}
		return result;
	}

	ComplexNumber determinant() {
		if (this.row != this.col) {
			System.out.println("정사각행렬이 아닙니다.");
			return null;
		}
		switch (this.row) {
			case 1 : {return this.value[0][0];}
			default : {
				ComplexNumber result = new ComplexNumber();
				for (int i = 0; i < this.col; i++) {
					ComplexNumber[][] tempMat = new ComplexNumber[this.row -1][this.row -1];
					MatrixComplex tempMatC = new MatrixComplex(this.row -1, this.row -1);
					for (int j = 0; j < this.row - 1; j++) {
						for (int k = 0; k < this.col - 1; k++) {
							int tempcol = k >= i ? k + 1 : k;
							tempMat[j][k] = this.value[j + 1][tempcol];
						}
					}
					tempMatC.value = tempMat;
					if (i % 2 == 0) {
						result = result.add(this.value[0][i].product(tempMatC.determinant()));
					}
					else {
						result = result.substract(this.value[0][i].product(tempMatC.determinant()));
					}
				}
				return result;
			}
		}
	}
	/* 기본 행연산 */
	MatrixComplex swap(int r1, int r2) {
		MatrixComplex result = new MatrixComplex(this.row, this.col);

		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				if (i == r1) {
					result.value[i][j] = this.value[r2][j];
				}
				else if (i == r2) {
					result.value[i][j] = this.value[r1][j];
				}
				else {
					result.value[i][j] = this.value[i][j];
				}
			}
		}

		return result;
	}

	MatrixComplex multiply(int r, double value) {
		MatrixComplex result = this.returnThis(true);

		if (value == 0) {
			System.out.println("0으로 곱하지 못합니다.");
		}
		for (int i = 0; i < this.col; i++) {
			result.value[r][i] = this.value[r][i].product(value);
		}
		return result;
	}

	MatrixComplex multiply(int r, ComplexNumber value) {
		MatrixComplex result = this.returnThis(true);

		if (value.getReal() == 0 && value.getImage() == 0) {
			System.out.println("0으로 곱하지 못합니다.");
		}
		for (int i = 0; i < this.col; i++) {
			result.value[r][i] = this.value[r][i].product(value);
		}
		return result;
	}

	MatrixComplex addRow(int modifiedRow, int rowForAdding, double value) { // modifiedRow += rowForAdding * value
		MatrixComplex result = this.returnThis(true);

		for (int i = 0; i < this.col; i++) {
			result.value[modifiedRow][i] = this.value[modifiedRow][i].add(this.value[rowForAdding][i].product(value));
		}
		return result;
	}

	MatrixComplex addRow(int modifiedRow, int rowForAdding, ComplexNumber value) {
		MatrixComplex result = this.returnThis(true);

		for (int i = 0; i < this.col; i++) {
			result.value[modifiedRow][i] = this.value[modifiedRow][i].add(this.value[rowForAdding][i].product(value));
		}
		return result;
	}
	/* End 기본 행연산 */

	MatrixComplex makeTriMat(boolean makeOne, boolean ignoreSquare) {
		MatrixComplex temp = this.returnThis(true);
		if (temp.row != temp.col && ignoreSquare == false) {
			System.out.println("정사각행렬이 아닙니다.");
			return temp;
		}
		int loopNum = temp.row > temp.col ? temp.col : temp.row;
		int i = 0, findIndex = i;
		while (i < loopNum) {
			if (temp.value[findIndex][findIndex].isZero()) {
				findIndex++;
				if (findIndex >= loopNum) {
					i++;
					findIndex = i;
					continue;
				}
				continue;
			}
			if (i != findIndex) {
				temp = temp.swap(i, findIndex);
			}
			for (int j = i + 1; j < temp.row; j++) {
				if (!temp.value[j][i].isZero()) {
					temp = temp.addRow(j, i, temp.value[j][i].divide(temp.value[i][i].changeSign()));
				}
			}
			if (makeOne) {
				temp = temp.multiply(i, temp.value[i][i].multiInverse());
			}
			i++;
			findIndex = i;
		}
		return temp;
	}

	MatrixComplex getInverseMat() {
		/* 가역행렬 판단 */
		if (this.row == this.col) {
			if (this.determinant().equals(new ComplexNumber())) {
				System.out.println("가역행렬이 아닙니다.");
				return this;
			}
		}
		else {
			System.out.println("정사각행렬이 아닙니다.");
			return this;
		}

		/* 역행렬 계산(augmented matrix 이용) */
		MatrixComplex augMat = new MatrixComplex(this.row, 2 * this.col);
		for (int i = 0; i < this.row; i++) {
			augMat.value[i][this.row + i].setReal(1);
			for (int j = 0; j < this.col; j++) {
				augMat.value[i][j] = this.value[i][j];
			}
		}

		augMat = augMat.makeTriMat(true, true);
		augMat = augMat.multiply(this.row - 1, augMat.value[this.row - 1][this.row - 1].multiInverse());
		for (int i = 1; i < this.col; i++) {
			for (int j = 0; j < i; j++) {
				augMat.addRow(j, i, augMat.value[j][i].changeSign());
			}
		}

		MatrixComplex resultMat = new MatrixComplex(this.col, this.row);
		for (int i = 0; i < this.col; i++) {
			for (int j = 0; j < this.row; j++) {
				resultMat.value[i][j] = augMat.value[i][j + this.col];
			}
		}
		return resultMat;

	}

	MatrixComplex transpose() {
		MatrixComplex resultMat = new MatrixComplex(this.col, this.row);
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				resultMat.value[j][i] = this.value[i][j];
			}
		}
		return resultMat;
	}

	MatrixComplex returnThis(boolean valueToo) { // 행과 열이 같은 클래스를 반환 valueToo가 true면 값까지 반환
		MatrixComplex result = new MatrixComplex(this.row, this.col);
		if (!valueToo) {
			return result;
		}
		else {
			for (int i = 0; i < this.row; i++) {
				for (int j = 0; j < this.col; j++) {
					result.value[i][j] = this.value[i][j];
				}
			}
			return result;
		}
	}

	void print() {
		int maxLength = 0;
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				maxLength = this.value[i][j].complexToString(1, 4, false).length() > maxLength ? this.value[i][j].complexToString(1, 4, false).length() : maxLength;
			}
		}

		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				this.value[i][j].print(maxLength + 5, 4);
			}
			System.out.println("\n");
		}
	}


	public ComplexNumber[][] getValue() {
		return value;
	}

	public void setValue(ComplexNumber[][] value) {
		this.value = value;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
}
