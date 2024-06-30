[English](README.md) | [Korean](README.ko.md)

# Korean Utils

[![Build](https://github.com/crizin/korean-utils/actions/workflows/build.yml/badge.svg)](https://github.com/crizin/korean-utils/actions)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/274ee8e6cb014384b35cc6e4a3b82718)](https://app.codacy.com/gh/crizin/korean-utils/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=crizin_korean-utils&metric=alert_status)](https://sonarcloud.io/summary/overall?id=crizin_korean-utils)
[![License: MIT](https://img.shields.io/github/license/crizin/korean-utils)](https://opensource.org/licenses/MIT)

Korean Utils is a Java library for handling and manipulating Korean text. It provides utility classes for working with Korean characters,
including decomposition and composition of Hangul syllables, text searching considering Korean syllable structure,
and various other operations useful for processing Korean text.

## Features

- Decomposition and composition of Korean syllables
- Korean text search with support for partial matching
- Conversion between English and Korean keyboard layouts
- Proper particle (조사) attachment for Korean words
- N-gram generation for Korean text
- Length calculation considering Korean character width

## Installation

You can include this library in your project using Maven or Gradle.

### Maven

Add the following dependency to your `pom.xml`:

```xml

<dependency>
	<groupId>io.github.crizin</groupId>
	<artifactId>korean-utils</artifactId>
	<version>1.0.0</version>
</dependency>
```

### Gradle

Add the following dependency to your build.gradle:

```groovy
implementation 'io.github.crizin:korean-utils:1.0.0'
```

## Usage

### KoreanCharacter

`KoreanCharacter` represents a single Korean character (Hangul syllable) and provides methods for its manipulation.

#### Constructors

```java
public KoreanCharacter() {
}

public KoreanCharacter(Character character) {
}

public KoreanCharacter(Jamo.Choseong choseong, Jamo.Jungseong jungseong, Jamo.Jongseong jongseong) {
}
```

#### Methods

- `getCharacter()`: Returns the Unicode character representation.
- `getChoseong()`: Returns the Choseong (initial consonant).
- `getJungseong()`: Returns the Jungseong (vowel).
- `getJongseong()`: Returns the Jongseong (final consonant).
- `compose(char character)`: Composes a new KoreanCharacter by adding a Jamo.
- `decompose()`: Decomposes the Korean character into its constituent Jamo.
- `include(KoreanCharacter other)`: Checks if this KoreanCharacter includes the specified KoreanCharacter.

#### Example

```java
class KoreanCharacterTest {

	@Test
	void test() {
		KoreanCharacter kc = new KoreanCharacter('한');

		assertThat(kc.getCharacter()).isEqualTo('한');
		assertThat(kc.getChoseong()).isEqualTo(Jamo.Choseong.ㅎ);
		assertThat(kc.getJungseong()).isEqualTo(Jamo.Jungseong.ㅏ);
		assertThat(kc.getJongseong()).isEqualTo(Jamo.Jongseong.ㄴ);
		assertThat(kc.compose('ㅈ')).isEqualTo(new KoreanCharacter('핝'));
		assertThat(kc.decompose(true, true)).containsExactly('ㅎ', 'ㅏ', 'ㄴ');
		assertThat(kc.include(new KoreanCharacter('ㅎ'))).isTrue();
		assertThat(KoreanCharacter.isKoreanCharacter('한')).isTrue();
		assertThat(KoreanCharacter.convert("한글")).containsExactly(
			new KoreanCharacter('한'),
			new KoreanCharacter('글')
		);
	}
}
```

### KoreanUtils

The `KoreanUtils` class provides utility methods for handling Korean text.

#### Methods

- `length(CharSequence text, int koreanLength)`: Calculates the length of text, applying a specified length for Korean characters.
- `contains(CharSequence text, CharSequence searchText)`: Checks if the text contains the search text, considering Korean Jamo components.
- `containsKorean(CharSequence text)`: Checks if the text contains any Korean characters or Jamo.
- `startsWith(CharSequence text, String prefix)`: Checks if the text starts with the specified prefix, considering Korean Jamo components.
- `endsWith(CharSequence text, String suffix)`: Checks if the text ends with the specified suffix, considering Korean Jamo components.
- `compose(CharSequence decomposedText)`: Composes a string of decomposed Korean characters into composed form.
- `decompose(CharSequence composedText)`: Decomposes a string of Korean characters into their Jamo components.
- `convertEnglishTypedToKorean(CharSequence englishTypedText)`: Converts text typed using an English keyboard layout to its Korean equivalent.
- `convertKoreanTypedToEnglish(CharSequence koreanTypedText)`: Converts text typed using a Korean keyboard layout to its English equivalent.
- `attachJosa(CharSequence text, Josa josa)`: Attaches the appropriate Korean particle (조사) to the given text.
- `ngram(CharSequence text, int length)`: Generates n-grams from Korean text based on Jamo decomposition.

#### Example

```java
class KoreanCharacterTest {

	@Test
	void test() {
		assertThat(KoreanUtils.length("한글ABC", 2)).isEqualTo(7);
		assertThat(KoreanUtils.contains("한글", "ㅎㄱ")).isTrue();
		assertThat(KoreanUtils.containsKorean("한글")).isTrue();
		assertThat(KoreanUtils.startsWith("한글", "ㅎ")).isTrue();
		assertThat(KoreanUtils.endsWith("한글", "ㄹ")).isTrue();
		assertThat(KoreanUtils.compose("ㅎㅏㄴ")).isEqualTo("한");
		assertThat(KoreanUtils.decompose("한")).isEqualTo("ㅎㅏㄴ");
		assertThat(KoreanUtils.convertEnglishTypedToKorean("gks")).isEqualTo("한");
		assertThat(KoreanUtils.convertKoreanTypedToEnglish("한")).isEqualTo("gks");
		assertThat(KoreanUtils.attachJosa("한", KoreanUtils.Josa.은_는)).isEqualTo("한은");
		assertThat(KoreanUtils.ngram("한국어", 2))
			.containsExactly("하", "ㅏㄴ", "ㄴㄱ", "구", "ㅜㄱ", "ㄱㅇ", "어");
	}
}
```

## License

This project is licensed under the [MIT License](https://opensource.org/license/MIT) - see the [LICENSE](LICENSE) file for details.
