public class Main {
  public static void main(String[] args) {
    System.out.println(sumNumbers("aa1122"));
  }

  public static int sumNumbers(String str) {
    boolean found = false;
    String num = "";
    int sum = 0;
    for (char ch : str.toCharArray()) {
      if (Character.isDigit(ch)) {
        found = true;
        num += ch;
      } else {
        if (found) {
          sum += Integer.parseInt(num);
          num = "";
          found = false;
        }
      }
    }
    if (num.length() > 0 && found) {
      sum += Integer.parseInt(num);
    }
    return sum;
  }
}
