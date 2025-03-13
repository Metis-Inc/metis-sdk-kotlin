package com.metis.sdk.common.exceptions

/**
 * Base exception for all Metis SDK exceptions.
 */
open class MetisException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Exception thrown when there is a network error.
 */
class MetisNetworkException(message: String, cause: Throwable? = null) : MetisException(message, cause)

/**
 * Exception thrown when there is an authentication error.
 */
class MetisAuthException(message: String, cause: Throwable? = null) : MetisException(message, cause)

/**
 * Exception thrown when there is an API error.
 */
class MetisApiException(message: String, statusCode: Int, cause: Throwable? = null) :
    MetisException("API error (status $statusCode): $message", cause)

/**
 * Exception thrown when there is a validation error.
 */
class MetisValidationException(message: String, cause: Throwable? = null) : MetisException(message, cause)

/**
 * Exception thrown when a resource is not found.
 */
class MetisNotFoundException(message: String, cause: Throwable? = null) : MetisException(message, cause)