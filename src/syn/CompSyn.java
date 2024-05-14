package syn;

public class CompSyn implements Syn {
	private final Syn[] _syns;

	public CompSyn(final Syn[] syns) {
		_syns = syns;
	}
	@Override
	public void noteOn(final int channel, final int key, final int velocity) {
		for (final Syn _syn : _syns) _syn.noteOn(channel, key, velocity);
	}
	@Override
	public void noteOff(final int channel, final int key, final int velocity) {
		for (final Syn _syn : _syns) _syn.noteOff(channel, key, velocity);
	}
}
