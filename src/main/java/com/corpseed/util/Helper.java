package com.corpseed.util;

public class Helper {

    public static String[] findFirstAndLastName(String title) {
        String str[]=title.split(" ");
        String data[]=new String[2];
        String lastName="";
        if(str.length>2) {
            data[0] = str[0] + " " + str[1];
            for(int i=2;i<str.length;i++)lastName+=" "+str[i];
            data[1]=lastName;
        }else if(str.length>1){
            data[0] = str[0];
            data[1]=" "+str[1];
        }else{data[0]=title;data[1]="";}

       return data;
    }
}
