package com.developfuture.fortknox.utiles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regex {

    public boolean isValidInputValue(final String value) {

        Pattern pattern;
        Matcher matcher;

        final String InputValueFormat = "^(\\+|\\-)1?[0-9]\\d*(\\.\\d+)?$";

        pattern = Pattern.compile(InputValueFormat);
        matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
