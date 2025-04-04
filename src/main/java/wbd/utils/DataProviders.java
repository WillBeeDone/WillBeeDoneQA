package wbd.utils;

import org.testng.annotations.DataProvider;

public class DataProviders {
    // payload'ами для SQL-инъекций
    @DataProvider(name = "sqlInjectionPayloads")
    public static Object[][] sqlInjectionPayloads() {
        return new Object[][]{
                {"' OR '1'='1"},
                {"\" OR \"1\"=\"1"},
                {"admin' --"},
                {"' UNION SELECT null,null --"},
                {"1; DROP TABLE users"},
                {"' OR '' = '"}
        };
    }

    @DataProvider
    public Object[][] invalidEmails() {
        return new Object[][]{
                {"test", "❌ Missing '@' and domain)"},
                {"test@", "❌ Missing domain"},
                {"test@gmail", "❌ Missing '.' and top-level domain (TLD)"},
                {"test@gmail.", "❌ Missing top-level domain (TLD)"},
                {"test@.com", "❌ Missing second-level domain"},
                {"test@@gmail.com", "❌ Superfluous '@' "},
                {"test@gmail..com", "❌ Superfluous '.'"},
                {"test@ gmail.com", "❌ Superfluous spaces"},
                {"test @gmail.com", "❌ Superfluous spaces"},
                {"test@gmail .com", "❌ Superfluous spaces"},
                {"test@gmail. com", "❌ Superfluous spaces"},
                {"я@есть.баг", "❌ Inadmissible symbols"}
        };
    }

    @DataProvider
    public Object[][] validPasswords() {
        return new Object[][]{
                {"Abc123!@#", "✅ Valid password with upper, lower, digits, specials"},
                {"Qwerty9$", "✅ Valid password (min 8 chars, all required types)"},
                {"Ümläüt8#", "✅ Password with German letters + digit + special"},
                {"Pässwörd1!", "✅ Password with umlauts + special + digit"}
        };
    }

    @DataProvider
    public Object[][] invalidPasswords() {
        return new Object[][]{
                {"12345678", "❌ Only digits"},
                {"Password", "❌ Only letters (no digits or specials)"},
                {"        ", "❌ Only spaces"},
                {" abc123!", "❌ Leading space"},
                {"abc123! ", "❌ Trailing space"},
                {"Abc 123!", "❌ Space in the middle"},
                {"user@mail.com", "❌ Password same as email"}
        };
    }

    @DataProvider
    public Object[][] categories() {
        return new Object[][]{
                {"Plumber"},
                {"Computer Technician"},
                {"Pet Care"},
                {"Moving"},
                {"Gardening Services"},
                {"Auto Mechanic"},
                {"Cleaning"},
                {"Electrician"},
                {"Beauty & Care"},
                {"Appliance Repair"},
                {"Handyman"},
                {"Other"}
        };
    }
}
