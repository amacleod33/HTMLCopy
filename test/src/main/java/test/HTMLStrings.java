package test;

import java.util.ArrayList;
import java.util.List;

/**
 * HTML Copy editor PSP0
 * 
 * @author alex macleod
 * @version 1
 */
public class HTMLStrings {

    /**
     * Gets tags that are opened before beginning of indexes
     * 
     * @param begin
     *            Beginning index
     * @param end
     *            End index
     * @param stringArr
     *            List of characters from input array
     * @return Array containing all tags before starting index
     */
    private static
        List<String>
        getOpenTags(int begin, int end, String[] stringArr) {
        String copy = "";
        ArrayList<String> messageArr = new ArrayList<String>();
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
        return messageArr;
    }


    /**
     * Gets the message between start and end indexes, checks to see if tags are
     * already added to messageArr or not
     * 
     * @param begin
     *            Beginning index
     * @param end
     *            Ending index
     * @param messageArr
     *            Array of beginning tags
     * @param stringArr
     *            Array of string from input
     * @return Modified array of all beginning tags and end and message between
     *         indexes
     */
    private static List<String> getMessageBetweenTags(
        int begin,
        int end,
        List<String> messageArr,
        String[] stringArr) {
        String copy = "";
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
        return messageArr;
    }


    /**
     * Finds the end tags of all tags that were already opened.
     * 
     * @param begin
     *            Beginning index
     * @param end
     *            End index
     * @param stringArr
     *            Array of all characters from input
     * @param messageArr
     *            Array with message and beginning tags
     * @return Array of tags that should be added to end of message
     */
    private static List<String> getEndTags(
        int begin,
        int end,
        String[] stringArr,
        List<String> messageArr) {
        List<String> tagArr = new ArrayList<String>();
        String copy = "";
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
        return tagArr;
    }


    /**
     * Determines if tags have been closed before the starting index; if they
     * have been they're removed
     * 
     * @param begin
     *            Beginning index
     * @param end
     *            End index
     * @param stringArr
     *            Array of all characters in input
     * @param messageArr
     *            Array of all tags and message
     */
    private static void findClosedTagsBeforeStart(
        int begin,
        int end,
        String[] stringArr,
        List<String> messageArr) {
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
    }


    /**
     * Puts together messageArr and tagArr to form final string
     * 
     * @param begin
     *            Beginning index
     * @param end
     *            End index
     * @param length
     *            Length of input string
     * @param messageArr
     *            Array containing beginning tags and message between indexes
     * @param tagArr
     *            Array of end tags
     * @return Final string to be returned to user
     */
    private static String getFinalString(
        int begin,
        int end,
        int length,
        List<String> messageArr,
        List<String> tagArr) {
        String copy = "";
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
            if (end == length) {
                break;
            }
            copy += tagArr.get(i);
        }

        return copy;
    }


    /**
     * Copy editor
     * 
     * @param input
     *            csv of html code to copy
     * @return String to be copied from input
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
        List<String> messageArr = new ArrayList<String>();
        List<String> tagArr = new ArrayList<String>();

        // Finds open tags
        messageArr = getOpenTags(begin, end, stringArr);

        // Finds message, including tags if it's in the message
        messageArr = getMessageBetweenTags(begin, end, messageArr, stringArr);

        // Finds end tags
        tagArr = getEndTags(begin, end, stringArr, messageArr);

        // Finds if tags are closed before start of index
        findClosedTagsBeforeStart(begin, end, stringArr, messageArr);

        // Iterates through the arrayList to make it one string which can be
        // returned.
        copy = getFinalString(begin, end, stringArr.length, messageArr, tagArr);

        return copy;
    }


    /**
     * Main method for testing
     * 
     * @param args
     *            arguments
     */
    public static void main(String[] args) {
        System.out.println(copy("0,15,Testing<b>!</b>"));
    }
}
