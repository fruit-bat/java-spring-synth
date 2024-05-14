package syn;

public class PrSyn implements Syn {
	private final char[] _keys = new char[128];

	public PrSyn() {
		for(int i = 0; i < _keys.length; ++i) _keys[i] = '-';
	}
	@Override
	public void noteOn(final int channel, final int key, final int velocity) {
		_keys[key] = '+';
	}
	@Override
	public void noteOff(final int channel, final int key, final int velocity) {
		_keys[key] = '-';
	}
	@Override
	public String toString() {
		return String.valueOf(_keys);
	}
}
