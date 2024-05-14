package syn;

public interface Syn {
	public void noteOn(final int channel, final int key, final int velocity);
	public void noteOff(final int channel, final int key, final int velocity);
}
