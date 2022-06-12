import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComplexNumber {
	double r;
	double i;
	private double epsilon = 1.0e-9;
	
	ComplexNumber(double r, double i) {
		this.r = r;
		this.i = i;
	}
	ComplexNumber(double r) {
		this(r, 0);
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
			this.r = Double.parseDouble(str);
			this.i = 0;
		}
		else if (matcher[1].matches()) { // i
			this.r = 0;
			this.i = 1;
		}
		else if (matcher[2].matches() || matcher[3].matches()) { // bi, ib
			this.r = 0;
			this.i = Double.parseDouble(str.replaceAll("(?i)i", ""));
		}
		else if (matcher[4].matches()) { // a + i
			String operator = matcher[4].group(1);
			String[] splitStr;
			if (operator.equals("+")) {
				splitStr = str.split("(?!^)\\" + operator);
				this.i = 1;
			}
			else {
				splitStr = str.split("(?!^)" + operator);				
				this.i = -1;
			}
			this.r = Double.parseDouble(splitStr[0]);
		}
		else if (matcher[5].matches() || matcher[6].matches()) { // a + bi, a + ib
			String operator = matcher[5].matches() ? matcher[5].group(1) : matcher[6].group(1);
			String[] splitStr;
			if (operator.equals("+")) {
				splitStr = str.split("(?!^)\\" + operator);
				this.i = Double.parseDouble(splitStr[1].replaceAll("(?i)i", ""));
			}
			else {
				splitStr = str.split("(?!^)" + operator);
				this.i = -Double.parseDouble(splitStr[1].replaceAll("(?i)i", ""));
			}
			this.r = Double.parseDouble(splitStr[0]);
		}
		else if (matcher[7].matches()) { // i + a
			String operator = matcher[7].group(1);
			String[] splitStr;
			if (operator.equals("+")) {
				splitStr = str.split("(?!^)\\" + operator);
				this.r = Double.parseDouble(splitStr[1]);
			}
			else {
				splitStr = str.split("(?!^)" + operator);
				this.r = -Double.parseDouble(splitStr[1]);
			}
			this.i = 1;
		}
		else if (matcher[8].matches() || matcher[9].matches()) { // bi + a, ib + a
			String operator = matcher[8].matches() ? matcher[8].group(1) : matcher[9].group(1);
			String[] splitStr;
			if (operator.equals("+")) {
				splitStr = str.split("(?!^)\\" + operator);
				this.r = Double.parseDouble(splitStr[1]);
			}
			else {
				splitStr = str.split("(?!^)" + operator);
				this.r = -Double.parseDouble(splitStr[1]);
			}
			this.i = Double.parseDouble(splitStr[0].replaceAll("(?i)i", ""));
		}
		else {
			System.out.println("잘못된 형식입니다. 0으로 입력됩니다.");
			this.r = 0;
			this.i = 0;
		}	
	}
	ComplexNumber() {
		this(0, 0);
	}
	
	ComplexNumber changeSign() {
		ComplexNumber result = new ComplexNumber(-this.r, -this.i);
		return result;
	}
	ComplexNumber add(ComplexNumber c) {
		ComplexNumber result = new ComplexNumber();
		result.r = this.r + c.r;
		result.i = this.i + c.i;
		return result.controlError();
	}
	ComplexNumber substract(ComplexNumber c) {
		ComplexNumber result = new ComplexNumber();
		result.r = this.r - c.r;
		result.i = this.i - c.i;
		return result.controlError();
	}
	ComplexNumber product(double r) {
		ComplexNumber result = new ComplexNumber();
		result.r = this.r * r;
		result.i = this.i * r;
		return result.controlError();
	}
	ComplexNumber product(ComplexNumber c) {
		ComplexNumber result = new ComplexNumber();
		result.r = this.r * c.r - this.i * c.i;
		result.i = this.r * c.i + this.i * c.r;
		return result.controlError();
	}
	ComplexNumber multiInverse() {
		return this.conjugate().divide(this.abs() * this.abs()).controlError();
	}
	ComplexNumber divide(double r) {
		ComplexNumber result = new ComplexNumber();
		result.r = this.r / r;
		result.i = this.i / r;
		return result.controlError();
	}
	ComplexNumber divide(ComplexNumber c) {
		ComplexNumber result = new ComplexNumber();
		result = this.product(c.conjugate()).divide(c.r * c.r + c.i * c.i);
		return result.controlError();
	}
	ComplexNumber conjugate() {
		ComplexNumber result = new ComplexNumber(this.r, -this.i);
		return result.controlError();
	}

	double abs() {
		return Math.sqrt(this.r * this.r + this.i * this.i);
	}
	private ComplexNumber ePow() {
		ComplexNumber result = new ComplexNumber();
		double ex = Math.pow(Math.E, this.r);
		result.r = ex * Math.cos(this.i);
		result.i = ex * Math.sin(this.i);
		return result.controlError();
	}
	ComplexNumber pow(double base) {
		double lnBase = Math.log(base);		
		ComplexNumber result = new ComplexNumber(this.r, this.i);
		result = result.product(lnBase);
		result = result.ePow();
		return result.controlError();
	}
	private ComplexNumber toPolar() {
		ComplexNumber result = new ComplexNumber();
		result.r = Math.sqrt(this.r * this.r + this.i * this.i);
		result.i = Math.atan(this.i / this.r);
		return result.controlError();
	}
	private ComplexNumber toCarte() {
		double r = this.r * Math.cos(this.i);
		double i = this.r * Math.sin(this.i);
		ComplexNumber result = new ComplexNumber(r, i);
		return result.controlError();
	}
	ComplexNumber sqrt() {
		ComplexNumber result = new ComplexNumber(this.r, this.i);
		result = result.toPolar();
		result.r = Math.sqrt(result.r);
		result.i /= 2;
		result = result.toCarte();
		return result.controlError();
	}
	boolean isZero() {
		return Math.abs(this.r) < this.epsilon && Math.abs(this.i) < this.epsilon;
	}
	boolean equal(ComplexNumber c) {
		return Math.abs(this.r - c.r) < this.epsilon && Math.abs(this.i - c.i) < this.epsilon;
	}
	boolean equal(double d) {
		return Math.abs(this.r - d) < this.epsilon && Math.abs(this.i) < this.epsilon;
	}
	private ComplexNumber controlError() {
		double r = Math.abs(this.r - Math.round(this.r)) < this.epsilon ? Math.round(this.r) : this.r;
		double i = Math.abs(this.i - Math.round(this.i)) < this.epsilon ? Math.round(this.i) : this.i;
		return new ComplexNumber(r, i);
	}
	String complexToString(int length, int decimalPlace, boolean truncate) {
		long rRound = Math.round(this.r);
		long iRound = Math.round(this.i);
		
		boolean rInt = Math.abs(this.r - rRound) < this.epsilon;
		boolean iInt = Math.abs(this.i - iRound) < this.epsilon;
		
		String rForm = rInt ? "d" : "." + decimalPlace + "f";
		String iForm = iInt ? "d" : "." + decimalPlace + "f";
		
		String result;
		String rResult = rInt ? String.format("%," + rForm, rRound) : String.format("%," + rForm, this.r);
		String iResult = iInt ? String.format("%," + iForm, iRound) : String.format("%," + iForm, this.i);
		if (Math.abs(this.i - 1) < this.epsilon || Math.abs(this.i + 1) < this.epsilon) {
			iResult = "";
		}
		
		if (Math.abs(this.i) < this.epsilon) {
			result = rResult;
		}
		else {
			if (Math.abs(this.r) < this.epsilon) {
				result = iResult + "i";
			}
			else {
				result = rResult + (this.i > 0 ? "+" : "") + iResult + "i";
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
}