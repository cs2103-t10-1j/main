package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lol.Constants;

public class IndexParser {
	/************* Attribute ***************/
	private String userInput;

	/************* Constructor ***************/
	public IndexParser(String userInput) {
		setUserInput(userInput);
	}

	/************* Accessor ***************/
	public String getUserInput() {
		return userInput;
	}

	/************* Mutator ***************/
	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}

	/************* Other methods ***************/
	/**
	 * Removes multiple spaces between words, leading and trailing spaces in the
	 * userInput
	 * 
	 * @return string without extra spaces
	 */
	public String cleanUp() {
		String input = getUserInput();
		input = input.trim();
		input = input.replaceAll(Constants.REGEX_ONE_OR_MORE_SPACES,
				Constants.SPACE);
		setUserInput(input);
		return input;
	}

	/**
	 * Removes multiple spaces between words, leading and trailing spaces
	 * 
	 * @param input
	 *            string to be cleaned up
	 * @return string without extra spaces
	 */
	public String cleanUp(String input) {
		input = input.trim();
		input = input.replaceAll("\\s+", " ");
		return input;
	}

	/**
	 * Removes multiple spaces between words, leading and trailing spaces for
	 * all strings in an array
	 * 
	 * @param input
	 *            array containing strings to be formatted
	 * @return array with formatted strings
	 */
	public String[] cleanUp(String[] input) {
		for (int i = 0; i < input.length; i++) {
			String temp = input[i];
			temp = temp.trim();
			temp = cleanUp(temp);
			input[i] = temp;
		}
		return input;
	}

	/**
	 * Removes the first word of the input and returns the resulting string
	 * 
	 * @param input
	 *            Input string
	 * @return Input string without the first word
	 */
	public String removeFirstWord(String input) {
		try {
			return input.split(" ", 2)[1];
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Removes the command name from the user input and returns the resulting
	 * string
	 * 
	 * @param input
	 *            Input string
	 * @return Input string without the command name
	 */
	public String removeCommandName(String userInput) {
		return removeFirstWord(userInput);
	}

	/**
	 * Returns an array of task indices for delete/done commands, counting from
	 * 1.
	 * 
	 * @param input
	 *            user input
	 * @return array of task indices. If any index is not an integer or no index
	 *         is found, null is returned.
	 */
	public int[] getTaskIndexArray() {
		try {
			String input = cleanUp();
			String inputWithoutCommand = cleanUp(removeFirstWord(input));

			/* individual indices are separated by space e.g delete 1 4 6 */
			int[] indexSeparatedBySpace = getIndexSeparatedBySpace(inputWithoutCommand);
			if (indexSeparatedBySpace != null) {
				return removeDuplicates(indexSeparatedBySpace);
			}

			/* individual indices are separated by comma e.g delete 1,4,6 */
			int[] indexSeparatedByComma = getIndexSeparatedByComma(inputWithoutCommand);
			if (indexSeparatedByComma != null) {
				return removeDuplicates(indexSeparatedByComma);
			}

			/*
			 * index ranges may be mixed with individual indexes and are
			 * separated by comma e.g delete 1-4, 6
			 */
			String[] indexRanges = getIndexRangesSeparatedByComma(inputWithoutCommand);
			ArrayList<Integer> indexesToDelete = new ArrayList<Integer>();

			for (String indexRange : indexRanges) {
				if (isInteger(indexRange)) {
					// single index
					indexesToDelete.add(Integer.parseInt(indexRange));
				} else {
					// index range
					int start = getStartIndex(indexRange);
					int end = getEndIndex(indexRange);

					while (start <= end) {
						indexesToDelete.add(start);
						start++;
					}
				}
			}

			int[] outputArr = removeDuplicates(buildIntArray(indexesToDelete));
			if (outputArr.length == 1 && outputArr[0] == -1) {
				return null;
			} else {
				return outputArr;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns an array of task indexes for an input where individual indices
	 * are separated by space. Example: delete 1 2 3 returns {1, 2, 3}
	 * 
	 * @param input
	 *            user input without command name. Example: 1 2 3
	 * @return array of task indices if input format is valid, else returns null
	 */
	public int[] getIndexSeparatedBySpace(String userInputWithoutCommand) {
		try {
			String input = cleanUp(userInputWithoutCommand);
			String[] words = input.split(" ");
			int[] index = new int[words.length];

			for (int i = 0; i < words.length; i++) {
				index[i] = Integer.parseInt(words[i].trim());
			}
			return index;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns an array of task indexes for an input where individual indices
	 * are separated by comma. Spaces before and after commas are ignored.
	 * Example: delete 1,2,3 returns {1, 2, 3}
	 * 
	 * @param input
	 *            user input without command name. Example: 1,2,3
	 * @return array of task indices if input format is valid, else returns null
	 */
	public int[] getIndexSeparatedByComma(String userInputWithoutCommand) {
		try {
			String input = cleanUp(userInputWithoutCommand);
			String[] words = input.split(",");
			int[] index = new int[words.length];

			for (int i = 0; i < words.length; i++) {
				index[i] = Integer.parseInt(words[i].trim());
			}
			return index;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns an array of task index ranges for an input where index ranges are
	 * separated by space. Example: delete 1-3, 4, 6 to 9 returns {1-3, 4, 6 to
	 * 9}
	 * 
	 * @param input
	 *            user input without command name. Example: 1-3, 4, 6 to 9
	 * @return array of task index ranges if input format is valid, else returns
	 *         null
	 */
	public String[] getIndexRangesSeparatedByComma(
			String userInputWithoutCommand) {
		try {
			String input = cleanUp(userInputWithoutCommand);
			return cleanUp(input.split(","));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns the start index of an index range. Example: 6 to 9 returns 6, 1-3
	 * returns 1
	 * 
	 * @param range
	 *            index range
	 * @return start index of the range
	 */
	public int getStartIndex(String range) {
		try {
			// replace all non-digits with space
			return getIndexSeparatedBySpace(range.replaceAll("\\D", " "))[0];
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Returns the starting index of an index range. Example: 6 to 9 returns 9,
	 * 1-3 returns 3
	 * 
	 * @param range
	 *            index range
	 * @return end index of the range
	 */
	public int getEndIndex(String range) {
		try {
			// replace all non-digits with space
			return getIndexSeparatedBySpace(range.replaceAll("\\D", " "))[1];
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Creates an integer array from an arraylist of integers
	 * 
	 * @param integersList
	 *            arraylist of integers
	 * @return array of integers
	 */
	public int[] buildIntArray(ArrayList<Integer> integersList) {
		int[] integersArray = new int[integersList.size()];
		int i = 0;
		for (Integer n : integersList) {
			integersArray[i++] = n;
		}
		return integersArray;
	}

	/**
	 * Checks whether a string is an integer
	 * 
	 * @param s
	 *            string to be checked
	 * @return true if the string is an integer, else false
	 */
	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Removes duplicate elements from an integer array
	 * 
	 * @param arr
	 *            array in which duplicates are to be removed
	 * @return array without duplicates sorted in ascending order
	 */
	public int[] removeDuplicates(int[] arr) {
		ArrayList<Integer> integers = new ArrayList<Integer>();
		int length = arr.length;

		Set<Integer> set = new HashSet<Integer>();

		for (int i = 0; i < length; i++) {
			set.add(arr[i]);
		}

		Iterator<Integer> it = set.iterator();
		while (it.hasNext()) {
			integers.add(it.next());
		}
		int[] intArray = buildIntArray(integers);
		Arrays.sort(intArray);
		return intArray;
	}
}
