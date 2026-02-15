# Moxymind Autotesty
Projekt Moxymind obsahuje automatizované API a FE testy pre overenie funkcionality systému.

---

## Sumár

- [Setup](#setup)
- [Spustenie testov](#spustenie-testov)

---

## Setup

1. Uistite sa, že máte nainštalovanú **Javu 17** a **Gradle 9+**.
2. Projekt musí byť sklonovaný alebo uložený v lokálnom adresári.
3. Testy sú umiestnené v `src/test/java`:
    - **API testy** používajú **JUnit 5** a **RestAssured knižnicu**.
    - **FE testy** používajú **JUnit 5** a **Selenide framework**.
4. Pre FE testy je potrebné mať nainštalovaný podporovaný prehliadač (napr. Chrome alebo Firefox)


---

## Spustenie testov


- Spustenie API testov (Test Case 1 a 2):

```bash
./gradlew test --tests "tests.API_tests.*"
```
- Spustenie FE testov (Test Case 1 - 5):
```bash
./gradlew test --tests "tests.UI_tests.*"
```
Spustenie všetkych testov cez Test Suite:
```bash
./gradlew test --tests suite.MoxyMindTestSuite

alebo

./gradlew test --tests "tests.**"
```