import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatrixCalculator {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("행렬 연산\n");
		
		boolean run = true;
		
		System.out.println("----- 초기 행렬 입력 -----");
		MatrixComplex mat1 = new MatrixComplex();
		mat1.nextMatrix();
		
		while (run) {
			
			System.out.println("------------------------------------------------------------------------------------------------------------------------");
			System.out.println("1. 더하기 | 2. 빼기 | 3. 곱하기 | 4. 기본 행 연산 | 5. 전치행렬 | 6. 상삼각행렬 | 7. 역행렬 출력 | 8. 행렬식 | 9. 출력 | 0. 종료");
			System.out.println("\n숫자 뒤에 +를 입력하면 연산 후 현재 행렬이 변경됩니다.");
			System.out.println("------------------------------------------------------------------------------------------------------------------------");
			System.out.print(">>> ");
			
			String numS = scanner.nextLine();
			boolean substitution = false;
			
			Pattern pattern = Pattern.compile("\\s*[1-7]\\s*\\+\\s*");
			Matcher matcher = pattern.matcher(numS);
			
			if (matcher.matches()) {
				substitution = true;
			}
			int num;
			try {
				numS = numS.replaceAll("[^0-9]", "");
				num = Integer.parseInt(numS);
				System.out.println(num);
			}
			catch (Exception e) {
				continue;
			}
			
			switch (num) {
				case 0 -> {
					run = false;
				}
				case 1 -> {
					MatrixComplex mat2 = mat1.returnThis(false);
					mat2.nextMatrix();
					System.out.println("------------------------------------------------------------------------------------------------------------------------\n");
					mat2 = mat1.add(mat2);
					mat2.print();
					if (substitution) {
						mat1 = mat2;
					}
				}
				case 2 -> {
					MatrixComplex mat2 = mat1.returnThis(false);
					mat2.nextMatrix();
					mat2 = mat1.substract(mat2);
					System.out.println("------------------------------------------------------------------------------------------------------------------------\n");
					mat2.print();
					if (substitution) {
						mat1 = mat2;
					}
				}
				case 3 -> {
					MatrixComplex mat2 = new MatrixComplex(mat1.c);
					mat2.nextMatrix();
					mat2 = mat1.product(mat2);
					System.out.println("------------------------------------------------------------------------------------------------------------------------\n");
					mat2.print();
					if (substitution) {
						mat1 = mat2;
					}
				}
				case 4 -> {
					boolean runR = true;
					while (runR) {
						System.out.println("------------------------------------------------------------------------------------------------------------------------");
						System.out.println("1. 행의 치환 | 2. 행의 상수곱 | 3. 행의 합 | 0. 종료");
						System.out.println("\n숫자 뒤에 +를 입력하면 연산 후 현재 행렬이 변경됩니다. (전 단계에서 입력시 불필요)");
						System.out.println("------------------------------------------------------------------------------------------------------------------------");
						System.out.print(" >>> ");
						
						String num6 = scanner.nextLine();
						boolean substitution6 = false;
						
						Pattern pattern6 = Pattern.compile("\\s*[1-3]\\s*\\+\\s*");
						Matcher matcher6 = pattern6.matcher(num6);
						
						if (matcher6.matches()) {
							substitution6 = true;
						}
						int numR;
						try {
							num6 = num6.replaceAll("[^1-30]", "");
							numR = Integer.parseInt(num6);
						}
						catch (Exception e) {
							continue;
						}
						
						switch (numR) {
							case 0 -> {
								runR = false;
							}
							case 1 -> {
								System.out.printf("변경할 행 > ");
								int r1 = scanner.nextInt();
								scanner.nextLine(); // nextInt() 후 버퍼에서 개행문자 지우기
								System.out.printf("변경할 행 > ");
								int r2 = scanner.nextInt();
								scanner.nextLine(); // nextInt() 후 버퍼에서 개행문자 지우기
								if (substitution6 || substitution) {
									mat1 = mat1.swap(r1, r2);
									mat1.print();
								}
								else {
									mat1.swap(r1, r2).print();
								}
							}
							case 2 -> {
								System.out.printf("변경할 행 > ");
								int r1 = scanner.nextInt();
								scanner.nextLine(); // nextInt() 후 버퍼에서 개행문자 지우기
								System.out.printf("곱할 값 > ");
								
								if (substitution6 || substitution) {
									mat1 = mat1.multiply(r1, new ComplexNumber(scanner.nextLine()));
									mat1.print();
								}
								else {
									mat1.multiply(r1, new ComplexNumber(scanner.nextLine())).print();
								}
							}
							case 3 -> {
								System.out.printf("변경할 행 > ");
								int r1 = scanner.nextInt();
								scanner.nextLine(); // nextInt() 후 버퍼에서 개행문자 지우기
								System.out.printf("더할 행 > ");
								int r2 = scanner.nextInt();
								scanner.nextLine(); // nextInt() 후 버퍼에서 개행문자 지우기
								System.out.printf("곱할 값 > ");
								
								if (substitution6 || substitution) {
									mat1 = mat1.addRow(r1, r2, new ComplexNumber(scanner.nextLine()));;
									mat1.print();
								}
								else {
									mat1.addRow(r1, r2, new ComplexNumber(scanner.nextLine())).print();
								}
							}
							default -> {
								System.out.println("\n목록에 있는 값을 입력해주세요.");
							}
						}
						substitution6 = false;
					}
				}
				case 5 -> {
					MatrixComplex transMat = mat1.transpose();
					transMat.print();
					if (substitution) {
						mat1 = transMat;
					}
				}
				case 6 -> {
					MatrixComplex triMat = mat1.returnThis(true);
					triMat = mat1.makeTriMat(false, false);
					triMat.print();
					if (substitution) {
						mat1 = triMat;
					}
				}
				case 7 -> {
					MatrixComplex inverseMat = new MatrixComplex(mat1.c, mat1.r);
					inverseMat = mat1.getInverseMat();
					inverseMat.print();
					if (substitution) {
						mat1 = inverseMat;
					}
				}
				case 8 -> {
					System.out.println("------------------------------------------------------------------------------------------------------------------------\n");
					System.out.print("행렬식 : ");
					mat1.determinant().print();
					System.out.println();
					System.out.println();
				}
				case 9 -> {
					System.out.println("------------------------------------------------------------------------------------------------------------------------\n");
					mat1.print();
				}
				default -> {
					System.out.println("\n목록에 있는 값을 입력해주세요.");
				}
			}
			substitution = false;
		}
		scanner.close();
	}

}
