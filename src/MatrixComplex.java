
import java.util.Scanner;

public class MatrixComplex {
		ComplexNumber[][] value;
		int r;
		int c;
		Scanner scanner = new Scanner(System.in);
		
		MatrixComplex(int r, int c) {
			this.r = r > 0 ? r : 1;
			this.c = c > 0 ? c : 1;
			this.value = new ComplexNumber[r][c];
			this.initalize();
		}
		MatrixComplex(int r) {
			this.r = r > 0 ? r : 1;
			System.out.print("열 입력 > ");
			int c = scanner.nextInt();
			scanner.nextLine(); // nextInt() 후 버퍼에서 개행문자 지우기
			this.c = c > 0 ? c : 1; 
			
			this.value = new ComplexNumber[this.r][this.c];
			this.initalize();
		}
		MatrixComplex() {
			System.out.print("행 입력 > ");
			int r = scanner.nextInt();
			scanner.nextLine(); // nextInt() 후 버퍼에서 개행문자 지우기
			this.r = r > 0 ? r : 1; 
			System.out.print("열 입력 > ");
			int c = scanner.nextInt();
			scanner.nextLine(); // nextInt() 후 버퍼에서 개행문자 지우기
			this.c = c > 0 ? c : 1; 
			
			this.value = new ComplexNumber[this.r][this.c];
			this.initalize();
		}
		MatrixComplex(ComplexNumber[][] mat) {
			this.r = mat.length;
			this.c = mat[0].length;
			this.value = mat;
		}
		MatrixComplex(ComplexNumber[] array, int c) { // c : 열의 갯수
			this((array.length / (c > 0 ? c : 1)) > 0 ? (array.length / (c > 0 ? c : 1)) : 1, c > 0 ? c : 1);
			if (array.length % c != 0) {
				System.out.println("값 손실이 있습니다.");
			}
			int count = 0;
			for (int i = 0; i < this.r; i++) {
				for (int j = 0; j < this.c; j++) {
					this.value[i][j] = array[count];
					count++;
				}
			}
		}
		
		private void initalize() {
			for (int i = 0; i < this.r; i++) {
				for (int j = 0; j < this.c; j++) {
					this.value[i][j] = new ComplexNumber(0, 0);
				}
			}
		}
		
		void nextMatrix() {
			for (int i = 0; i < this.r; i++) {
				for (int j = 0; j < this.c; j++) {
					System.out.print(i + "행 " + j + "열의 값 > ");
					String complexNumber = scanner.nextLine();
					this.value[i][j] = new ComplexNumber(complexNumber);
				}
			}
		}
		
		MatrixComplex add(MatrixComplex mat) {
			if (this.r != mat.r || this.c != mat.c) {
				System.out.println("두 행렬의 행의 갯수와 열의 갯수가 다릅니다.");
				return this;
			}
			
			MatrixComplex result = new MatrixComplex(this.r, this.c);
			
			for (int i = 0; i < r; i++) {
				for (int j = 0; j < c; j++) {
					result.value[i][j] = this.value[i][j].add(mat.value[i][j]);
				}
			}
			return result;
		}
				
		MatrixComplex substract(MatrixComplex mat) {
			if (this.r != mat.r || this.c != mat.c) {
				System.out.println("두 행렬의 행의 갯수와 열의 갯수가 다릅니다.");
				return this;
			}
			
			MatrixComplex result = new MatrixComplex(this.r, this.c);

			for (int i = 0; i < r; i++) {
				for (int j = 0; j < c; j++) {
					result.value[i][j] = this.value[i][j].substract(mat.value[i][j]);
				}
			}
			return result;
		}
		
		MatrixComplex product(MatrixComplex mat) {
			if (this.c != mat.r) {
				System.out.println("곱이 불가능합니다.(행렬의 열의 갯수와 피연산 행렬의 행의 갯수가 같아야 함)");
				return this;
			}
			MatrixComplex result = new MatrixComplex(this.r, mat.c);
			for (int a = 0; a < this.r; a++) {
				for (int c = 0; c < mat.c; c++) {
					for (int b = 0; b < this.c; b++) {
						result.value[a][c] = result.value[a][c].add(this.value[a][b].product(mat.value[b][c]));
					}
					
				}
			}
			return result;
		}
		
		ComplexNumber determinant() {
			if (this.r != this.c) {
				System.out.println("정사각행렬이 아닙니다.");
				return null;
			}
			switch (this.r) {
				case 1 : {return this.value[0][0];}
				default : {
					ComplexNumber result = new ComplexNumber();
					for (int i = 0; i < this.c; i++) {
						ComplexNumber[][] tempMat = new ComplexNumber[this.r-1][this.r-1];
						MatrixComplex tempMatC = new MatrixComplex(this.r-1, this.r-1);
						for (int j = 0; j < this.r - 1; j++) {
							for (int k = 0; k < this.c - 1; k++) {
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
			MatrixComplex result = new MatrixComplex(this.r, this.c);
			
			for (int i = 0; i < this.r; i++) {
				for (int j = 0; j < this.c; j++) {
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
			for (int i = 0; i < this.c; i++) {
				result.value[r][i] = this.value[r][i].product(value);
			}
			return result;
		}
		
		MatrixComplex multiply(int r, ComplexNumber value) {
			MatrixComplex result = this.returnThis(true);
			
			if (value.r == 0 && value.i == 0) {
				System.out.println("0으로 곱하지 못합니다.");
			}
			for (int i = 0; i < this.c; i++) {
				result.value[r][i] = this.value[r][i].product(value);
			}
			return result;
		}
		
		MatrixComplex addRow(int modifiedRow, int rowForAdding, double value) { // modifiedRow += rowForAdding * value
			MatrixComplex result = this.returnThis(true);
			
			for (int i = 0; i < this.c; i++) {
				result.value[modifiedRow][i] = this.value[modifiedRow][i].add(this.value[rowForAdding][i].product(value));
			}
			return result;
		}
		
		MatrixComplex addRow(int modifiedRow, int rowForAdding, ComplexNumber value) {
			MatrixComplex result = this.returnThis(true);
			
			for (int i = 0; i < this.c; i++) {
				result.value[modifiedRow][i] = this.value[modifiedRow][i].add(this.value[rowForAdding][i].product(value));
			}
			return result;
		}
		/* End 기본 행연산 */
		
		MatrixComplex makeTriMat(boolean makeOne, boolean ignoreSquare) {
			MatrixComplex temp = this.returnThis(true);
			if (temp.r != temp.c && ignoreSquare == false) {
				System.out.println("정사각행렬이 아닙니다.");
				return temp;
			}
			int loopNum = temp.r > temp.c ? temp.c : temp.r;
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
				for (int j = i + 1; j < temp.r; j++) {
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
			if (this.r == this.c) {
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
			MatrixComplex augMat = new MatrixComplex(this.r, 2 * this.c);
			for (int i = 0; i < this.r; i++) {
				augMat.value[i][this.r + i].r = 1;
				for (int j = 0; j < this.c; j++) {
					augMat.value[i][j] = this.value[i][j];
				}
			}
			
			augMat = augMat.makeTriMat(true, true);
			augMat = augMat.multiply(this.r - 1, augMat.value[this.r - 1][this.r - 1].multiInverse());
			for (int i = 1; i < this.c; i++) {
				for (int j = 0; j < i; j++) {
					augMat.addRow(j, i, augMat.value[j][i].changeSign());
				}
			}
			
			MatrixComplex resultMat = new MatrixComplex(this.c, this.r);
			for (int i = 0; i < this.c; i++) {
				for (int j = 0; j < this.r; j++) {
					resultMat.value[i][j] = augMat.value[i][j + this.c];
				}
			}
			return resultMat;
			
		}
		
		MatrixComplex transpose() {
			MatrixComplex resultMat = new MatrixComplex(this.c, this.r);
			for (int i = 0; i < this.r; i++) {
				for (int j = 0; j < this.c; j++) {
					resultMat.value[j][i] = this.value[i][j];
				}
			}
			return resultMat;
		}
		
		MatrixComplex returnThis(boolean valueToo) { // 행과 열이 같은 클래스를 반환 valueToo가 true면 값까지 반환
			MatrixComplex result = new MatrixComplex(this.r, this.c);
			if (!valueToo) {
				return result;
			}
			else {
				for (int i = 0; i < this.r; i++) {
					for (int j = 0; j < this.c; j++) {
						result.value[i][j] = this.value[i][j];
					}
				}
				return result;
			}
		}
		
		void print() {
			int maxLength = 0;
			for (int i = 0; i < this.r; i++) {
				for (int j = 0; j < this.c; j++) {
					maxLength = this.value[i][j].complexToString(1, 4, false).length() > maxLength ? this.value[i][j].complexToString(1, 4, false).length() : maxLength;
				}
			}
			
			for (int i = 0; i < this.r; i++) {
				for (int j = 0; j < this.c; j++) {
					this.value[i][j].print(maxLength + 5, 4);
				}
				System.out.println("\n");
			}
		}
}
