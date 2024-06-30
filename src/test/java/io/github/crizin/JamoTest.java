package io.github.crizin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class JamoTest {

	@Test
	@DisplayName("getConjoiningJamo() Test")
	void testJamoGetConjoiningJamo() {
		Jamo.Choseong.ㄱ.compose(Jamo.Choseong.ㄱ);

		assertThat(Jamo.Choseong.ㄱ.getConjoiningJamo()).isEqualTo('ᄀ');
		assertThat(Jamo.Choseong.ㄲ.getConjoiningJamo()).isEqualTo('ᄁ');
		assertThat(Jamo.Jungseong.ㅏ.getConjoiningJamo()).isEqualTo('ᅡ');
		assertThat(Jamo.Jungseong.ㅘ.getConjoiningJamo()).isEqualTo('ᅪ');
		assertThat(Jamo.Jongseong.ㄱ.getConjoiningJamo()).isEqualTo('ᆨ');
		assertThat(Jamo.Jongseong.ㄲ.getConjoiningJamo()).isEqualTo('ᆩ');
	}

	@Test
	@DisplayName("getCompatibilityJamo() Test")
	void testJamoGetCompatibilityJamo() {
		assertThat(Jamo.Choseong.ㄱ.getCompatibilityJamo()).isEqualTo('ㄱ');
		assertThat(Jamo.Choseong.ㄲ.getCompatibilityJamo()).isEqualTo('ㄲ');
		assertThat(Jamo.Jungseong.ㅏ.getCompatibilityJamo()).isEqualTo('ㅏ');
		assertThat(Jamo.Jungseong.ㅘ.getCompatibilityJamo()).isEqualTo('ㅘ');
		assertThat(Jamo.Jongseong.ㄱ.getCompatibilityJamo()).isEqualTo('ㄱ');
		assertThat(Jamo.Jongseong.ㄲ.getCompatibilityJamo()).isEqualTo('ㄲ');
	}

	@Test
	@DisplayName("getComponents(useCompatibilityJamo: true) Test")
	void testJamoGetComponents_true() {
		assertThat(Jamo.Choseong.ㄱ.getComponents(true)).isEqualTo("ㄱ");
		assertThat(Jamo.Choseong.ㄲ.getComponents(true)).isEqualTo("ㄱㄱ");
		assertThat(Jamo.Jungseong.ㅏ.getComponents(true)).isEqualTo("ㅏ");
		assertThat(Jamo.Jungseong.ㅘ.getComponents(true)).isEqualTo("ㅗㅏ");
		assertThat(Jamo.Jongseong.ㄱ.getComponents(true)).isEqualTo("ㄱ");
		assertThat(Jamo.Jongseong.ㄲ.getComponents(true)).isEqualTo("ㄱㄱ");
	}

	@Test
	@DisplayName("getComponents(useCompatibilityJamo: false) Test")
	void testJamoGetComponents_false() {
		assertThat(Jamo.Choseong.ㄱ.getComponents(false)).isEqualTo("ᄀ");
		assertThat(Jamo.Choseong.ㄲ.getComponents(false)).isEqualTo("ᄀᄀ");
		assertThat(Jamo.Jungseong.ㅏ.getComponents(false)).isEqualTo("ᅡ");
		assertThat(Jamo.Jungseong.ㅘ.getComponents(false)).isEqualTo("ᅩᅡ");
		assertThat(Jamo.Jongseong.ㄱ.getComponents(false)).isEqualTo("ᆨ");
		assertThat(Jamo.Jongseong.ㄲ.getComponents(false)).isEqualTo("ᆨᆨ");
	}

	@Test
	@DisplayName("compose() Test")
	void testJamoCompose() {
		assertThat(Jamo.Choseong.ㄱ.compose(Jamo.Choseong.ㄱ)).isEqualTo(Jamo.Choseong.ㄲ);
		assertThat(Jamo.Jungseong.ㅗ.compose(Jamo.Jungseong.ㅏ)).isEqualTo(Jamo.Jungseong.ㅘ);
		assertThat(Jamo.Jongseong.ㄹ.compose(Jamo.Jongseong.ㄱ)).isEqualTo(Jamo.Jongseong.ㄺ);

		assertThatThrownBy(() -> Jamo.Choseong.ㅇ.compose(Jamo.Choseong.ㅇ))
			.isInstanceOf(Jamo.JamoComposeException.class);
		assertThatThrownBy(() -> Jamo.Jungseong.ㅜ.compose(Jamo.Jungseong.ㅏ))
			.isInstanceOf(Jamo.JamoComposeException.class);
		assertThatThrownBy(() -> Jamo.Jongseong.ㅇ.compose(Jamo.Jongseong.ㅇ))
			.isInstanceOf(Jamo.JamoComposeException.class);
	}

	@Test
	@DisplayName("find() Test")
	void testJamoFind() {
		assertThat(Jamo.Choseong.find('ᄀ')).isEqualTo(Jamo.Choseong.ㄱ);
		assertThat(Jamo.Choseong.find('ㄱ')).isEqualTo(Jamo.Choseong.ㄱ);
		assertThat(Jamo.Choseong.find('ᆨ')).isNull();
		assertThat(Jamo.Choseong.find('ㅜ')).isNull();

		assertThat(Jamo.Jungseong.find('ㅏ')).isEqualTo(Jamo.Jungseong.ㅏ);
		assertThat(Jamo.Jungseong.find('ᅡ')).isEqualTo(Jamo.Jungseong.ㅏ);
		assertThat(Jamo.Jungseong.find('ㄱ')).isNull();

		assertThat(Jamo.Jongseong.find('ᆨ')).isEqualTo(Jamo.Jongseong.ㄱ);
		assertThat(Jamo.Jongseong.find('ㄱ')).isEqualTo(Jamo.Jongseong.ㄱ);
		assertThat(Jamo.Jongseong.find('ᄀ')).isNull();
		assertThat(Jamo.Jongseong.find('ㅜ')).isNull();
	}

	@Test
	@DisplayName("Choseong definition test")
	void testChoseong() {
		char codePoint = 0x1100;
		for (Jamo.Choseong choseong : Jamo.Choseong.values()) {
			assertThat(choseong.getConjoiningJamo()).isEqualTo(codePoint++);
		}

		char[] codePoints = new char[] {
			0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148,
			0x3149, 0x314A, 0x314B, 0x314C, 0x314D, 0x314E
		};
		for (int i = 0; i < Jamo.Choseong.values().length; i++) {
			assertThat(Jamo.Choseong.values()[i].getCompatibilityJamo()).isEqualTo(codePoints[i]);
		}
	}

	@Test
	@DisplayName("Jungseong definition test")
	void testJungseong() {
		char codePoint = 0x1161;
		for (Jamo.Jungseong jungseong : Jamo.Jungseong.values()) {
			assertThat(jungseong.getConjoiningJamo()).isEqualTo(codePoint++);
		}

		char[] codePoints = new char[] {
			0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a, 0x315b,
			0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163
		};
		for (int i = 0; i < Jamo.Jungseong.values().length; i++) {
			assertThat(Jamo.Jungseong.values()[i].getCompatibilityJamo()).isEqualTo(codePoints[i]);
		}
	}

	@Test
	@DisplayName("Jongseong definition test")
	void testJongseong() {
		char codePoint = 0x11a8;
		for (Jamo.Jongseong jongseong : Jamo.Jongseong.values()) {
			assertThat(jongseong.getConjoiningJamo()).isEqualTo(codePoint++);
		}

		char[] codePoints = new char[] {
			0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c, 0x313d, 0x313e,
			0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d,
			0x314e
		};
		for (int i = 0; i < Jamo.Jongseong.values().length; i++) {
			assertThat(Jamo.Jongseong.values()[i].getCompatibilityJamo()).isEqualTo(codePoints[i]);
		}
	}
}
