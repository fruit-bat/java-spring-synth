package syn;

import java.util.ArrayList;
import java.util.List;

public class ShoSyn implements Syn {
	private static final int KEYS = 128;
	private static final int STRINGS = 3;
	private final SHO[][] _shos = new SHO[KEYS][STRINGS];
	private final List<SPR> _sprs = new ArrayList<SPR>();
	final double[] fac = new double[] {-.16, .3, -.03};

	public static double mnf(final int n) {
		return 440d * Math.pow(2d, (n - 60d)/12d);
	}

	public ShoSyn() {
		for (int i = 0; i < KEYS; ++i) {
			final double f = mnf(i);
			_shos[i][0] = new SHO(f * 1.998, 0.008 );
			_shos[i][1] = new SHO(f, 0.008 );
			_shos[i][2] = new SHO(f * 4.001, 0.008 );
		}
	}

	@Override
	public void noteOn(final int channel, final int key, final int velocity) {
		final double x = 2 * velocity;
		for (int j = 0; j < STRINGS; ++j) {
			final SHO sho = _shos[key][j];
			// pluck
			sho._v += x * fac[j]; sho._d = 0;
			// drive
			sho._d = velocity * sho._m * fac[j];
		}
	}

	@Override
	public void noteOff(final int channel, final int key, final int velocity) {
		for (int j = 0; j < STRINGS; ++j) {
			final SHO sho = _shos[key][j];
			//sho._d = 0;
			sho._d = -4d * velocity * sho._m;
		}
	}

	public double a() {
		double a = 0d;
		for (int i = 0; i < KEYS; ++i) {
			for (int j = 0; j < STRINGS; ++j) {
				a += _shos[i][j]._x;
			}
		}
		if (a > 1) a = 1;
		if (a <-1) a = -1;
		return a ;
	}

	public void prepare(final double dt) {
		for (int i = 0; i < KEYS; ++i) {
			for (int j = 0; j < STRINGS; ++j) {
				_shos[i][j].prepare(dt);
			}
		}
	}

	public void step(final double dt) {
		for (final SPR sho : _sprs) sho.step(dt);
		for (int i = 0; i < KEYS; ++i) {
			for (int j = 0; j < STRINGS; ++j) {
				_shos[i][j].step(dt);
			}
		}
	}
}

