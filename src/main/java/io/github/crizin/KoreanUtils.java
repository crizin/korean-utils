package io.github.crizin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>Utility class providing a collection of static methods for Korean language text processing.</p>
 *
 * <p>This class offers a wide range of functionalities for handling Korean text, including:</p>
 *
 * <ul>
 *   <li>Text length calculation considering Korean character width</li>
 *   <li>String operations like contains, startsWith, and endsWith with Korean character support</li>
 *   <li>Conversion between composed and decomposed Korean characters</li>
 *   <li>Transliteration between Korean and English keyboard layouts</li>
 *   <li>Proper attachment of Korean particles (조사) to words</li>
 *   <li>N-gram generation for Korean text</li>
 * </ul>
 *
 * <p>KoreanUtils is designed to work seamlessly with other classes in this library, particularly {@link KoreanCharacter} and {@link Jamo},
 * to provide comprehensive support for Korean text processing tasks.</p>
 *
 * <p>Example:</p>
 *
 * <pre>
 * {@code
 * String text = "안녕하세요";
 * boolean containsKorean = KoreanUtils.containsKorean(text);
 * String decomposed = KoreanUtils.decompose(text);
 * }
 * </pre>
 *
 * <p>Note: This class cannot be instantiated and all methods are static.</p>
 *
 * @see KoreanCharacter
 * @see Jamo
 */
@SuppressWarnings({"java:S115", "NonAsciiCharacters"})
public class KoreanUtils {

	/**
	 * <p>Enum representing Korean particles (조사) that change based on the preceding word's final sound.</p>
	 *
	 * <p>In Korean grammar, certain particles change their form depending on whether the preceding word ends with a consonant or a vowel sound.
	 * This enum encapsulates those particle pairs and provides a method to select the appropriate form.</p>
	 *
	 * <p>The enum includes common particle pairs such as 은/는, 이/가, 을/를, etc.
	 * Each enum constant represents a specific particle pair and provides a detailed description of its usage.</p>
	 *
	 * <p>Usage example with {@link KoreanUtils#attachJosa(CharSequence, Josa)}:</p>
	 *
	 * <pre>
	 * {@code
	 * String result1 = KoreanUtils.attachJosa("사슴", KoreanUtils.Josa.은_는); //=> "사슴은"
	 * String result2 = KoreanUtils.attachJosa("사자", KoreanUtils.Josa.은_는); //=> "사자는"
	 * String result3 = KoreanUtils.attachJosa("Hazel", KoreanUtils.Josa.은_는); //=> "Hazel은"
	 * }
	 * </pre>
	 *
	 * <p>Note: This enum works in conjunction with the {@link KoreanUtils#attachJosa(CharSequence, Josa)} method,
	 * which handles the logic for determining whether a word ends with a consonant or vowel sound, including special cases for English words and numbers.</p>
	 *
	 * @see KoreanUtils#attachJosa(CharSequence, Josa)
	 */
	public enum Josa {
		/**
		 * Represents the subject marker particles "은" and "는".
		 * Used to indicate the subject of a sentence or to contrast with other subjects.
		 */
		은_는("은", "는"),

		/**
		 * Represents the subject marker particles "이" and "가".
		 * Used to indicate the subject of a sentence.
		 */
		이_가("이", "가"),

		/**
		 * Represents the object marker particles "을" and "를".
		 * Used to indicate the object of a verb.
		 */
		을_를("을", "를"),

		/**
		 * Represents the conjunction particles "과" and "와".
		 * Used to mean "and" or "with" when connecting nouns.
		 */
		과_와("과", "와"),

		/**
		 * Represents the instrumental particles "으로" and "로".
		 * Used to indicate means, direction, or transformation.
		 */
		으로_로("으로", "로"),

		/**
		 * Represents the vocative particles "아" and "야".
		 * Used when calling out to someone or something.
		 */
		아_야("아", "야");

		private final String whenEndsWithConsonant;
		private final String whenEndsWithVowel;

		/**
		 * Constructs a Josa enum constant with the specified particle forms.
		 *
		 * @param whenEndsWithConsonant The particle form used when the preceding word ends with a consonant
		 * @param whenEndsWithVowel The particle form used when the preceding word ends with a vowel
		 */
		Josa(final String whenEndsWithConsonant, final String whenEndsWithVowel) {
			this.whenEndsWithConsonant = whenEndsWithConsonant;
			this.whenEndsWithVowel = whenEndsWithVowel;
		}

		/**
		 * Selects the appropriate particle form based on whether the preceding word ends with a consonant.
		 *
		 * @param endsWithConsonant true if the preceding word ends with a consonant, false otherwise
		 *
		 * @return The appropriate particle form as a String
		 */
		public String find(final boolean endsWithConsonant) {
			return endsWithConsonant ? whenEndsWithConsonant : whenEndsWithVowel;
		}
	}

	/**
	 * <p>Mapping of lowercase English alphabet characters to their corresponding Korean Jamo when typed on a Korean keyboard.</p>
	 *
	 * <p>This array is used in the {@link #convertEnglishTypedToKorean(CharSequence)} method
	 * to convert English characters typed on a Korean keyboard layout to their intended Korean Jamo.</p>
	 *
	 * <p>For example, 'q' maps to 'ㅂ', 'w' maps to 'ㅈ', etc.</p>
	 */
	private static final char[] ALPHABET_KOREAN_LOWER_CASE_MAP = new char[] {
		'ㅁ', 'ㅠ', 'ㅊ', 'ㅇ', 'ㄷ', 'ㄹ', 'ㅎ', 'ㅗ', 'ㅑ', 'ㅓ', 'ㅏ', 'ㅣ', 'ㅡ', 'ㅜ', 'ㅐ', 'ㅔ', 'ㅂ', 'ㄱ', 'ㄴ', 'ㅅ', 'ㅕ', 'ㅍ', 'ㅈ', 'ㅌ', 'ㅛ', 'ㅋ'
	};

	/**
	 * <p>Mapping of uppercase English alphabet characters to their corresponding Korean Jamo when typed on a Korean keyboard.</p>
	 *
	 * <p>This array is used in the {@link #convertEnglishTypedToKorean(CharSequence)} method
	 * to convert uppercase English characters typed on a Korean keyboard layout to their intended Korean Jamo.</p>
	 *
	 * <p>For example, 'Q' maps to 'ㅃ', 'W' maps to 'ㅉ', etc. Note that some mappings result in double consonants.</p>
	 */
	private static final char[] ALPHABET_KOREAN_UPPER_CASE_MAP = new char[] {
		'ㅁ', 'ㅠ', 'ㅊ', 'ㅇ', 'ㄸ', 'ㄹ', 'ㅎ', 'ㅗ', 'ㅑ', 'ㅓ', 'ㅏ', 'ㅣ', 'ㅡ', 'ㅜ', 'ㅒ', 'ㅖ', 'ㅃ', 'ㄲ', 'ㄴ', 'ㅆ', 'ㅕ', 'ㅍ', 'ㅉ', 'ㅌ', 'ㅛ', 'ㅋ'
	};

	/**
	 * <p>Mapping of Korean Jamo to their corresponding English alphabet characters when typed on an English keyboard.</p>
	 *
	 * <p>This array is used in the {@link #convertKoreanTypedToEnglish(CharSequence)} method
	 * to convert Korean Jamo typed on an English keyboard layout to their intended English characters.</p>
	 *
	 * <p>Examples of mappings:</p>
	 *
	 * <ul>
	 *   <li>'ㅂ' maps to "q"</li>
	 *   <li>'ㅃ' maps to "Q"</li>
	 *   <li>'ㅈ' maps to "w"</li>
	 *   <li>'ㄲ' maps to "R" (note: single English character for this double consonant)</li>
	 * </ul>
	 *
	 * <p>Some compound consonants (겹자음) map to multiple English characters. For example:</p>
	 *
	 * <ul>
	 *   <li>'ㄳ' maps to "rt"</li>
	 *   <li>'ㄵ' maps to "sw"</li>
	 * </ul>
	 *
	 * <p>This mapping allows for accurate conversion of Korean text typed using an English keyboard layout.</p>
	 */
	private static final String[] KOREAN_ALPHABET_MAP = new String[] {
		"r", "R", "rt", "s", "sw", "sg", "e", "E", "f", "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a", "q", "Q", "qt", "t", "T", "d", "w", "W", "c", "z", "x",
		"v", "g", "k", "o", "i", "O", "j", "p", "u", "P", "h", "hk", "ho", "hl", "y", "n", "nj", "np", "nl", "b", "m", "ml", "l",
	};

	/**
	 * Private constructor to prevent instantiation of this utility class.
	 */
	private KoreanUtils() {
		throw new AssertionError("KoreanUtils is a utility class and should not be instantiated");
	}

	/**
	 * <p>Calculates the length of a text, applying a specified length for Korean characters.</p>
	 *
	 * <p>This method counts the length of the input text, treating non-Korean characters as length 1,
	 * and Korean characters as the specified {@code koreanLength}. This is useful in scenarios where Korean characters need to be weighted differently,
	 * such as when calculating string lengths for display purposes or when emulating byte-length calculations in certain encoding schemes.</p>
	 *
	 * <p>For example, this method can be used to emulate length calculations where non-Korean characters
	 * are considered 1 byte and Korean characters are considered 3 bytes.</p>
	 *
	 * <p>Note: This method uses {@link KoreanCharacter#isKoreanCharacter(Character)} to determine if a character is Korean.</p>
	 *
	 * <p>Examples:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanUtils.length(null, 3) //=> 0
	 * KoreanUtils.length("a", 3) //=> 1
	 * KoreanUtils.length("가", 3) //=> 3
	 * KoreanUtils.length("일이삼123", 3) //=> 12
	 * }
	 * </pre>
	 *
	 * @param text the input text whose length is to be calculated. Can be null.
	 * @param koreanLength the length to be assigned to each Korean character
	 *
	 * @return the calculated length of the text. Returns 0 for null or empty input.
	 *
	 * @see KoreanCharacter#isKoreanCharacter(Character)
	 */
	public static int length(final CharSequence text, final int koreanLength) {
		final int length = safeLength(text);

		if (length == 0) {
			return 0;
		}

		int size = 0;

		for (int i = 0; i < length; i++) {
			if (KoreanCharacter.isKoreanCharacter(text.charAt(i))) {
				size += koreanLength;
			} else {
				size++;
			}
		}

		return size;
	}

	/**
	 * <p>Checks if the given text contains the specified search text, considering Korean Jamo components.</p>
	 *
	 * <p>This method is similar to {@link String#contains(CharSequence)}, but it performs the search at the Korean Jamo (자모) level.
	 * This allows for more flexible searching in Korean text, including:</p>
	 *
	 * <ul>
	 *   <li>Initial consonant (초성) search</li>
	 *   <li>Partial Hangul syllable matching</li>
	 *   <li>Mixed Jamo and complete syllable searching</li>
	 * </ul>
	 *
	 * <p>The method considers a match if the Jamo components of the search text are found in order within the Jamo components of the main text.
	 * This enables powerful search capabilities for Korean text, such as searching with only consonants or partial syllables.</p>
	 *
	 * <p>Note: This method uses {@link KoreanCharacter} for Jamo-level comparisons.</p>
	 *
	 * <p>Examples:</p>
	 *
	 * <pre>
	 * {@code
	 * String text = "홍길동";
	 * KoreanUtils.contains(text, "홍길동") //=> true
	 * KoreanUtils.contains(text, "ㅎㄱㄷ") //=> true (Initial consonant search)
	 * KoreanUtils.contains(text, "ㅎ") //=> true (Single consonant search)
	 * KoreanUtils.contains(text, "홍ㄱ") //=> true (Mixed full syllable and Jamo search)
	 * KoreanUtils.contains(text, "길") //=> true (Partial syllable search)
	 * KoreanUtils.contains(text, "김") //=> false (No match)
	 * KoreanUtils.contains(text, null) //=> false (Null search text)
	 * KoreanUtils.contains(null, "홍") //=> false (Null main text)
	 * }
	 * </pre>
	 *
	 * @param text the text to search within. Can be null.
	 * @param queryText the text to search for. Can be null.
	 *
	 * @return {@code true} if the query text is found within the main text at the Jamo level,
	 * {@code false} otherwise. Returns {@code false} if either input is null.
	 *
	 * @see KoreanCharacter
	 */
	public static boolean contains(final CharSequence text, final CharSequence queryText) {
		final int textLength = safeLength(text);
		final int queryLength = safeLength(queryText);

		if (textLength == 0 || queryLength == 0 || queryLength > textLength) {
			return false;
		}

		List<KoreanCharacter> textCharacters = KoreanCharacter.convert(text);
		List<KoreanCharacter> queryCharacters = KoreanCharacter.convert(queryText);

		for (int i = 0; i <= textLength - queryLength; i++) {
			boolean found = true;

			for (int j = 0; j < queryText.length(); j++) {
				if (!textCharacters.get(i + j).include(queryCharacters.get(j))) {
					found = false;
					break;
				}
			}

			if (found) {
				return true;
			}
		}

		return false;
	}

	/**
	 * <p>Checks if the given text contains any Korean characters or Jamo.</p>
	 *
	 * <p>This method examines each character in the input text and returns true if it finds any Korean character (완성형 한글) or Jamo (자모).
	 * It can be used to quickly determine if a text contains any Korean writing.</p>
	 *
	 * <p>Examples:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanUtils.containsKorean("홍길동") //=> true
	 * KoreanUtils.containsKorean("ab길cd") //=> true
	 * KoreanUtils.containsKorean("ㄱㄴㄷ") //=> true
	 * KoreanUtils.containsKorean("abcd") //=> false
	 * KoreanUtils.containsKorean("") //=> false
	 * KoreanUtils.containsKorean(null) //=> false
	 * }
	 * </pre>
	 *
	 * @param text the text to check for Korean characters. Can be null.
	 *
	 * @return {@code true} if the text contains any Korean character or Jamo, {@code false} otherwise. Returns {@code false} for null or empty input.
	 *
	 * @see KoreanCharacter#isKoreanCharacter(Character)
	 */
	public static boolean containsKorean(final CharSequence text) {
		final int length = safeLength(text);

		if (length == 0) {
			return false;
		}

		for (int i = 0; i < length; i++) {
			if (KoreanCharacter.isKoreanCharacter(text.charAt(i))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * <p>Checks if the given text starts with the specified prefix, considering Korean Jamo components.</p>
	 *
	 * <p>This method is similar to {@link String#startsWith(String)}, but it performs the comparison at the Korean Jamo (자모) level.
	 * This allows for more flexible matching, including partial Hangul syllables and individual Jamo.</p>
	 *
	 * <p>Examples:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanUtils.startsWith("홍길동", "") //=> true
	 * KoreanUtils.startsWith("홍길동", "ㅎ") //=> true
	 * KoreanUtils.startsWith("홍길동", "호") //=> true
	 * KoreanUtils.startsWith("홍길동", "홍") //=> true
	 * KoreanUtils.startsWith("홍길동", "홍ㄱ") //=> true
	 * KoreanUtils.startsWith("홍길동", "ㅎ길") //=> false
	 * KoreanUtils.startsWith("홍길동", null) //=> false
	 * KoreanUtils.startsWith("홍길동", "길") //=> false
	 * }
	 * </pre>
	 *
	 * @param text the text to check. Can be null.
	 * @param prefix the prefix to look for. Can be null.
	 *
	 * @return {@code true} if the text starts with the prefix at the Jamo level, {@code false} otherwise. Returns {@code true} for null or empty prefix.
	 *
	 * @see KoreanCharacter#include(KoreanCharacter)
	 */
	public static boolean startsWith(final CharSequence text, final String prefix) {
		Optional<Boolean> condition = validateAffix(text, prefix);

		if (condition.isPresent()) {
			return condition.get();
		}

		final int prefixLength = safeLength(prefix);

		for (int i = 0; i < prefixLength; i++) {
			KoreanCharacter textChar = new KoreanCharacter(text.charAt(i));
			KoreanCharacter prefixChar = new KoreanCharacter(prefix.charAt(i));

			boolean isLast = (i == prefixLength - 1);

			if (isInvalidOnStartsWith(isLast, textChar.getChoseong(), prefixChar.getChoseong())
				|| isInvalidOnStartsWith(isLast, textChar.getJungseong(), prefixChar.getJungseong())
				|| isInvalidOnStartsWith(isLast, textChar.getJongseong(), prefixChar.getJongseong())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * <p>Checks if a Jamo component is invalid for the startsWith operation.</p>
	 *
	 * <p>This internal method is used by the {@link #startsWith(CharSequence, String)} method to determine
	 * if a specific Jamo component in the text and prefix match appropriately for the startsWith condition.</p>
	 *
	 * @param <T> The type of Jamo component (Choseong, Jungseong, or Jongseong)
	 * @param isLast Indicates if this is the last character being checked in the prefix
	 * @param textJamo The Jamo component from the text being checked
	 * @param prefixJamo The Jamo component from the prefix being matched
	 *
	 * @return {@code true} if the Jamo component is invalid for the startsWith condition, {@code false} otherwise
	 */
	private static <T extends Enum<T>> boolean isInvalidOnStartsWith(final boolean isLast, final Jamo<T> textJamo, final Jamo<T> prefixJamo) {
		return (textJamo == null && prefixJamo != null) || (textJamo != null && (textJamo != prefixJamo && (!isLast || prefixJamo != null)));
	}

	/**
	 * Checks if the given text ends with the specified suffix, considering Korean Jamo components.
	 *
	 * <p>This method is similar to {@link String#endsWith(String)}, but it performs the comparison at the Korean Jamo (자모) level.
	 * This allows for more flexible matching, including partial Hangul syllables and individual Jamo.</p>
	 *
	 * <p>Examples:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanUtils.endsWith("홍길동", "") //=> true
	 * KoreanUtils.endsWith("홍길동", "ㅇ") //=> true
	 * KoreanUtils.endsWith("홍길동", "동") //=> true
	 * KoreanUtils.endsWith("홍길동", "ㄹ동") //=> true
	 * KoreanUtils.endsWith("홍길동", "길동") //=> true
	 * KoreanUtils.endsWith("홍길동", "ㅇ길동") //=> true
	 * KoreanUtils.endsWith("홍길동", "홍길동") //=> true
	 * KoreanUtils.endsWith("홍길동", null) //=> false
	 * KoreanUtils.endsWith("홍길동", "길") //=> false
	 * }
	 * </pre>
	 *
	 * @param text the text to check. Can be null.
	 * @param suffix the suffix to look for. Can be null.
	 *
	 * @return {@code true} if the text ends with the suffix at the Jamo level, {@code false} otherwise. Returns {@code true} for null or empty suffix.
	 *
	 * @see KoreanCharacter#include(KoreanCharacter)
	 */
	public static boolean endsWith(final CharSequence text, final String suffix) {
		Optional<Boolean> condition = validateAffix(text, suffix);

		if (condition.isPresent()) {
			return condition.get();
		}

		final int suffixLength = safeLength(suffix);
		final int textLength = safeLength(text);

		for (int i = suffixLength - 1, j = textLength - 1; i >= 0; i--, j--) {
			KoreanCharacter textChar = new KoreanCharacter(text.charAt(j));
			KoreanCharacter suffixChar = new KoreanCharacter(suffix.charAt(i));
			Jamo.Choseong suffixChoseong = suffixChar.getChoseong();
			Jamo.Jongseong suffixJongseong = suffixChar.getJongseong();

			if (suffixChoseong != null && suffixChar.getJungseong() == null && suffixJongseong == null) {
				Jamo.Jongseong candidateJongseong = Jamo.Jongseong.find(suffixChoseong.getCompatibilityJamo());

				if (candidateJongseong != null) {
					suffixJongseong = candidateJongseong;
					suffixChoseong = null;
				}
			}

			boolean isFirst = (i == 0);

			if (isInvalidOnEndsWith(isFirst, textChar.getJongseong(), suffixJongseong)
				|| isInvalidOnEndsWith(isFirst, textChar.getJungseong(), suffixChar.getJungseong())
				|| isInvalidOnEndsWith(isFirst, textChar.getChoseong(), suffixChoseong)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * <p>Checks if a Jamo component is invalid for the endsWith operation.</p>
	 *
	 * <p>This internal method is used by the {@link #endsWith(CharSequence, String)} method to determine
	 * if a specific Jamo component in the text and suffix match appropriately for the endsWith condition.</p>
	 *
	 * @param <T> The type of Jamo component (Choseong, Jungseong, or Jongseong)
	 * @param isFirst Indicates if this is the first character being checked in the suffix
	 * @param textJamo The Jamo component from the text being checked
	 * @param suffixJamo The Jamo component from the suffix being matched
	 *
	 * @return {@code true} if the Jamo component is invalid for the endsWith condition, {@code false} otherwise
	 */
	private static <T extends Enum<T>> boolean isInvalidOnEndsWith(final boolean isFirst, final Jamo<T> textJamo, final Jamo<T> suffixJamo) {
		return (textJamo == null && suffixJamo != null) || (textJamo != null && (textJamo != suffixJamo && (!isFirst || suffixJamo != null)));
	}

	/**
	 * <p>Validates the affix (prefix or suffix) for startsWith and endsWith operations.</p>
	 *
	 * <p>This internal method is used by both {@link #startsWith(CharSequence, String)} and {@link #endsWith(CharSequence, String)}
	 * to perform initial validation checks on the text and affix. It handles null inputs, empty strings, and length comparisons.</p>
	 *
	 * @param text The main text being checked. Can be null.
	 * @param affix The affix (prefix or suffix) being validated. Can be null.
	 *
	 * @return An Optional containing a Boolean result if validation determines a definite outcome,
	 * or an empty Optional if further checking is required
	 */
	private static Optional<Boolean> validateAffix(final CharSequence text, final CharSequence affix) {
		if (affix == null) {
			return Optional.of(false);
		}

		final int suffixLength = safeLength(affix);

		if (suffixLength == 0) {
			return Optional.of(true);
		}

		final int textLength = safeLength(text);

		if (textLength == 0 || suffixLength > textLength) {
			return Optional.of(false);
		}

		return Optional.empty();
	}

	/**
	 * <p>Composes a string of decomposed Korean characters into composed form.</p>
	 *
	 * <p>This method takes a string that may contain decomposed Korean characters (Jamo) and combines them into composed Korean syllables where possible.
	 * Non-Korean characters are left unchanged.</p>
	 *
	 * <p>Examples:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanUtils.compose("ㄷㅏㄺㄱㅗㄱㅣ") //=> "닭고기"
	 * KoreanUtils.compose("ㅎㅏㄴㅏ") //=> "하나"
	 * KoreanUtils.compose("닭고기") //=> "닭고기"
	 * KoreanUtils.compose("ab cd") //=> "ab cd"
	 * KoreanUtils.compose("") //=> ""
	 * KoreanUtils.compose(null) //=> ""
	 * }
	 * </pre>
	 *
	 * @param decomposedText the text containing decomposed Korean characters. Can be null.
	 *
	 * @return the composed string, or an empty string if the input is null.
	 *
	 * @see #decompose(CharSequence)
	 */
	public static String compose(final CharSequence decomposedText) {
		final int length = safeLength(decomposedText);

		if (length == 0) {
			return "";
		}

		final StringBuilder sb = new StringBuilder(length / 2);
		KoreanCharacter koreanCharacter = new KoreanCharacter();

		for (int i = 0; i < length; i++) {
			final char c = decomposedText.charAt(i);

			try {
				koreanCharacter = koreanCharacter.compose(c);
			} catch (KoreanCharacter.ComposeException e) {
				Optional.ofNullable(e.getCompletedCharacter().getCharacter()).ifPresent(sb::append);
				koreanCharacter = e.getNextCharacter();
			}
		}

		Optional.ofNullable(koreanCharacter.getCharacter()).ifPresent(sb::append);

		return sb.toString();
	}

	/**
	 * <p>Decomposes a string of Korean characters into their Jamo components.</p>
	 *
	 * <p>This method is equivalent to calling {@code decompose(composedText, true, true)}.</p>
	 *
	 * @param composedText the text to decompose. Can be null.
	 *
	 * @return the decomposed string, or an empty string if the input is null.
	 *
	 * @see #decompose(CharSequence, boolean, boolean)
	 * @see #compose(CharSequence)
	 */
	public static String decompose(final CharSequence composedText) {
		return decompose(composedText, true, true);
	}

	/**
	 * <p>Decomposes a string of Korean characters into their Jamo components with specific options.</p>
	 *
	 * <p>This method takes a string that may contain composed Korean syllables and decomposes them into Jamo (자모) based on the specified options.
	 * Non-Korean characters are left unchanged.</p>
	 *
	 * <p>Examples:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanUtils.decompose("닭고기", true, true) //=> "ㄷㅏㄹㄱㄱㅗㄱㅣ"
	 * KoreanUtils.decompose("닭고기", true, false) //=> "ㄷㅏㄺㄱㅗㄱㅣ"
	 * KoreanUtils.decompose("닭고기", false, true) //=> "달ᆨ고기"
	 * KoreanUtils.decompose("닭고기", false, false) //=> "닭고기"
	 * KoreanUtils.decompose("ab cd") //=> "ab cd"
	 * KoreanUtils.decompose("") //=> ""
	 * KoreanUtils.decompose(null) //=> ""
	 * }
	 * </pre>
	 *
	 * @param composedText the text to decompose. Can be null.
	 * @param useCompatibilityJamo if true, uses compatibility Jamo characters; if false, uses conjoining Jamo.
	 * @param separateDoubleConsonantAndVowel if true, separates double consonants and compound vowels into their components.
	 *
	 * @return the decomposed string, or an empty string if the input is null.
	 *
	 * @see #compose(CharSequence)
	 */
	public static String decompose(final CharSequence composedText, final boolean useCompatibilityJamo, final boolean separateDoubleConsonantAndVowel) {
		final int length = safeLength(composedText);

		if (length == 0) {
			return "";
		}

		final StringBuilder sb = new StringBuilder(length * 3);

		for (int i = 0; i < length; i++) {
			final char c = composedText.charAt(i);

			if (KoreanCharacter.isKoreanCharacter(c)) {
				sb.append(new KoreanCharacter(c).decompose(useCompatibilityJamo, separateDoubleConsonantAndVowel));
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * <p>Converts text typed using an English keyboard layout to its Korean equivalent.</p>
	 *
	 * <p>This method transforms text that was typed on an English keyboard as if it were typed on a Korean keyboard.
	 * It's useful for correcting text entered without switching the keyboard layout.</p>
	 *
	 * <p>The conversion uses the following mapping:</p>
	 *
	 * <ul>
	 *   <li>Lowercase English letters are mapped using {@code ALPHABET_KOREAN_LOWER_CASE_MAP}</li>
	 *   <li>Uppercase English letters are mapped using {@code ALPHABET_KOREAN_UPPER_CASE_MAP}</li>
	 * </ul>
	 *
	 * <p>Examples:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanUtils.convertEnglishTypedToKorean("ghdrlfehd") //=> "홍길동"
	 * KoreanUtils.convertEnglishTypedToKorean("RKCL") //=> "까치"
	 * KoreanUtils.convertEnglishTypedToKorean("Hello") //=> "ㅗ디ㅣㅐ"
	 * }
	 * </pre>
	 *
	 * @param englishTypedText the text typed using an English keyboard layout. Can be null.
	 *
	 * @return the converted Korean text, or an empty string if the input is null.
	 *
	 * @see #convertKoreanTypedToEnglish(CharSequence)
	 */
	public static String convertEnglishTypedToKorean(final CharSequence englishTypedText) {
		String decomposedText = englishTypedText.toString().chars()
			.mapToObj(c -> {
				if (c >= 'a' && c <= 'z') {
					return ALPHABET_KOREAN_LOWER_CASE_MAP[c - 'a'];
				} else if (c >= 'A' && c <= 'Z') {
					return ALPHABET_KOREAN_UPPER_CASE_MAP[c - 'A'];
				} else {
					return (char) c;
				}
			})
			.map(String::valueOf)
			.collect(Collectors.joining());

		return compose(decomposedText);
	}

	/**
	 * <p>Converts text typed using a Korean keyboard layout to its English equivalent.</p>
	 *
	 * <p>This method transforms text that was typed on a Korean keyboard as if it were typed on an English keyboard.
	 * It's useful for retrieving the original English input when the keyboard layout was mistakenly set to Korean.</p>
	 *
	 * <p>The conversion uses the {@code KOREAN_ALPHABET_MAP} for mapping Korean Jamo to English characters.</p>
	 *
	 * <p>Examples:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanUtils.convertKoreanTypedToEnglish("홍길동") //=> "ghdrlfehd"
	 * KoreanUtils.convertKoreanTypedToEnglish("까치") //=> "Rkcl"
	 * KoreanUtils.convertKoreanTypedToEnglish("ㅗ디ㅣㅐ") //=> "hello"
	 * }
	 * </pre>
	 *
	 * @param koreanTypedText the text typed using a Korean keyboard layout. Can be null.
	 *
	 * @return the converted English text, or an empty string if the input is null.
	 *
	 * @see #convertEnglishTypedToKorean(CharSequence)
	 */
	public static String convertKoreanTypedToEnglish(final CharSequence koreanTypedText) {
		return decompose(koreanTypedText, true, false).chars()
			.mapToObj(c -> (c < 'ㄱ' || c > 'ㅣ') ? c : KOREAN_ALPHABET_MAP[c - 'ㄱ'])
			.map(String::valueOf)
			.collect(Collectors.joining());
	}

	/**
	 * <p>Attaches the appropriate Korean particle (조사) to the given text based on its ending sound.</p>
	 *
	 * <p>This method determines the correct particle form (은/는, 이/가, etc.) to attach to the given text by analyzing its final character.
	 * It considers various cases including:</p>
	 *
	 * <ul>
	 *   <li>Korean syllables with or without final consonants</li>
	 *   <li>English words (considering their pronunciation)</li>
	 *   <li>Numbers (converting them to Korean pronunciation first)</li>
	 *   <li>Special cases like words ending with 'L' in English</li>
	 * </ul>
	 *
	 * <p>The method handles null input by returning an empty string.</p>
	 *
	 * <p>Examples:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanUtils.attachJosa("사슴", KoreanUtils.Josa.은_는) // Returns "사슴은"
	 * KoreanUtils.attachJosa("사자", KoreanUtils.Josa.은_는) // Returns "사자는"
	 * KoreanUtils.attachJosa("TV", KoreanUtils.Josa.은_는)   // Returns "TV는"
	 * KoreanUtils.attachJosa("1234", KoreanUtils.Josa.은_는) // Returns "1234는"
	 * KoreanUtils.attachJosa("Hazel", KoreanUtils.Josa.은_는) // Returns "Hazel은"
	 * }
	 * </pre>
	 *
	 * @param text The text to which the particle should be attached. Can be null.
	 * @param josa The {@link KoreanUtils.Josa} enum representing the particle pair to use.
	 *
	 * @return A string with the appropriate particle attached, or an empty string if the input is null.
	 *
	 * @see KoreanUtils.Josa
	 */
	public static String attachJosa(final CharSequence text, final Josa josa) {
		final int length = safeLength(text);

		if (length == 0) {
			return "";
		}

		char c = text.charAt(length - 1);

		if ('0' <= c && c <= '9') {
			boolean endsWithConsonant = c == '0' || c == '1' || c == '3' || c == '6' || c == '7' || c == '8';
			return text + josa.find(endsWithConsonant);
		} else {
			char lowerCased = Character.toLowerCase(c);

			if ('a' <= lowerCased && lowerCased <= 'z') {
				boolean endsWithConsonant = lowerCased == 'b' || lowerCased == 'c' || lowerCased == 'k' || lowerCased == 'l'
											|| lowerCased == 'm' || lowerCased == 'n' || lowerCased == 'p' || lowerCased == 't';
				return text + josa.find(endsWithConsonant);
			}
		}

		if (KoreanCharacter.isKoreanCharacter(c)) {
			KoreanCharacter koreanCharacter = new KoreanCharacter(c);
			return text + josa.find(koreanCharacter.getJongseong() != null);
		}

		return text + josa.find(false);
	}

	/**
	 * <p>Generates n-grams from Korean text based on Jamo (자모) decomposition.</p>
	 *
	 * <p>This method creates n-grams by first decomposing Korean characters into their constituent Jamo (consonants and vowels)
	 * and then generating n-grams from these components. This approach allows for more flexible and detailed matching in Korean text search scenarios.</p>
	 *
	 * <p>The method treats spaces as word boundaries and generates n-grams for each word separately.</p>
	 *
	 * <p>Example:</p>
	 *
	 * <pre>
	 * {@code
	 * List<String> result = KoreanUtils.ngram("안녕하세요", 3);
	 *   // Result might include: "ㅇㅏㄴ", "ㅏㄴㄴ", "ㄴㄴㅕ", "ㄴㅕㅇ", ...
	 * }
	 * </pre>
	 *
	 * <p>Note: This method uses the default setting for separating double consonants and vowels.
	 * For more control over this behavior, see the overloaded method with additional parameters.</p>
	 *
	 * @param text The Korean text to process
	 * @param length The length of each n-gram
	 *
	 * @return A list of n-grams generated from the Jamo components of the input text
	 *
	 * @see #ngram(CharSequence, int, boolean)
	 */
	public static List<String> ngram(final CharSequence text, final int length) {
		return ngram(text, length, false);
	}

	/**
	 * <p>Generates n-grams from Korean text based on Jamo (자모) decomposition with additional options.</p>
	 *
	 * <p>This method extends the basic n-gram functionality by allowing control over the separation of double consonants (쌍자음) and compound vowels (복합모음).
	 * This can be particularly useful for more granular text analysis or search applications.</p>
	 *
	 * <p>The method treats spaces as word boundaries and generates n-grams for each word separately.</p>
	 *
	 * <p>Examples:</p>
	 *
	 * <pre>
	 * {@code
	 * // Without separating double consonants and compound vowels
	 * List<String> result1 = KoreanUtils.ngram("꿈꾸는", 3, false);
	 * // Result might include: "ㄲㅜㅁ", "ㅜㅁㄲ", "ㅁㄲㅜ", ...
	 *
	 * // With separating double consonants and compound vowels
	 * List<String> result2 = KoreanUtils.ngram("꿈꾸는", 3, true);
	 * // Result might include: "ㄱㄱㅜ", "ㄱㅜㅁ", "ㅜㅁㄱ", ...
	 *
	 * // Example with a compound vowel
	 * List<String> result3 = KoreanUtils.ngram("궤도", 3, true);
	 * // Result might include: "ㄱㅜㅔ", "ㅜㅔㄷ", "ㅔㄷㅗ", ...
	 * }
	 * </pre>
	 *
	 * <p>When {@code separateDoubleConsonantAndVowel} is true:</p>
	 *
	 * <ul>
	 *   <li>Double consonants like 'ㄲ' are separated into 'ㄱㄱ'</li>
	 *   <li>Compound vowels like 'ㅝ' are separated (e.g., 'ㅜㅓ')</li>
	 *   <li>Simple vowels like 'ㅐ' or 'ㅔ' are not separated</li>
	 * </ul>
	 *
	 * <p>This method is particularly useful for applications requiring detailed phonetic analysis or advanced search capabilities in Korean text.</p>
	 *
	 * @param text The Korean text to process
	 * @param length The length of each n-gram
	 * @param separateDoubleConsonantAndVowel If true, separates double consonants and compound vowels
	 *
	 * @return A list of n-grams generated from the Jamo components of the input text
	 */
	public static List<String> ngram(final CharSequence text, final int length, final boolean separateDoubleConsonantAndVowel) {
		List<String> result = new ArrayList<>(text.length() * 2);

		for (String word : text.toString().split("\\s+")) {
			List<Character> components = KoreanCharacter.convert(word).stream()
				.map(w -> w.decompose(true, separateDoubleConsonantAndVowel))
				.flatMap(chars -> IntStream.range(0, chars.length).mapToObj(i -> chars[i]))
				.collect(Collectors.toList());

			for (int i = 0; i < components.size() - length + 1; i++) {
				StringBuilder sb = new StringBuilder();

				for (int j = 0; j < length; j++) {
					sb.append(components.get(i + j));
				}

				if (separateDoubleConsonantAndVowel) {
					result.add(sb.toString());
				} else {
					result.add(compose(sb.toString()));
				}
			}
		}

		return result;
	}

	/**
	 * <p>Returns the length of the given CharSequence, or 0 if the CharSequence is null.</p>
	 *
	 * <p>This is a utility method used internally to safely get the length of a possibly null CharSequence.</p>
	 *
	 * @param text the CharSequence whose length is to be determined, may be null
	 *
	 * @return the length of the CharSequence, or 0 if the CharSequence is null
	 */
	private static int safeLength(final CharSequence text) {
		return text == null ? 0 : text.length();
	}
}
