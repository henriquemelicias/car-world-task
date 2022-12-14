package com.car_world.core.utils

/**
 * Represents a result class for error handling, in which you can have a structure that holds a
 * value on success and/or another on failure.
 *
 * The objective is to avoid problems with control flow caused by the use of exceptions as the
 * main way of error handling.
 * It's based on Railway Oriented Programming used by many functional programming languages.
 *
 * @see (https://blog.damo.io/posts/error-handling-in-kotlin-and-any-modern-static-type-system/)
 */
sealed class Result<A, E> {

    fun <B> map(mapping: (A) -> B): Result<B, E> =
        when (this) {
            is Success -> Success(mapping(value))
            is Failure -> Failure(reason)
        }

    fun <B> bind(mapping: (A) -> Result<B, E>): Result<B, E> =
        when (this) {
            is Success -> mapping(value)
            is Failure -> Failure(reason)
        }

    fun <F> mapFailure(mapping: (E) -> F): Result<A, F> =
        when (this) {
            is Success -> Success(value)
            is Failure -> Failure(mapping(reason))
        }

    fun bindFailure(mapping: (E) -> Result<A, E>): Result<A, E> =
        when (this) {
            is Success -> Success(value)
            is Failure -> mapping(reason)
        }

    fun orElse(other: A): A =
        when (this) {
            is Success -> value
            is Failure -> other
        }

    fun orElse(function: (E) -> A): A =
        when (this) {
            is Success -> value
            is Failure -> function(reason)
        }

    fun orNull(): A? =
        when (this) {
            is Success -> value
            is Failure -> null
        }

    // For functions that supposedly should never fail. If its fails, crash the program immediately, it's a bug.
    fun expect(error: String): A =
        when(this) {
            is Success -> value
            is Failure -> throw NullPointerException(error)
        }
}

data class Success<A, E>(val value: A) : Result<A, E>()
data class Failure<A, E>(val reason: E) : Result<A, E>()