package syn;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DisMonoPcmInputStream extends InputStream
{
    private final DataInputStream dis;
    private int framesCounter;
    private final int[] pcmOut = new int[2];
    private final int[] frameBytes = new int[4];
    private int idx;

    private final int framesToRead;

    public DisMonoPcmInputStream(final DataInputStream dis, final int l) {
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

                // scale to 16 bits
                long k = (long)(dis.readDouble() * Short.MAX_VALUE);
                if (k > Short.MAX_VALUE) k = Short.MAX_VALUE;
                if (k < Short.MIN_VALUE) k = Short.MIN_VALUE;
                pcmOut[0] = (int)k;
                pcmOut[1] = pcmOut[0];

                // output as unsigned bytes, in range [0..255]
                frameBytes[0] = (char)pcmOut[0];
                frameBytes[1] = (char)(pcmOut[0] >> 8);
                frameBytes[2] = (char)pcmOut[1];
                frameBytes[3] = (char)(pcmOut[1] >> 8);
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
