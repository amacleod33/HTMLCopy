package test;

import java.util.ArrayList;

/**
 * HTML Copy editor PSP0
 * 
 * @author alex macleod
 * @version 1
 */
public class HTMLStrings {

    /**
     * Copy editor
     * 
     * @param input
     *            csv of html code to copy
     * @return //
     */
    public static String copy(String input) {
        String message = "";
        String[] arr = input.split(",");
        int begin = Integer.parseInt(arr[0]);
        int end = Integer.parseInt(arr[1]);

        // if input is null
        if (begin == 0 && end == 0) {
            return "";
        }

        String[] strings = arr[2].split("");
        ArrayList<String> arrList = new ArrayList<String>();
        ArrayList<String> tagList = new ArrayList<String>();

        // Finds message between indexes
        for (int i = 0; i < begin; i++) {
            if (strings[i].equals("<")) {
                if (strings[i + 1].equals("/")) {
                    continue;
                }
                while (!strings[i].equals(">")) {
                    message += strings[i];
                    i++;
                }
                message += ">";
                if (arrList.contains(message)) {
                    continue;
                }
                arrList.add(message);
                message = "";

            }

        }

        // Finds start tags
        for (int i = begin; i < end; i++) {

            // finds tags
            if (strings[i].equals("<")) {
                // Loop to find end of tag
                while (!strings[i].equals(">")) {
                    message += strings[i];
                    i++;
                }
                message += ">";
                arrList.add(message);
                message = "";
            }
            else {
                arrList.add(strings[i]);

            }
        }

        message = "";

        // Finds end tags

        // Guard if index starts at zero so loop will be applied
        if (begin == 0) {
            begin = end;
        }

        for (int i = 1; i < end; i++) {

            if (strings[i - 1].equals("<")) {
                if (strings[i].equals("/")) {

                    continue;
                }
                while (!strings[i].equals(">")) {
                    message += strings[i];
                    i++;
                }
                message += ">";
                if (!arrList.contains("</" + message)) {
                    tagList.add("</" + message);
                    message = "";
                    continue;
                }
                if (i == end - 1) {
                    break;
                }

                message = "";

            }

        }
        String temp = "<";

        // Guard for if indexes are whole string length
        if (begin == end && end == strings.length) {
            begin = 0;
        }
        // Finds if tags are closed before start of index
        for (int i = 0; i < begin; i++) {

            if (strings[i].equals("/")) {
                while (!strings[i + 1].equals(">")) {
                    temp += strings[i + 1];
                    i++;

                }
                temp += ">";
                if (arrList.contains(temp)) {
                    arrList.remove(temp);
                    temp = "<";
                }
            }
        }

        for (String s : arrList) {
            message += s;
        }

        for (int i = tagList.size() - 1; i >= 0; i--) {
            if (end == strings.length) {
                break;
            }
            message += tagList.get(i);
        }
        return message;
    }


    public static void main(String[] args) {
        System.out.println(copy("0,15,Testing<b>!</b>"));
    }
}
