package syn;

public class ShoSynP implements Syn {
	private final SHO[] _shos = new SHO[128 * 3];

	public static double mnf(final int n) {
		return 440d * Math.pow(2d, (n - 60d)/12d);
	}

	public ShoSynP() {
		for (int i = 0; i < 128; ++i) {
				final double f = mnf(i);
				_shos[i * 3 + 0] = new SHO(f * 1.997, 0.01 );
				_shos[i * 3 + 1] = new SHO(f, 0.01 );
				_shos[i * 3 + 2] = new SHO(f * 4.001, 0.01 );
		}
	}

	@Override
	public void noteOn(final int channel, final int key, final int velocity) {
		final int i = key * 3;
		final double x = 2 * velocity;
		final double[] fac = new double[] {-.16, .3, -.03};
		for (int j = 0; j < 3; ++j) {
			final SHO sho = _shos[i + j];
			// pluck
			sho._v += x * fac[j];
			// drive
			sho._d = 4d * velocity * sho._m * fac[j];
		}
	}

	@Override
	public void noteOff(final int channel, final int key, final int velocity) {
		final int i = key * 3;
		for (int j = 0; j < 3; ++j) {
			final SHO sho = _shos[i + j];
			sho._d = 0;
		}
	}

	public double a() {
		double a = 0d;
		for (final SHO sho : _shos) {
			a += sho._x;
		}
		if (a > 1) a = 1;
		if (a <-1) a = -1;
		return a;
	}

	public void prepare(final double dt) {
		for (final SHO sho : _shos) sho.prepare(dt);
	}

	public void step(final double dt) {
		for (final SHO sho : _shos) sho.step(dt);
	}

}
