package syn;

public class SPR {

	private final SHO _sho1;
	private final SHO _sho2;

	private final double _k;

	public SPR(
			final SHO sho1,
			final SHO sho2,
			final double k) {
		_sho1 = sho1;
		_sho2 = sho2;
		_k = k;
	}

	public void step(final double dt) {
		final double f = - _k * (_sho1.x() - _sho2.x()) * dt;
		_sho1._v += f / _sho1._m;
		_sho2._v -= f / _sho2._m;
	}
}
