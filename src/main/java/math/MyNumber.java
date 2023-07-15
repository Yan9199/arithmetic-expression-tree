package math;

import exception.WrongOperandException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * The abstract class Number represents the numbers of the programming language Racket in a very
 * simplified way.
 */
public abstract class MyNumber {

    /**
     * Returns the representation of this number as an integer.
     *
     * @return the representation of this number as an integer.
     */
    public abstract BigInteger toInteger();

    /**
     * Returns the representation of this number as a rational number.
     *
     * @return the representation of this number as a rational number.
     */
    public abstract Rational toRational();

    /**
     * Returns the representation of this number as a real number.
     *
     * @return the representation of this number as a real number.
     */
    public abstract BigDecimal toReal();

    /**
     * Returns {@code true} if this number is zero.
     *
     * @return {@code true} if this number is zero
     */
    public abstract boolean isZero();

    /**
     * Returns the hash code for this {@code MyNumber}. The hash code is computed by the value
     * representation of this number.
     *
     * @return hash code for this {@code MyNumber}.
     * @see #equals(Object)
     */
    @Override
    public abstract int hashCode();

    /**
     * Compares this {@code MyNumber} with the specified {@code Object} for equality. Two {@code
     * MyNumber} objects equal only if they are equal their class and their value representation.
     * Therefore, 0.5 is not equal to 1/2 when compared by this.
     *
     * @param obj {@code Object} to which this {@code MyNumber} is to be compared.
     * @return {@code true} if and only if the specified {@code Object} is a {@code MyNumber} whose
     * value representation and class are equal to this {@code MyNumber}'s.
     * @see #hashCode
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * Returns the string representation of this {@code MyNumber}.
     *
     * @return the string representation of this {@code MyNumber}.
     */
    @Override
    public abstract String toString();

    /**
     * Returns a number whose value is {@code (-this)}.
     *
     * @return {@code -this}
     */
    public abstract MyNumber negate();

    /**
     * Returns the sum of this number and the neutral element 0 {@code 0 + this}.
     *
     * @return the sum of this number and the neutral element 0
     */
    public MyNumber plus() {
        return this;
    }

    /**
     * Returns the sum of this number and the given number ({@code this + other}).
     *
     * <ol>
     *     <li>If both numbers are integers, the result will be an integer</li>
     *     <li>If one of the number is real, the result will be real</li>
     *     <li>Otherwise (both numbers are rational,) the result will be rational</li>
     * </ol>
     *
     * <p>Notice if the result can be represented as an integer, it will be an integer.
     *
     * @param other the number to add
     * @return the sum of this number and the given number
     */
    public abstract MyNumber plus(MyNumber other);

    /**
     * Returns the difference of this number and the neutral element 0 {@code 0 - this}.
     *
     * @return the difference of this number and the neutral element 0
     */
    public abstract MyNumber minus();

    /**
     * Returns the difference of this number and the given number ({@code this - other}).
     *
     * <ol>
     *     <li>If both numbers are integers, the result will be an integer</li>
     *     <li>If one of the number is real, the result will be real</li>
     *     <li>Otherwise (both numbers are rational,) the result will be rational</li>
     * </ol>
     *
     * <p>Notice if the result can be represented as an integer, it will be an integer.
     *
     * @param other the number to subtract
     * @return the difference of this number and the given number
     */
    public abstract MyNumber minus(MyNumber other);

    /**
     * Returns the product of this number and the neutral element 1 {@code 1 * this}.
     *
     * @return the product of this number and the neutral element 1
     */
    public MyNumber times() {
        return this;
    }

    /**
     * Returns the product of this number and the given number ({@code this * other}).
     *
     * <ol>
     *     <li>If both numbers are integers, the result will be an integer</li>
     *     <li>If one of the number is real, the result will be real</li>
     *     <li>If both numbers are rational, the result will be rational</li></li>
     * </ol>
     *
     * <p>Notice if the result can be represented as an integer, it will be an integer.
     *
     * @param other the number to multiply
     * @return the product of this number and the given number
     */
    public abstract MyNumber times(MyNumber other);

    /**
     * Returns the quotient of this number and the neutral element 1 ({@code 1 / this}).
     *
     * <ol>
     *     <li>If the number is an integer, the result will be rational</li>
     *      <li>If the number is an real, the result will be real</li>
     *     <li>Otherwise (the number rational,) the result will be rational</li>
     * </ol>
     *
     * @return the quotient of this number and the neutral element 1
     * @throws WrongOperandException if the number is 0
     */
    public abstract MyNumber divide();

    /**
     * Returns the quotient of this number and the given number ({@code this / other}).
     *
     * <ol>
     *     <li>If both numbers are integers, the result will be an rational</li>
     *     <li>If one of the number is real, the result will be real</li>
     *     <li>Otherwise (both numbers are rational,) the result will be rational</li>
     * </ol>
     *
     * <p>Notice if the result can be represented as an integer, it will be an integer.
     *
     * @param other the number to divide
     * @return the quotient of this number and the given number
     * @throws WrongOperandException if the given number is 0
     */
    public abstract MyNumber divide(MyNumber other);

    /**
     * Returns the square root of this number. The result will always be real or an integer.
     *
     * @return the square root of this number
     */
    public MyNumber sqrt() {
        return checkRealToInt(toReal().sqrt(MathContext.DECIMAL128));
    }

    /**
     * Returns {@code this} number raised to the power of {@code n} (x^n). The result will always be
     * real or an integer.
     *
     * @param n the exponent
     * @return {@code this} number raised to the power of {@code n}
     */
    public abstract MyNumber expt(MyNumber n);

    /**
     * Returns Euler’s number raised to the power of {@code this} number (exp(x)). The result will
     * always be real or an integer.
     *
     * @return Euler’s number raised to the power of {@code this}
     * @throws WrongOperandException if this number is not positive or the large
     */
    public abstract MyNumber exp();

    /**
     * Returns the natural logarithm of this number (ln(x)). The result will always be real or an
     * integer.
     *
     * @return the natural logarithm of this number
     * @throws WrongOperandException if this number is not positive
     */
    public abstract MyNumber ln();

    /**
     * Returns the logarithm of this number with base {@code base} (log_x(y)). The result will
     * always be real or an integer.
     *
     * @param base the base of the logarithm
     * @return the logarithm of this number with base {@code base}
     * @throws WrongOperandException if this number is not positive or the base is not positive
     */
    public abstract MyNumber log(MyNumber base);

    /**
     * Checks if the given real number can be represented as an integer.
     *
     * @param real the real number to check
     * @return an integer if the real number can be represented as an integer, otherwise the real
     * number
     */
    protected MyNumber checkRealToInt(BigDecimal real) {
        final BigDecimal stripped = real.stripTrailingZeros();
        return stripped.scale() <= 0 ? new MyInteger(stripped.toBigIntegerExact()) : new MyReal(real);
    }

    /**
     * Checks if the given rational number can be represented as an integer.
     *
     * @param rational the real number to check
     * @return an integer if the rational number can be represented as an integer, otherwise the
     * rational number
     */
    protected MyNumber checkRationalToInt(Rational rational) {
        return rational.getDenominator().equals(BigInteger.ONE) ? new MyInteger(rational.getNumerator()) : new MyRational(rational);
    }
}
