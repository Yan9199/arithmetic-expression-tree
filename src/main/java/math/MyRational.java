package math;

import exception.Comparison;
import exception.WrongOperandException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Represents a rational number in Racket.
 *
 * @author Nhan Huynh
 */
public final class MyRational extends MyNumber {

    /**
     * The {@link MyNumber} 0 as a {@link MyRational}.
     */
    public static final MyNumber ZERO = new MyRational(Rational.ZERO);

    /**
     * The {@link MyNumber} 1 as a {@link MyRational}.
     */
    public static final MyNumber ONE = new MyRational(Rational.ONE);

    /**
     * The value of this rational number.
     */
    private final Rational value;

    /**
     * Constructs and initializes a rational number with the specified value.
     *
     * @param value the value of the rational number
     * @throws NullPointerException if the value is null
     */
    public MyRational(Rational value) {
        this.value = Objects.requireNonNull(value, "value null");
    }

    @Override
    public BigInteger toInteger() {
        return value.getNumerator().divide(value.getDenominator());
    }

    @Override
    public Rational toRational() {
        return value;
    }

    @Override
    public BigDecimal toReal() {
        return new BigDecimal(value.getNumerator()).divide(new BigDecimal(value.getDenominator()), MyReal.SCALE, MyReal.ROUNDING_MODE);
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
        if (!(o instanceof MyRational number)) return false;
        return value.equals(number.value);
    }

    @Override
    public MyNumber negate() {
        return new MyRational(value.negate());
    }

    @Override
    public MyNumber plus(MyNumber other) {
        if (other instanceof MyInteger) return checkRationalToInt(value.plus(other.toInteger()));
        if (other instanceof MyReal) return checkRealToInt(toReal().add(other.toReal()));
        return checkRationalToInt(value.plus(other.toRational()));
    }

    @Override
    public MyNumber minus() {
        return new MyRational(value.negate());
    }

    @Override
    public MyNumber minus(MyNumber other) {
        if (other instanceof MyInteger) return checkRationalToInt(value.plus(other.negate().toInteger()));
        if (other instanceof MyReal) return checkRealToInt(toReal().subtract(other.toReal()));
        return checkRationalToInt(value.plus(other.negate().toRational()));
    }

    @Override
    public MyNumber times(MyNumber other) {
        if (other instanceof MyReal) return checkRealToInt(toReal().multiply(other.toReal()));
        return checkRationalToInt(value.times(other.toRational()));
    }

    @Override
    public MyNumber divide() {
        if (equals(ZERO)) throw new WrongOperandException(this, Comparison.DIFFERENT_FROM, ZERO);
        return new MyRational(value.invert());
    }

    @Override
    public MyNumber divide(MyNumber other) {
        if (other instanceof MyInteger) {
            if (other.equals(MyInteger.ZERO))
                throw new WrongOperandException(other, Comparison.DIFFERENT_FROM, MyInteger.ZERO);
            final Rational r = value.times(other.toRational().invert());
            return checkRationalToInt(new Rational(r.getNumerator(), r.getDenominator()));
        }
        if (other instanceof MyReal) {
            if (other.equals(MyReal.ZERO))
                throw new WrongOperandException(other, Comparison.DIFFERENT_FROM, MyReal.ZERO);
            return checkRealToInt(toReal().divide(other.toReal(), MyReal.SCALE, MyReal.ROUNDING_MODE));
        }
        if (other.equals(ZERO)) throw new WrongOperandException(other, Comparison.DIFFERENT_FROM, ZERO);
        final Rational r = value.times(other.toRational().invert());
        return checkRationalToInt(new Rational(r.getNumerator(), r.getDenominator()));
    }

    @Override
    public MyNumber expt(MyNumber n) {
        if (value.equals(Rational.ZERO) || value.getNumerator().signum() < 0)
            throw new WrongOperandException(this, Comparison.GREATER_THAN, ZERO);
        if (n instanceof MyInteger) {
            final BigInteger b = n.toInteger();
            if (b.compareTo(BigInteger.ZERO) <= 0)
                throw new WrongOperandException(n, Comparison.GREATER_THAN, MyInteger.ZERO);
            return checkRealToInt(new BigDecimal(value.getNumerator().pow(b.intValue())).divide(new BigDecimal(value.getDenominator().pow(b.intValue())), MyReal.SCALE, MyReal.ROUNDING_MODE));
        }
        if (n instanceof MyReal) if (n.toReal().compareTo(BigDecimal.ZERO) <= 0)
            throw new WrongOperandException(n, Comparison.GREATER_THAN, MyReal.ZERO);
        if (n instanceof MyRational) {
            final Rational r = n.toRational();
            if (r.equals(Rational.ZERO) || r.getNumerator().signum() == -1)
                throw new WrongOperandException(n, Comparison.GREATER_THAN, ZERO);
        }
        final BigDecimal x = log().toReal().multiply(n.toReal());
        final BigInteger m = x.toBigInteger();
        return checkRealToInt(BigDecimal.TEN.pow(m.intValue()).multiply(BigDecimal.valueOf(Math.pow(10, x.subtract(new BigDecimal(m)).doubleValue()))));
    }

    @Override
    public MyNumber exp() {
        if (value.equals(Rational.ZERO) || value.getNumerator().signum() == -1)
            throw new WrongOperandException(this, Comparison.GREATER_THAN, ZERO);
        final BigDecimal x = BigDecimal.valueOf(Math.log10(Math.E)).multiply(toReal());
        final BigInteger m = x.toBigInteger();
        return checkRealToInt(BigDecimal.TEN.pow(m.intValue()).multiply(BigDecimal.valueOf(Math.pow(10, x.subtract(new BigDecimal(m)).doubleValue()))));
    }

    @Override
    public MyNumber ln() {
        if (value.equals(Rational.ZERO) || value.getNumerator().signum() == -1)
            throw new WrongOperandException(this, Comparison.GREATER_THAN, ZERO);
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
        if (value.equals(Rational.ZERO) || value.getNumerator().signum() < 0)
            throw new WrongOperandException(this, Comparison.GREATER_THAN, ZERO);
        if (base instanceof MyInteger) if (base.toInteger().compareTo(BigInteger.ZERO) <= 0)
            throw new WrongOperandException(base, Comparison.GREATER_THAN, MyInteger.ZERO);
        if (base instanceof MyReal) if (base.toReal().compareTo(BigDecimal.ZERO) <= 0)
            throw new WrongOperandException(base, Comparison.GREATER_THAN, MyReal.ZERO);
        if (base instanceof MyRational) {
            final Rational r = base.toRational();
            if (r.equals(Rational.ZERO) || r.getNumerator().signum() == -1)
                throw new WrongOperandException(base, Comparison.GREATER_THAN, ZERO);
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
