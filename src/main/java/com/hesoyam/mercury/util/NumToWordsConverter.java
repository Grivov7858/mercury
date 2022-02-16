package com.hesoyam.mercury.util;

public class NumToWordsConverter {

    public static String convertAmountToWords(int numberLv, int numberSt) {
        String totalText = "";
        String newNumberLv = "";
        String newNumberSt = "";

        if(numberLv >= 2000 && numberLv <= 2999) {
            newNumberLv = NumberToText(numberLv).replaceFirst("два", "две") + " лв.";
        }

        else {
            newNumberLv = NumberToText(numberLv) + " лв.";
        }
        newNumberSt = NumberToTextStotinki(numberSt) + " ст.";

        if(newNumberSt.contains("един")){
            newNumberLv = newNumberLv.replaceFirst("един", "една");
        }
        if(newNumberSt.contains("два")){
            newNumberLv = newNumberLv.replaceFirst("два", "две");
        }

        if (newNumberSt.length() == 4) {
            totalText = newNumberLv;
        } else {
            totalText = newNumberLv + " и " + newNumberSt;
        }

        return totalText;
    }


    private static String NumberToText(int n) {
        if(n == 0) {
            return "";
        } else if(n>=1 && n<=19) {
            String[] arr = new String[] {
                    "един", "два", "три", "четири", "пет", "шест", "седем", "осем", "девет", "десет", "единадесет",
                    "дванадесет", "тринадесет","четиринадесет", "петнадесет", "шестнадесет", "седемнадесет", "осемнадесет", "деветнадесет"
            };
            return arr[n-1];
        } else if(n>=20 && n<=99) {
            String[] arr = new String[]{
                    "двадесет", "тридесет", "четиридесет", "петдесет", "шестдесет", "седемдесет", "осемдесет", "деветдесет"
            };
            if ((n%10)==0) return arr[n/10-2] + NumberToText(n%10);
            else return arr[n/10-2] + " и " + NumberToText(n%10);
        } else if(n>=100 && n<=199) {
            if (n%100==0) return "сто" + NumberToText(n%100);
            if (((n%100)<=19)||((n%10)==0)) return "сто и " + NumberToText(n%100);
            else return "сто " + NumberToText(n%100);
        } else if(n>=200 && n<=299) {
            if (n%100==0) return "двеста" + NumberToText(n%100);
            if (((n%100)<=19)||((n%10)==0)) return "двеста и " + NumberToText(n%100);
            else return "двеста " + NumberToText(n%100);
        } else if (n>=300 && n<=399) {
            if (n%100==0) return "триста" + NumberToText(n%100);
            if (((n%100)<=19)||((n%10)==0)) return "триста и " + NumberToText(n%100);
            else return "триста " + NumberToText(n%100);
        } else if (n>=400 && n<=999) {
            if (n%100==0) return NumberToText(n/100) + "стотин" + NumberToText(n%100);
            if (((n%100)<=19)||((n%10)==0)) return NumberToText(n/100) + "стотин и " + NumberToText(n%100);
            else return NumberToText(n/100) + "стотин " + NumberToText(n%100);
            // 1000 - 1999
        } else if (n>=1000 && n<=1999) {
            if (n%1000==0) return "хиляда " + NumberToText(n%1000);
            if (n%1000<=99) {
                if (((n%100)<=19)||((n%10)==0)) return "хиляда и " + NumberToText(n%1000);
                else return "хиляда " + NumberToText(n%1000);
            } else {
                if (n%100==0) return "хиляда и " + NumberToText(n%1000);
                else return "хиляда " + NumberToText(n%1000);
            }
            // 2000 - 999,999
        } else {
            if (n%1000==0) return NumberToText(n/1000) + " хиляди " + NumberToText(n%1000);
            if (n%1000<=99) {
                if (((n%100)<=19)||((n%10)==0)) return NumberToText(n/1000) + " хиляди и " + NumberToText(n%1000);
                else return NumberToText(n/1000) + " хиляди " + NumberToText(n%1000);
            } else {
                if (n%100==0) return NumberToText(n/1000) + " хиляди и " + NumberToText(n%1000);
                else return NumberToText(n/1000) + " хиляди " + NumberToText(n%1000);
            }
        }
    }

    private static String NumberToTextStotinki(int n) {
        if(n == 0) {
            return "";
        }
        else if(n>=1 && n<=19) {
            String[] arr = new String[] {
                    "една", "две", "три", "четири", "пет", "шест", "седем", "осем", "девет", "десет", "единадесет",
                    "дванадесет", "тринадесет","четиринадесет", "петнадесет", "шестнадесет", "седемнадесет", "осемнадесет", "деветнадесет"
            };
            return arr[n-1];
        }
        else {
            String[] arr = new String[]{
                    "двадесет", "тридесет", "четиридесет", "петдесет", "шестдесет", "седемдесет", "осемдесет", "деветдесет"
            };
            if ((n%10)==0) return arr[n/10-2] + NumberToText(n%10);
            else return arr[n/10-2] + " и " + NumberToText(n%10);
        }
    }

}