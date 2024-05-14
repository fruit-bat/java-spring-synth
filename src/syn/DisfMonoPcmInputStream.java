package syn;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class DisfMonoPcmInputStream extends InputStream
{
    private final DataInputStream dis;
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream(4);
    private final DataOutputStream dos = new DataOutputStream(baos);

    private int framesCounter;
    private final int[] pcmOut = new int[2];
    private final int[] frameBytes = new int[4];
    private int idx;

    private final int framesToRead;

    public DisfMonoPcmInputStream(final DataInputStream dis, final int l) {
    	reset();
        this.dis = dis;
        framesToRead = l;
    }

    @Override
    public int read() throws IOException
    {
        while(available() > 0)
        {
            idx &= 3;
            if (idx == 0) // set up next frame's worth of data
            {
                framesCounter++; // count elapsing frames

                try {
                  dos.writeFloat((float)dis.readDouble());
                }
                catch(final EOFException e) {
                	return -1;
                }
                final byte[] d = baos.toByteArray();
                // output as unsigned bytes, in range [0..255]
                frameBytes[0] = d[0] & 0xff;
                frameBytes[1] = d[1] & 0xff;
                frameBytes[2] = d[2] & 0xff;
                frameBytes[3] = d[3] & 0xff;
                baos.reset();
            }
            return frameBytes[idx++];
        }
        return -1;
    }

    @Override
    public int available()
    {
        // NOTE: not concurrency safe.
        // 1st half of sum: there are 4 reads available per frame to be read
        // 2nd half of sum: the # of bytes of the current frame that remain to be read
        return 4 * (framesToRead - 1 - framesCounter)
                + 4 - idx % 4;
    }

    @Override
    public void reset()
    {
        framesCounter = 0;
        idx = 0;
    }

    @Override
    public void close()
    {
        System.out.println(
            "StereoPcmInputStream stopped after reading frames:"
                + framesCounter);
    }
}
