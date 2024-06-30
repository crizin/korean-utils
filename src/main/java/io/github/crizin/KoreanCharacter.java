package io.github.crizin;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Represents a single Korean character (Hangul syllable) and provides utility methods for its manipulation.</p>
 *
 * <p>This class encapsulates a Korean character, which can be a complete syllable or individual Jamo components.
 * It provides functionality to:</p>
 *
 * <ul>
 *   <li>Construct a Korean character from Unicode, individual Jamo components, or decompose an existing character</li>
 *   <li>Access the Choseong (initial consonant), Jungseong (vowel), and Jongseong (final consonant) components</li>
 *   <li>Compose and decompose Korean characters</li>
 *   <li>Convert between Unicode and Jamo representations</li>
 *   <li>Compare and manipulate Korean characters</li>
 * </ul>
 *
 * <p>This class is designed to work seamlessly with the {@link Jamo} interface and its subclasses,
 * providing a comprehensive toolkit for handling Korean text at a character level.</p>
 *
 * @see Jamo
 * @see Jamo.Choseong
 * @see Jamo.Jungseong
 * @see Jamo.Jongseong
 */
public class KoreanCharacter implements Serializable, Comparable<KoreanCharacter> {

	/**
	 * The Unicode code point for the first Hangul syllable (가).
	 * This marks the beginning of the Hangul Syllables block in the Unicode table.
	 */
	public static final int KOREAN_UNICODE_BEGIN = 0xAC00;
	/**
	 * The Unicode code point for the last Hangul syllable (힣).
	 * This marks the end of the Hangul Syllables block in the Unicode table.
	 */
	public static final int KOREAN_UNICODE_END = 0xD7A3;

	/**
	 * The total number of possible Jungseong (vowel) values in Hangul.
	 * This count is derived from the number of Jungseong enum values defined in {@link Jamo.Jungseong}.
	 */
	private static final int JUNGSEONG_COUNT = Jamo.Jungseong.values().length;
	/**
	 * The total number of possible Jongseong (final consonant) configurations in Hangul, including the absence of Jongseong.
	 * This count is calculated as the number of Jongseong enum values defined in {@link Jamo.Jongseong} plus one.
	 * The additional count accounts for syllables with no final consonant, which is represented as null in the enum
	 * but needs to be considered in Unicode calculations.
	 */
	private static final int JONGSEONG_COUNT = Jamo.Jongseong.values().length + 1;

	/**
	 * <p>The character representation stored in this KoreanCharacter instance.</p>
	 *
	 * <p>This field can hold various types of characters:</p>
	 *
	 * <ul>
	 *   <li>A complete Hangul syllable (e.g., '한')</li>
	 *   <li>A single Jamo (e.g., 'ㄱ', 'ㅏ')</li>
	 *   <li>Any non-Korean Unicode character (e.g., 'a', '1')</li>
	 *   <li>null, if the instance is created without a character or constructed only from Jamo components</li>
	 * </ul>
	 */
	private final Character character;
	/**
	 * The Choseong (initial consonant) component of this Korean character.
	 * This can be null if the character does not have a Choseong (e.g., for a vowel Jamo).
	 */
	private final Jamo.Choseong choseong;
	/**
	 * The Jungseong (vowel) component of this Korean character.
	 * This can be null if the character does not have a Jungseong (e.g., for a consonant Jamo).
	 */
	private final Jamo.Jungseong jungseong;
	/**
	 * The Jongseong (final consonant) component of this Korean character.
	 * This is null if the character does not have a Jongseong (e.g., for an open syllable or a non-final consonant Jamo).
	 */
	private final Jamo.Jongseong jongseong;

	/**
	 * <p>Constructs an empty KoreanCharacter instance.</p>
	 *
	 * <p>This constructor creates a KoreanCharacter with all fields (character, choseong, jungseong, jongseong) initialized to null.
	 * It's useful when you want to build a Korean character incrementally using the {@link #compose(char)} method.</p>
	 *
	 * <p>Example usage:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanCharacter kc = new KoreanCharacter();
	 * kc = kc.compose('ㅎ'); // Adds choseong 'ㅎ'
	 * kc = kc.compose('ㅏ'); // Adds jungseong 'ㅏ'
	 * kc = kc.compose('ㄴ'); // Adds jongseong 'ㄴ', completing the character '한'
	 * }
	 * </pre>
	 *
	 * <p>This approach allows for flexible construction of Korean characters,
	 * especially when dealing with user input or streaming scenarios where Jamo components arrive sequentially.</p>
	 */
	public KoreanCharacter() {
		this.character = null;
		this.choseong = null;
		this.jungseong = null;
		this.jongseong = null;
	}

	/**
	 * <p>Constructs a KoreanCharacter instance from a given Character.</p>
	 *
	 * <p>This is likely to be the most commonly used constructor, as it allows for easy creation
	 * of KoreanCharacter instances from existing characters. It handles various input scenarios:</p>
	 *
	 * <ul>
	 *   <li>Complete Hangul syllables (e.g., '한'): Decomposes into Choseong, Jungseong, and Jongseong</li>
	 *   <li>Single Jamo (e.g., 'ㄱ', 'ㅏ'): Assigns to the appropriate Jamo field</li>
	 *   <li>Non-Korean characters: Stores the character as-is, with null Jamo fields</li>
	 * </ul>
	 *
	 * <p>Note: For ambiguous Jamo that can be either Choseong or Jongseong (e.g., 'ㄱ'),
	 * this constructor prioritizes assigning it as Choseong.</p>
	 *
	 * <p>Example usage:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanCharacter kc1 = new KoreanCharacter('한'); // Complete syllable
	 * KoreanCharacter kc2 = new KoreanCharacter('ㄱ'); // Single Jamo (assigned as Choseong)
	 * KoreanCharacter kc3 = new KoreanCharacter('A');  // Non-Korean character
	 * }
	 * </pre>
	 *
	 * <p>After construction, various utility methods can be used to analyze or manipulate the character.</p>
	 *
	 * @param character The Character to construct this KoreanCharacter from.
	 */
	public KoreanCharacter(final Character character) {
		this.character = character;

		if (KOREAN_UNICODE_BEGIN <= character && character <= KOREAN_UNICODE_END) {
			int value = character - KOREAN_UNICODE_BEGIN;
			this.choseong = Jamo.Choseong.values()[value / (JUNGSEONG_COUNT * JONGSEONG_COUNT)];
			this.jungseong = Jamo.Jungseong.values()[(value % (JUNGSEONG_COUNT * JONGSEONG_COUNT)) / JONGSEONG_COUNT];
			int mod = value % JONGSEONG_COUNT;
			this.jongseong = mod == 0 ? null : Jamo.Jongseong.values()[mod - 1];
		} else {
			this.choseong = Jamo.Choseong.find(character);
			this.jungseong = Jamo.Jungseong.find(character);
			this.jongseong = this.choseong == null ? Jamo.Jongseong.find(character) : null;
		}
	}

	/**
	 * <p>Constructs a KoreanCharacter instance from individual Jamo components.</p>
	 *
	 * <p>This constructor allows for the creation of a KoreanCharacter by explicitly specifying
	 * its Choseong, Jungseong, and Jongseong components. It's particularly useful when working
	 * with decomposed Hangul or when constructing Korean characters programmatically.</p>
	 *
	 * <p>The constructor performs validation on the input to ensure it represents a valid
	 * Korean character combination. Invalid combinations will result in an exception.</p>
	 *
	 * <p>Valid combinations are:</p>
	 *
	 * <ul>
	 *   <li>Choseong + Jungseong (+ optional Jongseong)</li>
	 *   <li>Choseong only</li>
	 *   <li>Jungseong only</li>
	 *   <li>Jongseong only</li>
	 * </ul>
	 *
	 * <p>Example usage:</p>
	 *
	 * <pre>
	 * {@code
	 * // Creating '한'
	 * KoreanCharacter kc1 = new KoreanCharacter(Jamo.Choseong.ㅎ, Jamo.Jungseong.ㅏ, Jamo.Jongseong.ㄴ);
	 *
	 * // Creating 'ㅎ'
	 * KoreanCharacter kc2 = new KoreanCharacter(Jamo.Choseong.ㅎ, null, null);
	 *
	 * // Invalid: will throw IllegalArgumentException
	 * KoreanCharacter kc3 = new KoreanCharacter(null, Jamo.Jungseong.ㅏ, Jamo.Jongseong.ㄴ);
	 * }
	 * </pre>
	 *
	 * @param choseong The Choseong (initial consonant) of the character, or null
	 * @param jungseong The Jungseong (vowel) of the character, or null
	 * @param jongseong The Jongseong (final consonant) of the character, or null
	 *
	 * @throws IllegalArgumentException if the combination of Jamo is invalid
	 */
	public KoreanCharacter(final Jamo.Choseong choseong, final Jamo.Jungseong jungseong, final Jamo.Jongseong jongseong) throws IllegalArgumentException {
		validation(choseong, jungseong, jongseong);

		this.choseong = choseong;
		this.jungseong = jungseong;
		this.jongseong = jongseong;

		if (choseong == null) {
			if (jungseong == null) {
				if (jongseong == null) {
					this.character = null;
				} else {
					this.character = jongseong.getCompatibilityJamo();
				}
			} else {
				this.character = jungseong.getCompatibilityJamo();
			}
		} else {
			if (jungseong == null) {
				this.character = choseong.getCompatibilityJamo();
			} else {
				int c = KOREAN_UNICODE_BEGIN + (choseong.ordinal() * JUNGSEONG_COUNT * JONGSEONG_COUNT) + (jungseong.ordinal() * JONGSEONG_COUNT);
				if (jongseong != null) {
					c += jongseong.ordinal() + 1;
				}
				this.character = (char) c;
			}
		}
	}

	/**
	 * <p>Validates the combination of Jamo components to ensure they form a valid Korean character.</p>
	 *
	 * <p>This method checks for the following valid combinations:</p>
	 *
	 * <ul>
	 *   <li>Choseong + Jungseong (+ optional Jongseong)</li>
	 *   <li>Choseong only</li>
	 *   <li>Jungseong only</li>
	 *   <li>Jongseong only</li>
	 * </ul>
	 *
	 * <p>Invalid combinations, such as Jungseong + Jongseong without Choseong, will result in an IllegalArgumentException.</p>
	 *
	 * @param choseong The Choseong (initial consonant) to validate
	 * @param jungseong The Jungseong (vowel) to validate
	 * @param jongseong The Jongseong (final consonant) to validate
	 *
	 * @throws IllegalArgumentException if the combination of Jamo is invalid
	 */
	private void validation(final Jamo.Choseong choseong, final Jamo.Jungseong jungseong, final Jamo.Jongseong jongseong) throws IllegalArgumentException {
		if (choseong == null) {
			if (jungseong != null && jongseong != null) {
				throw new IllegalArgumentException("If Choseong is not given, then both Jungseong and Jongseong must not be given.");
			}
		} else {
			if (jungseong == null && jongseong != null) {
				throw new IllegalArgumentException("If Choseong is given and Jungseong is not given, then Jongseong must not be given.");
			}
		}
	}

	/**
	 * Returns the Unicode character representation of this KoreanCharacter.
	 *
	 * @return The Character value, or null if not set
	 */
	public Character getCharacter() {
		return character;
	}

	/**
	 * Returns the Choseong (initial consonant) of this KoreanCharacter.
	 *
	 * @return The Choseong value, or null if not present
	 */
	public Jamo.Choseong getChoseong() {
		return choseong;
	}

	/**
	 * Returns the Jungseong (vowel) of this KoreanCharacter.
	 *
	 * @return The Jungseong value, or null if not present
	 */
	public Jamo.Jungseong getJungseong() {
		return jungseong;
	}

	/**
	 * Returns the Jongseong (final consonant) of this KoreanCharacter.
	 *
	 * @return The Jongseong value, or null if not present
	 */
	public Jamo.Jongseong getJongseong() {
		return jongseong;
	}

	/**
	 * <p>Composes a new KoreanCharacter by adding a Jamo to the current character.</p>
	 *
	 * <p>This method attempts to combine the current character with the provided Jamo to form a new Korean character.
	 * As KoreanCharacter is immutable, this method returns a new KoreanCharacter instance rather than modifying the current one.</p>
	 *
	 * <p>The composition rules are as follows:</p>
	 *
	 * <ul>
	 *   <li>If the current character is empty, the new Jamo becomes the initial component.</li>
	 *   <li>If the current character is incomplete, the new Jamo is added if it forms a valid combination.</li>
	 *   <li>If the current character is complete, a new character is started with the provided Jamo.</li>
	 * </ul>
	 *
	 * <p>Example:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanCharacter kc = new KoreanCharacter('ㄱ');
	 * kc = kc.compose('ㅏ');  // Results in 'ㄱ' + 'ㅏ' = '가'
	 * kc = kc.compose('ㄴ');  // Results in '간'
	 * }
	 * </pre>
	 *
	 * @param character The Jamo character to compose with the current character.
	 *
	 * @return A new KoreanCharacter instance representing the composed character.
	 *
	 * @throws ComposeException if the composition is not possible. This exception contains information about the completed character and the next character,
	 * allowing the caller to handle the composition manually. See {@link ComposeException} for more details on how to handle this exception.
	 */
	public KoreanCharacter compose(final char character) throws ComposeException {
		Jamo.Choseong newChoseong = Jamo.Choseong.find(character);
		Jamo.Jungseong newJungseong = Jamo.Jungseong.find(character);
		Jamo.Jongseong newJongseong = Jamo.Jongseong.find(character);

		if (newChoseong != null && newJongseong != null) {
			if (this.jungseong == null && this.jongseong == null) {
				newJongseong = null;
			} else if (this.choseong == null && this.jungseong != null && this.jongseong == null) {
				throw new ComposeException(this, new KoreanCharacter(character));
			} else {
				newChoseong = null;
			}
		}

		if (newChoseong != null) {
			return composeChoseong(newChoseong);
		} else if (newJungseong != null) {
			return composeJungseong(newJungseong);
		} else if (newJongseong != null) {
			return composeJongseong(newJongseong);
		} else {
			throw new ComposeException(this, new KoreanCharacter(character));
		}
	}

	/**
	 * <p>Attempts to compose a new Choseong with the current character.</p>
	 *
	 * <p>This method is called when the input character is identified as a potential Choseong.</p>
	 *
	 * @param newChoseong The new Choseong to compose with the current character.
	 *
	 * @return A new KoreanCharacter with the composed Choseong.
	 *
	 * @throws ComposeException if the composition is not possible (e.g., if the current character already has a Jungseong or Jongseong).
	 */
	private KoreanCharacter composeChoseong(final Jamo.Choseong newChoseong) throws ComposeException {
		if (this.jungseong != null || this.jongseong != null) {
			throw new ComposeException(this, new KoreanCharacter(newChoseong, null, null));
		}

		if (this.choseong == null) {
			return new KoreanCharacter(newChoseong, null, null);
		}

		try {
			return new KoreanCharacter(this.choseong.compose(newChoseong), null, null);
		} catch (Jamo.JamoComposeException e) {
			throw new ComposeException(this, new KoreanCharacter(newChoseong, null, null));
		}
	}

	/**
	 * <p>Attempts to compose a new Jungseong with the current character.</p>
	 *
	 * <p>This method is called when the input character is identified as a potential Jungseong.</p>
	 *
	 * <p>Special case: If the current character is complete (has Choseong, Jungseong, and Jongseong) and a new Jungseong is given,
	 * this method will attempt to split the current character. For example, if the current character is "한" and the new Jungseong is "ㅏ",
	 * it will throw a ComposeException with "하" as the completed character and "나" as the next character.</p>
	 *
	 * @param newJungseong The new Jungseong to compose with the current character.
	 *
	 * @return A new KoreanCharacter with the composed Jungseong.
	 *
	 * @throws ComposeException if the composition is not possible or if the character needs to be split.
	 */
	private KoreanCharacter composeJungseong(final Jamo.Jungseong newJungseong) throws ComposeException {
		if (this.jongseong != null) {
			Jamo.Choseong movedChoseong = Jamo.Choseong.find(this.jongseong.getCompatibilityJamo());
			if (movedChoseong == null) {
				throw new ComposeException(this, new KoreanCharacter(null, newJungseong, null));
			} else {
				throw new ComposeException(new KoreanCharacter(this.choseong, this.jungseong, null), new KoreanCharacter(movedChoseong, newJungseong, null));
			}
		}
		if (this.jungseong == null) {
			return new KoreanCharacter(this.choseong, newJungseong, null);
		}
		try {
			return new KoreanCharacter(this.choseong, this.jungseong.compose(newJungseong), null);
		} catch (Jamo.JamoComposeException e) {
			throw new ComposeException(this, new KoreanCharacter(null, newJungseong, null));
		}
	}

	/**
	 * <p>Attempts to compose a new Jongseong with the current character.</p>
	 *
	 * <p>This method is called when the input character is identified as a potential Jongseong.</p>
	 *
	 * @param newJongseong The new Jongseong to compose with the current character.
	 *
	 * @return A new KoreanCharacter with the composed Jongseong.
	 *
	 * @throws ComposeException if the composition is not possible (e.g., if the current character doesn't have both Choseong and Jungseong).
	 */
	private KoreanCharacter composeJongseong(final Jamo.Jongseong newJongseong) throws ComposeException {
		if ((this.choseong == null && this.jungseong != null) || (this.choseong != null && this.jungseong == null)) {
			throw new ComposeException(this, new KoreanCharacter(null, null, newJongseong));
		}
		if (this.jongseong == null) {
			return new KoreanCharacter(this.choseong, this.jungseong, newJongseong);
		}
		try {
			return new KoreanCharacter(this.choseong, this.jungseong, this.jongseong.compose(newJongseong));
		} catch (Jamo.JamoComposeException e) {
			throw new ComposeException(this, new KoreanCharacter(null, null, newJongseong));
		}
	}

	/**
	 * <p>Decomposes the Korean character into its constituent Jamo.</p>
	 *
	 * <p>This is a convenience method that calls {@link #decompose(boolean, boolean)}
	 * with default parameters (useCompatibilityJamo=true, separateDoubleConsonantAndVowel=true).</p>
	 *
	 * @return An array of characters representing the decomposed Jamo.
	 */
	public char[] decompose() {
		return decompose(true, true);
	}

	/**
	 * <p>Decomposes the Korean character into its constituent Jamo with specified options.</p>
	 *
	 * <p>This method breaks down the Korean character into its component Jamo (consonants and vowels).
	 * For non-Korean characters, it returns the original character unchanged.</p>
	 *
	 * <p>Example: "한글abc" => "ㅎㅏㄴㄱㅡㄹabc"</p>
	 *
	 * @param useCompatibilityJamo If true, uses Compatibility Jamo characters; if false, uses Conjoining Jamo characters.
	 * @param separateDoubleConsonantAndVowel If true, separates double consonants and compound vowels into their constituent parts.
	 * For example, "까" becomes "ㄱㄱㅏ" instead of "ㄲㅏ".
	 *
	 * @return An array of characters representing the decomposed Jamo.
	 */
	public char[] decompose(final boolean useCompatibilityJamo, final boolean separateDoubleConsonantAndVowel) {
		StringBuilder sb = new StringBuilder();

		decomposeJamo(sb, choseong, useCompatibilityJamo, separateDoubleConsonantAndVowel);
		decomposeJamo(sb, jungseong, useCompatibilityJamo, separateDoubleConsonantAndVowel);
		decomposeJamo(sb, jongseong, useCompatibilityJamo, separateDoubleConsonantAndVowel);

		char[] result = sb.toString().toCharArray();

		if (result.length == 0) {
			return character == null ? new char[] {} : new char[] {character};
		} else {
			return result;
		}
	}

	/**
	 * <p>Helper method to decompose a single Jamo component.</p>
	 *
	 * <p>This method is used internally by the {@link #decompose(boolean, boolean)} method
	 * to handle the decomposition of individual Jamo components (Choseong, Jungseong, or Jongseong).</p>
	 *
	 * @param sb StringBuilder to append the decomposed Jamo to
	 * @param jamo The Jamo to decompose
	 * @param useCompatibilityJamo If true, uses Compatibility Jamo characters; if false, uses Conjoining Jamo characters
	 * @param separateDoubleConsonantAndVowel If true, separates double consonants and compound vowels
	 */
	private void decomposeJamo(final StringBuilder sb, final Jamo<?> jamo, final boolean useCompatibilityJamo, final boolean separateDoubleConsonantAndVowel) {
		if (jamo == null) {
			return;
		}

		if (separateDoubleConsonantAndVowel) {
			sb.append(jamo.getComponents(useCompatibilityJamo));
		} else {
			sb.append(useCompatibilityJamo ? jamo.getCompatibilityJamo() : jamo.getConjoiningJamo());
		}
	}

	/**
	 * <p>Checks if this KoreanCharacter includes the specified KoreanCharacter.</p>
	 *
	 * <p>This method is particularly useful for implementing "initial consonant search" (초성검색)
	 * or partial matching of Korean characters. It allows matching a full character against its components or partial combinations.</p>
	 *
	 * <p>The inclusion rules are as follows:</p>
	 *
	 * <ul>
	 *   <li>A full character includes its Choseong, Jungseong, Jongseong, and any partial combinations</li>
	 *   <li>A single Jamo or partial character is included if it matches the corresponding part of this character</li>
	 *   <li>Non-Korean characters are only included if they exactly match this character</li>
	 * </ul>
	 *
	 * <p>Examples:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanCharacter kc = new KoreanCharacter('한');
	 * kc.include(new KoreanCharacter('ㅎ')) // returns true
	 * kc.include(new KoreanCharacter('ㅏ')) // returns true
	 * kc.include(new KoreanCharacter('하')) // returns true
	 * kc.include(new KoreanCharacter('한')) // returns true
	 * kc.include(new KoreanCharacter('ㄴ')) // returns true
	 * kc.include(new KoreanCharacter('반')) // returns false
	 * kc.include(new KoreanCharacter('A')) // returns false
	 * }
	 * </pre>
	 *
	 * @param other The KoreanCharacter to check for inclusion
	 *
	 * @return true if this KoreanCharacter includes the other KoreanCharacter, false otherwise
	 */
	public boolean include(final KoreanCharacter other) {
		if (other == null) {
			return false;
		}

		if (this.character == null) {
			return other.character == null;
		}

		if (other.character == null) {
			return false;
		}

		if ((this.choseong == null && this.jungseong == null && this.jongseong == null)
			|| (other.choseong == null && other.jungseong == null && other.jongseong == null)) {
			return this.character.equals(other.character);
		}

		if (other.choseong != null && this.choseong != other.choseong) {
			return false;
		}

		if (other.jungseong != null && this.jungseong != other.jungseong) {
			return false;
		}

		return other.jongseong == null || this.jongseong == other.jongseong;
	}

	/**
	 * <p>Determines whether the given character is a Korean character or Jamo.</p>
	 *
	 * <p>This method checks if the input is either a complete Hangul syllable or a single Jamo (Choseong, Jungseong, or Jongseong).</p>
	 *
	 * <p>Examples:</p>
	 *
	 * <pre>
	 * {@code
	 * KoreanCharacter.isKoreanCharacter('한') // returns true
	 * KoreanCharacter.isKoreanCharacter('ㄱ') // returns true
	 * KoreanCharacter.isKoreanCharacter('ㅏ') // returns true
	 * KoreanCharacter.isKoreanCharacter('A')  // returns false
	 * KoreanCharacter.isKoreanCharacter('1')  // returns false
	 * }
	 * </pre>
	 *
	 * @param character The character to check
	 *
	 * @return true if the character is a Korean character or Jamo, false otherwise
	 */
	public static boolean isKoreanCharacter(final Character character) {
		return character != null
			   && (KOREAN_UNICODE_BEGIN <= character && character <= KOREAN_UNICODE_END
				   || Jamo.Choseong.find(character) != null || Jamo.Jungseong.find(character) != null || Jamo.Jongseong.find(character) != null
			   );
	}

	/**
	 * <p>Converts a CharSequence to a List of KoreanCharacter objects.</p>
	 *
	 * <p>This method takes a CharSequence (such as a String) and converts each character into a KoreanCharacter object.
	 * This is useful for processing mixed text that may contain both Korean and non-Korean characters.</p>
	 *
	 * <p>Example:</p>
	 *
	 * <pre>
	 * {@code
	 * List<KoreanCharacter> result = KoreanCharacter.convert("안녕123");
	 *   //=> result contains KoreanCharacter objects for '안', '녕', '1', '2', '3'
	 * }
	 * </pre>
	 *
	 * @param text The CharSequence to convert
	 *
	 * @return A List of KoreanCharacter objects representing each character in the input text
	 */
	public static List<KoreanCharacter> convert(final CharSequence text) {
		return text.chars().mapToObj(c -> (char) c).map(KoreanCharacter::new).collect(Collectors.toList());
	}

	/**
	 * <p>Compares this KoreanCharacter with the specified KoreanCharacter for order.</p>
	 *
	 * <p>The comparison is based on the Unicode value of the characters. If both characters are null,
	 * they are considered equal. A null character is considered less than any non-null character.</p>
	 *
	 * @param other the KoreanCharacter to be compared
	 *
	 * @return a negative integer, zero, or a positive integer as this KoreanCharacter is less than, equal to, or greater than the specified KoreanCharacter
	 */
	@Override
	public int compareTo(final KoreanCharacter other) {
		if (this.character == null && other.character == null) {
			return 0;
		} else if (this.character == null) {
			return -1;
		} else if (other.character == null) {
			return 1;
		} else {
			return Character.compare(this.character, other.character);
		}
	}

	/**
	 * <p>Indicates whether some other object is "equal to" this one.</p>
	 *
	 * <p>Two KoreanCharacter objects are considered equal if they represent the same Unicode character.
	 * This method considers null characters equal to each other.</p>
	 *
	 * @param other the reference object with which to compare
	 *
	 * @return true if this object is the same as the other argument; false otherwise
	 */
	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}

		if (other == null || getClass() != other.getClass()) {
			return false;
		}

		return character.equals(((KoreanCharacter) other).character);
	}

	/**
	 * <p>Returns a hash code value for the object.</p>
	 *
	 * <p>This method is supported for the benefit of hash tables such as those provided by HashMap.</p>
	 *
	 * <p>The hash code for a KoreanCharacter object is equal to the hash code of its character value, or 0 if the character is null.</p>
	 *
	 * @return a hash code value for this object
	 */
	@Override
	public int hashCode() {
		return character == null ? 0 : character.hashCode();
	}

	/**
	 * <p>Returns a string representation of the KoreanCharacter.</p>
	 *
	 * <p>This method returns the string representation of the Unicode character this KoreanCharacter represents.
	 * If the character is null, it returns an empty string.</p>
	 *
	 * @return a string representation of the KoreanCharacter
	 */
	@Override
	public String toString() {
		return character == null ? "" : character.toString();
	}

	/**
	 * <p>Exception thrown when a composition operation in KoreanCharacter cannot be completed.</p>
	 *
	 * <p>This exception is used to handle cases where a simple composition of Korean characters is not possible and requires special handling.
	 * It provides information about the current state of composition and suggests how to proceed.</p>
	 *
	 * <p>There are several scenarios where this exception might be thrown:</p>
	 *
	 * <ol>
	 *   <li>When trying to add a Choseong to a character that already has one.</li>
	 *   <li>When trying to add a Jungseong to a character that already has one.</li>
	 *   <li>When trying to add a Jongseong to a character that doesn't have both Choseong and Jungseong.</li>
	 *   <li>When trying to add a Jungseong to a complete syllable, requiring syllable splitting.</li>
	 * </ol>
	 *
	 * <p>The last case is particularly interesting. For example, when trying to compose "한" with "ㅏ",
	 * instead of failing, this exception suggests splitting into two syllables: "하" and "나".
	 * This behavior allows for more natural text input handling.</p>
	 *
	 * <p>Example usage:</p>
	 *
	 * <pre>
	 * {@code
	 * try {
	 *     KoreanCharacter kc = new KoreanCharacter('한');
	 *     kc = kc.compose('ㅏ');
	 * } catch (ComposeException e) {
	 *     KoreanCharacter completed = e.getCompletedCharacter(); //=> '하'
	 *     KoreanCharacter next = e.getNextCharacter();           //=> '나'
	 *     // Handle the split syllables as needed
	 * }
	 * }
	 * </pre>
	 *
	 * <p>By providing both the completed character and the next character,
	 * this exception allows the calling code to handle the composition failure gracefully,
	 * often by treating it as a successful composition of multiple characters.</p>
	 */
	public static class ComposeException extends RuntimeException {

		/**
		 * The KoreanCharacter that has been completed before the composition failed.
		 */
		private final KoreanCharacter completedCharacter;
		/**
		 * The KoreanCharacter that should start the next syllable.
		 */
		private final KoreanCharacter nextCharacter;

		/**
		 * Constructs a new ComposeException with the specified completed and next characters.
		 *
		 * @param completedCharacter the KoreanCharacter that has been completed before the composition failed
		 * @param nextCharacter the KoreanCharacter that should start the next syllable
		 */
		public ComposeException(final KoreanCharacter completedCharacter, final KoreanCharacter nextCharacter) {
			this.completedCharacter = completedCharacter;
			this.nextCharacter = nextCharacter;
		}

		/**
		 * <p>Returns the KoreanCharacter that results from the partial composition before the exception was thrown.</p>
		 *
		 * <p>In cases where syllable splitting occurs, this character represents the first part of the split.
		 * For example, when composing "한" with "ㅏ", this method would return "하".</p>
		 *
		 * @return the resulting KoreanCharacter from the partial composition
		 */
		public KoreanCharacter getCompletedCharacter() {
			return completedCharacter;
		}

		/**
		 * <p>Returns the KoreanCharacter that represents the next syllable or component that couldn't be composed.</p>
		 *
		 * <p>In cases where syllable splitting occurs, this character represents the second part of the split.
		 * For example, when composing "한" with "ㅏ", this method would return "나".</p>
		 *
		 * @return the next KoreanCharacter that couldn't be composed
		 */
		public KoreanCharacter getNextCharacter() {
			return nextCharacter;
		}
	}
}
