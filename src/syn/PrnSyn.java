package syn;

public class PrnSyn implements Syn {
	
	public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

	public PrnSyn() {}

	public void printNote(final int channel, final int key, final int velocity, final boolean on) {
		final int octave = key / 12-1;
		final int note = key % 12;
		final String noteName = NOTE_NAMES[note];

		System.out.println("Note "
				+ (on ? "on  " : "off ")
				+ noteName
				+ octave
				+ " key "
				+ key
				+ " velocity "
				+ velocity);
	}
	@Override
	public void noteOn(final int channel, final int key, final int velocity) {
		printNote(channel, key, velocity, true);
	}
	@Override
	public void noteOff(final int channel, final int key, final int velocity) {
		printNote(channel, key, velocity, false);
	}
}
