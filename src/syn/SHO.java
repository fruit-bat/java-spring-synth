package syn;

public class SHO {

	protected final double _m;
	protected final double _k;
	protected final double _c;

	protected double _x;
	protected double _v;
	protected double _d;
	private double _f;
	private double _dx;

	public SHO(
			final double m,
			final double k,
			final double c) {
		_m = m;
		_k = k;
		_c = c;
		_v = 0;
		_x = 0;
		System.out.println("m=" + _m + " k=" +_k + " c=" + _c);
	}

	public SHO(
			final double f,
			final double c) {
//		this(10d/(2 * Math.PI * f), 10d * 2 * Math.PI * f, c / (10d/(2 * Math.PI * f)));
		this(10d/(2 * Math.PI * f), 10d * 2 * Math.PI * f, c * 880d / f);
	}

	public double x() {
		return _x;
	}

	public void x(final double x) {
		_x = x;
	}

	public double v() {
		return _v;
	}

	public void v(final double v) {
		_v = v;
	}

	public void prepare(final double dt) {
		_dx = _v * dt;
		_f = -(_k * _x * dt + _dx * _c) + (_v > 0 ? _d * dt : -_d * dt);
	}

	public void step(final double dt) {
		_v += _f / _m;
		_x += _v * dt;
	}

	public double omega() {
		return Math.sqrt(_k / _m);
	}

	public double frequency() {
		return omega() / (2 * Math.PI);
	}

	@Override
	public String toString() {
		return "x = " + _x + ", v = " + _v + ", dx = " + _dx + ", f = " + _f;
	}

	public static void main(final String [] args) {
		final SHO sho = new SHO(.001, 10, 0);
		System.out.println("Created SHO with frequency " + sho.frequency() + "Hz");

		sho._x = 1;
//		final int uf = 44000 * 1024;
//		final double dt = 1d / uf;
//		final int pf = 10000; // output values per second
//		final int cpp = uf / pf; // Counts per print

		final int cpp = 1;
		final double dt = 0.0001;

		System.out.println("dt = " + dt + "s");
		int j = cpp;
		for(int i = 0; i < 1000; ++i) {
			sho.prepare(dt);
			sho.step(dt);
			if (--j == 0) {
				System.out.println("t = " + dt * i + ", " + sho);
				j = cpp;
			}
		}
	}
}
