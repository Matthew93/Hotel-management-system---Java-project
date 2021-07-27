package hotel.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {
	String phoneNumber;
	String email;
	String password;
	String roomNumber;
	String price;
	String available;

	public static boolean checkPhone(String phoneNumber) {
		Pattern phonePattern = Pattern.compile("^\\d{10}$");

		Matcher match = phonePattern.matcher(phoneNumber);

		return match.find() && match.group().equals(phoneNumber);

	}

	public static boolean checkEmail(String email) {
		Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

		Matcher match = emailPattern.matcher(email);

		return match.matches();
	}

	public static boolean checkPassword(String password) {
		Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[a-z]).{8,20}$");

		Matcher match = passwordPattern.matcher(password);

		return match.matches();
	}

	public static boolean checkRoomNumber(String roomNumber) {
		Pattern roomNumberPattern = Pattern.compile("^[A-Z]{1}+[0-9]{2}$");
		Matcher match = roomNumberPattern.matcher(roomNumber);

		return match.matches();
	}

	public static boolean checkAvailable(String available) {
		return available.equals("true") || available.equals("false");

	}

	public static boolean checkPrice(String price) {
		Pattern pricePattern = Pattern.compile("\\d+");
		Matcher match = pricePattern.matcher(price);
		return match.matches();

	}
}
