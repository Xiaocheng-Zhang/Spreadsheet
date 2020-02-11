package edu.cs3500.spreadsheets.model;

/**
 * generic function interface.
 *
 * @param <A> the input
 * @param <R> the output
 */
public interface IFunc<A, R> {
  R apply(A arg);
}
