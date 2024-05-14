package syn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat.Encoding;

public class ShoTest {
	private static void tread(final String mname) throws InvalidMidiDataException, IOException {

		final ShoSynQ shoSyn = new ShoSynQ();
		final PrSyn prSyn = new PrSyn();
		final Syn syn = new CompSyn(
				new Syn[] { shoSyn, prSyn });

		final Sequence sequence = MidiSystem.getSequence(new File(mname));
		final TrackTempo trackTempo = new TrackTempo(sequence);
		try (
				final FileOutputStream fos = new FileOutputStream("writeToFileDouble.txt");
				final BufferedOutputStream bos = new BufferedOutputStream(fos);
				final DataOutputStream dos = new DataOutputStream(bos);
		){
			final Track[] tracks = sequence.getTracks();
			final List<TrackHandler> trackHandlers = new ArrayList<>();
			for(int i = 0; i < tracks.length; ++i) {
				trackHandlers.add(new TrackHandler(tracks[i], i, syn, trackTempo));
			}

			long tick = 0;
			// long steps = stepsPerTick;
			while(true) {
				boolean active = false;
				for(int i = 0; i < tracks.length; ++i) {
					final TrackHandler th = trackHandlers.get(i);
					active |= th.active();
					th.tick(tick);
				}
				if (!active) break;

				for (long s = 0; s < trackTempo.stepsPerTick(); ++s) {
					shoSyn.prepare(trackTempo.stepPeriod());
					shoSyn.step(trackTempo.stepPeriod());
					final double a = shoSyn.a();
					dos.writeDouble(a);
				}
				tick++;
//				final long secs = (long)(tick * tickSeconds);
//				System.out.println(String.format("|%10d| %s", secs, prSyn));
//				if (secs > 15) break;
			}
		}
	}

	private static void dtw(final String wname) throws FileNotFoundException, IOException {
		final File file = new File("writeToFileDouble.txt");
		try (
				final FileInputStream fis = new FileInputStream(file);
				final BufferedInputStream bis = new BufferedInputStream(fis);
				final DataInputStream dis = new DataInputStream(bis);
		){
			final boolean bigEndian = false;
			final boolean signed = true;
			final int bits = 16;
			final float sampleFrequency = 44100f * 2f;
			final int channels = 1;
			final long size = file.length();
/*
			final AudioFormat format = new AudioFormat(
					sampleFrequency,
					bits,
					channels,
					signed,
					bigEndian
					);
*/
			final AudioFormat format = new AudioFormat(
					Encoding.PCM_FLOAT,
					sampleFrequency,
					32,
					channels,
					4,
					sampleFrequency,
					true
					);


			final AudioInputStream audioInputStream = new AudioInputStream(
					new DisfMonoPcmInputStream(
							dis,
							(int)(size/8)
					),
					format,
					(int)(size/4));

			AudioSystem.write(
					audioInputStream,
					AudioFileFormat.Type.WAVE,
					new FileOutputStream(wname));
		}
	}


	public static void main(String[] args) throws InvalidMidiDataException, IOException {	
		final List<String> files = new ArrayList<>();
		files.add("Toccata-and-Fugue-Dm");
		//files.add("moonlight");
		//files.add("bwv0248a");
		//files.add("bwv0248b");
		//files.add("deb_clai");
		//files.add("der_winter");
		//files.add("3771vivandte");
		//files.add("beethoven_opus10_1");
		for(final String name : files) {
			tread(name + ".mid");dtw(name + ".wav");
		};
	}


}
