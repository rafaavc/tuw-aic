package aic.g3t1.common.environment;

import aic.g3t1.common.exceptions.MissingEnvironmentVariableException;

public class EnvironmentVariables {
    public static String getVariable(String variableName) throws MissingEnvironmentVariableException {
        String value = System.getenv(variableName);
        if (value == null)
            throw new MissingEnvironmentVariableException(variableName);
        return value;
    }
}
