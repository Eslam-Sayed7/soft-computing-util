package softcomputing.fuzzy.utils;

/**
 * Strategy for handling invalid/missing inputs
 */
public enum InputHandlingStrategy {
    CLAMP_TO_DOMAIN,
    USE_DEFAULT,
    THROW_ERROR
}