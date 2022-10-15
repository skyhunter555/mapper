package ru.adapter.mapper.exceptions;

/**
 * Wrapper over RuntimeException. Includes additional options for formatting message text.
 *
 * @author Skyhunter
 * @date 14.10.2022
 */
public class AdapterMapperException extends RuntimeException {

    public AdapterMapperException(String message) {
        super(message);
    }

    public AdapterMapperException(String messageFormat, Object... messageArgs) {
        super(String.format(messageFormat, messageArgs));
    }

    public AdapterMapperException(Throwable throwable) {
        super(throwable);
    }

}
