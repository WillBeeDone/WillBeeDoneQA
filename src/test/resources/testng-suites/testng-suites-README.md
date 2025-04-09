# 📦 TestNG Suites для проекта WillBeeDoneQA

Здесь лежат все `.xml` файлы, используемые для запуска автотестов через Jenkins и локально через TestNG.

## 📁 Структура каталогов

Каждый участник команды создаёт свою папку с именем:
```
testng-suites/
├── jura/
├── ivan/
├── nastasiya/
└── natalia/
```

Внутри — XML-файлы с нужными сценариями запуска.

---

## 🧪 1. Пример XML для запуска целого класса

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="RunSingleClass">
  <test name="GetApiSecurityTests">
    <classes>
      <class name="wbd.tests.rest_assured.GetApiSecurityTests"/>
    </classes>
  </test>
</suite>
```

📌 Запустит **все методы** из указанного класса.

---

## 🧬 2. Пример XML для запуска конкретного метода

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="RunSingleMethod">
  <test name="TestSingleMethod">
    <classes>
      <class name="wbd.tests.rest_assured.GetApiSecurityTests">
        <methods>
          <include name="testSqlInjectionInOfferFilter"/>
        </methods>
      </class>
    </classes>
  </test>
</suite>
```

📌 Запустит только `testSqlInjectionInOfferFilter()` из указанного класса.

---

## 🏷 3. Пример XML для запуска по группе аннотаций

Тесты в коде должны быть помечены `@Test(groups = "...")`

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="RunByGroup">
  <test name="RunSecurityGroup">
    <groups>
      <run>
        <include name="security"/>
      </run>
    </groups>
    <packages>
      <package name="wbd.tests.rest_assured"/>
    </packages>
  </test>
</suite>
```

📌 Запустит все тесты в пакете `wbd.tests.rest_assured`, у которых указана `@Test(groups = "security")`.

---

## ⚠️ Рекомендации

- Имена классов, пакетов и методов **указываются полностью**, включая путь.
- XML можно запускать и в IntelliJ, и в Jenkins.
- Для новых видов тестов — создавайте новые XML (например `ui.xml`, `db.xml`, `performance.xml`).

---

💬 Вопросы — к Юре. Всё пойдёт в дело 😎