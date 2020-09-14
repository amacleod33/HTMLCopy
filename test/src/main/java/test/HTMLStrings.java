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
        String copy = "";
        String[] stringToArr = input.split(",");
        int begin = Integer.parseInt(stringToArr[0]);
        int end = Integer.parseInt(stringToArr[1]);

        // if input is null
        if (begin == 0 && end == 0) {
            return "";
        }

        String[] stringArr = stringToArr[2].split("");
        ArrayList<String> messageArr = new ArrayList<String>();
        ArrayList<String> tagArr = new ArrayList<String>();

        // Finds start tags
        for (int i = 0; i < begin; i++) {
            if (stringArr[i].equals("<")) {
                if (stringArr[i + 1].equals("/")) {
                    continue;
                }
                while (!stringArr[i].equals(">")) {
                    copy += stringArr[i];
                    i++;
                }
                copy += ">";
                if (messageArr.contains(copy)) {
                    continue;
                }
                messageArr.add(copy);
                copy = "";

            }

        }

        // Finds message, including tags if it's in the message
        for (int i = begin; i < end; i++) {

            // finds tags
            if (stringArr[i].equals("<")) {
                // Loop to find end of tag
                while (!stringArr[i].equals(">")) {
                    copy += stringArr[i];
                    i++;
                }
                copy += ">";
                messageArr.add(copy);
                copy = "";
            }
            else {
                messageArr.add(stringArr[i]);

            }
        }

        copy = "";

        // Finds end tags

        // Guard if index starts at zero so loop will be applied
        if (begin == 0) {
            begin = end;
        }

        for (int i = 1; i < end; i++) {

            // Determines if there is a tag
            if (stringArr[i - 1].equals("<")) {
                if (stringArr[i].equals("/")) {
                    continue;
                }
                while (!stringArr[i].equals(">")) {
                    copy += stringArr[i];
                    i++;
                }
                copy += ">";

                // If the arrayList does not contain the end tag, this will add
                // it to the list of tags.
                if (!messageArr.contains("</" + copy)) {
                    tagArr.add("</" + copy);
                    copy = "";
                    continue;
                }
                if (i == end - 1) {
                    break;
                }

                copy = "";

            }

        }
        String temp = "<";

        // Guard for if indexes are whole string length
        if (begin == end && end == stringArr.length) {
            begin = 0;
        }
        // Finds if tags are closed before start of index
        for (int i = 0; i < begin; i++) {

            if (stringArr[i].equals("/")) {
                while (!stringArr[i + 1].equals(">")) {
                    temp += stringArr[i + 1];
                    i++;

                }
                temp += ">";
                // Removes element from arrayList if it's already in there
                if (messageArr.contains(temp)) {
                    messageArr.remove(temp);
                    temp = "<";
                }
            }
        }

        // Iterates through the arrayList to make it one string which can be
        // returned.
        for (String s : messageArr) {
            copy += s;
        }

        // This loop reverses the order of the tags in the final message so the
        // tags are in the correct format.
        for (int i = tagArr.size() - 1; i >= 0; i--) {

            // Does not add the tags at the end if already contained in the
            // message.
            if (end == stringArr.length) {
                break;
            }
            copy += tagArr.get(i);
        }
        return copy;
    }


    /**
     * Main method
     * 
     * @param args
     *            arguments
     */
    public static void main(String[] args) {
        System.out.println(copy("0,15,Testing<b>!</b>"));
    }
}
