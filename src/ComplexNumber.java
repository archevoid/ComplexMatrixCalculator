import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComplexNumber {
	private double real;
	private double image;
	private final double epsilon = 1.0e-9;
	
	ComplexNumber(double real, double image) {
		this.real = real;
		this.image = image;
	}
	ComplexNumber(double real) {
		this(real, 0);
	}
	ComplexNumber(String abc) {
		String str = abc.replaceAll("\\s", "");
		String[] regex = new String[12];
		regex[11] = "-?"; // 부호
		regex[10] = "([+-])"; // 연산자
		regex[0] = "(?:\\d+\\.\\d+|\\.\\d+|\\d+)"; // 실수
		regex[1] = "[iI]"; // i
		
		regex[2] = regex[0] + regex[1];
		regex[3] = regex[1] + regex[0];
		String commonStr1 = regex[0] + regex[10];
		String commonStr2 = regex[10] + regex[0];
		for (int i = 4; i < 7; i++) {
			regex[i] = commonStr1 + regex[i-3];
			regex[i+3] = regex[i-3] + commonStr2;
		}
		
		Pattern[] pattern = new Pattern[10];
		Matcher[] matcher = new Matcher[10];
		for (int i = 0; i < pattern.length; i++) {
			pattern[i] = Pattern.compile("^" + regex[11] + regex[i] + "$");
			matcher[i] = pattern[i].matcher(str);
		}
		if (matcher[0].matches()) { // 실수
			this.real = Double.parseDouble(str);
			this.image = 0;
		}
		else if (matcher[1].matches()) { // i
			this.real = 0;
			this.image = 1;
		}
		else if (matcher[2].matches() || matcher[3].matches()) { // bi, ib
			this.real = 0;
			this.image = Double.parseDouble(str.replaceAll("(?i)i", ""));
		}
		else if (matcher[4].matches()) { // a + i
			String operator = matcher[4].group(1);
			String[] splitStr;
			if (operator.equals("+")) {
				splitStr = str.split("(?!^)\\" + operator);
				this.image = 1;
			}
			else {
				splitStr = str.split("(?!^)" + operator);				
				this.image = -1;
			}
			this.real = Double.parseDouble(splitStr[0]);
		}
		else if (matcher[5].matches() || matcher[6].matches()) { // a + bi, a + ib
			String operator = matcher[5].matches() ? matcher[5].group(1) : matcher[6].group(1);
			String[] splitStr;
			if (operator.equals("+")) {
				splitStr = str.split("(?!^)\\" + operator);
				this.image = Double.parseDouble(splitStr[1].replaceAll("(?i)i", ""));
			}
			else {
				splitStr = str.split("(?!^)" + operator);
				this.image = -Double.parseDouble(splitStr[1].replaceAll("(?i)i", ""));
			}
			this.real = Double.parseDouble(splitStr[0]);
		}
		else if (matcher[7].matches()) { // i + a
			String operator = matcher[7].group(1);
			String[] splitStr;
			if (operator.equals("+")) {
				splitStr = str.split("(?!^)\\" + operator);
				this.real = Double.parseDouble(splitStr[1]);
			}
			else {
				splitStr = str.split("(?!^)" + operator);
				this.real = -Double.parseDouble(splitStr[1]);
			}
			this.image = 1;
		}
		else if (matcher[8].matches() || matcher[9].matches()) { // bi + a, ib + a
			String operator = matcher[8].matches() ? matcher[8].group(1) : matcher[9].group(1);
			String[] splitStr;
			if (operator.equals("+")) {
				splitStr = str.split("(?!^)\\" + operator);
				this.real = Double.parseDouble(splitStr[1]);
			}
			else {
				splitStr = str.split("(?!^)" + operator);
				this.real = -Double.parseDouble(splitStr[1]);
			}
			this.image = Double.parseDouble(splitStr[0].replaceAll("(?i)i", ""));
		}
		else {
			System.out.println("잘못된 형식입니다. 0으로 입력됩니다.");
			this.real = 0;
			this.image = 0;
		}	
	}
	ComplexNumber() {
		this(0, 0);
	}
	
	ComplexNumber changeSign() {
		ComplexNumber result = new ComplexNumber(-this.real, -this.image);
		return result;
	}
	ComplexNumber add(ComplexNumber c) {
		ComplexNumber result = new ComplexNumber();
		result.real = this.real + c.real;
		result.image = this.image + c.image;
		return result.controlError();
	}
	ComplexNumber substract(ComplexNumber c) {
		ComplexNumber result = new ComplexNumber();
		result.real = this.real - c.real;
		result.image = this.image - c.image;
		return result.controlError();
	}
	ComplexNumber product(double r) {
		ComplexNumber result = new ComplexNumber();
		result.real = this.real * r;
		result.image = this.image * r;
		return result.controlError();
	}
	ComplexNumber product(ComplexNumber c) {
		ComplexNumber result = new ComplexNumber();
		result.real = this.real * c.real - this.image * c.image;
		result.image = this.real * c.image + this.image * c.real;
		return result.controlError();
	}
	ComplexNumber multiInverse() {
		return this.conjugate().divide(this.abs() * this.abs()).controlError();
	}
	ComplexNumber divide(double r) {
		ComplexNumber result = new ComplexNumber();
		result.real = this.real / r;
		result.image = this.image / r;
		return result.controlError();
	}
	ComplexNumber divide(ComplexNumber c) {
		ComplexNumber result = new ComplexNumber();
		result = this.product(c.conjugate()).divide(c.real * c.real + c.image * c.image);
		return result.controlError();
	}
	ComplexNumber conjugate() {
		ComplexNumber result = new ComplexNumber(this.real, -this.image);
		return result.controlError();
	}

	double abs() {
		return Math.sqrt(this.real * this.real + this.image * this.image);
	}
	private ComplexNumber ePow() {
		ComplexNumber result = new ComplexNumber();
		double ex = Math.pow(Math.E, this.real);
		result.real = ex * Math.cos(this.image);
		result.image = ex * Math.sin(this.image);
		return result.controlError();
	}
	ComplexNumber pow(double base) {
		double lnBase = Math.log(base);		
		ComplexNumber result = new ComplexNumber(this.real, this.image);
		result = result.product(lnBase);
		result = result.ePow();
		return result.controlError();
	}
	private ComplexNumber toPolar() {
		ComplexNumber result = new ComplexNumber();
		result.real = Math.sqrt(this.real * this.real + this.image * this.image);
		result.image = Math.atan(this.image / this.real);
		return result.controlError();
	}
	private ComplexNumber toCarte() {
		double r = this.real * Math.cos(this.image);
		double i = this.real * Math.sin(this.image);
		ComplexNumber result = new ComplexNumber(r, i);
		return result.controlError();
	}
	ComplexNumber sqrt() {
		ComplexNumber result = new ComplexNumber(this.real, this.image);
		result = result.toPolar();
		result.real = Math.sqrt(result.real);
		result.image /= 2;
		result = result.toCarte();
		return result.controlError();
	}
	boolean isZero() {
		return Math.abs(this.real) < this.epsilon && Math.abs(this.image) < this.epsilon;
	}
	boolean equal(ComplexNumber c) {
		return Math.abs(this.real - c.real) < this.epsilon && Math.abs(this.image - c.image) < this.epsilon;
	}
	boolean equal(double d) {
		return Math.abs(this.real - d) < this.epsilon && Math.abs(this.image) < this.epsilon;
	}
	private ComplexNumber controlError() {
		double r = Math.abs(this.real - Math.round(this.real)) < this.epsilon ? Math.round(this.real) : this.real;
		double i = Math.abs(this.image - Math.round(this.image)) < this.epsilon ? Math.round(this.image) : this.image;
		return new ComplexNumber(r, i);
	}
	String complexToString(int length, int decimalPlace, boolean truncate) {
		long rRound = Math.round(this.real);
		long iRound = Math.round(this.image);
		
		boolean rInt = Math.abs(this.real - rRound) < this.epsilon;
		boolean iInt = Math.abs(this.image - iRound) < this.epsilon;
		
		String rForm = rInt ? "d" : "." + decimalPlace + "f";
		String iForm = iInt ? "d" : "." + decimalPlace + "f";
		
		String result;
		String rResult = rInt ? String.format("%," + rForm, rRound) : String.format("%," + rForm, this.real);
		String iResult = iInt ? String.format("%," + iForm, iRound) : String.format("%," + iForm, this.image);
		if (Math.abs(this.image - 1) < this.epsilon || Math.abs(this.image + 1) < this.epsilon) {
			iResult = "";
		}
		
		if (Math.abs(this.image) < this.epsilon) {
			result = rResult;
		}
		else {
			if (Math.abs(this.real) < this.epsilon) {
				result = iResult + "i";
			}
			else {
				result = rResult + (this.image > 0 ? "+" : "") + iResult + "i";
			}
		}
		if (result.length() > length && !truncate) {
			return result;
		}
		else {
			return String.format("%" + length + "s", result);
		}
	}
	void print() {
		this.print(4);
	}
	void print(int decimalPlace) {
		this.print(1, decimalPlace);
	}
	void print(int length, int decimalPlace) {
		System.out.print(this.complexToString(length, decimalPlace, false));
	}


	public double getReal() {
		return real;
	}

	public void setReal(double real) {
		this.real = real;
	}

	public double getImage() {
		return image;
	}

	public void setImage(double image) {
		this.image = image;
	}
}