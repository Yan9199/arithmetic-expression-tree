package math;

import exception.Comparison;
import exception.WrongOperandException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents a real number in Racket.
 */
public final class MyReal extends MyNumber {

    /**
     * The scale of the real number for inexact numbers.
     */
    public static final int SCALE = 15;

    /**
     * The rounding mode of the real number for inexact numbers.
     */
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * The constant {@link MyNumber} 0 as a {@link MyReal}.
     */
    public static final MyNumber ZERO = new MyReal(BigDecimal.ZERO);

    /**
     * The constant {@link MyNumber} 1 as a {@link MyReal}.
     */
    public static final MyNumber ONE = new MyReal(BigDecimal.ONE);

    /**
     * The value of this real number.
     */
    private final BigDecimal value;

    /**
     * Constructs and initializes a real number with the specified value.
     *
     * @param value the value of the real number
     * @throws NullPointerException if the value is null
     */
    public MyReal(BigDecimal value) {
        this.value = Objects.requireNonNull(value, "value null").setScale(SCALE, ROUNDING_MODE);
    }

    /**
     * Rounds the number down.
     *
     * @return the rounded number
     */
    private BigDecimal round() {
        final int sign = value.signum();
        final BigDecimal rounded = value.abs();
        return sign == -1 ? rounded.negate() : rounded;
    }

    private BigDecimal roundNumber(BigDecimal b) {
        final int s = b.signum();
        final BigDecimal r = b.abs();
        return s == -1 ? r.negate() : r;
    }

    @Override
    public BigInteger toInteger() {
        return round().toBigInteger();
    }

    @Override
    public Rational toRational() {
        return new Rational(roundNumber(BigDecimal.TEN.pow(SCALE).multiply(value)).toBigInteger(), BigInteger.TEN.pow(SCALE));
    }

    @Override
    public BigDecimal toReal() {
        return value;
    }

    @Override
    public boolean isZero() {
        return this.equals(ZERO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyReal number)) return false;
        return value.equals(number.value);
    }

    @Override
    public MyNumber negate() {
        return new MyReal(value.negate());
    }

    @Override
    public MyNumber plus(MyNumber other) {
        return checkRealToInt(value.add(other.toReal()));
    }

    @Override
    public MyNumber minus() {
        return new MyReal(value.negate());
    }

    @Override
    public MyNumber minus(MyNumber other) {
        return checkRealToInt(value.subtract(other.toReal()));
    }

    @Override
    public MyNumber times(MyNumber other) {
        return checkRealToInt(value.multiply(other.toReal()));
    }

    @Override
    public MyNumber divide() {
        if (equals(ZERO)) throw new WrongOperandException(this, Comparison.DIFFERENT_FROM, ZERO);
        return new MyReal(BigDecimal.ONE.divide(value, SCALE, ROUNDING_MODE));
    }

    @Override
    public MyNumber divide(MyNumber other) {
        if (other instanceof MyInteger) if (other.equals(MyInteger.ZERO))
            throw new WrongOperandException(other, Comparison.DIFFERENT_FROM, MyInteger.ZERO);
        if (other instanceof MyReal)
            if (other.equals(ZERO)) throw new WrongOperandException(other, Comparison.DIFFERENT_FROM, ZERO);
        if (other.equals(MyRational.ZERO))
            throw new WrongOperandException(other, Comparison.DIFFERENT_FROM, MyRational.ZERO);
        return checkRealToInt(value.divide(other.toReal(), SCALE, ROUNDING_MODE));
    }

    @Override
    public MyNumber expt(MyNumber n) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) throw new WrongOperandException(this, Comparison.GREATER_THAN, ZERO);
        if (n instanceof MyInteger) if (n.toInteger().compareTo(BigInteger.ZERO) <= 0)
            throw new WrongOperandException(n, Comparison.GREATER_THAN, MyInteger.ZERO);
        if (n instanceof MyReal) if (n.toReal().compareTo(BigDecimal.ZERO) <= 0)
            throw new WrongOperandException(n, Comparison.GREATER_THAN, ZERO);
        if (n instanceof MyRational) {
            final Rational r = n.toRational();
            if (r.equals(Rational.ZERO) || r.getNumerator().signum() == -1)
                throw new WrongOperandException(n, Comparison.GREATER_THAN, MyRational.ZERO);
        }
        final BigDecimal x = log().toReal().multiply(n.toReal());
        final BigInteger m = x.toBigInteger();
        return checkRealToInt(BigDecimal.TEN.pow(m.intValue()).multiply(BigDecimal.valueOf(Math.pow(10, x.subtract(new BigDecimal(m)).doubleValue()))));
    }

    @Override
    public MyNumber exp() {
        if (value.compareTo(BigDecimal.ZERO) <= 0) throw new WrongOperandException(this, Comparison.GREATER_THAN, ZERO);
        final BigDecimal x = BigDecimal.valueOf(Math.log10(Math.E)).multiply(value);
        final BigInteger m = x.toBigInteger();
        return checkRealToInt(BigDecimal.TEN.pow(m.intValue()).multiply(BigDecimal.valueOf(Math.pow(10, x.subtract(new BigDecimal(m)).doubleValue()))));
    }

    @Override
    public MyNumber ln() {
        if (value.compareTo(BigDecimal.ZERO) <= 0) throw new WrongOperandException(this, Comparison.GREATER_THAN, ZERO);
        BigDecimal i = new BigDecimal(String.valueOf(value));
        double logarithm = 0;
        if (i.compareTo(BigDecimal.TEN) > 0) do {
            i = i.divide(BigDecimal.TEN, MyReal.SCALE, MyReal.ROUNDING_MODE);
            logarithm++;
        } while (i.compareTo(BigDecimal.TEN) > 0);
        else if (i.compareTo(BigDecimal.ONE) < 0) do {
            i = i.multiply(BigDecimal.TEN);
            logarithm--;
        } while (i.compareTo(BigDecimal.ONE) < 0);
        return checkRealToInt(BigDecimal.valueOf(logarithm + Math.log10(i.doubleValue())).divide(BigDecimal.valueOf(Math.log10(Math.E)), MyReal.SCALE, MyReal.ROUNDING_MODE));
    }

    @Override
    public MyNumber log(MyNumber base) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) throw new WrongOperandException(this, Comparison.GREATER_THAN, ZERO);
        if (base instanceof MyInteger) if (base.toInteger().compareTo(BigInteger.ZERO) <= 0)
            throw new WrongOperandException(base, Comparison.GREATER_THAN, MyInteger.ZERO);
        if (base instanceof MyReal) if (base.toReal().compareTo(BigDecimal.ZERO) <= 0)
            throw new WrongOperandException(base, Comparison.GREATER_THAN, ZERO);
        if (base instanceof MyRational) {
            final Rational r = base.toRational();
            if (r.equals(Rational.ZERO) || r.getNumerator().signum() == -1)
                throw new WrongOperandException(base, Comparison.GREATER_THAN, MyRational.ZERO);
        }
        BigDecimal i = new BigDecimal(String.valueOf(value));
        double logarithm = 0;
        if (i.compareTo(BigDecimal.TEN) > 0) do {
            i = i.divide(BigDecimal.TEN, MyReal.SCALE, MyReal.ROUNDING_MODE);
            logarithm++;
        } while (i.compareTo(BigDecimal.TEN) > 0);
        else if (i.compareTo(BigDecimal.ONE) < 0) do {
            i = i.multiply(BigDecimal.TEN);
            logarithm--;
        } while (i.compareTo(BigDecimal.ONE) < 0);
        return checkRealToInt(BigDecimal.valueOf(logarithm + Math.log10(i.doubleValue())).divide(BigDecimal.valueOf(Math.log10(base.toReal().doubleValue())), MyReal.SCALE, MyReal.ROUNDING_MODE));
    }

    private MyNumber log() {
        BigDecimal i = new BigDecimal(String.valueOf(value));
        double logarithm = 0;
        if (i.compareTo(BigDecimal.TEN) > 0) do {
            i = i.divide(BigDecimal.TEN, MyReal.SCALE, MyReal.ROUNDING_MODE);
            logarithm++;
        } while (i.compareTo(BigDecimal.TEN) > 0);
        else if (i.compareTo(BigDecimal.ONE) < 0) do {
            i = i.multiply(BigDecimal.TEN);
            logarithm--;
        } while (i.compareTo(BigDecimal.ONE) < 0);
        return checkRealToInt(BigDecimal.valueOf(logarithm + Math.log10(i.doubleValue())));
    }

    @Override
    public String toString() {
        return value.stripTrailingZeros().toString();
    }
}
