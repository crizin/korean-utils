package io.github.crizin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class KoreanCharacterUtilsTest {

	@Test
	@DisplayName("length()")
	void testLength() {
		assertThat(KoreanUtils.length(null, 3)).isZero();
		assertThat(KoreanUtils.length("a", 3)).isEqualTo(1);
		assertThat(KoreanUtils.length("가", 3)).isEqualTo(3);
		assertThat(KoreanUtils.length("일이삼123", 3)).isEqualTo(12);
	}

	@Test
	@DisplayName("contains()")
	void testContains() {
		String text = "홍길동";

		assertThat(KoreanUtils.contains(text, "홍길동")).isTrue();
		assertThat(KoreanUtils.contains(text, "ㅎㄱㄷ")).isTrue();
		assertThat(KoreanUtils.contains(text, "ㅎ")).isTrue();
		assertThat(KoreanUtils.contains(text, "호")).isTrue();
		assertThat(KoreanUtils.contains(text, "홍")).isTrue();
		assertThat(KoreanUtils.contains(text, "홍ㄱ")).isTrue();
		assertThat(KoreanUtils.contains(text, "홍길")).isTrue();
		assertThat(KoreanUtils.contains(text, "ㄱ")).isTrue();
		assertThat(KoreanUtils.contains(text, "기")).isTrue();
		assertThat(KoreanUtils.contains(text, "길")).isTrue();
		assertThat(KoreanUtils.contains(text, "길ㄷ")).isTrue();

		assertThat(KoreanUtils.contains(text, null)).isFalse();
		assertThat(KoreanUtils.contains(text, "a")).isFalse();
		assertThat(KoreanUtils.contains(text, "ㅇ")).isFalse();
		assertThat(KoreanUtils.contains(text, "김")).isFalse();
		assertThat(KoreanUtils.contains(text, "홍ㄹ")).isFalse();
	}

	@Test
	@DisplayName("containsKorean()")
	void testContainsKorean() {
		assertThat(KoreanUtils.containsKorean("홍길동")).isTrue();
		assertThat(KoreanUtils.containsKorean("ab길cd")).isTrue();

		assertThat(KoreanUtils.containsKorean(null)).isFalse();
		assertThat(KoreanUtils.containsKorean("")).isFalse();
		assertThat(KoreanUtils.containsKorean("abcd")).isFalse();
	}

	@Test
	@DisplayName("startsWith()")
	void testStartsWith() {
		assertThat(KoreanUtils.startsWith("홍길동", "")).isTrue();
		assertThat(KoreanUtils.startsWith("홍길동", "ㅎ")).isTrue();
		assertThat(KoreanUtils.startsWith("홍길동", "호")).isTrue();
		assertThat(KoreanUtils.startsWith("홍길동", "홍")).isTrue();
		assertThat(KoreanUtils.startsWith("홍길동", "홍ㄱ")).isTrue();
		assertThat(KoreanUtils.startsWith("홍길동", "홍길동")).isTrue();

		assertThat(KoreanUtils.startsWith("홍길동", null)).isFalse();
		assertThat(KoreanUtils.startsWith("홍길동", "ㅎ길")).isFalse();
		assertThat(KoreanUtils.startsWith("홍길동", "길")).isFalse();
	}

	@Test
	@DisplayName("endsWith()")
	void testEndsWith() {
		assertThat(KoreanUtils.endsWith("홍길동", "")).isTrue();
		assertThat(KoreanUtils.endsWith("홍길동", "ㅇ")).isTrue();
		assertThat(KoreanUtils.endsWith("홍길동", "동")).isTrue();
		assertThat(KoreanUtils.endsWith("홍길동", "ㄹ동")).isTrue();
		assertThat(KoreanUtils.endsWith("홍길동", "길동")).isTrue();
		assertThat(KoreanUtils.endsWith("홍길동", "ㅇ길동")).isTrue();
		assertThat(KoreanUtils.endsWith("홍길동", "홍길동")).isTrue();

		assertThat(KoreanUtils.endsWith("홍길동", null)).isFalse();
		assertThat(KoreanUtils.endsWith("홍길동", "길ㄷ")).isFalse();
		assertThat(KoreanUtils.endsWith("홍길동", "길")).isFalse();
	}

	@Test
	@DisplayName("compose()")
	void testCompose() {
		assertThat(KoreanUtils.compose(null)).isEmpty();
		assertThat(KoreanUtils.compose("")).isEmpty();
		assertThat(KoreanUtils.compose("ab cd")).isEqualTo("ab cd");
		assertThat(KoreanUtils.compose("ㄷㅏㄺㄱㅗㄱㅣ")).isEqualTo("닭고기");
		assertThat(KoreanUtils.compose("닭고기")).isEqualTo("닭고기");
		assertThat(KoreanUtils.compose("ㅎㅏㄴㅏ")).isEqualTo("하나");
		assertThat(KoreanUtils.compose("하나")).isEqualTo("하나");
	}

	@Test
	@DisplayName("decompose()")
	void testDecompose() {
		assertThat(KoreanUtils.decompose(null)).isEmpty();
		assertThat(KoreanUtils.decompose("")).isEmpty();
		assertThat(KoreanUtils.decompose("ab cd")).isEqualTo("ab cd");
		assertThat(KoreanUtils.decompose("닭고기", true, true)).isEqualTo("ㄷㅏㄹㄱㄱㅗㄱㅣ");
		assertThat(KoreanUtils.decompose("닭고기", true, false)).isEqualTo("ㄷㅏㄺㄱㅗㄱㅣ");
		assertThat(KoreanUtils.decompose("닭고기", false, true)).isEqualTo("달ᆨ고기");
		assertThat(KoreanUtils.decompose("닭고기", false, false)).isEqualTo("닭고기");
	}

	@Test
	@DisplayName("convertEnglishTypedToKorean()")
	void testConvertEnglishTypedToKorean() {
		assertThat(KoreanUtils.convertEnglishTypedToKorean("ghdrlfehd")).isEqualTo("홍길동");
		assertThat(KoreanUtils.convertEnglishTypedToKorean("RKCL")).isEqualTo("까치");
	}

	@Test
	@DisplayName("convertKoreanTypedToEnglish()")
	void testConvertKoreanTypedToEnglish() {
		assertThat(KoreanUtils.convertKoreanTypedToEnglish("홍길동")).isEqualTo("ghdrlfehd");
		assertThat(KoreanUtils.convertKoreanTypedToEnglish("까치")).isEqualTo("Rkcl");
	}

	@Test
	@DisplayName("attachJosa()")
	void testAttachJosa() {
		assertThat(KoreanUtils.attachJosa(null, KoreanUtils.Josa.은_는)).isEmpty();

		assertThat(KoreanUtils.attachJosa("사슴", KoreanUtils.Josa.은_는)).isEqualTo("사슴은");
		assertThat(KoreanUtils.attachJosa("사자", KoreanUtils.Josa.은_는)).isEqualTo("사자는");
		assertThat(KoreanUtils.attachJosa("TV", KoreanUtils.Josa.은_는)).isEqualTo("TV는");
		assertThat(KoreanUtils.attachJosa("123", KoreanUtils.Josa.은_는)).isEqualTo("123은");
		assertThat(KoreanUtils.attachJosa("1234", KoreanUtils.Josa.은_는)).isEqualTo("1234는");
		assertThat(KoreanUtils.attachJosa("道路", KoreanUtils.Josa.은_는)).isEqualTo("道路는");
		assertThat(KoreanUtils.attachJosa("Sofia", KoreanUtils.Josa.은_는)).isEqualTo("Sofia는");
		assertThat(KoreanUtils.attachJosa("Hazel", KoreanUtils.Josa.은_는)).isEqualTo("Hazel은");

		assertThat(KoreanUtils.attachJosa("사슴", KoreanUtils.Josa.이_가)).isEqualTo("사슴이");
		assertThat(KoreanUtils.attachJosa("사자", KoreanUtils.Josa.이_가)).isEqualTo("사자가");
		assertThat(KoreanUtils.attachJosa("사슴", KoreanUtils.Josa.을_를)).isEqualTo("사슴을");
		assertThat(KoreanUtils.attachJosa("사자", KoreanUtils.Josa.을_를)).isEqualTo("사자를");
		assertThat(KoreanUtils.attachJosa("사슴", KoreanUtils.Josa.과_와)).isEqualTo("사슴과");
		assertThat(KoreanUtils.attachJosa("사자", KoreanUtils.Josa.과_와)).isEqualTo("사자와");
		assertThat(KoreanUtils.attachJosa("사슴", KoreanUtils.Josa.으로_로)).isEqualTo("사슴으로");
		assertThat(KoreanUtils.attachJosa("사자", KoreanUtils.Josa.으로_로)).isEqualTo("사자로");
		assertThat(KoreanUtils.attachJosa("사슴", KoreanUtils.Josa.아_야)).isEqualTo("사슴아");
		assertThat(KoreanUtils.attachJosa("사자", KoreanUtils.Josa.아_야)).isEqualTo("사자야");
	}

	@Test
	@DisplayName("ngram()")
	void testNgram() {
		assertThat(KoreanUtils.ngram("abcd", 2)).containsExactly("ab", "bc", "cd");
		assertThat(KoreanUtils.ngram("홍길동", 2)).containsExactly("호", "ㅗㅇ", "ㅇㄱ", "기", "ㅣㄹ", "ㄹㄷ", "도", "ㅗㅇ");
		assertThat(KoreanUtils.ngram("홍길동", 3)).containsExactly("홍", "ㅗㅇㄱ", "ㅇ기", "길", "ㅣㄹㄷ", "ㄹ도", "동");
		assertThat(KoreanUtils.ngram("홍길동", 2, true)).containsExactly("ㅎㅗ", "ㅗㅇ", "ㅇㄱ", "ㄱㅣ", "ㅣㄹ", "ㄹㄷ", "ㄷㅗ", "ㅗㅇ");
		assertThat(KoreanUtils.ngram("홍길동", 3, true)).containsExactly("ㅎㅗㅇ", "ㅗㅇㄱ", "ㅇㄱㅣ", "ㄱㅣㄹ", "ㅣㄹㄷ", "ㄹㄷㅗ", "ㄷㅗㅇ");
		assertThat(KoreanUtils.ngram("닭발", 2, true)).containsExactly("ㄷㅏ", "ㅏㄹ", "ㄹㄱ", "ㄱㅂ", "ㅂㅏ", "ㅏㄹ");
		assertThat(KoreanUtils.ngram("닭발", 2, false)).containsExactly("다", "ㅏㄺ", "ㄺㅂ", "바", "ㅏㄹ");
	}
}
