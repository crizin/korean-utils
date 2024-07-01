package io.github.crizin;

import java.util.Map;
import java.util.Optional;

/**
 * <p>Interface to represent the Choseong, Jungseong, and Jongseong sounds in Korean.</p>
 *
 * <h1>Define terms</h1>
 *
 * <h2>Jamo</h2>
 *
 * <p>The fundamental letters from which syllable blocks are constructed.</p>
 *
 * <ol>
 *   <li><strong>Choseong</strong>: Leading consonants</li>
 *   <li><strong>Jungseong</strong>: Vowels</li>
 *   <li><strong>Jongseong</strong>: Trailing consonants</li>
 * </ol>
 *
 * <h2>Types of Jamo</h2>
 *
 * <ul>
 *   <li><a href="https://en.wikipedia.org/wiki/Hangul_Jamo_(Unicode_block)">Conjoining Jamo</a>:
 *     Conjoining Jamo represents the modern Hangul letters that can be combined to form syllable blocks.</li>
 *   <li><a href="https://en.wikipedia.org/wiki/Hangul_Compatibility_Jamo">Compatibility Jamo</a>:
 *     Compatibility Jamo consists of complete, indivisible syllables and clusters,
 *     used mainly for compatibility with older encodings and standards.</li>
 * </ul>
 *
 * <p>For example, the Korean character "한" is composed of a Chosung("ㅎ"), a Jungseong ("ㅏ"), and a Jongsung ("ㄴ").</p>
 *
 * <h1>Limit of this interface</h1>
 *
 * <p>While Unicode defines a broader set of Hangul Jamo, this interface specifically targets modern Korean usage.
 * It includes only the Hangul Jamo which are modern-usage characters
 * that can be converted into pre-composed Hangul syllables under Unicode normalization form NFC.</p>
 *
 * @param <T> One of Choseong, Jungseong and Jongseong.
 */
@SuppressWarnings({"java:S115", "NonAsciiCharacters"})
public interface Jamo<T extends Enum<T>> {

	/**
	 * Returns the conjoining Jamo character.
	 *
	 * @return The conjoining Jamo character.
	 */
	char getConjoiningJamo();

	/**
	 * Returns the compatibility Jamo character.
	 *
	 * @return The compatibility Jamo character.
	 */
	char getCompatibilityJamo();

	/**
	 * <p>Returns the individual components of the Jamo as a string.</p>
	 *
	 * <p>For single Jamo, it returns the Jamo itself. For compound Jamo, it separates into individual components.</p>
	 *
	 * @param useCompatibilityJamo If true, use the compatibility Jamo; if false, use the conjoining Jamo.
	 *
	 * @return String representing the components of the Jamo
	 */
	String getComponents(boolean useCompatibilityJamo);

	/**
	 * <p>Composes this Jamo with another Jamo to create a new compound Jamo.</p>
	 *
	 * <p>This method allows for the combination of two Jamo characters to form a new, compound Jamo.
	 * The composition follows the rules of Korean Jamo combination for creating compound consonants or vowels.</p>
	 *
	 * @param newJamo The Jamo to compose with this Jamo
	 *
	 * @return A new Jamo object representing the compound Jamo
	 *
	 * @throws JamoComposeException If the composition is not possible or not allowed in Korean Jamo combination rules
	 */
	T compose(T newJamo) throws JamoComposeException;

	/**
	 * <p>Represents the Choseong (initial consonants) in Korean syllables.</p>
	 *
	 * <p>This enum contains all possible Choseong characters in Hangul, including both basic and compound consonants.
	 * Each enum constant represents a single Choseong and provides methods to get its conjoining and compatibility Jamo representations.</p>
	 */
	enum Choseong implements Jamo<Choseong> {
		ㄱ('ᄀ', 'ㄱ', "ᄀ", "ㄱ"),
		ㄲ('ᄁ', 'ㄲ', "ᄀᄀ", "ㄱㄱ"),
		ㄴ('ᄂ', 'ㄴ', "ᄂ", "ㄴ"),
		ㄷ('ᄃ', 'ㄷ', "ᄃ", "ㄷ"),
		ㄸ('ᄄ', 'ㄸ', "ᄃᄃ", "ㄷㄷ"),
		ㄹ('ᄅ', 'ㄹ', "ᄅ", "ㄹ"),
		ㅁ('ᄆ', 'ㅁ', "ᄆ", "ㅁ"),
		ㅂ('ᄇ', 'ㅂ', "ᄇ", "ㅂ"),
		ㅃ('ᄈ', 'ㅃ', "ᄇᄇ", "ㅂㅂ"),
		ㅅ('ᄉ', 'ㅅ', "ᄉ", "ㅅ"),
		ㅆ('ᄊ', 'ㅆ', "ᄊ", "ㅅㅅ"),
		ㅇ('ᄋ', 'ㅇ', "ᄋ", "ㅇ"),
		ㅈ('ᄌ', 'ㅈ', "ᄌ", "ㅈ"),
		ㅉ('ᄍ', 'ㅉ', "ᄌᄌ", "ㅈㅈ"),
		ㅊ('ᄎ', 'ㅊ', "ᄎ", "ㅊ"),
		ㅋ('ᄏ', 'ㅋ', "ᄏ", "ㅋ"),
		ㅌ('ᄐ', 'ㅌ', "ᄐ", "ㅌ"),
		ㅍ('ᄑ', 'ㅍ', "ᄑ", "ㅍ"),
		ㅎ('ᄒ', 'ㅎ', "ᄒ", "ㅎ");

		private static final Map<Character, Choseong> CHARACTER_MAP = JamoUtils.createCharacterMap(values());
		private static final Map<String, Choseong> COMPOSE_MAP = JamoUtils.createComposeMap(values());

		private final char conjoiningJamo;
		private final char compatibilityJamo;
		private final String conjoiningComponents;
		private final String compatibilityComponents;

		Choseong(final char conjoiningJamo, final char compatibilityJamo, final String conjoiningComponents, final String compatibilityComponents) {
			this.conjoiningJamo = conjoiningJamo;
			this.compatibilityJamo = compatibilityJamo;
			this.conjoiningComponents = conjoiningComponents;
			this.compatibilityComponents = compatibilityComponents;
		}

		/**
		 * {@inheritDoc}
		 *
		 * <p>For example, for the Choseong "ㄱ", it returns 'ᄀ' (U+1100, Hangul Choseong Kiyeok).</p>
		 */
		@Override
		public char getConjoiningJamo() {
			return conjoiningJamo;
		}

		/**
		 * {@inheritDoc}
		 *
		 * <p>For example, for the Choseong "ㄱ", it returns 'ㄱ' (U+3131, Hangul Letter Kiyeok).</p>
		 */
		@Override
		public char getCompatibilityJamo() {
			return compatibilityJamo;
		}

		/**
		 * {@inheritDoc}
		 *
		 * <pre>
		 * {@code
		 * ㄱ => "ㄱ"
		 * ㄲ => "ㄱㄱ"
		 * }
		 * </pre>
		 */
		@Override
		public String getComponents(final boolean useCompatibilityJamo) {
			return useCompatibilityJamo ? compatibilityComponents : conjoiningComponents;
		}

		/**
		 * {@inheritDoc}
		 *
		 * <pre>
		 * {@code
		 * Choseong.ㄱ.compose(Choseong.ㄱ) => Choseong.ㄲ
		 * Choseong.ㄱ.compose(Choseong.ㅎ) => JamoComposeException
		 * }
		 * </pre>
		 *
		 * @throws JamoComposeException if the composition is not a valid Choseong
		 */
		@Override
		public Choseong compose(final Choseong newChoseong) throws JamoComposeException {
			return Optional.ofNullable(COMPOSE_MAP.get(String.format("%c%c", this.compatibilityJamo, newChoseong.compatibilityJamo)))
				.orElseThrow(JamoComposeException::new);
		}

		/**
		 * <p>Finds the corresponding Choseong enum constant for a given character.</p>
		 *
		 * <p>This method accepts either a conjoining Jamo or a compatibility Jamo character and returns the matching Choseong enum constant.
		 * For example, both 'ㄱ' (compatibility)  and 'ᄀ' (conjoining) will return Choseong.ㄱ.</p>
		 *
		 * @param character The character to find the Choseong for. This can be either a conjoining Jamo (e.g., 'ᄀ') or a compatibility Jamo (e.g., 'ㄱ').
		 *
		 * @return The corresponding Choseong enum constant, or null if no match is found.
		 */
		public static Choseong find(final char character) {
			return CHARACTER_MAP.get(character);
		}
	}

	/**
	 * <p>Represents the Jungseong (vowels) in Korean syllables.</p>
	 *
	 * <p>This enum contains all possible Jungseong characters in Hangul, including both basic and compound vowels.
	 * Each enum constant represents a single Jungseong and provides methods to get its conjoining and compatibility Jamo representations.</p>
	 */
	enum Jungseong implements Jamo<Jungseong> {
		ㅏ('ᅡ', 'ㅏ', "ᅡ", "ㅏ"),
		ㅐ('ᅢ', 'ㅐ', "ᅢ", "ㅐ"),
		ㅑ('ᅣ', 'ㅑ', "ᅣ", "ㅑ"),
		ㅒ('ᅤ', 'ㅒ', "ᅤ", "ㅒ"),
		ㅓ('ᅥ', 'ㅓ', "ᅥ", "ㅓ"),
		ㅔ('ᅦ', 'ㅔ', "ᅦ", "ㅔ"),
		ㅕ('ᅧ', 'ㅕ', "ᅧ", "ㅕ"),
		ㅖ('ᅨ', 'ㅖ', "ᅨ", "ㅖ"),
		ㅗ('ᅩ', 'ㅗ', "ᅩ", "ㅗ"),
		ㅘ('ᅪ', 'ㅘ', "ᅩᅡ", "ㅗㅏ"),
		ㅙ('ᅫ', 'ㅙ', "ᅩᅢ", "ㅗㅐ"),
		ㅚ('ᅬ', 'ㅚ', "ᅩᅵ", "ㅗㅣ"),
		ㅛ('ᅭ', 'ㅛ', "ᅭ", "ㅛ"),
		ㅜ('ᅮ', 'ㅜ', "ᅮ", "ㅜ"),
		ㅝ('ᅯ', 'ㅝ', "ᅮᅥ", "ㅜㅓ"),
		ㅞ('ᅰ', 'ㅞ', "ᅮᅦ", "ㅜㅔ"),
		ㅟ('ᅱ', 'ㅟ', "ᅮᅵ", "ㅜㅣ"),
		ㅠ('ᅲ', 'ㅠ', "ᅲ", "ㅠ"),
		ㅡ('ᅳ', 'ㅡ', "ᅳ", "ㅡ"),
		ㅢ('ᅴ', 'ㅢ', "ᅳᅵ", "ㅡㅣ"),
		ㅣ('ᅵ', 'ㅣ', "ᅵ", "ㅣ");

		private static final Map<Character, Jungseong> CHARACTER_MAP = JamoUtils.createCharacterMap(values());
		private static final Map<String, Jungseong> COMPOSE_MAP = JamoUtils.createComposeMap(values());

		private final char conjoiningJamo;
		private final char compatibilityJamo;
		private final String conjoiningComponents;
		private final String compatibilityComponents;

		Jungseong(final char conjoiningJamo, final char compatibilityJamo, final String conjoiningComponents, final String compatibilityComponents) {
			this.conjoiningJamo = conjoiningJamo;
			this.compatibilityJamo = compatibilityJamo;
			this.conjoiningComponents = conjoiningComponents;
			this.compatibilityComponents = compatibilityComponents;
		}

		/**
		 * {@inheritDoc}
		 *
		 * <p>For example, for the Jungseong "ㅏ", it returns 'ᅡ' (U+1161, Hangul Jungseong A).</p>
		 */
		@Override
		public char getConjoiningJamo() {
			return conjoiningJamo;
		}

		/**
		 * {@inheritDoc}
		 *
		 * <p>For example, for the Jungseong "ㅏ", it returns 'ㅏ' (U+314F, Hangul Letter A).</p>
		 */
		@Override
		public char getCompatibilityJamo() {
			return compatibilityJamo;
		}

		/**
		 * {@inheritDoc}
		 *
		 * <pre>
		 * {@code
		 * ㅏ => "ㅏ"
		 * ㅘ => "ㅗㅏ"
		 * }
		 * </pre>
		 */
		@Override
		public String getComponents(final boolean useCompatibilityJamo) {
			return useCompatibilityJamo ? compatibilityComponents : conjoiningComponents;
		}

		/**
		 * {@inheritDoc}
		 *
		 * <pre>
		 * {@code
		 * Jungseong.ㅗ.compose(Jungseong.ㅏ) => Jungseong.ㅘ
		 * Jungseong.ㅗ.compose(Jungseong.ㅜ) => JamoComposeException
		 * }
		 * </pre>
		 *
		 * @throws JamoComposeException if the composition is not a valid Jungseong
		 */
		@Override
		public Jungseong compose(final Jungseong newJungseong) throws JamoComposeException {
			return Optional.ofNullable(COMPOSE_MAP.get(String.format("%c%c", this.compatibilityJamo, newJungseong.compatibilityJamo)))
				.orElseThrow(JamoComposeException::new);
		}

		/**
		 * <p>Finds the corresponding Jungseong enum constant for a given character.</p>
		 *
		 * <p>This method accepts either a conjoining Jamo or a compatibility Jamo character and returns the matching Jungseong enum constant.
		 * For example, both 'ㅏ' (compatibility)  and 'ᅡ' (conjoining) will return Jungseong.ㅏ.</p>
		 *
		 * @param character The character to find the Jungseong for. This can be either a conjoining Jamo (e.g., 'ᅡ') or a compatibility Jamo (e.g., 'ㅏ').
		 *
		 * @return The corresponding Jungseong enum constant, or null if no match is found.
		 */
		public static Jungseong find(final char character) {
			return CHARACTER_MAP.get(character);
		}
	}

	/**
	 * <p>Represents the Jongseong (final consonants) in Korean syllables.</p>
	 *
	 * <p>This enum contains all possible Jongseong characters in Hangul, including both basic and compound consonants.
	 * Each enum constant represents a single Jongseong and provides methods to get its conjoining and compatibility Jamo representations.</p>
	 */
	enum Jongseong implements Jamo<Jongseong> {
		ㄱ('ᆨ', 'ㄱ', "ᆨ", "ㄱ"),
		ㄲ('ᆩ', 'ㄲ', "ᆨᆨ", "ㄱㄱ"),
		ㄳ('ᆪ', 'ㄳ', "ᆨᆺ", "ㄱㅅ"),
		ㄴ('ᆫ', 'ㄴ', "ᆫ", "ㄴ"),
		ㄵ('ᆬ', 'ㄵ', "ᆫᆽ", "ㄴㅈ"),
		ㄶ('ᆭ', 'ㄶ', "ᆫᇂ", "ㄴㅎ"),
		ㄷ('ᆮ', 'ㄷ', "ᆮ", "ㄷ"),
		ㄹ('ᆯ', 'ㄹ', "ᆯ", "ㄹ"),
		ㄺ('ᆰ', 'ㄺ', "ᆯᆨ", "ㄹㄱ"),
		ㄻ('ᆱ', 'ㄻ', "ᆯᆷ", "ㄹㅁ"),
		ㄼ('ᆲ', 'ㄼ', "ᆯᆸ", "ㄹㅂ"),
		ㄽ('ᆳ', 'ㄽ', "ᆯᆺ", "ㄹㅅ"),
		ㄾ('ᆴ', 'ㄾ', "ᆯᇀ", "ㄹㅌ"),
		ㄿ('ᆵ', 'ㄿ', "ᆯᇁ", "ㄹㅍ"),
		ㅀ('ᆶ', 'ㅀ', "ᆯᇂ", "ㄹㅎ"),
		ㅁ('ᆷ', 'ㅁ', "ᆷ", "ㅁ"),
		ㅂ('ᆸ', 'ㅂ', "ᆸ", "ㅂ"),
		ㅄ('ᆹ', 'ㅄ', "ᆸᆺ", "ㅂㅅ"),
		ㅅ('ᆺ', 'ㅅ', "ᆺ", "ㅅ"),
		ㅆ('ᆻ', 'ㅆ', "ᆺᆺ", "ㅅㅅ"),
		ㅇ('ᆼ', 'ㅇ', "ᆼ", "ㅇ"),
		ㅈ('ᆽ', 'ㅈ', "ᆽ", "ㅈ"),
		ㅊ('ᆾ', 'ㅊ', "ᆾ", "ㅊ"),
		ㅋ('ᆿ', 'ㅋ', "ᆿ", "ㅋ"),
		ㅌ('ᇀ', 'ㅌ', "ᇀ", "ㅌ"),
		ㅍ('ᇁ', 'ㅍ', "ᇁ", "ㅍ"),
		ㅎ('ᇂ', 'ㅎ', "ᇂ", "ㅎ");

		private static final Map<Character, Jongseong> CHARACTER_MAP = JamoUtils.createCharacterMap(values());
		private static final Map<String, Jongseong> COMPOSE_MAP = JamoUtils.createComposeMap(values());

		private final char conjoiningJamo;
		private final char compatibilityJamo;
		private final String conjoiningComponents;
		private final String compatibilityComponents;

		Jongseong(final char conjoiningJamo, final char compatibilityJamo, final String conjoiningComponents, final String compatibilityComponents) {
			this.conjoiningJamo = conjoiningJamo;
			this.compatibilityJamo = compatibilityJamo;
			this.conjoiningComponents = conjoiningComponents;
			this.compatibilityComponents = compatibilityComponents;
		}

		/**
		 * {@inheritDoc}
		 *
		 * <p>For example, for the Jongseong "ㄱ", it returns 'ㄱ' (U+11A8, Hangul Jongseong Kiyeok).</p>
		 */
		@Override
		public char getConjoiningJamo() {
			return conjoiningJamo;
		}

		/**
		 * {@inheritDoc}
		 *
		 * <p>For example, for the Jongseong "ㄱ", it returns 'ㄱ' (U+3131, Hangul Letter Kiyeok).</p>
		 */
		@Override
		public char getCompatibilityJamo() {
			return compatibilityJamo;
		}

		/**
		 * {@inheritDoc}
		 *
		 * <pre>
		 * {@code
		 * ㄹ => "ㄹ"
		 * ㄺ => "ㄹㄱ"
		 * }
		 * </pre>
		 */
		@Override
		public String getComponents(final boolean useCompatibilityJamo) {
			return useCompatibilityJamo ? compatibilityComponents : conjoiningComponents;
		}

		/**
		 * {@inheritDoc}
		 *
		 * <pre>
		 * {@code
		 * Jongseong.ㄹ.compose(Jongseong.ㄱ) => Jongseong.ㄺ
		 * Jongseong.ㄹ.compose(Jongseong.ㅎ) => JamoComposeException
		 * }
		 * </pre>
		 *
		 * @throws JamoComposeException if the composition is not a valid Jongseong
		 */
		@Override
		public Jongseong compose(final Jongseong newJongseong) throws JamoComposeException {
			return Optional.ofNullable(COMPOSE_MAP.get(String.format("%c%c", this.compatibilityJamo, newJongseong.compatibilityJamo)))
				.orElseThrow(JamoComposeException::new);
		}

		/**
		 * <p>Finds the corresponding Jongseong enum constant for a given character.</p>
		 *
		 * <p>This method accepts either a conjoining Jamo or a compatibility Jamo character and returns the matching Jongseong enum constant.
		 * For example, both 'ㄱ' (compatibility)  and 'ᆨ' (conjoining) will return Jongseong.ㄱ .</p>
		 *
		 * @param character The character to find the Jongseong for. This can be either a conjoining Jamo (e.g., 'ᆨ') or a compatibility Jamo (e.g., 'ㄱ').
		 *
		 * @return The corresponding Jongseong enum constant, or null if no match is found.
		 */
		public static Jongseong find(final char character) {
			return CHARACTER_MAP.get(character);
		}
	}

	/**
	 * <p>Exception thrown when an invalid Jamo composition is attempted.</p>
	 *
	 * <p>This exception is thrown when trying to compose two Jamo characters that cannot be combined according to Korean Jamo combination rules.
	 * For example, attempting to combine two vowels that do not form a valid compound vowel, or trying to combine consonants
	 * that do not form a valid compound consonant.</p>
	 *
	 * <p>Examples of invalid compositions that would throw this exception:</p>
	 *
	 * <ul>
	 *   <li>Choseong.ㄱ.compose(Choseong.ㅎ)</li>
	 *   <li>Jungseong.ㅏ.compose(Jungseong.ㅓ)</li>
	 * </ul>
	 */
	class JamoComposeException extends RuntimeException {}
}
