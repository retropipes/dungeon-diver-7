module com.puttysoftware.dungeondiver7 {
    requires java.desktop;
    requires com.github.vorbis;
    requires com.puttysoftware.audio.ogg;
    requires com.puttysoftware.diane;

    uses javax.sound.sampled.spi.AudioFileReader;
    uses javax.sound.sampled.spi.FormatConversionProvider;
}