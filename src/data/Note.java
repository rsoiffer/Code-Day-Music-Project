package data;


import soundconverter.wavfile.DataChunk;
import soundconverter.wavfile.Instrument;
import soundconverter.wavfile.Piano;

// A class that represents a single note. Feel free to change this as needed.
public class Note {

    public int bpm;
    public int note; // 60 is middle C, 61 is C-sharp, 62 is D, 63 is D-sharp, 64 is E, 65 is F, etc
    // n means play for 1/nth of a measure, for example, normal quarter notes are time=4
    // time duration for time n = .5^(n - 4) * 60 / bpm
    public int time;
    public double volume; // On a scale from 0 to 1, where 0 is the quietest and 1 is the loudest
    public Instrument instrument;

    private float maxAmp32 = Float.MAX_VALUE;
    private int dwSamplePerSec;

    public Note(Instrument instrument, int note, int time, double volume, int
            bpm, int
            dwSamplePerSec) {
        this.instrument = instrument;
        this.note = note;
        this.time = time;
        this.volume = volume;
        this.bpm = bpm;
        this.dwSamplePerSec = dwSamplePerSec;
    }

    public float[] toData32() {
        return toData32(instrument.freq(this.note));
    }

    private float mix(double t, int i) {
        double result = 0;
        for (int j = 0; j < i; j++)
            result += Math.sin(t * j);

        return Math.abs((float)result/i);
    }

    // returns data for that note (for 32-bit audio)
    public float[] toData32(double freq) {
        int sampleDataSize = (int) (dwSamplePerSec * (Math.pow(2, (-this.time + 4)) * 60 / bpm));
        System.out.println("sample size : " + sampleDataSize);

//        System.out.println("sampleDataSize = " + sampleDataSize);
        float[] data = new float[sampleDataSize];

        double amplitude = this.volume * Float.MAX_VALUE;

        // period
        double t = (Math.PI * freq * 2) / dwSamplePerSec;

        for (int i = 0; i < sampleDataSize; i++) {
            data[i] = (float)(amplitude * Math.sin(t * i));
//            data[i] = (float)(amplitude * mix(i,3));
        }

        return data;
    }

    public String toString() {
        String result = "";
        result += "instrument:\t" + this.instrument + "\n";
        result += "note:\t" + this.note + "\n";
        result += "time:\t" + this.time + "\n";
        result += "bpm:\t" + this.bpm + "\n";
        result += "volume:\t" + this.volume + "\n";

        return result;
    }

//    public void printData() {
//        float[] data = this.toData32();
//        for (int i = 0; i < data.length; i++) {
//            System.out.println(" " + data[i]);
//        }
//    }

    public static void main(String[] args) {
        Note note = new Note(new Piano(), 60, 1, 0.5, 60, 44100);
//        note.printData();
    }
}
