package math;

import exception.Comparison;
import exception.WrongOperandException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Represents an integer in Racket.
 *
 * @author Nhan Huynh
 */
public final class MyInteger extends MyNumber {

    /**
     * The constant {@link MyNumber} 0 as a {@link MyInteger}.
     */
    public static final MyNumber ZERO = new MyInteger(BigInteger.ZERO);

    /**
     * The constant {@link MyNumber} 1 as a {@link MyInteger}.
     */
    public static final MyNumber ONE = new MyInteger(BigInteger.ONE);

    /**
     * The value of the integer.
     */
    private final BigInteger value;

    /**
     * Constructs and initializes an integer with the specified value.
     *
     * @param value the value of the real number
     * @throws NullPointerException if the value is null
     */
    public MyInteger(BigInteger value) {
        this.value = Objects.requireNonNull(value, "value null");
    }

    @Override
    public BigInteger toInteger() {
        return value;
    }

    @Override
    public Rational toRational() {
        return new Rational(value, BigInteger.ONE, false);
    }

    @Override
    public BigDecimal toReal() {
        return new BigDecimal(value).setScale(MyReal.SCALE, MyReal.ROUNDING_MODE);
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyInteger number)) {
            return false;
        }
        return value.equals(number.value);
    }

    @Override
    public MyNumber negate() {
        return new MyInteger(value.negate());
    }

    @Override
    public MyNumber plus(MyNumber other) {
        if (other instanceof MyInteger) return new MyInteger(value.add((other.toInteger())));
        if (other instanceof MyReal) return checkRealToInt(toReal().add(other.toReal()));
        return checkRationalToInt(other.toRational().plus(value));
    }

    @Override
    public MyNumber minus() {
        return new MyInteger(value.negate());
    }

    @Override
    public MyNumber minus(MyNumber other) {
        if (other instanceof MyInteger) return new MyInteger(value.subtract(other.toInteger()));
        if (other instanceof MyReal) return checkRealToInt(toReal().subtract(other.toReal()));
        return checkRationalToInt(toRational().plus(other.toRational().negate()));
    }

    @Override
    public MyNumber times(MyNumber other) {
        if (other instanceof MyInteger) return new MyInteger(value.multiply(other.toInteger()));
        if (other instanceof MyReal) return checkRealToInt(toReal().multiply(other.toReal()));
        return checkRationalToInt(toRational().times(other.toRational()));
    }

    @Override
    public MyNumber divide() {
        if (equals(ZERO)) throw new WrongOperandException(this, Comparison.DIFFERENT_FROM, ZERO);
        final Rational r = value.signum() == -1 ? new Rational(BigInteger.ONE.negate(), value.negate(), false) : new Rational(BigInteger.ONE, value, false);
        return new MyRational(r);
    }

    @Override
    public MyNumber divide(MyNumber other) {
        if (other instanceof MyInteger) {
            if (other.equals(ZERO)) throw new WrongOperandException(other, Comparison.DIFFERENT_FROM, ZERO);
            return checkRationalToInt(new Rational(value, other.toInteger()));
        }
        if (other instanceof MyReal) {
            if (other.equals(MyReal.ZERO))
                throw new WrongOperandException(other, Comparison.DIFFERENT_FROM, MyReal.ZERO);
            return checkRealToInt(toReal().divide(other.toReal(), MyReal.SCALE, MyReal.ROUNDING_MODE));
        }
        if (other.equals(MyRational.ZERO))
            throw new WrongOperandException(other, Comparison.DIFFERENT_FROM, MyRational.ZERO);
        final Rational r = toRational().times(other.toRational().invert());
        return checkRationalToInt(new Rational(r.getNumerator(), r.getDenominator()));
    }

    @Override
    public MyNumber expt(MyNumber n) {
        if (value.compareTo(BigInteger.ZERO) <= 0) throw new WrongOperandException(this, Comparison.GREATER_THAN, ZERO);
        if (n instanceof MyInteger) {
            final BigInteger b = n.toInteger();
            if (b.compareTo(BigInteger.ZERO) <= 0) throw new WrongOperandException(n, Comparison.GREATER_THAN, ZERO);
            return new MyInteger(value.pow(b.intValue()));
        }
        if (n instanceof MyReal) if (n.toReal().compareTo(BigDecimal.ZERO) <= 0)
            throw new WrongOperandException(n, Comparison.GREATER_THAN, MyReal.ZERO);
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
        if (value.compareTo(BigInteger.ZERO) <= 0) throw new WrongOperandException(this, Comparison.GREATER_THAN, ZERO);
        final BigDecimal x = BigDecimal.valueOf(Math.log10(Math.E)).multiply(toReal());
        final BigInteger m = x.toBigInteger();
        return checkRealToInt(BigDecimal.TEN.pow(m.intValue()).multiply(BigDecimal.valueOf(Math.pow(10, x.subtract(new BigDecimal(m)).doubleValue()))));
    }

    @Override
    public MyNumber ln() {
        if (value.compareTo(BigInteger.ZERO) <= 0) throw new WrongOperandException(this, Comparison.GREATER_THAN, ZERO);
        BigDecimal i = toReal();
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
        if (value.compareTo(BigInteger.ZERO) <= 0) throw new WrongOperandException(this, Comparison.GREATER_THAN, ZERO);
        if (base instanceof MyInteger) if (base.toInteger().compareTo(BigInteger.ZERO) <= 0)
            throw new WrongOperandException(base, Comparison.GREATER_THAN, ZERO);
        if (base instanceof MyReal) if (base.toReal().compareTo(BigDecimal.ZERO) <= 0)
            throw new WrongOperandException(base, Comparison.GREATER_THAN, MyReal.ZERO);
        if (base instanceof MyRational) {
            final Rational r = base.toRational();
            if (r.equals(Rational.ZERO) || r.getNumerator().signum() == -1)
                throw new WrongOperandException(base, Comparison.GREATER_THAN, MyRational.ZERO);
        }
        BigDecimal i = toReal();
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
        BigDecimal i = toReal();
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
        return value.toString();
    }
}
