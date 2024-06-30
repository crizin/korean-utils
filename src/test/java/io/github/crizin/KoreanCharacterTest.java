package io.github.crizin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings({"java:S5961", "NonAsciiCharacters"})
class KoreanCharacterTest {

	@Test
	@DisplayName("new KoreanCharacter()")
	void testConstructor_empty() {
		KoreanCharacter c = new KoreanCharacter();
		assertThat(c.getCharacter()).isNull();
		assertThat(c.getChoseong()).isNull();
		assertThat(c.getJungseong()).isNull();
		assertThat(c.getJongseong()).isNull();
	}

	@Test
	@DisplayName("new KoreanCharacter(char)")
	void testConstructor_single() {
		KoreanCharacter c = new KoreanCharacter(' ');
		assertThat(c.getCharacter()).isEqualTo(' ');
		assertThat(c.getChoseong()).isNull();
		assertThat(c.getJungseong()).isNull();
		assertThat(c.getJongseong()).isNull();

		c = new KoreanCharacter('a');
		assertThat(c.getCharacter()).isEqualTo('a');
		assertThat(c.getChoseong()).isNull();
		assertThat(c.getJungseong()).isNull();
		assertThat(c.getJongseong()).isNull();

		c = new KoreanCharacter('한');
		assertThat(c.getCharacter()).isEqualTo('한');
		assertThat(c.getChoseong()).isEqualTo(Jamo.Choseong.ㅎ);
		assertThat(c.getJungseong()).isEqualTo(Jamo.Jungseong.ㅏ);
		assertThat(c.getJongseong()).isEqualTo(Jamo.Jongseong.ㄴ);

		c = new KoreanCharacter('하');
		assertThat(c.getCharacter()).isEqualTo('하');
		assertThat(c.getChoseong()).isEqualTo(Jamo.Choseong.ㅎ);
		assertThat(c.getJungseong()).isEqualTo(Jamo.Jungseong.ㅏ);
		assertThat(c.getJongseong()).isNull();

		c = new KoreanCharacter('ㄱ');
		assertThat(c.getCharacter()).isEqualTo('ㄱ');
		assertThat(c.getChoseong()).isEqualTo(Jamo.Choseong.ㄱ);
		assertThat(c.getJungseong()).isNull();
		assertThat(c.getJongseong()).isNull();

		c = new KoreanCharacter('ㅏ');
		assertThat(c.getCharacter()).isEqualTo('ㅏ');
		assertThat(c.getChoseong()).isNull();
		assertThat(c.getJungseong()).isEqualTo(Jamo.Jungseong.ㅏ);
		assertThat(c.getJongseong()).isNull();

		c = new KoreanCharacter('ㄵ');
		assertThat(c.getCharacter()).isEqualTo('ㄵ');
		assertThat(c.getChoseong()).isNull();
		assertThat(c.getJungseong()).isNull();
		assertThat(c.getJongseong()).isEqualTo(Jamo.Jongseong.ㄵ);

		c = new KoreanCharacter('ᄀ');
		assertThat(c.getCharacter()).isEqualTo('ᄀ');
		assertThat(c.getChoseong()).isEqualTo(Jamo.Choseong.ㄱ);
		assertThat(c.getJungseong()).isNull();
		assertThat(c.getJongseong()).isNull();

		c = new KoreanCharacter('ᅡ');
		assertThat(c.getCharacter()).isEqualTo('ᅡ');
		assertThat(c.getChoseong()).isNull();
		assertThat(c.getJungseong()).isEqualTo(Jamo.Jungseong.ㅏ);
		assertThat(c.getJongseong()).isNull();

		c = new KoreanCharacter('ᆬ');
		assertThat(c.getCharacter()).isEqualTo('ᆬ');
		assertThat(c.getChoseong()).isNull();
		assertThat(c.getJungseong()).isNull();
		assertThat(c.getJongseong()).isEqualTo(Jamo.Jongseong.ㄵ);
	}

	@Test
	@DisplayName("new KoreanCharacter(Jamo.Choseong, Jamo.Jungseong, Jamo.Jongseong)")
	void testConstructor_parts() {
		/*
		 * | Choseong | Jungseong | Jongseong | Result                   |
		 * | -------- | --------- | --------- | ------------------------ |
		 * | ㅎ       | ㅏ        | ㄴ        | 한                       |
		 * | ㅎ       | ㅏ        |           | 하                       |
		 * | ㅎ       |           | ㄴ        | IllegalArgumentException |
		 * | ㅎ       |           |           | ㅎ                       |
		 * |          | ㅏ        | ㄴ        | IllegalArgumentException |
		 * |          | ㅏ        |           | ㅏ                       |
		 * |          |           | ㄴ        | ㄴ                       |
		 * |          |           |           |                          |
		 */

		assertThat(new KoreanCharacter(Jamo.Choseong.ㅎ, Jamo.Jungseong.ㅏ, Jamo.Jongseong.ㄴ).getCharacter())
			.isEqualTo('한');

		assertThat(new KoreanCharacter(Jamo.Choseong.ㅎ, Jamo.Jungseong.ㅏ, null).getCharacter())
			.isEqualTo('하');

		assertThatThrownBy(() -> new KoreanCharacter(Jamo.Choseong.ㅎ, null, Jamo.Jongseong.ㄴ))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("If Choseong is given and Jungseong is not given, then Jongseong must not be given.");

		assertThat(new KoreanCharacter(Jamo.Choseong.ㅎ, null, null).getCharacter())
			.isEqualTo('ㅎ');

		assertThatThrownBy(() -> new KoreanCharacter(null, Jamo.Jungseong.ㅏ, Jamo.Jongseong.ㄴ))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("If Choseong is not given, then both Jungseong and Jongseong must not be given.");

		assertThat(new KoreanCharacter(null, Jamo.Jungseong.ㅏ, null).getCharacter())
			.isEqualTo('ㅏ');

		assertThat(new KoreanCharacter(null, null, Jamo.Jongseong.ㄴ).getCharacter())
			.isEqualTo('ㄴ');

		assertThat(new KoreanCharacter(null, null, null).getCharacter())
			.isNull();
	}

	@Test
	@DisplayName("compose()")
	void testCompose() {
		assertThat(new KoreanCharacter().compose('ㄷ')).isEqualTo(new KoreanCharacter('ㄷ'));
		assertThat(new KoreanCharacter('ㄷ').compose('ㅏ')).isEqualTo(new KoreanCharacter('다'));
		assertThat(new KoreanCharacter('다').compose('ㄹ')).isEqualTo(new KoreanCharacter('달'));
		assertThat(new KoreanCharacter('다').compose('ㄺ')).isEqualTo(new KoreanCharacter('닭'));
		assertThat(new KoreanCharacter('달').compose('ㄱ')).isEqualTo(new KoreanCharacter('닭'));
		assertThat(new KoreanCharacter('달').compose('ᆨ')).isEqualTo(new KoreanCharacter('닭'));
		assertThat(new KoreanCharacter('ㅇ').compose('ㅢ')).isEqualTo(new KoreanCharacter('의'));
		assertThat(new KoreanCharacter('으').compose('ㅣ')).isEqualTo(new KoreanCharacter('의'));

		assertThatThrownBy(() -> new KoreanCharacter('a').compose('b'))
			.isInstanceOf(KoreanCharacter.ComposeException.class)
			.isInstanceOfSatisfying(KoreanCharacter.ComposeException.class, e -> {
				assertThat(e.getCompletedCharacter()).isEqualTo(new KoreanCharacter('a'));
				assertThat(e.getNextCharacter()).isEqualTo(new KoreanCharacter('b'));
			});

		assertThatThrownBy(() -> new KoreanCharacter('한').compose('a'))
			.isInstanceOf(KoreanCharacter.ComposeException.class)
			.isInstanceOfSatisfying(KoreanCharacter.ComposeException.class, e -> {
				assertThat(e.getCompletedCharacter()).isEqualTo(new KoreanCharacter('한'));
				assertThat(e.getNextCharacter()).isEqualTo(new KoreanCharacter('a'));
			});

		assertThatThrownBy(() -> new KoreanCharacter('한').compose('ㄱ'))
			.isInstanceOf(KoreanCharacter.ComposeException.class)
			.isInstanceOfSatisfying(KoreanCharacter.ComposeException.class, e -> {
				assertThat(e.getCompletedCharacter()).isEqualTo(new KoreanCharacter('한'));
				assertThat(e.getNextCharacter()).isEqualTo(new KoreanCharacter('ㄱ'));
			});

		assertThatThrownBy(() -> new KoreanCharacter('한').compose('ㅏ'))
			.isInstanceOf(KoreanCharacter.ComposeException.class)
			.isInstanceOfSatisfying(KoreanCharacter.ComposeException.class, e -> {
				assertThat(e.getCompletedCharacter()).isEqualTo(new KoreanCharacter('하'));
				assertThat(e.getNextCharacter()).isEqualTo(new KoreanCharacter('나'));
			});
	}

	@Test
	@DisplayName("decompose(useCompatibilityJamo: true, separateDoubleConsonantAndVowel: true)")
	void testDecompose_compatibilityJamo_separateDoubleConsonantAndVowel() {
		assertThat(new KoreanCharacter().decompose(true, true)).isEqualTo(new char[] {});
		assertThat(new KoreanCharacter('a').decompose(true, true)).isEqualTo(new char[] {'a'});
		assertThat(new KoreanCharacter('ㅎ').decompose(true, true)).isEqualTo(new char[] {'ㅎ'});
		assertThat(new KoreanCharacter('하').decompose(true, true)).isEqualTo(new char[] {'ㅎ', 'ㅏ'});
		assertThat(new KoreanCharacter('한').decompose(true, true)).isEqualTo(new char[] {'ㅎ', 'ㅏ', 'ㄴ'});
		assertThat(new KoreanCharacter('ㅘ').decompose(true, true)).isEqualTo(new char[] {'ㅗ', 'ㅏ'});
		assertThat(new KoreanCharacter('ᆬ').decompose(true, true)).isEqualTo(new char[] {'ㄴ', 'ㅈ'});
		assertThat(new KoreanCharacter('ᆬ').decompose()).isEqualTo(new char[] {'ㄴ', 'ㅈ'});
	}

	@Test
	@DisplayName("decompose(useCompatibilityJamo: true, separateDoubleConsonantAndVowel: false)")
	void testDecompose_compatibilityJamo_keepDoubleConsonantAndVowel() {
		assertThat(new KoreanCharacter().decompose(true, false)).isEqualTo(new char[] {});
		assertThat(new KoreanCharacter('a').decompose(true, false)).isEqualTo(new char[] {'a'});
		assertThat(new KoreanCharacter('ㅎ').decompose(true, false)).isEqualTo(new char[] {'ㅎ'});
		assertThat(new KoreanCharacter('하').decompose(true, false)).isEqualTo(new char[] {'ㅎ', 'ㅏ'});
		assertThat(new KoreanCharacter('한').decompose(true, false)).isEqualTo(new char[] {'ㅎ', 'ㅏ', 'ㄴ'});
		assertThat(new KoreanCharacter('ㅘ').decompose(true, false)).isEqualTo(new char[] {'ㅘ'});
		assertThat(new KoreanCharacter('ᆬ').decompose(true, false)).isEqualTo(new char[] {'ㄵ'});
	}

	@Test
	@DisplayName("decompose(useCompatibilityJamo: false, separateDoubleConsonantAndVowel: true)")
	void testDecompose_conjoiningJamo_separateDoubleConsonantAndVowel() {
		assertThat(new KoreanCharacter().decompose(false, true)).isEqualTo(new char[] {});
		assertThat(new KoreanCharacter('a').decompose(false, true)).isEqualTo(new char[] {'a'});
		assertThat(new KoreanCharacter('ㅎ').decompose(false, true)).isEqualTo(new char[] {'ᄒ'});
		assertThat(new KoreanCharacter('하').decompose(false, true)).isEqualTo(new char[] {'ᄒ', 'ᅡ'});
		assertThat(new KoreanCharacter('한').decompose(false, true)).isEqualTo(new char[] {'ᄒ', 'ᅡ', 'ᆫ'});
		assertThat(new KoreanCharacter('ㅘ').decompose(false, true)).isEqualTo(new char[] {'ᅩ', 'ᅡ'});
		assertThat(new KoreanCharacter('ᆬ').decompose(false, true)).isEqualTo(new char[] {'ᆫ', 'ᆽ'});
	}

	@Test
	@DisplayName("decompose(useCompatibilityJamo: false, separateDoubleConsonantAndVowel: false)")
	void testDecompose_conjoiningJamo_keepDoubleConsonantAndVowel() {
		assertThat(new KoreanCharacter().decompose(false, false)).isEqualTo(new char[] {});
		assertThat(new KoreanCharacter('a').decompose(false, false)).isEqualTo(new char[] {'a'});
		assertThat(new KoreanCharacter('ㅎ').decompose(false, false)).isEqualTo(new char[] {'ᄒ'});
		assertThat(new KoreanCharacter('하').decompose(false, false)).isEqualTo(new char[] {'ᄒ', 'ᅡ'});
		assertThat(new KoreanCharacter('한').decompose(false, false)).isEqualTo(new char[] {'ᄒ', 'ᅡ', 'ᆫ'});
		assertThat(new KoreanCharacter('ㅘ').decompose(false, false)).isEqualTo(new char[] {'ᅪ'});
		assertThat(new KoreanCharacter('ᆬ').decompose(false, false)).isEqualTo(new char[] {'ᆬ'});
	}

	@Test
	@DisplayName("include()")
	void testInclude() {
		KoreanCharacter c = new KoreanCharacter('한');
		assertThat(c.include(new KoreanCharacter('ㅎ'))).isTrue();
		assertThat(c.include(new KoreanCharacter('ㅏ'))).isTrue();
		assertThat(c.include(new KoreanCharacter('하'))).isTrue();
		assertThat(c.include(new KoreanCharacter('한'))).isTrue();
		assertThat(c.include(new KoreanCharacter('ㄴ'))).isFalse();
		assertThat(c.include(new KoreanCharacter('반'))).isFalse();
		assertThat(c.include(new KoreanCharacter('A'))).isFalse();
	}

	@Test
	@DisplayName("isKoreanCharacter()")
	void testIsKoreanCharacter() {
		assertThat(KoreanCharacter.isKoreanCharacter(null)).isFalse();
		assertThat(KoreanCharacter.isKoreanCharacter('a')).isFalse();
		assertThat(KoreanCharacter.isKoreanCharacter('한')).isTrue();
		assertThat(KoreanCharacter.isKoreanCharacter('ㅎ')).isTrue();
		assertThat(KoreanCharacter.isKoreanCharacter('ㅏ')).isTrue();
		assertThat(KoreanCharacter.isKoreanCharacter('ㄴ')).isTrue();
	}

	@Test
	@DisplayName("convert()")
	void testConvert() {
		assertThat(KoreanCharacter.convert("hello 한글").stream()
			.map(KoreanCharacter::getCharacter)
			.map(Object::toString)
			.collect(Collectors.joining())
		).isEqualTo("hello 한글");
	}

	@Test
	@DisplayName("compareTo()")
	void testCompare() {
		KoreanCharacter a = new KoreanCharacter('가');
		KoreanCharacter b = new KoreanCharacter('힣');
		assertThat(a).isLessThan(b);
		assertThat(b).isGreaterThan(a);
	}

	@Test
	@DisplayName("equals()")
	void testEquals() {
		assertThat(new KoreanCharacter(Jamo.Choseong.ㄱ, Jamo.Jungseong.ㅏ, null)).isEqualTo(new KoreanCharacter('가'));
	}

	@Test
	@DisplayName("hashCode()")
	void testHashcode() {
		assertThat(new KoreanCharacter('가').hashCode()).isEqualTo(44032);
		assertThat(new KoreanCharacter('힣').hashCode()).isEqualTo(55203);
	}

	@Test
	@DisplayName("toString()")
	void testToString() {
		assertThat(new KoreanCharacter('가')).hasToString("가");
		assertThat(new KoreanCharacter('힣')).hasToString("힣");
	}
}
