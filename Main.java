package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    static String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static void main(String[] args) {
        while (true){

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            String type = scanner.nextLine();
            if(type.equals("/exit")){
                break;
            }
            String[] bases = type.split(" ");
            String sourceBase = bases[0];
            String targetBase = bases[1];
            while (true){
                System.out.print("Enter number in base " + sourceBase + " to convert to base " + targetBase + " (To go back type /back) ");
                String num = scanner.next();
                if(num.equals("/back")){
                    break;
                }
                String[] nums = num.split("\\.");
                if(nums.length == 1){
                    System.out.println("Conversion result: " + conversion(sourceBase, targetBase, nums[0]));
                }else if(nums.length == 2){
                    String frac = convertFractions(sourceBase,targetBase,nums[1]);
                    if(frac.length()>7){
                        System.out.println("Conversion result: " + conversion(sourceBase, targetBase, nums[0]) + "." + frac.substring(2, 7));
                    }else if(frac.length() < 7){
                        frac = frac.substring(2);
                        StringBuilder sb = new StringBuilder(frac);
                        for (int i = 0; i < 5 - frac.length(); i++){
                            sb.append("0");
                        }
                        frac = sb.toString();
                        System.out.println("Conversion result: " + conversion(sourceBase, targetBase, nums[0]) + "." + frac);
                    }
                    else System.out.println("Conversion result: " + conversion(sourceBase, targetBase, nums[0]) + "." + frac.substring(2, frac.length()));

                }
            }
        }
    }

    static String convertFractions(String sourceBase, String targetBase, String num){
        BigDecimal Num;
        try{
            if(new BigInteger(num).compareTo(BigInteger.ZERO) == 0){
                return "0.00000";
            }
        }catch (Exception e){

        }

        if(!sourceBase.equals("10")){
            Num = fracToDec(sourceBase, num);
        }else Num = new BigDecimal(num);
        if(!Objects.equals(targetBase, "10")){
            return fracDecToBase(targetBase, Num);
        }else return Num.toString();
    }

    static BigDecimal fracToDec(String sourceBase, String fraction){
        String[] fracs = fraction.split("");
        BigDecimal sum = BigDecimal.ZERO;
        x:
        for(int i = 0; i < fracs.length; i++){
            for (int t = 0; t < alphabet.length; t++){
                if(fracs[i].toUpperCase().equals(alphabet[t])){
                    sum = sum.add(new BigDecimal(t+10+"").divide(new BigDecimal(sourceBase).pow(i+1), 7,RoundingMode.DOWN));
                    continue x;
                }
            }
            sum = sum.add(new BigDecimal(fracs[i]).divide(new BigDecimal(sourceBase).pow(i+1), 7, RoundingMode.DOWN));
        }
        return sum;
    }

    static String fracDecToBase(String sourceBase, BigDecimal bigFraction){
        if(!bigFraction.toString().contains(".")){
            bigFraction = new BigDecimal("0." + bigFraction);
        }
        BigDecimal bigBase = new BigDecimal(sourceBase);
        BigDecimal res = bigFraction.multiply(bigBase);
        BigDecimal ost = res.divide(BigDecimal.ONE, 0, RoundingMode.DOWN);
        StringBuilder stringBuilder = new StringBuilder();
        if(ost.compareTo(new BigDecimal("9"))==1){
            stringBuilder.append(alphabet[Integer.parseInt(ost.toString()) - 10]);
        }else stringBuilder.append(ost);
        int counter = 0;
        while (true){
            if(res.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0){
                break;
            }else if (res.compareTo(BigDecimal.ONE) == 1){
                if(counter>6){
                    break;
                }
                res = res.remainder(BigDecimal.ONE);
                res = res.multiply(bigBase);
                ost = res.divide(BigDecimal.ONE, 0, RoundingMode.DOWN);
                if(ost.compareTo(new BigDecimal("9"))==1){
                    stringBuilder.append(alphabet[Integer.parseInt(ost.toString()) - 10]);
                }else stringBuilder.append(ost);
                counter++;
            }else if (res.compareTo(BigDecimal.ONE) == -1){
                if(counter>6){
                    break;
                }
                res = res.multiply(bigBase);
                ost = res.divide(BigDecimal.ONE, 0, RoundingMode.DOWN);
                if(ost.compareTo(new BigDecimal("9"))==1){
                    stringBuilder.append(alphabet[Integer.parseInt(ost.toString()) - 10]);
                }else stringBuilder.append(ost);
                counter++;
            }
        }
        stringBuilder.insert(0, "0.");
        return stringBuilder.toString();
    }

    static String conversion(String sourceBase, String targetBase, String num){
        BigInteger bigNum;
        if(!sourceBase.equals("10")){
            bigNum = toDecimal(Integer.parseInt(sourceBase), num);
        }else bigNum = new BigInteger(num);
        if(!Objects.equals(targetBase, "10")){
            return decToBase(bigNum, targetBase);
        }else return bigNum.toString();
    }

    static BigInteger toDecimal(int sourceBase, String source){
        BigInteger sum = BigInteger.ZERO ;
        String[] letters = source.split("");
        ArrayList<String> ll = new ArrayList<>();
        for (int i = letters.length-1; i >= 0; i--){
            ll.add(letters[i]);
        }
        x:
        for(int i = 0; i < letters.length; i++){
            int counter = 0;
            for (String x : alphabet){
                if(Objects.equals(ll.get(i).toUpperCase(), x)){
                    sum = sum.add((new BigInteger(String.valueOf(counter+10)).multiply(new BigInteger(String.valueOf(sourceBase)).pow(i))));
                    continue x;
                }
                counter++;
            }
            sum = sum.add(new BigInteger(ll.get(i)).multiply(new BigInteger(String.valueOf(sourceBase)).pow(i)));
        }
        return sum;
    }

    static String decToBase(BigInteger number, String base){
        StringBuilder sb = new StringBuilder();
        BigInteger bigBase = new BigInteger(base);

        BigInteger ost = number.remainder(bigBase);
        BigInteger res = number.divide(bigBase);
        if(ost.compareTo(new BigInteger("9"))==1){
            sb.append(alphabet[Integer.parseInt(ost.subtract(BigInteger.TEN).toString())]);
        }else {
            sb.append(ost);
        }
        while (res.compareTo(BigInteger.ZERO)!=0){
            ost = res.remainder(bigBase);
            res = res.divide(bigBase);
            if(ost.compareTo(new BigInteger("9"))==1){
                sb.append(alphabet[Integer.parseInt(ost.subtract(BigInteger.TEN).toString())]);
            }else {
                sb.append(ost);
            }
        }
        return sb.reverse().toString();
    }
}