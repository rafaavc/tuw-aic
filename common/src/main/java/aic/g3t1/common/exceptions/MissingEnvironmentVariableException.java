package aic.g3t1.common.exceptions;

public class MissingEnvironmentVariableException extends Exception {
    public MissingEnvironmentVariableException(String variableName) {
        super(String.format("Missing environment variable '%s'", variableName));
    }
}
