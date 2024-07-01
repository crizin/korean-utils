package io.github.crizin;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Utility class for creating and managing maps related to Jamo characters.</p>
 *
 * <p>This class provides static utility methods to create character maps and composition maps for Jamo enums (Choseong, Jungseong, Jongseong).
 * These maps are used internally by the Jamo enums to efficiently look up Jamo characters and handle composition operations.</p>
 *
 * <p>This class is not intended to be instantiated.</p>
 */
class JamoUtils {

	/**
	 * Private constructor to prevent instantiation of this utility class.
	 */
	private JamoUtils() {
	}

	/**
	 * <p>Creates a map that associates both conjoining and compatibility Jamo characters with their corresponding Jamo enum constants.</p>
	 *
	 * <p>This method is used internally by the Jamo enums to create their CHARACTER_MAP static fields, which are used for efficient character lookups.</p>
	 *
	 * @param <T> The type of the Jamo enum (Choseong, Jungseong, or Jongseong)
	 * @param values The array of enum constants for the Jamo type
	 *
	 * @return A map where the keys are characters (both conjoining and compatibility Jamo), and the values are the corresponding Jamo enum constants
	 */
	public static <T extends Enum<T> & Jamo<T>> Map<Character, T> createCharacterMap(T[] values) {
		return Stream.of(values)
			.flatMap(element -> Stream.of(element.getConjoiningJamo(), element.getCompatibilityJamo())
				.collect(Collectors.toMap(Function.identity(), c -> element))
				.entrySet()
				.stream())
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * <p>Creates a map that associates Jamo component strings with their corresponding compound Jamo enum constants.</p>
	 *
	 * <p>This method is used internally by the Jamo enums to create their COMPOSE_MAP static fields,
	 * which are used for efficient Jamo composition operations.</p>
	 *
	 * @param <T> The type of the Jamo enum (Choseong, Jungseong, or Jongseong)
	 * @param values The array of enum constants for the Jamo type
	 *
	 * @return A map where the keys are strings representing Jamo components (in both conjoining and compatibility forms),
	 * and the values are the corresponding compound Jamo enum constants
	 */
	public static <T extends Enum<T> & Jamo<T>> Map<String, T> createComposeMap(T[] values) {
		return Stream.of(values)
			.flatMap(element -> Stream.of(element.getComponents(true), element.getComponents(false))
				.filter(components -> components.length() > 1)
				.collect(Collectors.toMap(Function.identity(), c -> element))
				.entrySet()
				.stream())
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
