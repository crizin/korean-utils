[영어](README.md) | [한국어](README.ko.md)

# Korean Utils

[![Build](https://github.com/crizin/korean-utils/actions/workflows/build.yml/badge.svg)](https://github.com/crizin/korean-utils/actions)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/274ee8e6cb014384b35cc6e4a3b82718)](https://app.codacy.com/gh/crizin/korean-utils/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=crizin_korean-utils&metric=alert_status)](https://sonarcloud.io/summary/overall?id=crizin_korean-utils)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=crizin_korean-utils&metric=coverage)](https://sonarcloud.io/summary/overall?id=crizin_korean-utils)
[![License: MIT](https://img.shields.io/github/license/crizin/korean-utils)](https://opensource.org/licenses/MIT)

Korean Utils는 한글 텍스트를 처리하고 조작하기 위한 Java 라이브러리입니다.
한글 음절의 분해와 조합, 한글 음절 구조를 고려한 텍스트 검색, 그리고 한글 텍스트 처리에 유용한 다양한 기능을 제공합니다.

## Features

- 한글 음절의 분해 및 조합
- 부분 일치를 지원하는 한글 텍스트 검색
- 영문 키보드 레이아웃과 한글 키보드 레이아웃 사이의 변환
- 한글 단어에 대한 적절한 조사 붙이기
- 한글 텍스트에 대한 N-gram 생성
- 한글 문자 길이를 고려한 길이 계산

## Installation

Maven이나 Gradle을 사용하여 프로젝트에 이 라이브러리를 포함할 수 있습니다.

### Maven

`pom.xml`에 다음 의존성을 추가하세요.

```xml

<dependency>
    <groupId>io.github.crizin</groupId>
    <artifactId>korean-utils</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

`build.gradle`에 다음 의존성을 추가하세요.

```groovy
implementation 'io.github.crizin:korean-utils:1.0.0'
```

## 사용법

### KoreanCharacter

`KoreanCharacter` 클래스는 단일 한글 문자를 나타내며 이를 조작하는 메서드를 제공합니다.

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

- `getCharacter()`: Unicode 문자 표현을 반환합니다.
- `getChoseong()`: 초성을 반환합니다.
- `getJungseong()`: 중성을 반환합니다.
- `getJongseong()`: 종성을 반환합니다.
- `compose(char character)`: 자모를 조합하여 새로운 KoreanCharacter를 생성합니다.
- `decompose()`: 한글 문자를 자모로 분해합니다.
- `include(KoreanCharacter other)`: 이 KoreanCharacter가 지정된 KoreanCharacter를 포함하는지 확인합니다.

#### 예제

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

`KoreanUtils` 클래스는 한글 텍스트를 처리하기 위한 유틸리티 메서드를 제공합니다.

#### Methods

- `length(CharSequence text, int koreanLength)`: 한글 문자에 지정된 길이를 적용하여 텍스트의 길이를 계산합니다.
- `contains(CharSequence text, CharSequence queryText)`: 한글 자모 구성요소를 고려하여 텍스트에 검색 텍스트가 포함되어 있는지 확인합니다.
- `containsKorean(CharSequence text)`: 텍스트에 한글 문자나 자모가 포함되어 있는지 확인합니다.
- `startsWith(CharSequence text, String prefix)`: 한글 자모 구성요소를 고려하여 텍스트가 지정된 접두사로 시작하는지 확인합니다.
- `endsWith(CharSequence text, String suffix)`: 한글 자모 구성요소를 고려하여 텍스트가 지정된 접미사로 끝나는지 확인합니다.
- `compose(CharSequence decomposedText)`: 분해된 한글 문자열을 조합된 형태로 변환합니다.
- `decompose(CharSequence composedText)`: 한글 문자열을 자모 구성요소로 분해합니다.
- `convertEnglishTypedToKorean(CharSequence englishTypedText)`: 영문 키보드 레이아웃으로 입력된 텍스트를 한글로 변환합니다.
- `convertKoreanTypedToEnglish(CharSequence koreanTypedText)`: 한글 키보드 레이아웃으로 입력된 텍스트를 영문으로 변환합니다.
- `attachJosa(CharSequence text, Josa josa)`: 주어진 텍스트에 적절한 한글 조사를 붙입니다.
- `ngram(CharSequence text, int length)`: 자모 분해를 기반으로 한글 텍스트의 n-gram을 생성합니다.

#### 예제

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

## 라이선스

이 프로젝트는 [MIT 라이선스](https://opensource.org/license/MIT)를 따릅니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.
