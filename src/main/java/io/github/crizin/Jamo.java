package io.github.crizin;

import java.util.Map;
import java.util.Optional;

/**
 * <p>Interface to represent the Choseong, Jungseong, and Jongseong sounds in Korean.</p>
 *
 * <h2>Define terms</h2>
 *
 * <h3>Jamo</h3>
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
 * <h2>Limit of this interface</h2>
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
		/**
		 * HANGUL CHOSEONG KIYEOK (U+1100) or HANGUL LETTER KIYEOK (U+3131)
		 */
		ㄱ('ᄀ', 'ㄱ', "ᄀ", "ㄱ"),
		/**
		 * HANGUL CHOSEONG SSANGKIYEOK (U+1101) or HANGUL LETTER SSANGKIYEOK (U+3132)
		 */
		ㄲ('ᄁ', 'ㄲ', "ᄀᄀ", "ㄱㄱ"),
		/**
		 * HANGUL CHOSEONG NIEUN (U+1102) or HANGUL LETTER NIEUN (U+3134)
		 */
		ㄴ('ᄂ', 'ㄴ', "ᄂ", "ㄴ"),
		/**
		 * HANGUL CHOSEONG TIKEUT (U+1103) or HANGUL LETTER TIKEUT (U+3137)
		 */
		ㄷ('ᄃ', 'ㄷ', "ᄃ", "ㄷ"),
		/**
		 * HANGUL CHOSEONG SSANGTIKEUT (U+1104) or HANGUL LETTER SSANGTIKEUT (U+3138)
		 */
		ㄸ('ᄄ', 'ㄸ', "ᄃᄃ", "ㄷㄷ"),
		/**
		 * HANGUL CHOSEONG RIEUL (U+1105) or HANGUL LETTER RIEUL (U+3139)
		 */
		ㄹ('ᄅ', 'ㄹ', "ᄅ", "ㄹ"),
		/**
		 * HANGUL CHOSEONG MIEUM (U+1106) or HANGUL LETTER MIEUM (U+3141)
		 */
		ㅁ('ᄆ', 'ㅁ', "ᄆ", "ㅁ"),
		/**
		 * HANGUL CHOSEONG PIEUP (U+1107) or HANGUL LETTER PIEUP (U+3142)
		 */
		ㅂ('ᄇ', 'ㅂ', "ᄇ", "ㅂ"),
		/**
		 * HANGUL CHOSEONG SSANGPIEUP (U+1108) or HANGUL LETTER SSANGPIEUP (U+3143)
		 */
		ㅃ('ᄈ', 'ㅃ', "ᄇᄇ", "ㅂㅂ"),
		/**
		 * HANGUL CHOSEONG SIOS (U+1109) or HANGUL LETTER SIOS (U+3145)
		 */
		ㅅ('ᄉ', 'ㅅ', "ᄉ", "ㅅ"),
		/**
		 * HANGUL CHOSEONG SSANGSIOS (U+110A) or HANGUL LETTER SSANGSIOS (U+3146)
		 */
		ㅆ('ᄊ', 'ㅆ', "ᄊ", "ㅅㅅ"),
		/**
		 * HANGUL CHOSEONG IEUNG (U+110B) or HANGUL LETTER IEUNG (U+3147)
		 */
		ㅇ('ᄋ', 'ㅇ', "ᄋ", "ㅇ"),
		/**
		 * HANGUL CHOSEONG CIEUC (U+110C) or HANGUL LETTER CIEUC (U+3148)
		 */
		ㅈ('ᄌ', 'ㅈ', "ᄌ", "ㅈ"),
		/**
		 * HANGUL CHOSEONG SSANGCIEUC (U+110D) or HANGUL LETTER SSANGCIEUC (U+3149)
		 */
		ㅉ('ᄍ', 'ㅉ', "ᄌᄌ", "ㅈㅈ"),
		/**
		 * HANGUL CHOSEONG CHIEUCH (U+110E) or HANGUL LETTER CHIEUCH (U+314A)
		 */
		ㅊ('ᄎ', 'ㅊ', "ᄎ", "ㅊ"),
		/**
		 * HANGUL CHOSEONG KHIEUKH (U+110F) or HANGUL LETTER KHIEUKH (U+314B)
		 */
		ㅋ('ᄏ', 'ㅋ', "ᄏ", "ㅋ"),
		/**
		 * HANGUL CHOSEONG THIEUTH (U+1110) or HANGUL LETTER THIEUTH (U+314C)
		 */
		ㅌ('ᄐ', 'ㅌ', "ᄐ", "ㅌ"),
		/**
		 * HANGUL CHOSEONG PHIEUPH (U+1111) or HANGUL LETTER PHIEUPH (U+314D)
		 */
		ㅍ('ᄑ', 'ㅍ', "ᄑ", "ㅍ"),
		/**
		 * HANGUL CHOSEONG HIEUH (U+1112) or HANGUL LETTER HIEUH (U+314E)
		 */
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
		/**
		 * HANGUL JUNGSEONG A (U+1161) or HANGUL LETTER A (U+314F)
		 */
		ㅏ('ᅡ', 'ㅏ', "ᅡ", "ㅏ"),
		/**
		 * HANGUL JUNGSEONG AE (U+1162) or HANGUL LETTER AE (U+3150)
		 */
		ㅐ('ᅢ', 'ㅐ', "ᅢ", "ㅐ"),
		/**
		 * HANGUL JUNGSEONG YA (U+1163) or HANGUL LETTER YA (U+3151)
		 */
		ㅑ('ᅣ', 'ㅑ', "ᅣ", "ㅑ"),
		/**
		 * HANGUL JUNGSEONG YAE (U+1164) or HANGUL LETTER YAE (U+3152)
		 */
		ㅒ('ᅤ', 'ㅒ', "ᅤ", "ㅒ"),
		/**
		 * HANGUL JUNGSEONG EO (U+1165) or HANGUL LETTER EO (U+3153)
		 */
		ㅓ('ᅥ', 'ㅓ', "ᅥ", "ㅓ"),
		/**
		 * HANGUL JUNGSEONG E (U+1166) or HANGUL LETTER E (U+3154)
		 */
		ㅔ('ᅦ', 'ㅔ', "ᅦ", "ㅔ"),
		/**
		 * HANGUL JUNGSEONG YEO (U+1167) or HANGUL LETTER YEO (U+3155)
		 */
		ㅕ('ᅧ', 'ㅕ', "ᅧ", "ㅕ"),
		/**
		 * HANGUL JUNGSEONG YE (U+1168) or HANGUL LETTER YE (U+3156)
		 */
		ㅖ('ᅨ', 'ㅖ', "ᅨ", "ㅖ"),
		/**
		 * HANGUL JUNGSEONG O (U+1169) or HANGUL LETTER O (U+3157)
		 */
		ㅗ('ᅩ', 'ㅗ', "ᅩ", "ㅗ"),
		/**
		 * HANGUL JUNGSEONG WA (U+116A) or HANGUL LETTER WA (U+3158)
		 */
		ㅘ('ᅪ', 'ㅘ', "ᅩᅡ", "ㅗㅏ"),
		/**
		 * HANGUL JUNGSEONG WAE (U+116B) or HANGUL LETTER WAE (U+3159)
		 */
		ㅙ('ᅫ', 'ㅙ', "ᅩᅢ", "ㅗㅐ"),
		/**
		 * HANGUL JUNGSEONG OE (U+116C) or HANGUL LETTER OE (U+315A)
		 */
		ㅚ('ᅬ', 'ㅚ', "ᅩᅵ", "ㅗㅣ"),
		/**
		 * HANGUL JUNGSEONG YO (U+116D) or HANGUL LETTER YO (U+315B)
		 */
		ㅛ('ᅭ', 'ㅛ', "ᅭ", "ㅛ"),
		/**
		 * HANGUL JUNGSEONG U (U+116E) or HANGUL LETTER U (U+315C)
		 */
		ㅜ('ᅮ', 'ㅜ', "ᅮ", "ㅜ"),
		/**
		 * HANGUL JUNGSEONG WEO (U+116F) or HANGUL LETTER WEO (U+315D)
		 */
		ㅝ('ᅯ', 'ㅝ', "ᅮᅥ", "ㅜㅓ"),
		/**
		 * HANGUL JUNGSEONG WE (U+1170) or HANGUL LETTER WE (U+315E)
		 */
		ㅞ('ᅰ', 'ㅞ', "ᅮᅦ", "ㅜㅔ"),
		/**
		 * HANGUL JUNGSEONG WI (U+1171) or HANGUL LETTER WI (U+315F)
		 */
		ㅟ('ᅱ', 'ㅟ', "ᅮᅵ", "ㅜㅣ"),
		/**
		 * HANGUL JUNGSEONG YU (U+1172) or HANGUL LETTER YU (U+3160)
		 */
		ㅠ('ᅲ', 'ㅠ', "ᅲ", "ㅠ"),
		/**
		 * HANGUL JUNGSEONG EU (U+1173) or HANGUL LETTER EU (U+3161)
		 */
		ㅡ('ᅳ', 'ㅡ', "ᅳ", "ㅡ"),
		/**
		 * HANGUL JUNGSEONG YI (U+1174) or HANGUL LETTER YI (U+3162)
		 */
		ㅢ('ᅴ', 'ㅢ', "ᅳᅵ", "ㅡㅣ"),
		/**
		 * HANGUL JUNGSEONG I (U+1175) or HANGUL LETTER I (U+3163)
		 */
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
		/**
		 * HANGUL JONGSEONG KIYEOK (U+11A8) or HANGUL LETTER KIYEOK (U+3131)
		 */
		ㄱ('ᆨ', 'ㄱ', "ᆨ", "ㄱ"),
		/**
		 * HANGUL JONGSEONG SSANGKIYEOK (U+11A8) or HANGUL LETTER SSANGKIYEOK (U+3132)
		 */
		ㄲ('ᆩ', 'ㄲ', "ᆨᆨ", "ㄱㄱ"),
		/**
		 * HANGUL JONGSEONG KIYEOK-SIOS (U+11A8) or HANGUL LETTER KIYEOK-SIOS (U+3133)
		 */
		ㄳ('ᆪ', 'ㄳ', "ᆨᆺ", "ㄱㅅ"),
		/**
		 * HANGUL JONGSEONG NIEUN (U+11A8) or HANGUL LETTER NIEUN (U+3134)
		 */
		ㄴ('ᆫ', 'ㄴ', "ᆫ", "ㄴ"),
		/**
		 * HANGUL JONGSEONG NIEUN-CIEUC (U+11A8) or HANGUL LETTER NIEUN-CIEUC (U+3135)
		 */
		ㄵ('ᆬ', 'ㄵ', "ᆫᆽ", "ㄴㅈ"),
		/**
		 * HANGUL JONGSEONG NIEUN-HIEUH (U+11A8) or HANGUL LETTER NIEUN-HIEUH (U+3136)
		 */
		ㄶ('ᆭ', 'ㄶ', "ᆫᇂ", "ㄴㅎ"),
		/**
		 * HANGUL JONGSEONG TIKEUT (U+11A8) or HANGUL LETTER TIKEUT (U+3137)
		 */
		ㄷ('ᆮ', 'ㄷ', "ᆮ", "ㄷ"),
		/**
		 * HANGUL JONGSEONG RIEUL (U+11A8) or HANGUL LETTER RIEUL (U+3139)
		 */
		ㄹ('ᆯ', 'ㄹ', "ᆯ", "ㄹ"),
		/**
		 * HANGUL JONGSEONG RIEUL-KIYEOK (U+11A8) or HANGUL LETTER RIEUL-KIYEOK (U+313A)
		 */
		ㄺ('ᆰ', 'ㄺ', "ᆯᆨ", "ㄹㄱ"),
		/**
		 * HANGUL JONGSEONG RIEUL-MIEUM (U+11A8) or HANGUL LETTER RIEUL-MIEUM (U+313B)
		 */
		ㄻ('ᆱ', 'ㄻ', "ᆯᆷ", "ㄹㅁ"),
		/**
		 * HANGUL JONGSEONG RIEUL-PIEUP (U+11A8) or HANGUL LETTER RIEUL-PIEUP (U+313C)
		 */
		ㄼ('ᆲ', 'ㄼ', "ᆯᆸ", "ㄹㅂ"),
		/**
		 * HANGUL JONGSEONG RIEUL-SIOS (U+11A8) or HANGUL LETTER RIEUL-SIOS (U+313D)
		 */
		ㄽ('ᆳ', 'ㄽ', "ᆯᆺ", "ㄹㅅ"),
		/**
		 * HANGUL JONGSEONG RIEUL-THIEUTH (U+11A8) or HANGUL LETTER RIEUL-THIEUTH (U+313E)
		 */
		ㄾ('ᆴ', 'ㄾ', "ᆯᇀ", "ㄹㅌ"),
		/**
		 * HANGUL JONGSEONG RIEUL-PHIEUPH (U+11A8) or HANGUL LETTER RIEUL-PHIEUPH (U+313F)
		 */
		ㄿ('ᆵ', 'ㄿ', "ᆯᇁ", "ㄹㅍ"),
		/**
		 * HANGUL JONGSEONG RIEUL-HIEUH (U+11A8) or HANGUL LETTER RIEUL-HIEUH (U+3140)
		 */
		ㅀ('ᆶ', 'ㅀ', "ᆯᇂ", "ㄹㅎ"),
		/**
		 * HANGUL JONGSEONG MIEUM (U+11A8) or HANGUL LETTER MIEUM (U+3141)
		 */
		ㅁ('ᆷ', 'ㅁ', "ᆷ", "ㅁ"),
		/**
		 * HANGUL JONGSEONG PIEUP (U+11A8) or HANGUL LETTER PIEUP (U+3142)
		 */
		ㅂ('ᆸ', 'ㅂ', "ᆸ", "ㅂ"),
		/**
		 * HANGUL JONGSEONG PIEUP-SIOS (U+11A8) or HANGUL LETTER PIEUP-SIOS (U+3144)
		 */
		ㅄ('ᆹ', 'ㅄ', "ᆸᆺ", "ㅂㅅ"),
		/**
		 * HANGUL JONGSEONG SIOS (U+11A8) or HANGUL LETTER SIOS (U+3145)
		 */
		ㅅ('ᆺ', 'ㅅ', "ᆺ", "ㅅ"),
		/**
		 * HANGUL JONGSEONG SSANGSIOS (U+11A8) or HANGUL LETTER SSANGSIOS (U+3146)
		 */
		ㅆ('ᆻ', 'ㅆ', "ᆺᆺ", "ㅅㅅ"),
		/**
		 * HANGUL JONGSEONG IEUNG (U+11A8) or HANGUL LETTER IEUNG (U+3147)
		 */
		ㅇ('ᆼ', 'ㅇ', "ᆼ", "ㅇ"),
		/**
		 * HANGUL JONGSEONG CIEUC (U+11A8) or HANGUL LETTER CIEUC (U+3148)
		 */
		ㅈ('ᆽ', 'ㅈ', "ᆽ", "ㅈ"),
		/**
		 * HANGUL JONGSEONG CHIEUCH (U+11A8) or HANGUL LETTER CHIEUCH (U+314A)
		 */
		ㅊ('ᆾ', 'ㅊ', "ᆾ", "ㅊ"),
		/**
		 * HANGUL JONGSEONG KHIEUKH (U+11A8) or HANGUL LETTER KHIEUKH (U+314B)
		 */
		ㅋ('ᆿ', 'ㅋ', "ᆿ", "ㅋ"),
		/**
		 * HANGUL JONGSEONG THIEUTH (U+11A8) or HANGUL LETTER THIEUTH (U+314C)
		 */
		ㅌ('ᇀ', 'ㅌ', "ᇀ", "ㅌ"),
		/**
		 * HANGUL JONGSEONG PHIEUPH (U+11A8) or HANGUL LETTER PHIEUPH (U+314D)
		 */
		ㅍ('ᇁ', 'ㅍ', "ᇁ", "ㅍ"),
		/**
		 * HANGUL JONGSEONG HIEUH (U+11A8) or HANGUL LETTER HIEUH (U+314E)
		 */
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
	class JamoComposeException extends RuntimeException {

		/**
		 * Constructs a new JamoComposeException with no detail message.
		 * This constructor is provided for use cases where no additional information is needed or available at the time of the exception.
		 */
		public JamoComposeException() {
			super();
		}
	}
}
