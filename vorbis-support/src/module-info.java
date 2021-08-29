module com.github.vorbis {
    requires transitive java.desktop;
    requires transitive java.logging;

    provides javax.sound.sampled.spi.AudioFileReader with
            com.github.trilarion.sound.vorbis.sampled.spi.VorbisAudioFileReader;
    provides javax.sound.sampled.spi.FormatConversionProvider with
            com.github.trilarion.sound.vorbis.sampled.spi.VorbisFormatConversionProvider;
}