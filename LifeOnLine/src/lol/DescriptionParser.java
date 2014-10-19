package lol;

public class DescriptionParser {
	private String userInput;

	public DescriptionParser(String userInput) {
		this.userInput = userInput;
	}

	public String getUserInput() {
		return userInput;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
	
	public String getDescription() {
		cleanUp();
		String input = getUserInput();
		
		LocationParser lp = new LocationParser(input);
		String inputWithoutLocation = lp.getUserInputWithoutLocation();
		
		DateParser datep = new DateParser(inputWithoutLocation);
		String inputWithoutLocationAndDate = datep.getUserInputWithoutDueDate();
		
		TimeParser tp = new TimeParser(inputWithoutLocationAndDate);
		String inputWithoutLocationDateAndTime = tp.getUserInputWithoutTime();
		
		return removeFirstWord(inputWithoutLocationDateAndTime).trim();
	}
	
	/**
	 * This method is called with user input without the command name and index.
	 * @return
	 */
	public String getDescriptionForEdit() {
		cleanUp();
		// user input without command name and index
		// for example, for input "edit 3 do something today", inputWithoutCommandAndIndex is "do something today"
		String inputWithoutCommandAndIndex = getUserInput();
		
		LocationParser lp = new LocationParser(inputWithoutCommandAndIndex);
		String inputWithoutLocation = lp.getUserInputWithoutLocation();
		
		DateParser datep = new DateParser(inputWithoutLocation);
		String inputWithoutLocationAndDate = datep.getUserInputWithoutDueDate();
		
		TimeParser tp = new TimeParser(inputWithoutLocationAndDate);
		String inputWithoutLocationDateAndTime = tp.getUserInputWithoutTime();
		
		if (inputWithoutLocationDateAndTime.isEmpty()) {
			return null;
		} else {
			return inputWithoutLocationDateAndTime.trim();
		}
	}
	
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
	
	public String removeFirstWord(String input) {
		try {
			return input.split(" ", 2)[1];
		} catch (Exception e) {
			return input;
		}	
	}
}
